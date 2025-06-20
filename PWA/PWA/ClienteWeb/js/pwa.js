// pwa.js

// Registro del Service Worker
if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('../service-worker.js')
    .then(registro => {
      console.log('Service Worker registrado con éxito:', registro.scope);
    })
    .catch(error => {
      console.error('Error al registrar Service Worker:', error);
    });
}

let deferredPrompt;

window.addEventListener('beforeinstallprompt', (e) => {
  e.preventDefault();
  deferredPrompt = e;
  const linkInstalar = document.getElementById('linkInstalar');
  if (linkInstalar) {
    linkInstalar.style.display = 'block';
  }
});

function instalarApp() {
  if (deferredPrompt) {
    deferredPrompt.prompt();
    deferredPrompt.userChoice.then((choiceResult) => {
      if (choiceResult.outcome === 'accepted') {
        console.log('El usuario aceptó instalar la app');
      } else {
        console.log('El usuario canceló la instalación');
      }
      deferredPrompt = null;
      document.getElementById('linkInstalar').style.display = 'none'; // Esconde el link después
    });
  }
}

