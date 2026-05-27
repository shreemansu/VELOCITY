const API_BASE = "http://localhost:8080";

  // ── toggle password
  function togglePassword() {
    const input = document.getElementById("password");
    const btn   = document.querySelector(".eye-btn");
    input.type  = input.type === "password" ? "text" : "password";
    btn.textContent = input.type === "password" ? "👁" : "🙈";
  }

  // ── show alert
  function showAlert(message, type) {
    const box = document.getElementById("alertBox");
    box.textContent  = message;
    box.className    = "alert " + type;
    box.style.display = "block";
    box.scrollIntoView({ behavior: "smooth", block: "nearest" });
  }

  function hideAlert() {
    document.getElementById("alertBox").style.display = "none";
  }

  // ── loading state
  function setLoading(loading) {
    const btn    = document.getElementById("registerBtn");
    const text   = document.getElementById("btnText");
    const loader = document.getElementById("btnLoader");
    btn.disabled        = loading;
    text.style.display  = loading ? "none"  : "block";
    loader.style.display = loading ? "block" : "none";
  }

  // ── field hint helper
  function setHint(id, message, type) {
    const el = document.getElementById(id);
    el.textContent = message;
    el.className   = "field-hint " + (type || "");
  }

  function setInputState(id, state) {
    const el = document.getElementById(id);
    el.classList.remove("valid", "invalid");
    if (state) el.classList.add(state);
  }

  // ── VALIDATORS ──

  function validateName() {
    const val = document.getElementById("name").value.trim();
    if (val.length === 0) {
      setHint("nameHint", "", "");
      setInputState("name", "");
      return false;
    }
    if (val.length < 2) {
      setHint("nameHint", "Name too short", "err");
      setInputState("name", "invalid");
      return false;
    }
    setHint("nameHint", "✓ Looks good", "ok");
    setInputState("name", "valid");
    return true;
  }

  function validatePhone() {
    const val = document.getElementById("phone").value.trim();
    // only allow digits
    document.getElementById("phone").value = val.replace(/\D/g, "");
    if (val.length === 0) {
      setHint("phoneHint", "", "");
      setInputState("phone", "");
      return false;
    }
    if (!/^[6-9]\d{9}$/.test(val)) {
      setHint("phoneHint", "Enter valid 10-digit number", "err");
      setInputState("phone", "invalid");
      return false;
    }
    setHint("phoneHint", "✓ Valid number", "ok");
    setInputState("phone", "valid");
    return true;
  }

  function validateEmail() {
    const val = document.getElementById("email").value.trim();
    if (val.length === 0) {
      setHint("emailHint", "", "");
      setInputState("email", "");
      return false;
    }
    const ok = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val);
    if (!ok) {
      setHint("emailHint", "Enter a valid email address", "err");
      setInputState("email", "invalid");
      return false;
    }
    setHint("emailHint", "✓ Valid email", "ok");
    setInputState("email", "valid");
    return true;
  }

  function validatePassword() {
    const val  = document.getElementById("password").value;
    const segs = [
      document.getElementById("s1"),
      document.getElementById("s2"),
      document.getElementById("s3"),
      document.getElementById("s4"),
    ];

    // reset
    segs.forEach(s => s.style.background = "var(--border)");

    if (val.length === 0) {
      setHint("passwordHint", "", "");
      setInputState("password", "");
      return false;
    }

    // score
    let score = 0;
    if (val.length >= 6)  score++;
    if (val.length >= 10) score++;
    if (/[A-Z]/.test(val) && /[a-z]/.test(val)) score++;
    if (/[0-9]/.test(val) && /[^A-Za-z0-9]/.test(val)) score++;

    const colors = ["#ff4d4d", "#ff9f4d", "#e8ff00", "#4dff91"];
    const labels = ["Weak", "Fair", "Good", "Strong"];

    for (let i = 0; i < score; i++) {
      segs[i].style.background = colors[score - 1];
    }

    if (val.length < 6) {
      setHint("passwordHint", "Minimum 6 characters required", "err");
      setInputState("password", "invalid");
      return false;
    }

    setHint("passwordHint", labels[score - 1] + " password", score >= 3 ? "ok" : "err");
    setInputState("password", score >= 2 ? "valid" : "invalid");
    return val.length >= 6;
  }

  // ── REGISTER ──
  async function register() {
    hideAlert();

    const name     = document.getElementById("name").value.trim();
    const phone    = document.getElementById("phone").value.trim();
    const email    = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    // run all validators
    const nameOk     = validateName();
    const phoneOk    = validatePhone();
    const emailOk    = validateEmail();
    const passwordOk = validatePassword();

    if (!nameOk || !phoneOk || !emailOk || !passwordOk) {
      showAlert("Please fix the errors above before submitting.", "error");
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(`${API_BASE}/api/v2/car/user/registration`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, phone, email, password })
      });

      const data = await response.json();

      if (response.ok) {
        showAlert("Otp sent to given email address. Redirecting to login...","success") 
        setTimeout(() => {
          window.location.href = "../verification/verify.html";
        }, 1500);
      } else {
        const msg = data.message || "Registration failed. Please try again.";
        showAlert(msg, "error");
      }

    } catch (err) {
      showAlert("Cannot connect to server. Make sure Spring Boot is running on port 8080.", "error");
    } finally {
      setLoading(false);
    }
  }

  // ── Enter key support
  document.addEventListener("keydown", (e) => {
    if (e.key === "Enter") register();
  });