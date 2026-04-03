import { useState, useEffect, useCallback } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import ChatWindow from "./components/ChatWindow";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import OAuth2Callback from "./components/OAuth2Callback";
import { useAuth } from "./context/AuthContext";
import {
  createSession,
  getSessions,
  sendMessage as sendMessageApi,
  getChatHistory,
} from "./api/chatApi";

function App() {
  const { user, loading: authLoading } = useAuth();
  const [sessions, setSessions] = useState([]);
  const [activeSessionId, setActiveSessionId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedModel, setSelectedModel] = useState("gemini-2.5-flash");

  const fetchSessions = useCallback(async () => {
    try {
      const data = await getSessions();
      setSessions(data);
    } catch (err) {
      console.error("Failed to fetch sessions:", err);
    }
  }, []);

  useEffect(() => {
    if (user) {
      fetchSessions();
    }
  }, [fetchSessions, user]);

  const handleSelectSession = useCallback(async (sessionId) => {
    setActiveSessionId(sessionId);
    setMessages([]);
    setError(null);
    try {
      const data = await getChatHistory(sessionId);
      setMessages(data);
    } catch (err) {
      setError("Failed to load chat history");
      console.error(err);
    }
  }, []);

  const handleNewChat = useCallback(async () => {
    try {
      const session = await createSession("New Chat");
      setSessions((prev) => [session, ...prev]);
      setActiveSessionId(session.id);
      setMessages([]);
      setError(null);
    } catch (err) {
      setError("Failed to create new chat");
      console.error(err);
    }
  }, []);

  const handleSendMessage = useCallback(
    async (messageText) => {
      setError(null);
      let sessionId = activeSessionId;

      // Auto-create session if none is active
      if (!sessionId) {
        try {
          const session = await createSession(null);
          setSessions((prev) => [session, ...prev]);
          setActiveSessionId(session.id);
          sessionId = session.id;
        } catch (err) {
          setError("Failed to create chat session");
          return;
        }
      }

      // Optimistically add user message
      const userMessage = {
        id: crypto.randomUUID(),
        role: "USER",
        content: messageText,
        createdAt: new Date().toISOString(),
      };
      setMessages((prev) => [...prev, userMessage]);
      setIsLoading(true);

      try {
        const aiResponse = await sendMessageApi(
          sessionId,
          messageText,
          selectedModel,
        );
        setMessages((prev) => [...prev, aiResponse]);
        // Refresh sessions to update title and timestamp
        fetchSessions();
      } catch (err) {
        setError(err.message || "Failed to send message");
        // Remove the optimistic user message on error
        setMessages((prev) => prev.filter((m) => m.id !== userMessage.id));
      } finally {
        setIsLoading(false);
      }
    },
    [activeSessionId, fetchSessions, selectedModel],
  );

  if (authLoading) {
    return (
      <div className="auth-page">
        <div className="auth-card">
          <p>Loading...</p>
        </div>
      </div>
    );
  }

  return (
    <Routes>
      <Route
        path="/login"
        element={user ? <Navigate to="/" /> : <LoginPage />}
      />
      <Route
        path="/register"
        element={user ? <Navigate to="/" /> : <RegisterPage />}
      />
      <Route path="/oauth2/callback" element={<OAuth2Callback />} />
      <Route
        path="/"
        element={
          !user ? (
            <Navigate to="/login" />
          ) : (
            <div className="app">
              <Sidebar
                sessions={sessions}
                activeSessionId={activeSessionId}
                onSelectSession={handleSelectSession}
                onNewChat={handleNewChat}
              />
              <ChatWindow
                messages={messages}
                isLoading={isLoading}
                error={error}
                onSendMessage={handleSendMessage}
                onClearError={() => setError(null)}
                selectedModel={selectedModel}
                onModelChange={setSelectedModel}
              />
            </div>
          )
        }
      />
    </Routes>
  );
}

export default App;
