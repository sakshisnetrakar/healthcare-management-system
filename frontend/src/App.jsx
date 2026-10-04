import Login from "./pages/Login";
import Patients from "./pages/Patients";
import { useState } from "react";
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
  useNavigate,
} from "react-router-dom";

import {
  LayoutDashboard,
  Users,
  UserRound,
  CalendarDays,
  Building2,
  FileText,
  Pill,
  Search,
  Bell,
  Activity,
  LogOut,
  Menu,
  X,
} from "lucide-react";


import "./App.css";

const navigation = [
  { name: "Dashboard", icon: LayoutDashboard },
  { name: "Patients", icon: Users },
  { name: "Doctors", icon: UserRound },
  { name: "Appointments", icon: CalendarDays },
  { name: "Departments", icon: Building2 },
  { name: "Medical Reports", icon: FileText },
  { name: "Prescriptions", icon: Pill },
];

function ProtectedDashboard() {
  const [activePage, setActivePage] = useState("Dashboard");
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const navigate = useNavigate();
  const token = sessionStorage.getItem("token");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  const logout = () => {
    sessionStorage.removeItem("token");
    navigate("/login", { replace: true });
  };

  const selectPage = (page) => {
    setActivePage(page);
    setSidebarOpen(false);
  };

  const stats = [
    { title: "Total Patients", icon: Users },
    { title: "Total Doctors", icon: UserRound },
    { title: "Appointments", icon: CalendarDays },
    { title: "Departments", icon: Building2 },
  ];

  return (
    <div className="app">
      {sidebarOpen && (
        <button
          className="sidebar-overlay"
          aria-label="Close navigation"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      <aside className={`sidebar ${sidebarOpen ? "sidebar-open" : ""}`}>
        <div className="brand">
          <div className="brand-icon">
            <Activity size={25} />
          </div>

          <div>
            <h2>HealthCare</h2>
            <span>Management System</span>
          </div>

          <button
            className="close-sidebar"
            onClick={() => setSidebarOpen(false)}
            aria-label="Close sidebar"
          >
            <X size={20} />
          </button>
        </div>

        <p className="menu-label">MAIN MENU</p>

        <nav>
          {navigation.map(({ name, icon: Icon }) => (
            <button
              key={name}
              className={`nav-item ${
                activePage === name ? "active" : ""
              }`}
              onClick={() => selectPage(name)}
            >
              <Icon size={19} />
              <span>{name}</span>
            </button>
          ))}
        </nav>

        <button className="logout-button" onClick={logout}>
          <LogOut size={19} />
          <span>Logout</span>
        </button>

        <div className="sidebar-footer">
          <Activity size={18} />
          <span>Healthcare Portal</span>
        </div>
      </aside>

      <main className="main">
        <header className="topbar">
          <button
            className="icon-button mobile-menu"
            onClick={() => setSidebarOpen(true)}
            aria-label="Open navigation"
          >
            <Menu size={22} />
          </button>

          <div className="breadcrumb">
            <span>Pages</span>
            <span>/</span>
            <strong>{activePage}</strong>
          </div>

          <div className="top-actions">
            <label className="search-box">
              <Search size={18} />
              <input placeholder="Search..." />
            </label>

            <button className="icon-button" aria-label="Notifications">
              <Bell size={20} />
            </button>

            <div className="avatar">A</div>

            <div className="profile">
              <strong>Healthcare User</strong>
              <span>Signed in</span>
            </div>
          </div>
        </header>

        <section className="content">
          {activePage === "Patients" ? (
            <Patients />
          ) : (
            <>
              <div className="welcome">
                <div>
                  <p className="eyebrow">HEALTHCARE OVERVIEW</p>

                  <h1>
                    {activePage === "Dashboard"
                      ? "Welcome back!"
                      : activePage}
                  </h1>

                  <p className="subtitle">
                    Manage your hospital operations from one place.
                  </p>
                </div>

                <div className="date-label">
                  <Activity size={17} />
                  Healthcare Management
                </div>
              </div>

              {activePage === "Dashboard" ? (
                <>
                  <div className="stats-grid">
                    {stats.map(({ title, icon: Icon }) => (
                      <div className="stat-card" key={title}>
                        <div className="stat-top">
                          <span>{title}</span>

                          <div className="stat-icon">
                            <Icon size={21} />
                          </div>
                        </div>

                        <h2>—</h2>
                        <p>Awaiting backend integration</p>
                      </div>
                    ))}
                  </div>

                  <div className="dashboard-grid">
                    <section className="panel activity-panel">
                      <div className="panel-heading">
                        <div>
                          <h3>Hospital Overview</h3>
                          <p>Your management workspace</p>
                        </div>

                        <Activity size={21} />
                      </div>

                      <div className="overview-placeholder">
                        <div className="overview-icon">
                          <Activity size={32} />
                        </div>

                        <h3>Your hospital at a glance</h3>

                        <p>
                          Patient, doctor and appointment statistics will
                          appear here after connecting the backend APIs.
                        </p>
                      </div>
                    </section>

                    <section className="panel quick-panel">
                      <div className="panel-heading">
                        <div>
                          <h3>Quick Actions</h3>
                          <p>Open a management section</p>
                        </div>
                      </div>

                      <div className="quick-actions">
                        {[
                          ["Patients", Users],
                          ["Doctors", UserRound],
                          ["Appointments", CalendarDays],
                          ["Departments", Building2],
                        ].map(([name, Icon]) => (
                          <button
                            className="quick-action"
                            key={name}
                            onClick={() => selectPage(name)}
                          >
                            <span className="quick-icon">
                              <Icon size={19} />
                            </span>

                            <span>{name}</span>
                            <span className="arrow">→</span>
                          </button>
                        ))}
                      </div>
                    </section>
                  </div>

                  <section className="panel bottom-panel">
                    <div className="panel-heading">
                      <div>
                        <h3>System Information</h3>
                        <p>Application status</p>
                      </div>
                    </div>

                    <div className="status-row">
                      <div className="status-item">
                        <Activity size={19} />

                        <div>
                          <strong>React Frontend</strong>
                          <span>Running</span>
                        </div>
                      </div>

                      <div className="status-item">
                        <Activity size={19} />

                        <div>
                          <strong>JWT Session</strong>
                          <span>Token present</span>
                        </div>
                      </div>
                    </div>
                  </section>
                </>
              ) : (
                <section className="panel page-placeholder">
                  <div className="overview-icon">
                    {(() => {
                      const item = navigation.find(
                        (entry) => entry.name === activePage
                      );

                      const Icon = item?.icon || FileText;

                      return <Icon size={32} />;
                    })()}
                  </div>

                  <h2>{activePage}</h2>

                  <p>
                    This section is ready for its management interface
                    and Spring Boot API integration.
                  </p>

                  <button
                    className="primary-button"
                    onClick={() => selectPage("Dashboard")}
                  >
                    Back to Dashboard
                  </button>
                </section>
              )}
            </>
          )}
        </section>
      </main>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />

        <Route
          path="/dashboard"
          element={<ProtectedDashboard />}
        />

        <Route
          path="/"
          element={<Navigate to="/dashboard" replace />}
        />

        <Route
          path="*"
          element={<Navigate to="/dashboard" replace />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;