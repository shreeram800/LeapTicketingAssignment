'use client';

import React, { useEffect, useState } from 'react';
import Layout from '@/components/Layout';
import Card from '@/components/ui/Card';
import { api, Ticket } from '@/lib/api';

export default function Dashboard() {
  const [stats, setStats] = useState({
    totalTickets: 0,
    openTickets: 0,
    closedTickets: 0,
    highPriorityTickets: 0,
  });
  const [recentTickets, setRecentTickets] = useState<Ticket[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [allTickets, openTickets, closedTickets, highPriorityTickets] = await Promise.all([
          api.tickets.getAll(0, 1000),
          api.tickets.getByStatus('OPEN', 0, 1000),
          api.tickets.getByStatus('CLOSED', 0, 1000),
          api.tickets.getByPriority('HIGH', 0, 1000),
        ]);

        setStats({
          totalTickets: allTickets.totalElements,
          openTickets: openTickets.totalElements,
          closedTickets: closedTickets.totalElements,
          highPriorityTickets: highPriorityTickets.totalElements,
        });

        setRecentTickets(allTickets.content.slice(0, 5));
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  if (loading) {
    return (
      <Layout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="space-y-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
          <p className="mt-2 text-gray-600">Overview of your help desk system</p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card>
            <div className="text-center">
              <div className="text-3xl font-bold text-blue-600">{stats.totalTickets}</div>
              <div className="text-sm text-gray-600">Total Tickets</div>
            </div>
          </Card>
          
          <Card>
            <div className="text-center">
              <div className="text-3xl font-bold text-yellow-600">{stats.openTickets}</div>
              <div className="text-sm text-gray-600">Open Tickets</div>
            </div>
          </Card>
          
          <Card>
            <div className="text-center">
              <div className="text-3xl font-bold text-green-600">{stats.closedTickets}</div>
              <div className="text-sm text-gray-600">Closed Tickets</div>
            </div>
          </Card>
          
          <Card>
            <div className="text-center">
              <div className="text-3xl font-bold text-red-600">{stats.highPriorityTickets}</div>
              <div className="text-sm text-gray-600">High Priority</div>
            </div>
          </Card>
        </div>

        {/* Recent Tickets */}
        <Card>
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Recent Tickets</h2>
          {recentTickets.length === 0 ? (
            <p className="text-gray-500 text-center py-8">No tickets found</p>
          ) : (
            <div className="space-y-3">
              {recentTickets.map((ticket) => (
                <div key={ticket.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div className="flex-1">
                    <div className="flex items-center space-x-3">
                      <span className="text-sm font-medium text-gray-900">#{ticket.code}</span>
                      <span className="text-sm text-gray-600">{ticket.subject}</span>
                    </div>
                    <div className="mt-1 flex items-center space-x-2">
                      <span className="text-xs text-gray-500">
                        {ticket.owner?.fullName || 'Unknown'}
                      </span>
                      <span className="text-xs text-gray-400">•</span>
                      <span className="text-xs text-gray-500">
                        {new Date(ticket.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                  </div>
                  <div className="flex items-center space-x-2">
                    <span className={`px-2 py-1 text-xs rounded-full ${
                      ticket.status === 'OPEN' ? 'bg-green-100 text-green-800' :
                      ticket.status === 'IN_PROGRESS' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-gray-100 text-gray-800'
                    }`}>
                      {ticket.status}
                    </span>
                    <span className={`px-2 py-1 text-xs rounded-full ${
                      ticket.priority === 'HIGH' ? 'bg-red-100 text-red-800' :
                      ticket.priority === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-green-100 text-green-800'
                    }`}>
                      {ticket.priority}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </Card>
      </div>
    </Layout>
  );
}
