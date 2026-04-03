function MessageBubble({ message }) {
  const isUser = message.role === "USER";

  return (
    <div className={`message-bubble ${isUser ? "user" : "assistant"}`}>
      <div className="message-content">
        <div className="message-role">{isUser ? "You" : "AI"}</div>
        <div className="message-text">{message.content}</div>
        <div className="message-time">
          {new Date(message.createdAt).toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit",
          })}
        </div>
      </div>
    </div>
  );
}

export default MessageBubble;
