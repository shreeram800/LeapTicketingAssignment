'use client';

import React, { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import Layout from '@/components/Layout';
import Card from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import Badge from '@/components/ui/Badge';
import { api, Ticket, Comment, User } from '@/lib/api';

export default function TicketDetailPage() {
  const params = useParams();
  const router = useRouter();
  const ticketId = parseInt(params.id as string);
  
  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [commentText, setCommentText] = useState('');
  const [submittingComment, setSubmittingComment] = useState(false);
  const [assigningTicket, setAssigningTicket] = useState(false);

  useEffect(() => {
    if (ticketId) {
      fetchTicketData();
      fetchUsers();
    }
  }, [ticketId]);

  const fetchTicketData = async () => {
    try {
      const [ticketData, commentsData] = await Promise.all([
        api.tickets.getById(ticketId),
        api.comments.getByTicketId(ticketId),
      ]);
      setTicket(ticketData);
      setComments(commentsData);
    } catch (error) {
      console.error('Error fetching ticket data:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchUsers = async () => {
    try {
      const activeUsers = await api.users.getActive();
      setUsers(activeUsers);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  const handleAddComment = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!commentText.trim()) return;

    setSubmittingComment(true);
    try {
      const newComment = await api.comments.create(
        { body: commentText, ticketId },
        1 // Assuming current user ID is 1 for demo
      );
      
      setComments(prev => [newComment, ...prev]);
      setCommentText('');
    } catch (error) {
      console.error('Error adding comment:', error);
      alert('Failed to add comment. Please try again.');
    } finally {
      setSubmittingComment(false);
    }
  };

  const handleAssignTicket = async (assigneeId: number) => {
    setAssigningTicket(true);
    try {
      await api.tickets.assign(ticketId, assigneeId);
      await fetchTicketData(); // Refresh ticket data
    } catch (error) {
      console.error('Error assigning ticket:', error);
      alert('Failed to assign ticket. Please try again.');
    } finally {
      setAssigningTicket(false);
    }
  };

  const handleStatusChange = async (newStatus: string) => {
    try {
      await api.tickets.changeStatus(ticketId, newStatus);
      await fetchTicketData(); // Refresh ticket data
    } catch (error) {
      console.error('Error changing ticket status:', error);
      alert('Failed to change ticket status. Please try again.');
    }
  };

  const handleDeleteComment = async (commentId: number) => {
    if (confirm('Are you sure you want to delete this comment?')) {
      try {
        await api.comments.delete(commentId, 1); // Assuming current user ID is 1
        setComments(prev => prev.filter(c => c.id !== commentId));
      } catch (error) {
        console.error('Error deleting comment:', error);
        alert('Failed to delete comment. Please try again.');
      }
    }
  };

  if (loading) {
    return (
      <Layout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
      </Layout>
    );
  }

  if (!ticket) {
    return (
      <Layout>
        <div className="text-center py-12">
          <h1 className="text-2xl font-bold text-gray-900 mb-4">Ticket Not Found</h1>
          <p className="text-gray-600 mb-6">The ticket you're looking for doesn't exist.</p>
          <Button onClick={() => router.push('/tickets')}>
            Back to Tickets
          </Button>
        </div>
      </Layout>
    );
  }

  const getStatusBadgeVariant = (status: string) => {
    switch (status) {
      case 'OPEN': return 'success';
      case 'IN_PROGRESS': return 'warning';
      case 'CLOSED': return 'default';
      default: return 'default';
    }
  };

  const getPriorityBadgeVariant = (priority: string) => {
    switch (priority) {
      case 'HIGH': return 'danger';
      case 'MEDIUM': return 'warning';
      case 'LOW': return 'success';
      default: return 'default';
    }
  };

  return (
    <Layout>
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Ticket #{ticket.code}</h1>
            <p className="mt-2 text-gray-600">{ticket.subject}</p>
          </div>
          <Button variant="outline" onClick={() => router.push('/tickets')}>
            Back to Tickets
          </Button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Main Content */}
          <div className="lg:col-span-2 space-y-6">
            {/* Ticket Details */}
            <Card>
              <h2 className="text-xl font-semibold text-gray-900 mb-4">Details</h2>
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Subject</label>
                  <p className="mt-1 text-sm text-gray-900">{ticket.subject}</p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Description</label>
                  <p className="mt-1 text-sm text-gray-900 whitespace-pre-wrap">{ticket.description}</p>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Status</label>
                    <div className="mt-1">
                      <Badge variant={getStatusBadgeVariant(ticket.status)}>
                        {ticket.status}
                      </Badge>
                    </div>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Priority</label>
                    <div className="mt-1">
                      <Badge variant={getPriorityBadgeVariant(ticket.priority)}>
                        {ticket.priority}
                      </Badge>
                    </div>
                  </div>
                </div>
              </div>
            </Card>

            {/* Comments */}
            <Card>
              <h2 className="text-xl font-semibold text-gray-900 mb-4">Comments</h2>
              
              {/* Add Comment Form */}
              <form onSubmit={handleAddComment} className="mb-6">
                <div className="mb-4">
                  <textarea
                    value={commentText}
                    onChange={(e) => setCommentText(e.target.value)}
                    rows={3}
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="Add a comment..."
                    required
                  />
                </div>
                <Button
                  type="submit"
                  loading={submittingComment}
                  disabled={submittingComment || !commentText.trim()}
                >
                  Add Comment
                </Button>
              </form>

              {/* Comments List */}
              <div className="space-y-4">
                {comments.length === 0 ? (
                  <p className="text-gray-500 text-center py-4">No comments yet</p>
                ) : (
                  comments.map((comment) => (
                    <div key={comment.id} className="border-l-4 border-blue-500 pl-4 py-2">
                      <div className="flex justify-between items-start">
                        <div className="flex-1">
                          <div className="flex items-center space-x-2 mb-2">
                            <span className="font-medium text-sm text-gray-900">
                              {comment.author?.fullName || 'Unknown'}
                            </span>
                            <span className="text-xs text-gray-500">
                              {new Date(comment.createdAt).toLocaleString()}
                            </span>
                          </div>
                          <p className="text-sm text-gray-700">{comment.body}</p>
                        </div>
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => handleDeleteComment(comment.id)}
                        >
                          Delete
                        </Button>
                      </div>
                    </div>
                  ))
                )}
              </div>
            </Card>
          </div>

          {/* Sidebar */}
          <div className="space-y-6">
            {/* Ticket Actions */}
            <Card>
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Actions</h3>
              <div className="space-y-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Change Status</label>
                  <select
                    value={ticket.status}
                    onChange={(e) => handleStatusChange(e.target.value)}
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="OPEN">Open</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="CLOSED">Closed</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Assign To</label>
                  <select
                    value={ticket.assignee?.id || ''}
                    onChange={(e) => handleAssignTicket(parseInt(e.target.value))}
                    disabled={assigningTicket}
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Unassigned</option>
                    {users.map((user) => (
                      <option key={user.id} value={user.id}>
                        {user.fullName}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            </Card>

            {/* Ticket Info */}
            <Card>
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Information</h3>
              <div className="space-y-3 text-sm">
                <div>
                  <span className="font-medium text-gray-700">Owner:</span>
                  <p className="text-gray-900">{ticket.owner?.fullName || 'Unknown'}</p>
                </div>
                <div>
                  <span className="font-medium text-gray-700">Assignee:</span>
                  <p className="text-gray-900">{ticket.assignee?.fullName || 'Unassigned'}</p>
                </div>
                <div>
                  <span className="font-medium text-gray-700">Created:</span>
                  <p className="text-gray-900">
                    {new Date(ticket.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <span className="font-medium text-gray-700">Updated:</span>
                  <p className="text-gray-900">
                    {new Date(ticket.updatedAt).toLocaleString()}
                  </p>
                </div>
                {ticket.closedAt && (
                  <div>
                    <span className="font-medium text-gray-700">Closed:</span>
                    <p className="text-gray-900">
                      {new Date(ticket.closedAt).toLocaleString()}
                    </p>
                  </div>
                )}
              </div>
            </Card>
          </div>
        </div>
      </div>
    </Layout>
  );
}
