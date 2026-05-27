 const API_BASE = "http://localhost:8080";
  const boxes    = ["o1","o2","o3","o4","o5","o6"];
  let timerInterval = null;
  let secondsLeft   = 120; // 2 minutes

  // ── on page load — prefill email if coming from register
  window.onload = () => {
    const savedEmail = localStorage.getItem("pendingVerifyEmail");
    if (savedEmail) {
      document.getElementById("email").value = savedEmail;
      document.getElementById("emailDisplay").textContent = savedEmail;
    }
    startTimer();
    setupOtpBoxes();
    document.getElementById("o1").focus();
  };

  // ── OTP box auto-jump logic
  function setupOtpBoxes() {
    boxes.forEach((id, index) => {
      const el = document.getElementById(id);

      el.addEventListener("input", (e) => {
        // only allow digits
        el.value = el.value.replace(/\D/g, "");

        if (el.value) {
          el.classList.add("filled");
          // jump to next
          if (index < boxes.length - 1) {
            document.getElementById(boxes[index + 1]).focus();
          } else {
            // last box filled — auto verify
            verify();
          }
        } else {
          el.classList.remove("filled");
        }
      });

      el.addEventListener("keydown", (e) => {
        // backspace — go to previous
        if (e.key === "Backspace" && !el.value && index > 0) {
          document.getElementById(boxes[index - 1]).focus();
        }
        // paste handling
        if (e.key === "v" && (e.ctrlKey || e.metaKey)) return;
      });

      // handle paste on any box
      el.addEventListener("paste", (e) => {
        e.preventDefault();
        const pasted = (e.clipboardData || window.clipboardData)
          .getData("text")
          .replace(/\D/g, "")
          .slice(0, 6);

        if (pasted) {
          pasted.split("").forEach((char, i) => {
            if (i < boxes.length) {
              const box = document.getElementById(boxes[i]);
              box.value = char;
              box.classList.add("filled");
            }
          });
          // focus last filled
          const lastIndex = Math.min(pasted.length - 1, boxes.length - 1);
          document.getElementById(boxes[lastIndex]).focus();

          if (pasted.length === 6) verify();
        }
      });
    });
  }

  // ── get full OTP string
  function getOtp() {
    return boxes.map(id => document.getElementById(id).value).join("");
  }

  // ── clear OTP boxes
  function clearOtp() {
    boxes.forEach(id => {
      const el = document.getElementById(id);
      el.value = "";
      el.classList.remove("filled", "error-box");
    });
    document.getElementById("o1").focus();
  }

  // ── shake boxes on error
  function shakeBoxes() {
    boxes.forEach(id => {
      const el = document.getElementById(id);
      el.classList.add("error-box");
      setTimeout(() => el.classList.remove("error-box"), 500);
    });
  }

  // ── timer
  function startTimer() {
    secondsLeft = 120;
    clearInterval(timerInterval);
    updateTimerDisplay();
    document.getElementById("resendBtn").classList.remove("active");

    timerInterval = setInterval(() => {
      secondsLeft--;
      updateTimerDisplay();
      if (secondsLeft <= 0) {
        clearInterval(timerInterval);
        document.getElementById("timerDisplay").textContent = "00:00";
        document.getElementById("resendBtn").classList.add("active");
      }
    }, 1000);
  }

  function updateTimerDisplay() {
    const m = String(Math.floor(secondsLeft / 60)).padStart(2, "0");
    const s = String(secondsLeft % 60).padStart(2, "0");
    document.getElementById("timerDisplay").textContent = `${m}:${s}`;
  }

  // ── alert
  function showAlert(msg, type) {
    const box = document.getElementById("alertBox");
    box.textContent   = msg;
    box.className     = "alert " + type;
    box.style.display = "block";
  }

  // ── loading
  function setLoading(loading) {
    const btn    = document.getElementById("verifyBtn");
    const text   = document.getElementById("btnText");
    const loader = document.getElementById("btnLoader");
    btn.disabled         = loading;
    text.style.display   = loading ? "none"  : "block";
    loader.style.display = loading ? "block" : "none";
  }

  // ── VERIFY
  async function verify() {
    const email = document.getElementById("email").value.trim();
    const otp   = getOtp();

    if (!email) {
      showAlert("Please enter your email address.", "error");
      return;
    }
    if (otp.length < 6) {
      showAlert("Please enter the complete 6-digit OTP.", "error");
      shakeBoxes();
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(`${API_BASE}/api/v2/car/user/verification`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, otp })
      });

      const data = await response.json();

      if (response.ok) {
        // show success state
        clearInterval(timerInterval);
        document.getElementById("formState").style.display   = "none";
        document.getElementById("successState").style.display = "block";
        localStorage.removeItem("pendingVerifyEmail");

        setTimeout(() => {
          window.location.href = "../login/login.html";
        }, 2000);

      } else {
        const msg = data.message || "Invalid OTP. Please try again.";
        showAlert(msg, "error");
        shakeBoxes();
        clearOtp();
      }

    } catch (err) {
      showAlert("Cannot connect to server. Make sure Spring Boot is running.", "error");
    } finally {
      setLoading(false);
    }
  }

  // ── RESEND OTP
  async function resendOtp() {
    const btn = document.getElementById("resendBtn");
    if (!btn.classList.contains("active")) return;

    const email = document.getElementById("email").value.trim();
    if (!email) {
      showAlert("Please enter your email to resend OTP.", "error");
      return;
    }

    try {
      const response = await fetch(`${API_BASE}/api/v2/car/user/resendOtp`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email })
      });

      if (response.ok) {
        showAlert("OTP resent successfully. Check your inbox.", "success");
        clearOtp();
        startTimer();
      } else {
        showAlert("Failed to resend OTP. Try again.", "error");
      }
    } catch (err) {
      showAlert("Cannot connect to server.", "error");
    }
  }