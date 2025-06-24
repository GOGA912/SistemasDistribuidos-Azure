document.getElementById("loginForm").addEventListener("submit", function (e) {
  e.preventDefault();
  const cuenta = document.getElementById("cuenta").value;
  const nip = parseInt(document.getElementById("nip").value);
  const mensaje = document.getElementById("mensaje");
  fetch("https://login-service-pwa-ceh8bacme8gjg0f2.centralus-01.azurewebsites.net/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ numero: cuenta, nip: nip })
  })
      .then(response => response.json())
      .then(data => {
      if (data.mensaje && data.mensaje.includes("Exitosa")) {
        // Guardar en localStorage
        localStorage.setItem("cuentaActiva", cuenta);
        localStorage.setItem("nombreActivo", data.nombre);
        localStorage.setItem("sexoActivo", data.sexo);
        window.location.href = "html/dashboard.html";
      } else {
        mensaje.textContent = "Error: " + (data.mensaje || "Respuesta inválida");
      }
    })
    .catch(error => {
      mensaje.textContent = "No se pudo conectar con el servidor. "+error;
      console.error(error);
    });
});

