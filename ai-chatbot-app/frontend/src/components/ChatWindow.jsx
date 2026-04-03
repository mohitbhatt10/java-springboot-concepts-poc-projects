import { useRef, useEffect } from "react";
import MessageBubble from "./MessageBubble";
import MessageInput from "./MessageInput";
import LoadingIndicator from "./LoadingIndicator";

function ChatWindow({
  messages,
  isLoading,
  error,
  onSendMessage,
  onClearError,
  selectedModel,
  onModelChange,
}) {
  const messagesEndRef = useRef(null);

  const models = [
    { value: "gemini-2.5-flash", label: "Gemini 2.5 Flash" },
    { value: "gemini-3-flash", label: "Gemini 3 Flash" },
    { value: "gemini-2.5-flash-lite", label: "Gemini 2.5 Flash Lite" },
    { value: "gemini-3.1-flash-lite", label: "Gemini 3.1 Flash Lite" },
  ];

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages, isLoading]);

  return (
    <div className="chat-window">
      <div className="chat-header">
        <div className="model-selector">
          <label htmlFor="model-select">Model:</label>
          <select
            id="model-select"
            value={selectedModel}
            onChange={(e) => onModelChange(e.target.value)}
          >
            {models.map((m) => (
              <option key={m.value} value={m.value}>
                {m.label}
              </option>
            ))}
          </select>
        </div>
      </div>
      <div className="messages-container">
        {messages.length === 0 && !isLoading ? (
          <div className="empty-chat">
            <div className="empty-chat-icon">💬</div>
            <h3>Start a conversation</h3>
            <p>Send a message to begin chatting with AI</p>
          </div>
        ) : (
          <>
            {messages.map((msg) => (
              <MessageBubble key={msg.id} message={msg} />
            ))}
            {isLoading && <LoadingIndicator />}
          </>
        )}
        <div ref={messagesEndRef} />
      </div>

      {error && (
        <div className="error-banner">
          <span>{error}</span>
          <button className="error-dismiss" onClick={onClearError}>
            ✕
          </button>
        </div>
      )}

      <MessageInput onSend={onSendMessage} disabled={isLoading} />
    </div>
  );
}

export default ChatWindow;
