import { useState } from "react";
import {
  loginUser,
  registerUser,
  getTasks,
  createTask,
  deleteTask,
  updateTask,
} from "./api/api";
import "./App.css";

// Small, local SVG icons keep the UI independent of an icon library.
function Icon({ name, className = "" }) {
  const paths = {
    arrow: "M5 12h14m-6-6 6 6-6 6",
    plus: "M12 5v14M5 12h14",
    logout: "M9 5H5v14h4m6-14 7 7-7 7M9 12h13",
    refresh: "M20 7v5h-5M4 17v-5h5M6.1 6.1A8 8 0 0 1 20 12M4 12a8 8 0 0 0 13.9 5.9",
    edit: "m15 5 4 4M4 20l4-1L20 7a2.8 2.8 0 0 0-4-4L4 15v5Z",
    trash: "M3 6h18M9 6V3h6v3M5 6l1 15h12l1-15M10 10v7m4-7v7",
    check: "m5 12 4 4L19 6",
    list: "M9 6h11M9 12h11M9 18h11M4 6h.01M4 12h.01M4 18h.01",
    clock: "M12 8v4l3 2M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0",
  };

  return (
    <svg
      className={`icon ${className}`}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="1.7"
      strokeLinecap="round"
      strokeLinejoin="round"
      aria-hidden="true"
    >
      <path d={paths[name]} />
    </svg>
  );
}

const statusLabels = {
  PENDING: "Pending",
  IN_PROGRESS: "In progress",
  COMPLETED: "Completed",
};

function App() {
  // Existing form state and request handlers power both views.
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [tasks, setTasks] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [status, setStatus] = useState("PENDING");
  const [editingId, setEditingId] = useState(null);
  const [username, setUsername] = useState("");
  const [registerEmail, setRegisterEmail] = useState("");
  const [registerPassword, setRegisterPassword] = useState("");
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem("token"));

  const handleLogin = async (event) => {
    event.preventDefault();
    try {
      const response = await loginUser(email, password);
      localStorage.setItem("token", response.token);
      setIsLoggedIn(true);
      alert("Login successful");
      await loadTasks();
    } catch {
      alert("Invalid email or password");
    }
  };

  const handleRegister = async (event) => {
    event.preventDefault();
    try {
      await registerUser(username, registerEmail, registerPassword);
      alert("Registration successful. You can now login.");
      setUsername("");
      setRegisterEmail("");
      setRegisterPassword("");
    } catch {
      alert("Registration failed");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    setTasks([]);
    setIsLoggedIn(false);
    alert("Logged out");
  };

  const loadTasks = async () => {
    try {
      const response = await getTasks();
      setTasks(response.content);
    } catch {
      alert("Could not load tasks");
    }
  };

  const handleSubmitTask = async (event) => {
    event.preventDefault();
    try {
      const taskData = { title, description, status };
      if (editingId !== null) {
        await updateTask(editingId, taskData);
        setEditingId(null);
        alert("Task updated");
      } else {
        await createTask(taskData);
        alert("Task created");
      }
      setTitle("");
      setDescription("");
      setStatus("PENDING");
      await loadTasks();
    } catch {
      alert("Could not save task");
    }
  };

  const handleDeleteTask = async (id) => {
    try {
      await deleteTask(id);
      await loadTasks();
      alert("Task deleted");
    } catch {
      alert("Could not delete task");
    }
  };

  const handleEditTask = (task) => {
    setEditingId(task.id);
    setTitle(task.title);
    setDescription(task.description);
    setStatus(task.status);
  };

  return (
    <div className="app-shell">
      <header className="site-header">
        <div className="brand" aria-label="TrackOn task manager">
          <span className="brand-mark">
            <Icon name="check" />
          </span>
          <span>
            TrackOn<span className="brand-dot">.</span>
          </span>
        </div>
        <span className="header-note">
          A little structure. A lot more headspace.
        </span>
        {isLoggedIn ? (
          <button
            className="button button-quiet"
            type="button"
            onClick={handleLogout}
          >
            Logout <Icon name="logout" />
          </button>
        ) : (
          <span className="header-label">
            <span className="status-dot" /> Your day, in focus
          </span>
        )}
      </header>

      <main>
        {!isLoggedIn ? (
          <div className="auth-layout">
            <section className="intro" aria-labelledby="intro-title">
              <p className="eyebrow">
                <span className="small-line" /> LESS CHAOS. MORE CLARITY.
              </p>
              <h1 id="intro-title">
                Make room for<br />
                what <span className="highlight">matters.</span>
              </h1>
              <p className="intro-description">
                Big plans or little to-dos. Give them a place,
                <br className="desktop-break" /> find your focus, and keep moving forward.
              </p>

              <div className="focus-art" aria-hidden="true">
                <div className="orbit orbit-one" />
                <div className="orbit orbit-two" />
                <span className="art-spark">✳</span>
                <div className="mini-task mini-task-back">
                  <span className="mini-check">
                    <Icon name="check" />
                  </span>
                  <span>A fresh perspective</span>
                </div>
                <div className="mini-task mini-task-front">
                  <span className="mini-check">
                    <Icon name="check" />
                  </span>
                  <span>
                    One thing at a time.
                    <small>THAT’S HOW GREAT THINGS HAPPEN.</small>
                  </span>
                  <span className="mini-dot" />
                </div>
                <span className="art-caption">Small steps. Real progress.</span>
              </div>

              <div className="intro-footer">
                <span className="intro-footer-icon">
                  <Icon name="list" />
                </span>
                <p>
                  A clear space for a clearer mind.<br />
                  <strong>Meet your everyday task manager.</strong>
                </p>
              </div>
            </section>

            <section className="auth-panel" aria-labelledby="auth-title">
              <div className="panel-heading">
                <span className="eyebrow">YOUR NEXT CHAPTER</span>
                <span className="panel-index">01 — LET’S BEGIN</span>
              </div>
              <h2 id="auth-title">Good to have you here.</h2>
              <p className="section-description">A more organized day starts right here.</p>

              <div className="auth-forms">
                <form
                  className="form-stack"
                  onSubmit={handleRegister}
                  aria-labelledby="register-title"
                >
                  <div className="form-heading">
                    <span className="form-number">01</span>
                    <div>
                      <h3 id="register-title">Create an account</h3>
                      <p>New here? Make yourself at home.</p>
                    </div>
                  </div>
                  <div className="field">
                    <label htmlFor="register-name">Name</label>
                    <input
                      id="register-name"
                      type="text"
                      autoComplete="name"
                      placeholder="Your name"
                      value={username}
                      onChange={(event) => setUsername(event.target.value)}
                    />
                  </div>
                  <div className="field">
                    <label htmlFor="register-email">Email address</label>
                    <input
                      id="register-email"
                      type="email"
                      autoComplete="email"
                      placeholder="you@example.com"
                      value={registerEmail}
                      onChange={(event) => setRegisterEmail(event.target.value)}
                    />
                  </div>
                  <div className="field">
                    <label htmlFor="register-password">Password</label>
                    <input
                      id="register-password"
                      type="password"
                      autoComplete="new-password"
                      placeholder="Choose a password"
                      value={registerPassword}
                      onChange={(event) => setRegisterPassword(event.target.value)}
                    />
                  </div>
                  <button className="button button-dark form-submit" type="submit">
                    Register <Icon name="arrow" />
                  </button>
                </form>

                <form
                  className="form-stack login-form"
                  onSubmit={handleLogin}
                  aria-labelledby="login-title"
                >
                  <div className="form-heading">
                    <span className="form-number">02</span>
                    <div>
                      <h3 id="login-title">Welcome back</h3>
                      <p>Pick up where you left off.</p>
                    </div>
                  </div>
                  <div className="field">
                    <label htmlFor="login-email">Email address</label>
                    <input
                      id="login-email"
                      type="email"
                      autoComplete="email"
                      placeholder="you@example.com"
                      value={email}
                      onChange={(event) => setEmail(event.target.value)}
                    />
                  </div>
                  <div className="field">
                    <label htmlFor="login-password">Password</label>
                    <input
                      id="login-password"
                      type="password"
                      autoComplete="current-password"
                      placeholder="Your password"
                      value={password}
                      onChange={(event) => setPassword(event.target.value)}
                    />
                  </div>
                  <div className="login-note">
                    <Icon name="check" />
                    <span>
                      Your tasks. Your pace.<br />
                      Let’s make a little progress.
                    </span>
                  </div>
                  <button className="button button-lime form-submit" type="submit">
                    Login <Icon name="arrow" />
                  </button>
                </form>
              </div>
              <div className="panel-footer">
                <span className="status-dot" /> Less juggling. More doing.
              </div>
            </section>
          </div>
        ) : (
          <div className="workspace">
            <section className="workspace-heading" aria-labelledby="workspace-title">
              <div>
                <p className="eyebrow">
                  <span className="small-line" /> YOUR PERSONAL WORKSPACE
                </p>
                <h1 id="workspace-title">
                  Big plans. <span className="highlight">Small steps.</span>
                </h1>
                <p className="section-description">
                  Clear your head. Capture a task. Make your next move.
                </p>
              </div>
              <div className="workspace-stamp" aria-hidden="true">
                <Icon name="check" />
                <span>ONE THING<br />AT A TIME</span>
              </div>
            </section>

            <div className="workspace-grid">
              <section className="composer panel" aria-labelledby="composer-title">
                <div className="composer-heading">
                  <span className="section-icon">
                    <Icon name={editingId !== null ? "edit" : "plus"} />
                  </span>
                  <span className="eyebrow">
                    {editingId !== null ? "A LITTLE FINE-TUNING" : "START WITH AN IDEA"}
                  </span>
                </div>
                <h2 id="composer-title">
                  {editingId !== null ? "Update Task" : "Create Task"}
                </h2>
                <p className="section-description">
                  {editingId !== null
                    ? "A fresh take on your next step."
                    : "Get it out of your head and onto your list."}
                </p>
                <form
                  className="form-stack task-form"
                  onSubmit={handleSubmitTask}
                  aria-labelledby="composer-title"
                >
                  <div className="field">
                    <label htmlFor="task-title">Task title</label>
                    <input
                      id="task-title"
                      type="text"
                      placeholder="What’s on your mind?"
                      value={title}
                      onChange={(event) => setTitle(event.target.value)}
                    />
                  </div>
                  <div className="field">
                    <label htmlFor="task-description">Description</label>
                    <textarea
                      id="task-description"
                      placeholder="Add a little detail…"
                      rows={4}
                      value={description}
                      onChange={(event) => setDescription(event.target.value)}
                    />
                  </div>
                  <div className="field">
                    <label htmlFor="task-status">Status</label>
                    <select
                      id="task-status"
                      value={status}
                      onChange={(event) => setStatus(event.target.value)}
                    >
                      <option value="PENDING">Pending</option>
                      <option value="IN_PROGRESS">In Progress</option>
                      <option value="COMPLETED">Completed</option>
                    </select>
                  </div>
                  <button className="button button-dark form-submit" type="submit">
                    {editingId !== null ? "Update Task" : "Create Task"}
                    <Icon name={editingId !== null ? "check" : "plus"} />
                  </button>
                </form>
                <p className="composer-note">Progress starts with putting it down.</p>
              </section>

              <section className="task-section" aria-labelledby="tasks-title">
                <div className="task-list-heading">
                  <div>
                    <span className="eyebrow">A LITTLE MORE ORGANIZED</span>
                    <h2 id="tasks-title">
                      My Tasks <span className="task-count">{tasks.length}</span>
                    </h2>
                  </div>
                  <button
                    className="button button-outline"
                    type="button"
                    onClick={loadTasks}
                  >
                    <Icon name="refresh" />Load Tasks
                  </button>
                </div>
                {tasks.length === 0 ? (
                  <div className="empty-state">
                    <div className="empty-illustration" aria-hidden="true">
                      <span />
                      <span />
                      <Icon name="check" />
                    </div>
                    <p className="eyebrow">ROOM FOR YOUR NEXT BIG THING</p>
                    <h3>A little space. A fresh start.</h3>
                    <p>
                      Create your first task, or use <strong>Load Tasks</strong><br />
                      to bring your existing list into view.
                    </p>
                  </div>
                ) : (
                  <div className="task-list">
                    {tasks.map((task, index) => (
                      <article
                        className={`task-card ${editingId === task.id ? "task-card-editing" : ""}`}
                        key={task.id}
                      >
                        <div className="task-card-top">
                          <span className="task-sequence">
                            {String(index + 1).padStart(2, "0")}
                          </span>
                          <span
                            className={`status-badge status-${String(task.status ?? "unknown").toLowerCase()}`}
                          >
                            <span className="status-dot" />
                            {statusLabels[task.status] || task.status}
                          </span>
                        </div>
                        <h3>{task.title}</h3>
                        <p className="task-description">{task.description}</p>
                        <div className="task-card-footer">
                          <span className="task-caption">
                            <Icon name={task.status === "COMPLETED" ? "check" : "clock"} />
                            {task.status === "COMPLETED"
                              ? "One more thing, done."
                              : "One step closer."}
                          </span>
                          <div className="task-actions">
                            <button
                              className="button button-small"
                              type="button"
                              onClick={() => handleEditTask(task)}
                              aria-label={`Edit ${task.title}`}
                            >
                              <Icon name="edit" />Edit
                            </button>
                            <button
                              className="button button-small button-delete"
                              type="button"
                              onClick={() => handleDeleteTask(task.id)}
                              aria-label={`Delete ${task.title}`}
                            >
                              <Icon name="trash" />Delete
                            </button>
                          </div>
                        </div>
                      </article>
                    ))}
                  </div>
                )}
                <p className="task-list-note">Make space for what matters. The rest can wait.</p>
              </section>
            </div>
          </div>
        )}
      </main>

      <footer className="site-footer">
        <span>
          TRACKON <span className="footer-slash">/</span> A TASK MANAGEMENT SYSTEM
        </span>
        <span>
          Made for a little more focus.
          <span className="footer-asterisk" aria-hidden="true">✳</span>
        </span>
      </footer>
    </div>
  );
}

export default App;
