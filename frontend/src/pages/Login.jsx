import { useState } from "react";
import { Activity, Mail, Lock, Eye, EyeOff } from "lucide-react";
import "./Login.css";
import api from "../api/axios";
import { useNavigate } from "react-router-dom";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
  e.preventDefault();

  try {
    const response = await api.post("/auth/login", {
      email,
      password,
    });

    const token = response.data.token;

    if (!token) {
      alert("Login response did not contain a token.");
      return;
    }

    sessionStorage.setItem("token", token);

    alert("Login successful!");
    navigate("/dashboard");
  } catch (error) {
    alert(
      error.response?.data?.message ||
        "Login failed. Please check your credentials and backend."
    );
  }
  };


  return (
    <div className="login-page">
      <div className="login-left">
        <div className="login-brand">
          <div className="login-brand-icon">
            <Activity size={26} />
          </div>
          <span>HealthCare</span>
        </div>

        <div className="login-message">
          <span className="login-tag">SMART HEALTHCARE MANAGEMENT</span>
          <h1>
            Better care.
            <br />
            Smarter management.
          </h1>
          <p>
            Manage patients, doctors, appointments and medical records
            through one simple healthcare platform.
          </p>

          <div className="login-feature">
            <Activity size={20} />
            <span>One platform for hospital operations</span>
          </div>
        </div>

        <p className="login-copyright">
          © 2026 HealthCare Management System
        </p>
      </div>

      <div className="login-right">
        <form className="login-card" onSubmit={handleSubmit}>
          <div className="login-mobile-logo">
            <Activity size={25} />
          </div>

          <p className="login-welcome">WELCOME BACK</p>
          <h2>Sign in to your account</h2>
          <p className="login-description">
            Enter your credentials to access your dashboard.
          </p>

          <label htmlFor="email">Email address</label>
          <div className="login-input">
            <Mail size={19} />
            <input
              id="email"
              type="email"
              placeholder="you@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              autoComplete="username"
              required
            />
          </div>

          <label htmlFor="password">Password</label>
          <div className="login-input">
            <Lock size={19} />
            <input
              id="password"
              type={showPassword ? "text" : "password"}
              placeholder="Enter your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
              required
            />
            <button
              type="button"
              className="password-toggle"
              onClick={() => setShowPassword(!showPassword)}
              aria-label={showPassword ? "Hide password" : "Show password"}
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>

          <button type="submit" className="login-submit">
            Sign In
          </button>

          <p className="login-security">
            <Lock size={14} />
            Secure access to your healthcare portal
          </p>
        </form>
      </div>
    </div>
  );
}

export default Login;
