# Help Desk Frontend

A modern Next.js frontend application for the Spring Boot ticketing system.

## Features

- **Dashboard**: Overview of ticket statistics and recent activity
- **Ticket Management**: Create, view, edit, and delete tickets
- **User Management**: View and manage system users
- **Comment System**: Add comments to tickets for collaboration
- **Responsive Design**: Modern UI built with Tailwind CSS
- **Real-time Updates**: Dynamic data fetching and state management

## Prerequisites

- Node.js 18+ 
- Spring Boot backend running on port 8080
- Modern web browser

## Getting Started

1. **Install Dependencies**
   ```bash
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm run dev
   ```

3. **Open Browser**
   Navigate to [http://localhost:3000](http://localhost:3000)

## API Endpoints

The frontend integrates with the following Spring Boot endpoints:

### Tickets
- `GET /api/tickets` - List all tickets with pagination
- `GET /api/tickets/{id}` - Get ticket by ID
- `GET /api/tickets/code/{code}` - Get ticket by code
- `POST /api/tickets` - Create new ticket
- `PUT /api/tickets/{id}` - Update ticket
- `DELETE /api/tickets/{id}` - Delete ticket
- `PATCH /api/tickets/{id}/assign/{assigneeId}` - Assign ticket
- `PATCH /api/tickets/{id}/status/{status}` - Change ticket status
- `GET /api/tickets/status/{status}` - Filter by status
- `GET /api/tickets/priority/{priority}` - Filter by priority
- `GET /api/tickets/search` - Search tickets

### Users
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `GET /api/users/active` - Get active users
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `PATCH /api/users/{id}/deactivate` - Deactivate user

### Comments
- `GET /api/comments/ticket/{ticketId}` - Get comments for ticket
- `POST /api/comments` - Create new comment
- `PUT /api/comments/{id}` - Update comment
- `DELETE /api/comments/{id}` - Delete comment
- `GET /api/comments/ticket/{ticketId}/count` - Get comment count

## Project Structure

```
src/
├── app/                    # Next.js App Router pages
│   ├── page.tsx          # Dashboard
│   ├── tickets/          # Ticket-related pages
│   │   ├── page.tsx      # Tickets list
│   │   ├── create/       # Create ticket
│   │   └── [id]/         # Ticket detail
│   └── users/            # Users management
├── components/            # Reusable components
│   ├── Layout.tsx        # Main layout with navigation
│   └── ui/               # UI components
│       ├── Button.tsx    # Button component
│       ├── Card.tsx      # Card component
│       └── Badge.tsx     # Badge component
└── lib/                  # Utility functions
    └── api.ts            # API service layer
```

## Components

### Layout
The main layout component provides:
- Header with application title
- Sidebar navigation
- Main content area

### UI Components
- **Button**: Configurable button with variants (primary, secondary, danger, outline, ghost)
- **Card**: Container component for content sections
- **Badge**: Status and priority indicators

## State Management

The application uses React hooks for state management:
- `useState` for local component state
- `useEffect` for side effects and data fetching
- Custom API service layer for HTTP requests

## Styling

Built with Tailwind CSS v4 for:
- Responsive design
- Consistent spacing and typography
- Modern UI components
- Dark mode support (CSS variables)

## Development

### Available Scripts
- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run start` - Start production server
- `npm run lint` - Run ESLint

### Code Style
- TypeScript for type safety
- ESLint for code quality
- Prettier for code formatting
- Component-based architecture

## Deployment

1. **Build the Application**
   ```bash
   npm run build
   ```

2. **Start Production Server**
   ```bash
   npm run start
   ```

3. **Environment Variables**
   - Ensure backend is accessible at `http://localhost:8080`
   - Update `API_BASE_URL` in `src/lib/api.ts` if needed

## Troubleshooting

### Common Issues

1. **Backend Connection Error**
   - Ensure Spring Boot application is running on port 8080
   - Check CORS configuration in backend
   - Verify network connectivity

2. **Build Errors**
   - Clear `node_modules` and reinstall dependencies
   - Check Node.js version compatibility
   - Verify TypeScript configuration

3. **Runtime Errors**
   - Check browser console for error messages
   - Verify API endpoint responses
   - Check network tab for failed requests

## Contributing

1. Follow the existing code structure
2. Use TypeScript for all new code
3. Add proper error handling
4. Test with different data scenarios
5. Ensure responsive design

## License

This project is part of the Help Desk Ticketing System.
