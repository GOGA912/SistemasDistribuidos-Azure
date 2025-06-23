document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const cuenta = document.getElementById("cuenta").value;
  const nip = parseInt(document.getElementById("nip").value);
  const mensaje = document.getElementById("mensaje");

  fetch("https://login-service-499721146204.us-central1.run.app/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ numero: cuenta, nip: nip })
  })
    .then(response => response.text())
    .then(texto => {
      if (texto.includes("Exitosa")) {
        localStorage.setItem("cuentaActiva", cuenta);
        window.location.href = "dashboard.html";
      } else {
        mensaje.textContent = "Error: " + texto;
      }
    })
    .catch(error => {
      mensaje.textContent = "No se pudo conectar con el servidor. " + error;
      console.error(error);
    });
});
