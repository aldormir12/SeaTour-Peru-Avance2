# Verificacion de entrega - 15 de octubre de 2026

Carpeta verificada: SEATOUR-AVANCE2/entrega-avance2/SeaTour-Peru-Avance2.
No se modificaron fuentes, README ni dependencias declaradas. No hubo commits, pushes ni publicaciones. No se detuvieron procesos del proyecto original.

## Resultado

La entrega NO esta lista para publicarse como validada de extremo a extremo.

- Requisitos: Java 21.0.12.1 y npm 11.6.2. Se instalo Node 22.23.3 exclusivamente en target/verification-tools; el Node global es 24.11.1.
- npm ci termino correctamente; se repitio usando Node 22 para respetar el requisito del README.
- Los comandos exactos del README fallaron al iniciar porque 8080 y 4200 estaban ocupados por otros servicios. El usuario autorizo continuar en puertos alternativos.
- Backend: spring-boot:run, con SERVER_PORT=18080. H2 2.4.240, jdbc:h2:file:./data/seatourdb. La base anterior se conservo como target/h2-previa-*.mv.db; se genero otra base desde cero. El intento inicial en 8080 creo el esquema, pero fallo antes de crear ADMIN/demo; el inicio en 18080 completo la inicializacion.
- Primer ADMIN: creado mediante variables de entorno; login HTTP correcto y rol ADMIN.
- Datos ficticios: carga correcta; login de operador1@example.test correcto y rol OPERADOR.
- API autenticada: HTTP 200 en /api/admin/tours, /api/admin/categorias, /api/admin/embarcaciones y /api/salidas; dos registros en cada listado.
- Consola H2: HTTP 200.
- Angular: npm start -- --port 14200 arranco correctamente; HTTP 200. Imagen demo local: HTTP 200.
- npm run build: termino con codigo 0; hay advertencias de presupuestos CSS, entre otras.
- mvnw.cmd test: 268 pruebas, 13 fallos, 62 errores, 0 omitidas; BUILD FAILURE.
- CORS: la API permite http://localhost:4200. Con origen http://localhost:14200 devuelve 403. Angular conserva su API fija en localhost:8080. Por eso NO se verifico comunicacion entre ambos procesos de esta copia ni login/listados visuales en navegador. No hay navegador conectado disponible en la herramienta.
- Portada: ya existia una alternativa CSS con degradados marinos y enlace Explorar tours. No hay referencias a hero-seatour.mp4 en src/public. Compila, pero su aspecto visual no se pudo verificar. El README y ENTREGA.md todavia describen el video ausente y deben actualizarse.
- npm audit: 13 vulnerabilidades (2 moderadas, 8 altas, 3 criticas). Incluye paquetes directos @angular/build, @angular/cli, @angular/platform-server y @angular/router. Revisar target/audit-actual.json; no se ejecuto npm audit fix.

## Correcciones propuestas antes de modificar codigo

1. Revisar fixtures de SalidaProgramadaIntegrationTest: asignar operador valido donde la prueba no pretende comprobar su ausencia; aparecen 400 por operador obligatorio.
2. Adaptar la expectativa del JWT de AuthIntegrationTest si jti es parte del contrato vigente.
3. Revisar limpieza de datos de PagoIntegrationTest y ReservaIntegrationTest: hay claves foraneas de comprobantes_reserva que impiden borrar reservas.
4. Contrastar expectativas de permisos, endpoints y transiciones con las reglas actuales; hay respuestas 400/403/404 y expectativas EN_CURSO frente a NO_REALIZADA. No debilitar reglas de negocio solo para hacer pasar tests.
5. Revisar las actualizaciones compatibles de dependencias indicadas por npm audit y validar sus efectos.
6. Actualizar README/ENTREGA sobre portada y verificacion. Antes de distribuir, excluir target, dist, node_modules, bases H2, uploads de prueba y archivos de credenciales locales.
7. Repetir verificacion de navegador con puertos 8080/4200 libres para probar frontend/backend juntos sin cambiar el contrato local documentado.

## Evidencias locales

- target/arranque-actual.log: intento en 8080, fallo por puerto ocupado.
- target/arranque-18080.log: backend, H2 e inicializadores.
- target/angular-14200.log: servidor Angular.
- target/build-angular-actual.log: compilacion de produccion.
- target/pruebas-actuales.log y target/surefire-reports: resultados Maven.
- target/audit-actual.json: auditoria npm.

Los archivos target/verificacion-local.json y las bases contienen material de prueba y no deben publicarse. El archivo de credenciales ya existia y no se incluye en este informe.
