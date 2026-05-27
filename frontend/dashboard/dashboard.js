const API_BASE = "http://localhost:8080";
let allCars = [];

// ── check login
const jwt = localStorage.getItem("jwt");
const userId = localStorage.getItem("userId");
const userEmail = localStorage.getItem("userEmail");

if (!jwt || !userId || !userEmail) {
  window.location.href = "../login/login.html";
} else {
  document.getElementById("userEmail").textContent = userEmail;
}

// ── car emoji by brand
function getCarEmoji(brand) {
  const map = {
    honda: "🚗", toyota: "🚙", bmw: "🏎️",
    mercedes: "🚘", hyundai: "🚕", maruti: "🚖",
    tata: "🚐", ford: "🚗", kia: "🚙"
  };
  return map[(brand || "").toLowerCase()] || "🚗";
}

// ── on load
window.onload = () => {
  loadCars();
};

// ── load all cars
async function loadCars() {
  try {
    const response = await fetch(`${API_BASE}/api/v1/car/showAllCar`, {
      method: "GET",
      headers: { "Content-Type": "application/json", "Authorization": "Bearer " + jwt }
    });

    if (response.status === 401 || response.status === 403) {
      localStorage.clear();
      window.location.href = "../login/login.html";
      return;
    }

    const data = await response.json();
    const cars = data.payload || data;
    allCars = Array.isArray(cars) ? cars : [];

    updateStats();
    renderCars(allCars);

  } catch (err) {
    document.getElementById("carsGrid").innerHTML = `
      <div class="empty-state">
        <div class="empty-icon">⚠️</div>
        <div class="empty-text">Cannot connect to server.<br>Make sure Spring Boot is running.</div>
      </div>`;
  }
}

// ── update stats
function updateStats() {
  document.getElementById("totalCount").textContent = allCars.length;
  document.getElementById("availableCount").textContent =
    allCars.filter(c => c.status === "AVAILABLE").length;
  document.getElementById("rentedCount").textContent =
    allCars.filter(c => c.status === "RENTED").length;
}

// ── render cars
function renderCars(cars) {
  const grid = document.getElementById("carsGrid");

  if (cars.length === 0) {
    grid.innerHTML = `
      <div class="empty-state">
        <div class="empty-icon">🔍</div>
        <div class="empty-text">No cars found matching your search.</div>
      </div>`;
    return;
  }

  grid.innerHTML = cars.map((car, index) => `
    <div class="car-card" style="animation-delay: ${index * 0.05}s">
      <div class="car-image-area">
        ${getCarEmoji(car.brand)}
        <span class="status-badge ${car.status}">${car.status}</span>
      </div>
      <div class="car-body">
        <div class="car-brand">${car.brand || "—"}</div>
        <div class="car-model">${car.model || "Unknown Model"}</div>
        <div class="car-meta">
          <span class="meta-pill">🪪 ${car.plateNumber || car.plate_number || "—"}</span>
        </div>
        <div class="car-footer">
          <div class="car-price">
            ₹${car.pricePerday || car.price_per_day || "—"}<span>/ day</span>
          </div>
          <button
            class="btn-rent"
            onclick="rentCar(${car.carId})"
            ${car.status !== "AVAILABLE" ? "disabled" : ""}
          >
            ${car.status === "AVAILABLE" ? "RENT NOW" : "UNAVAILABLE"}
          </button>
        </div>
      </div>
    </div>
  `).join("");
}

// ── filter cars
function filterCars() {
  const search = document.getElementById("searchInput").value.toLowerCase();
  const status = document.getElementById("statusFilter").value;

  const filtered = allCars.filter(car => {
    const matchSearch =
      (car.brand || "").toLowerCase().includes(search) ||
      (car.model || "").toLowerCase().includes(search);
    const matchStatus = status === "ALL" || car.status === status;
    return matchSearch && matchStatus;
  });

  renderCars(filtered);
}

// ── rent a car
async function rentCar(carId) {
  if (!userId) {
    showToast("User ID not found. Please login again.", "error");
    window.location.href = "../login/login.html";
    return;
  }

  try {
    const response = await fetch(
      `${API_BASE}/api/v4/car/rental/rent?userId=${userId}&carId=${carId}`,
      { method: "POST", headers: { "Authorization": "Bearer " + jwt } }
    );

    if (response.ok) {
      showToast("Car rented successfully! 🚗", "success");
      loadCars();
    } else {
      const data = await response.json();
      showToast(data.message || "Failed to rent car.", "error");
    }
  } catch (err) {
    showToast("Cannot connect to server.", "error");
  }
}

// ── toast
function showToast(message, type) {
  const toast = document.getElementById("toast");
  toast.textContent = message;
  toast.className = "toast " + type;
  toast.style.display = "block";
  setTimeout(() => { toast.style.display = "none"; }, 3000);
}

// ── logout
function logout() {
  localStorage.clear();
  window.location.href = "../login/login.html";
}