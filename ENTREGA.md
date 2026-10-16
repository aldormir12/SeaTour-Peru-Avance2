# Estado de la entrega

Origen: estado de trabajo de avance2-evidencia, incluidos cambios locales.
Esta copia no contiene historial .git. No se hicieron commits, pushes ni
publicaciones. Esta revision actualiza README.md, ENTREGA.md y exclusiones de .gitignore;
no cambia reglas de negocio, dependencias, CORS ni el proyecto original.

## Portada sin video

La portada ya funciona con .hero-sea-background: degradados CSS locales de
apariencia marina. Mantiene la estructura general, capas, tipografia,
navegacion y acciones; Explorar tours abre el catalogo. No necesita
hero-seatour.mp4 ni otro video, descarga o servicio externo para ese fondo.
La alternativa compila; falta confirmar su aspecto visual en navegador.
Google Fonts, Open-Meteo y mapas pueden requerir Internet por separado.

## Primer administrador y datos ficticios

README.md contiene los comandos de configuracion y arranque en dos terminales.
JWT es obligatorio. En una base nueva, AdminInicial crea el primer ADMIN al
arrancar si se proporcionan SEATOUR_ADMIN_EMAIL y SEATOUR_ADMIN_PASSWORD;
password de 12 a 1024 caracteres. No hay password universal. Si ya existe un
ADMIN, incluso inactivo, no cambia credenciales ni crea otro con esas variables.
El registro publico crea CLIENTE. No borre la base para cambiar una password.

La demo es opcional y esta desactivada por defecto. Para una base nueva,
configurar SEATOUR_DEMO_HABILITADO=true y SEATOUR_DEMO_PASSWORD (12 a 1024
caracteres) en la misma terminal del backend, antes del arranque. Crea dos
categorias, tours, embarcaciones, operadores y salidas para el dia siguiente
en America/Lima. Operadores: operador1@example.test y operador2@example.test;
usan la password demo elegida. No crea ADMIN, reservas ni clientes personales.

La categoria marcadora DEMO Avance 2 - Naturaleza impide repetir la carga.
No renueva fechas, passwords ni repara registros en reinicios. No sobrescribe
archivos existentes. Desactivar la demo tras la primera carga; si las salidas
vencen, crear otras desde ADMIN. La copia actual ya tiene datos de verificacion.

## Evidencia anterior y pendiente funcional

La verificacion previa instalo dependencias y confirmo Spring Boot con H2,
creacion y login ADMIN, carga demo, login OPERADOR y API con dos registros
por listado de tours, categorias, embarcaciones y salidas. Angular arranco y
su compilacion termino correctamente, con advertencias.
Maven: 268 pruebas, 13 fallos y 62 errores. npm informo 13 vulnerabilidades,
3 criticas. No se repitieron Maven ni npm audit en esta revision.
Detalles y correcciones propuestas: INFORME-VERIFICACION.md.

## Verificacion funcional HTTP completada (15 de octubre de 2026)

8080 y 4200 estaban libres al comenzar. Se inicio exclusivamente esta copia:
Spring Boot en 8080 y Angular en 4200, sin cambiar CORS ni URL API y usando
la H2 demo existente. No se borraron bases ni se altero el proyecto original.

Resultados: Angular HTTP 200; login ADMIN HTTP 200 y rol ADMIN; listados
/api/admin/tours, /api/admin/categorias, /api/admin/embarcaciones y /api/salidas
HTTP 200, con dos registros ficticios en cada uno. Consola H2 e imagen demo
HTTP 200. CORS permite exactamente http://localhost:4200 en las respuestas
comprobadas; el preflight OPTIONS del login respondio 200 para POST/content-type.
No hay navegador conectado: queda pendiente la revision visual de portada,
login y pantallas. No se afirma haber verificado esos recorridos mediante clics.
Solo se detienen al finalizar los procesos iniciados para esta comprobacion.
Logs locales: target/funcional-8080.log y target/funcional-4200.log (ignorados).

La comprobacion HTTP solicitada es satisfactoria. La copia esta preparada para
subir sus fuentes a GitHub respetando .gitignore y sin forzar archivos ignorados.
No equivale a certificarla como libre de fallos: siguen documentados los fallos
Maven, vulnerabilidades, revision visual y derechos de imagenes pendientes.
No se repitieron Maven test, npm audit, npm ci ni npm run build.
No hubo commits, pushes ni publicaciones.

## Preparacion antes de distribuir

Excluir .git, .codex-tmp, configuraciones de agentes, secretos, bases H2,
uploads de prueba, node_modules, target, dist y caches. La verificacion genero
archivos locales; no confundirlos con fuentes distribuibles. No publicar
archivos de credenciales locales. Conservar recursos demo incluidos en sources.
MANIFIESTO-FUENTES.json registra hashes de origen, no resultados funcionales.

Las imagenes versionadas fueron aportadas por el proyecto. Confirmar sus
derechos de redistribucion antes de publicar; no se incorporaron recursos de
terceros nuevos en esta revision. El video excluido ya no bloquea la portada.

## Revision de archivos publicables

.gitignore excluye archivos Codex/agentes, .env reales, credenciales locales,
claves privadas, bases H2, uploads, target, dist, node_modules, caches, coverage
y logs. Conservar fuentes y recursos demo; .env.example solo puede contener
placeholders, nunca secretos. No usar git add -f sobre archivos ignorados.
La busqueda de credenciales locales conocidas y patrones comunes de tokens y
claves privadas no encontro coincidencias en los archivos candidatos revisados.
Es una revision local por patrones, no una garantia de ausencia de todo secreto.
La base demo y target/verificacion-local.json permanecen locales y excluidos;
una clonacion nueva crea ADMIN y datos demo siguiendo README.md.
