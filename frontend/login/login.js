const API_BASE = "http://localhost:8080";

// ── toggle password visibility
function togglePassword() {
  const input = document.getElementById("password");
  const btn = document.getElementById("eyeBtn");
  if (input.type === "password") {
    input.type = "text";
    btn.textContent = "🙈";
  } else {
    input.type = "password";
    btn.textContent = "👁";
  }
}

// ── show alert
function showAlert(message, type) {
  const box = document.getElementById("alertBox");
  box.textContent = message;
  box.className = "alert " + type;
  box.style.display = "block";
}

// ── set loading state
function setLoading(loading) {
  const btn = document.getElementById("loginBtn");
  const text = document.getElementById("btnText");
  const loader = document.getElementById("btnLoader");
  btn.disabled = loading;
  text.style.display = loading ? "none" : "block";
  loader.style.display = loading ? "block" : "none";
}

// ── login function
async function login() {
  const username = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value.trim();

  if (!username || !password) {
    showAlert("Please fill in all fields.", "error");
    return;
  }
  if (!username.includes("@")) {
    showAlert("Please enter a valid email.", "error");
    return;
  }

  setLoading(true);

  try {
    const response = await fetch(`${API_BASE}/api/v3/car/auth/login?username=${username}&password=${password}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    const data = await response.json();

    if (response.ok && data.payload) {
      // Save JWT, userId, userEmail
      localStorage.setItem("jwt", data.payload.token);
      localStorage.setItem("userId", data.payload.userId);
      localStorage.setItem("userEmail", data.payload.username);

      showAlert("Login successful! Redirecting...", "success");

      // redirect to dashboard
      setTimeout(() => {
        window.location.href = "../dashboard/dashboard.html";
      }, 1000);

    } else {
      const msg = data.message || data.payload || "Invalid username or password.";
      showAlert(msg, "error");
    }
  } catch (err) {
    console.error(err);
    showAlert("Cannot connect to server. Make sure Spring Boot is running.", "error");
  } finally {
    setLoading(false);
  }
}

// ── allow Enter key to submit
document.addEventListener("keydown", (e) => {
  if (e.key === "Enter") login();
});