import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function OAuth2Callback() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { loginUser } = useAuth();

  useEffect(() => {
    const token = searchParams.get("token");
    if (token) {
      // Decode basic info from JWT payload
      try {
        const payload = JSON.parse(atob(token.split(".")[1]));
        loginUser({
          token,
          username: payload.sub,
          email: payload.sub,
        });
      } catch {
        loginUser({ token, username: "User", email: "" });
      }
      navigate("/");
    } else {
      navigate("/login");
    }
  }, [searchParams, loginUser, navigate]);

  return (
    <div className="auth-page">
      <div className="auth-card">
        <p>Completing sign in...</p>
      </div>
    </div>
  );
}

export default OAuth2Callback;
