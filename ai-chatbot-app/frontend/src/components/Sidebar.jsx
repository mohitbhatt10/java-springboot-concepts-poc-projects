import { useAuth } from "../context/AuthContext";

function Sidebar({ sessions, activeSessionId, onSelectSession, onNewChat }) {
  const { user, logout } = useAuth();

  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <h2>Chats</h2>
        <button className="new-chat-btn" onClick={onNewChat}>
          + New Chat
        </button>
      </div>
      <div className="session-list">
        {sessions.length === 0 ? (
          <p className="no-sessions">No conversations yet</p>
        ) : (
          sessions.map((session) => (
            <div
              key={session.id}
              className={`session-item ${session.id === activeSessionId ? "active" : ""}`}
              onClick={() => onSelectSession(session.id)}
            >
              <span className="session-title">
                {session.title || "New Chat"}
              </span>
              <span className="session-date">
                {new Date(session.updatedAt).toLocaleDateString()}
              </span>
            </div>
          ))
        )}
      </div>
      <div className="sidebar-footer">
        <div className="user-info">
          <span className="user-avatar">
            {user?.username?.charAt(0)?.toUpperCase()}
          </span>
          <span className="user-name">{user?.username}</span>
        </div>
        <button className="logout-btn" onClick={logout}>
          Logout
        </button>
      </div>
    </div>
  );
}

export default Sidebar;
