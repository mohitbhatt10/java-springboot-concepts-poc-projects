# AI Chatbot Application

A full-stack AI chatbot application with Google Gemini integration, built with Spring Boot and React.

## Tech Stack

**Backend:**

- Java 21
- Spring Boot 3.4.2
- Spring Data JPA + PostgreSQL
- WebClient (for Gemini API calls)
- Lombok

**Frontend:**

- React 19 with Vite
- Axios for API calls

## Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 15+
- Node.js 18+
- Google Gemini API Key

## Setup

### 1. Database

Create a PostgreSQL database:

```sql
CREATE DATABASE ai_chatbot;
```

### 2. Environment Variables

Set the following environment variables:

```bash
# Required
export GEMINI_API_KEY=your-gemini-api-key

# Optional (defaults shown)
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=ai_chatbot
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
```

### 3. Backend

```bash
cd ai-chatbot-app
mvn clean install
mvn spring-boot:run
```

The backend starts at `http://localhost:8084`.

### 4. Frontend

```bash
cd ai-chatbot-app/frontend
npm install
npm run dev
```

The frontend starts at `http://localhost:5173` with API proxying to the backend.

## API Endpoints

| Method | Endpoint                           | Description                        |
| ------ | ---------------------------------- | ---------------------------------- |
| POST   | `/api/chat/sessions`               | Create a new chat session          |
| GET    | `/api/chat/sessions`               | List all chat sessions             |
| POST   | `/api/chat/sessions/{id}/messages` | Send a message and get AI response |
| GET    | `/api/chat/sessions/{id}/messages` | Get chat history for a session     |

### Example Requests

**Create Session:**

```bash
curl -X POST http://localhost:8084/api/chat/sessions \
  -H "Content-Type: application/json" \
  -d '{"title": "My Chat"}'
```

**Send Message:**

```bash
curl -X POST http://localhost:8084/api/chat/sessions/{sessionId}/messages \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello, explain Spring Boot to me"}'
```

## Project Structure

```
ai-chatbot-app/
├── pom.xml
├── src/main/java/com/example/aichatbot/
│   ├── AiChatbotApplication.java
│   ├── config/
│   │   ├── CorsConfig.java
│   │   └── WebClientConfig.java
│   ├── controller/
│   │   └── ChatController.java
│   ├── dto/
│   │   ├── ChatMessageResponse.java
│   │   ├── ChatSessionResponse.java
│   │   ├── CreateSessionRequest.java
│   │   ├── ErrorResponse.java
│   │   ├── SendMessageRequest.java
│   │   └── gemini/
│   │       ├── GeminiRequest.java
│   │       └── GeminiResponse.java
│   ├── entity/
│   │   ├── ChatMessage.java
│   │   ├── ChatSession.java
│   │   └── MessageRole.java
│   ├── exception/
│   │   ├── GeminiApiException.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── ResourceNotFoundException.java
│   ├── repository/
│   │   ├── ChatMessageRepository.java
│   │   └── ChatSessionRepository.java
│   └── service/
│       ├── ChatService.java
│       ├── GeminiService.java
│       └── impl/
│           ├── ChatServiceImpl.java
│           └── GeminiServiceImpl.java
├── src/main/resources/
│   └── application.yml
└── frontend/
    ├── package.json
    ├── vite.config.js
    ├── index.html
    └── src/
        ├── main.jsx
        ├── App.jsx
        ├── api/
        │   └── chatApi.js
        ├── components/
        │   ├── ChatWindow.jsx
        │   ├── LoadingIndicator.jsx
        │   ├── MessageBubble.jsx
        │   ├── MessageInput.jsx
        │   └── Sidebar.jsx
        └── styles/
            └── App.css
```
