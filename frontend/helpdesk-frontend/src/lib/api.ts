const API_BASE_URL = 'http://localhost:8080/api';

// Types
export interface User {
  id: number;
  fullName: string;
  email: string;
  active: boolean;
  roleNames: string[];
  createdAt: string;
  updatedAt: string;
}

export interface Comment {
  id: number;
  body: string;
  author: User;
  ticketId: number;
  createdAt: string;
  updatedAt: string;
}

export interface Ticket {
  id: number;
  code: string;
  subject: string;
  description: string;
  status: string;
  priority: string;
  owner: User;
  assignee: User | null;
  closedAt: string | null;
  comments: Comment[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateTicketData {
  subject: string;
  description: string;
  priority: string;
}

export interface UpdateTicketData {
  subject?: string;
  description?: string;
  priority?: string;
}

export interface CreateCommentData {
  body: string;
  ticketId: number;
}

export interface CreateUserData {
  fullName: string;
  email: string;
  password: string;
}

// API Functions
export const api = {
  // Ticket endpoints
  tickets: {
    getAll: async (page = 0, size = 10, sortBy = 'createdAt', sortDir = 'desc'): Promise<{ content: Ticket[]; totalElements: number; totalPages: number }> => {
      const response = await fetch(`${API_BASE_URL}/tickets?page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`);
      if (!response.ok) throw new Error('Failed to fetch tickets');
      return response.json();
    },

    getById: async (id: number): Promise<Ticket> => {
      const response = await fetch(`${API_BASE_URL}/tickets/${id}`);
      if (!response.ok) throw new Error('Failed to fetch ticket');
      return response.json();
    },

    getByCode: async (code: string): Promise<Ticket> => {
      const response = await fetch(`${API_BASE_URL}/tickets/code/${code}`);
      if (!response.ok) throw new Error('Failed to fetch ticket');
      return response.json();
    },

    create: async (data: CreateTicketData, ownerId: number): Promise<Ticket> => {
      const response = await fetch(`${API_BASE_URL}/tickets?ownerId=${ownerId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });
      if (!response.ok) throw new Error('Failed to create ticket');
      return response.json();
    },

    update: async (id: number, data: UpdateTicketData): Promise<Ticket> => {
      const response = await fetch(`${API_BASE_URL}/tickets/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });
      if (!response.ok) throw new Error('Failed to update ticket');
      return response.json();
    },

    delete: async (id: number): Promise<void> => {
      const response = await fetch(`${API_BASE_URL}/tickets/${id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error('Failed to delete ticket');
    },

    assign: async (id: number, assigneeId: number): Promise<void> => {
      const response = await fetch(`${API_BASE_URL}/tickets/${id}/assign/${assigneeId}`, { method: 'PATCH' });
      if (!response.ok) throw new Error('Failed to assign ticket');
    },

    changeStatus: async (id: number, status: string): Promise<void> => {
      const response = await fetch(`${API_BASE_URL}/tickets/${id}/status/${status}`, { method: 'PATCH' });
      if (!response.ok) throw new Error('Failed to change ticket status');
    },

    getByStatus: async (status: string, page = 0, size = 10): Promise<{ content: Ticket[]; totalElements: number; totalPages: number }> => {
      const response = await fetch(`${API_BASE_URL}/tickets/status/${status}?page=${page}&size=${size}`);
      if (!response.ok) throw new Error('Failed to fetch tickets by status');
      return response.json();
    },

    getByPriority: async (priority: string, page = 0, size = 10): Promise<{ content: Ticket[]; totalElements: number; totalPages: number }> => {
      const response = await fetch(`${API_BASE_URL}/tickets/priority/${priority}?page=${page}&size=${size}`);
      if (!response.ok) throw new Error('Failed to fetch tickets by priority');
      return response.json();
    },

    search: async (searchTerm: string, page = 0, size = 10): Promise<{ content: Ticket[]; totalElements: number; totalPages: number }> => {
      const response = await fetch(`${API_BASE_URL}/tickets/search?searchTerm=${encodeURIComponent(searchTerm)}&page=${page}&size=${size}`);
      if (!response.ok) throw new Error('Failed to search tickets');
      return response.json();
    },
  },

  // User endpoints
  users: {
    getAll: async (): Promise<User[]> => {
      const response = await fetch(`${API_BASE_URL}/users`);
      if (!response.ok) throw new Error('Failed to fetch users');
      return response.json();
    },

    getById: async (id: number): Promise<User> => {
      const response = await fetch(`${API_BASE_URL}/users/${id}`);
      if (!response.ok) throw new Error('Failed to fetch user');
      return response.json();
    },

    getByEmail: async (email: string): Promise<User> => {
      const response = await fetch(`${API_BASE_URL}/users/email/${email}`);
      if (!response.ok) throw new Error('Failed to fetch user');
      return response.json();
    },

    getActive: async (): Promise<User[]> => {
      const response = await fetch(`${API_BASE_URL}/users/active`);
      if (!response.ok) throw new Error('Failed to fetch active users');
      return response.json();
    },

    create: async (data: CreateUserData): Promise<User> => {
      const response = await fetch(`${API_BASE_URL}/users`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });
      if (!response.ok) throw new Error('Failed to create user');
      return response.json();
    },

    update: async (id: number, data: CreateUserData): Promise<User> => {
      const response = await fetch(`${API_BASE_URL}/users/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });
      if (!response.ok) throw new Error('Failed to update user');
      return response.json();
    },

    delete: async (id: number): Promise<void> => {
      const response = await fetch(`${API_BASE_URL}/users/${id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error('Failed to delete user');
    },

    deactivate: async (id: number): Promise<void> => {
      const response = await fetch(`${API_BASE_URL}/users/${id}/deactivate`, { method: 'PATCH' });
      if (!response.ok) throw new Error('Failed to deactivate user');
    },
  },

  // Comment endpoints
  comments: {
    getByTicketId: async (ticketId: number): Promise<Comment[]> => {
      const response = await fetch(`${API_BASE_URL}/comments/ticket/${ticketId}`);
      if (!response.ok) throw new Error('Failed to fetch comments');
      return response.json();
    },

    create: async (data: CreateCommentData, authorId: number): Promise<Comment> => {
      const response = await fetch(`${API_BASE_URL}/comments?authorId=${authorId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });
      if (!response.ok) throw new Error('Failed to create comment');
      return response.json();
    },

    update: async (id: number, body: string, authorId: number): Promise<Comment> => {
      const response = await fetch(`${API_BASE_URL}/comments/${id}?body=${encodeURIComponent(body)}&authorId=${authorId}`, {
        method: 'PUT',
      });
      if (!response.ok) throw new Error('Failed to update comment');
      return response.json();
    },

    delete: async (id: number, authorId: number): Promise<void> => {
      const response = await fetch(`${API_BASE_URL}/comments/${id}?authorId=${authorId}`, { method: 'DELETE' });
      if (!response.ok) throw new Error('Failed to delete comment');
    },

    getCount: async (ticketId: number): Promise<number> => {
      const response = await fetch(`${API_BASE_URL}/comments/ticket/${ticketId}/count`);
      if (!response.ok) throw new Error('Failed to fetch comment count');
      return response.json();
    },
  },
};
