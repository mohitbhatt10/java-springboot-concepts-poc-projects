function LoadingIndicator() {
  return (
    <div className="message-bubble assistant">
      <div className="message-content">
        <div className="message-role">AI</div>
        <div className="loading-dots">
          <span className="dot"></span>
          <span className="dot"></span>
          <span className="dot"></span>
        </div>
      </div>
    </div>
  );
}

export default LoadingIndicator;
