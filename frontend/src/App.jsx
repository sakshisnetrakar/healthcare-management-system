import { useState } from "react";
import {
  LayoutDashboard,
  Users,
  UserRound,
  CalendarDays,
  Building2,
  FileText,
  Pill,
  Menu,
  Search,
  Bell,
  Activity,
  Clock,
  CheckCircle,
} from "lucide-react";
import "./App.css";

function App() {
  const [activePage, setActivePage] = useState("Dashboard");

  const navigation = [
    { name: "Dashboard", icon: LayoutDashboard },
    { name: "Patients", icon: Users },
    { name: "Doctors", icon: UserRound },
    { name: "Appointments", icon: CalendarDays },
    { name: "Departments", icon: Building2 },
    { name: "Medical Reports", icon: FileText },
    { name: "Prescriptions", icon: Pill },
  ];

  const stats = [
    { title: "Total Patients", value: "—", icon: Users },
    { title: "Total Doctors", value: "—", icon: UserRound },
    { title: "Appointments", value: "—", icon: CalendarDays },
    { title: "Departments", value: "—", icon: Building2 },
  ];

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-icon">
            <Activity size={25} />
          </div>
          <div>
            <h2>HealthCare</h2>
            <span>Management System</span>
          </div>
        </div>

        <p className="menu-label">MAIN MENU</p>

        <nav>
          {navigation.map(({ name, icon: Icon }) => (
            <button
              key={name}
              className={`nav-item ${
                activePage === name ? "active" : ""
              }`}
              onClick={() => setActivePage(name)}
            >
              <Icon size={19} />
              <span>{name}</span>
            </button>
          ))}
        </nav>

        <div className="sidebar-footer">
          <Activity size={18} />
          <span>Healthcare Portal</span>
        </div>
      </aside>

      <main className="main">
        <header className="topbar">
          <button className="icon-button mobile-menu" aria-label="Menu">
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
              <strong>Administrator</strong>
              <span>Admin Portal</span>
            </div>
          </div>
        </header>

        <section className="content">
          <div className="welcome">
            <div>
              <p className="eyebrow">HEALTHCARE OVERVIEW</p>
              <h1>{activePage}</h1>
              <p className="subtitle">
                Manage your hospital operations from one place.
              </p>
            </div>

            <div className="date-label">
              <CalendarDays size={17} />
              Hospital Management
            </div>
          </div>

          {activePage === "Dashboard" ? (
            <>
              <div className="stats-grid">
                {stats.map(({ title, value, icon: Icon }) => (
                  <div className="stat-card" key={title}>
                    <div className="stat-top">
                      <span>{title}</span>
                      <div className="stat-icon">
                        <Icon size={21} />
                      </div>
                    </div>
                    <h2>{value}</h2>
                    <p>Connect backend to load data</p>
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
                      Patient, doctor and appointment statistics will appear
                      here once the backend is connected.
                    </p>
                  </div>
                </section>

                <section className="panel quick-panel">
                  <div className="panel-heading">
                    <div>
                      <h3>Quick Actions</h3>
                      <p>Navigate to a section</p>
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
                        onClick={() => setActivePage(name)}
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
                    <h3>System Status</h3>
                    <p>Application workspace</p>
                  </div>
                </div>

                <div className="status-row">
                  <div className="status-item">
                    <CheckCircle size={19} />
                    <div>
                      <strong>Frontend</strong>
                      <span>Running</span>
                    </div>
                  </div>
                  <div className="status-item pending">
                    <Clock size={19} />
                    <div>
                      <strong>Backend integration</strong>
                      <span>Next step</span>
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
                The {activePage.toLowerCase()} page will be implemented next
                and connected to your Spring Boot REST APIs.
              </p>
              <button
                className="primary-button"
                onClick={() => setActivePage("Dashboard")}
              >
                Back to Dashboard
              </button>
            </section>
          )}
        </section>
      </main>
    </div>
  );
}

export default App;
