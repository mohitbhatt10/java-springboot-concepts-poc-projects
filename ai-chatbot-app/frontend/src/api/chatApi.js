import axios from "axios";

const api = axios.create({
  baseURL: "/api/chat",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("email");
      window.location.href = "/login";
      return Promise.reject(new Error("Session expired. Please login again."));
    }
    const message =
      error.response?.data?.message ||
      error.message ||
      "An unexpected error occurred";
    console.error("API Error:", message);
    return Promise.reject(new Error(message));
  },
);

export const createSession = (title) =>
  api.post("/sessions", { title }).then((res) => res.data);

export const getSessions = () => api.get("/sessions").then((res) => res.data);

export const sendMessage = (sessionId, message, model) =>
  api
    .post(`/sessions/${sessionId}/messages`, { message, model })
    .then((res) => res.data);

export const getChatHistory = (sessionId) =>
  api.get(`/sessions/${sessionId}/messages`).then((res) => res.data);
