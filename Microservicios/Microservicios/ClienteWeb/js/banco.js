const cuenta = localStorage.getItem("cuentaActiva");
if (!cuenta) {
  alert("Sesión no iniciada.");
  window.location.href = "index.html";
} else {
  document.getElementById("cuentaActiva").textContent = "Cuenta: " + cuenta;
}

function cerrarSesion() {
  localStorage.removeItem("cuentaActiva");
  window.location.href = "index.html";
}

const modal = document.getElementById("modal");
const contenidoModal = document.getElementById("contenidoModal");
const modalMensaje = document.getElementById("modalMensaje");
const mensaje = document.getElementById("mensaje");

function abrirModal(tipo) {
  let html = "";

  if (tipo === "saldo") {
    consultarSaldo();
    return;
  }

  if (tipo === "deposito") {
    html = `
      <h3>Depósito</h3>
      <input type="number" id="montoDeposito" placeholder="Monto a depositar">
      <button onclick="realizarOperacion('deposito')">Confirmar</button>
    `;
  } else if (tipo === "retiro") {
    html = `
      <h3>Retiro</h3>
      <input type="number" id="montoRetiro" placeholder="Monto a retirar">
      <button onclick="realizarOperacion('retiro')">Confirmar</button>
    `;
  } else if (tipo === "transferencia") {
    html = `
      <h3>Transferencia</h3>
      <input type="text" id="cuentaDestino" placeholder="Cuenta destino">
      <input type="number" id="montoTransferencia" placeholder="Monto a transferir">
      <button onclick="realizarOperacion('transferencia')">Confirmar</button>
    `;
  }

  contenidoModal.innerHTML = html;
  modal.style.display = "flex";
}

function cerrarModal() {
  modal.style.display = "none";
}

function mostrarMensaje(texto, color = "green") {
  mensaje.style.color = color;
  mensaje.textContent = texto;
  modalMensaje.style.display = "flex";

  setTimeout(() => {
    cerrarModal();
    cerrarMensaje();
  }, 3000);
}

function cerrarMensaje() {
  modalMensaje.style.display = "none";
}

function realizarOperacion(tipo) {
  let body = {};
  let url = "";
  if (tipo === "deposito") {
    const monto = parseFloat(document.getElementById("montoDeposito").value);
    if (isNaN(monto) || monto <= 0) return mostrarMensaje("Monto inválido", "red");
    body = { cuenta, monto };
    url = "https://transaction-service-499721146204.us-central1.run.app/deposito";
  }

  if (tipo === "retiro") {
    const monto = parseFloat(document.getElementById("montoRetiro").value);
    if (isNaN(monto) || monto <= 0) return mostrarMensaje("Monto inválido", "red");
    body = { cuenta, monto };
    url = "https://transaction-service-499721146204.us-central1.run.app/retiro";
  }

  if (tipo === "transferencia") {
    const destino = document.getElementById("cuentaDestino").value;
    const monto = parseFloat(document.getElementById("montoTransferencia").value);
    if (!destino || destino === cuenta) return mostrarMensaje("Cuenta destino inválida", "red");
    if (isNaN(monto) || monto <= 0) return mostrarMensaje("Monto inválido", "red");
    body = {
      cuentaOrigen: cuenta,
      cuentaDestino: destino,
      monto
    };
    url = "https://transaction-service-499721146204.us-central1.run.app/transferencia";
  }

  fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  })
    .then(res => res.text())
    .then(mensajeServidor => mostrarMensaje(mensajeServidor, "green"))
    .catch(() => mostrarMensaje("Error en la operación", "red"));
}

function consultarSaldo() {
  fetch(`https://account-service-499721146204.us-central1.run.app/saldo?cuenta=${cuenta}`)
    .then(res => res.text())
    .then(saldo => {
      contenidoModal.innerHTML = `
        <h3>Saldo disponible</h3>
        <p style="font-size: 24px; text-align: center;"><strong>$${saldo}</strong></p>
      `;
      modal.style.display = "flex";
    })
    .catch(() => mostrarMensaje("Error al consultar saldo", "red"));
}
