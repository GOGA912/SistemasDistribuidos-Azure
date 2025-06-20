const CACHE_NAME = 'banco-pwa-cache-v1';
const urlsToCache = [
  '/',
  '/index.html',
  '/html/dashboard.html',
  '/css/estilo.css',
  '/js/login.js',
  '/js/banco.js',
  '/js/pwa.js',
  '/manifest.json',
  '/icons/icon-192.png',
  '/icons/icon-512.png'
];
self.addEventListener('install', (event) => {
  console.log('[Service Worker] Instalando...');
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => cache.addAll(urlsToCache))
  );
});
self.addEventListener('activate', (event) => {
  console.log('[Service Worker] Activando...');
  event.waitUntil(
    caches.keys().then(keys => Promise.all(
      keys.filter(key => key !== CACHE_NAME)
          .map(key => caches.delete(key))
    ))
  );
});
self.addEventListener('fetch', (event) => {
  const request = event.request;
  if (request.url.startsWith(self.location.origin)) {
    event.respondWith(
      caches.match(request)
        .then(response => {
          return response || fetch(request);
        })
        .catch(() => {
          console.warn('[Service Worker] Recurso no encontrado en cache ni red:', request.url);
          return caches.match('/index.html');
        })
    );
  } else {
    console.log('[Service Worker] Petición externa no manejada:', request.url);
  }
});
