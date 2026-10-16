# SeaTour Peru - Avance 2

Copia independiente de avance2-evidencia, incluidos los cambios locales al preparar
la entrega. Angular + Spring Boot + H2. No contiene historial Git ni requiere
Docker, Nginx o PostgreSQL. La verificacion previa confirmo por separado el
backend con H2, ADMIN y datos ficticios, y el arranque y compilacion de Angular.
La comprobacion HTTP conjunta en 8080/4200 fue correcta el 15 de octubre de
2026: Angular, H2, login ADMIN, listados demo y CORS. La revision visual en
navegador sigue pendiente. El informe anterior conserva los hallazgos de pruebas
y dependencias; el estado actualizado figura en ENTREGA.md.

## Requisitos Windows

- JDK 21; JAVA_HOME debe apuntar al JDK y su bin estar en PATH.
- Node.js 22.x, versi?n 22.12.0 o superior dentro de esa rama.
- npm 11.6.2, versi?n declarada en package.json.
- PowerShell, Internet y puertos 8080 y 4200 libres.
- Permisos de escritura en la carpeta del proyecto.

Spring Boot es 4.1.1 y Angular 21.2.x. mvnw.cmd descarga Maven 3.9.16;
no hace falta instalar Maven ni Angular CLI globalmente.
Compruebe, desde la carpeta que contiene pom.xml:

    java -version
    node --version
    npm.cmd --version

## 1. Obtener e instalar

Descomprima la entrega en una carpeta propia. Si recibe un repositorio, cl?nelo
y entre en la carpeta que contiene pom.xml. Todav?a no existe URL remota.
Desde esa ra?z, instale dependencias del frontend:

    cd .\seatour-frontend-angular
    npm.cmd ci
    cd ..

Use npm.cmd para evitar bloqueos de npm.ps1. El package.json de la ra?z no
corresponde al proyecto Angular; la instalaci?n debe hacerse en su subcarpeta.
La primera ejecuci?n necesita Internet para descargar dependencias y Maven.

## 2. JWT y primer ADMIN

En una terminal PowerShell, desde la ra?z, genere una clave local sin imprimirla:

    $jwtBytes = New-Object byte[] 32
    $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
    $rng.GetBytes($jwtBytes)
    $rng.Dispose()
    $env:SEATOUR_JWT_SECRET = [Convert]::ToBase64String($jwtBytes)
    $env:SEATOUR_ADMIN_EMAIL = Read-Host "Correo del primer ADMIN"
    $adminPassword = Read-Host "Contrasena ADMIN (minimo 12 caracteres)" -AsSecureString
    $env:SEATOUR_ADMIN_PASSWORD = [System.Net.NetworkCredential]::new('', $adminPassword).Password
    $env:SEATOUR_CORREO_HABILITADO = 'false'

No existe una contrase?a universal: elija un correo v?lido y una contrase?a
entre 12 y 1024 caracteres. No publique esos valores. Las variables duran solo
en esa terminal y sus procesos hijos. Configure nuevamente al abrir otra.
Generar otra clave JWT invalida tokens anteriores, pero no borra usuarios.

RolesIniciales crea CLIENTE, OPERADOR y ADMIN. AdminInicial crea el primer ADMIN
si no existe ninguno. Si ya existe, incluso inactivo, no modifica credenciales.
Cambiar estas variables NO restablece su contrase?a. Sin ambas variables ADMIN
no se crea la cuenta; con una sola falla la inicializaci?n si no existe ADMIN.
El registro p?blico asigna CLIENTE. El ADMIN puede crear operadores.

El ADMIN inicial se crea al arrancar el backend, no al ejecutar estos comandos.
Espere tambien a que terminen los inicializadores antes de intentar el login.
En una copia que ya se verifico puede existir un ADMIN: use sus credenciales
originales. No borre la base ni espere que otras variables cambien su password.
El ADMIN y los operadores demo son cuentas distintas; habilitar la demo no crea
un administrador. JWT sigue siendo obligatorio en cada nueva terminal backend.

## 3. Datos ficticios opcionales

Desactivados por defecto. Antes del primer arranque puede habilitarlos:

    $env:SEATOUR_DEMO_HABILITADO = 'true'
    $demoPassword = Read-Host "Contrasena de operadores ficticios (minimo 12 caracteres)" -AsSecureString
    $env:SEATOUR_DEMO_PASSWORD = [System.Net.NetworkCredential]::new('', $demoPassword).Password

DatosDemoInicial crea dos categor?as, tours, embarcaciones y operadores, y dos
salidas para ma?ana en Lima. Los correos ficticios son operador1@example.test
y operador2@example.test; ambos usan la contrase?a que usted eligi?. No se
incluyen reservas ni clientes personales. Las im?genes demo son recursos
geom?tricos originales incluidos en la copia, sin descargar fotos externas.

La carga usa los services existentes, una transacci?n y una categor?a marcadora
DEMO Avance 2 - Naturaleza para no repetirla. Si encuentra esa categor?a,
no recrea ni repara datos. No elimina ni actualiza registros existentes.
Para evitar interferencias, habil?tela solo en la base nueva de esta entrega.
Si hay colisiones o validaciones fallidas, revierten los datos de la carga;
puede quedar la imagen local de apoyo. No se sobrescriben archivos existentes.
Las fechas no se renuevan en reinicios: cree nuevas salidas desde ADMIN cuando
venzan. Desactive la carga despu?s del primer arranque quitando la variable o:

    $env:SEATOUR_DEMO_HABILITADO = 'false'

Tambi?n puede dejarla desactivada y crear manualmente categor?a, tour, barco,
operador y salida desde el panel ADMIN. Registre un CLIENTE para reservar.

Configure las variables de demo en la MISMA terminal del backend, antes de
arrancarlo. La password demo debe tener entre 12 y 1024 caracteres. La carga
crea dos registros de cada tipo: categorias, tours, embarcaciones, operadores
y salidas; estas ultimas quedan para el dia siguiente en America/Lima.
Si ya existe la categoria marcadora, no se duplican datos ni se cambian las
passwords de los operadores al reiniciar. En esta copia la carga ya se probo:
puede desactivarla y conservar los datos existentes. No elimine la base ni la
imagen demo para repetirla; cree salidas nuevas desde ADMIN si las fechas vencen.

## 4. Arrancar backend

En la MISMA terminal que contiene las variables, desde la ra?z:

    .\mvnw.cmd spring-boot:run

Espere el mensaje de inicio correcto de Spring Boot. Deje esa terminal abierta.

## 5. Arrancar frontend

En OTRA terminal, desde la ra?z:

    cd .\seatour-frontend-angular
    npm.cmd start -- --port 4200

Abra http://localhost:4200 e ingrese con el ADMIN del paso 2. Para detener los
procesos use Ctrl+C en cada terminal.

## Direcciones y persistencia

- Aplicaci?n: http://localhost:4200
- API: http://localhost:8080/api
- Login: POST http://localhost:8080/api/auth/login
- Consola H2: http://localhost:8080/h2-console
- JDBC: jdbc:h2:file:./data/seatourdb ; usuario sa ; contrase?a vac?a.

Angular apunta directamente a localhost:8080. CorsConfig admite exactamente
http://localhost:4200 para /api/**. No use 127.0.0.1 ni cambie puertos sin adaptar
URL y CORS. No hay proxy Nginx.

H2 crea data/seatourdb.mv.db y conserva datos tras reiniciar. Inicie el backend
desde la ra?z para no crear una base en otra ubicaci?n. Hibernate usa
spring.jpa.hibernate.ddl-auto=update. schema-h2.sql ampl?a el enum de estados
antes de JPA. No ejecute dos backends sobre el mismo archivo.
Im?genes cargadas: uploads/tours y uploads/embarcaciones. Estas carpetas y data
est?n ignoradas por Git; no distribuya bases personales.

## Variables opcionales

- SEATOUR_JWT_EXPIRATION_SECONDS: 3600 por defecto.
- SEATOUR_JWT_ISSUER: seatour por defecto.
- SEATOUR_IMAGENES_DIRECTORIO: ./uploads/tours.
- SEATOUR_EMBARCACIONES_IMAGENES_DIRECTORIO: ./uploads/embarcaciones.
- SEATOUR_CORREO_HABILITADO: false.
- SEATOUR_CORREO_REMITENTE: sin valor.
- SEATOUR_SMTP_HOST: localhost; SEATOUR_SMTP_PORT: 587.
- SEATOUR_SMTP_USUARIO y SEATOUR_SMTP_PASSWORD: sin valor.
- SEATOUR_SMTP_AUTH y SEATOUR_SMTP_STARTTLS: true.

No necesita correo para la demostraci?n b?sica. Para habilitarlo configure SMTP
y un remitente autorizado. Tras un pago simulado aprobado se prepara comprobante
con QR; el job intenta enviarlo cada 15 segundos si est? habilitado. Sin env?o,
queda pendiente en H2. Los pagos no mueven dinero real.

## Lanzador original

iniciar-seatour.bat se conserva sin cambios. Solo carga SEATOUR_JWT_SECRET de
.env, no carga del archivo ADMIN, demo o SMTP ni instala dependencias. Su espera
de 15 segundos no garantiza disponibilidad. Use las dos terminales para el primer
arranque. Spring Boot no carga .env autom?ticamente en esta configuraci?n.

## Recursos y l?mites

Las im?genes versionadas fueron aportadas por el proyecto; confirme su licencia
antes de redistribuirlas. No se incorporaron recursos de terceros nuevos.
La portada funciona sin hero-seatour.mp4: utiliza un fondo marino de degradados
CSS locales en .hero-sea-background. Conserva la estructura general, capas,
tipografia, navegacion y llamadas a la accion; el enlace Explorar tours dirige
al catalogo. No hay que descargar, copiar ni configurar un video para usarla.
El archivo de video no se distribuye ni es una dependencia de esta alternativa.
La alternativa compila; su revision visual en navegador sigue pendiente.
Google Fonts, Open-Meteo y servicios de mapas pueden necesitar Internet.
Consulte RECURSOS.md y ENTREGA.md.

## Soluci?n de problemas

- Java: comprobar JDK 21, JAVA_HOME y PATH.
- Node: usar 22.x >=22.12.0 e instalar con npm.cmd ci.
- mvnw no reconocido: usar .\mvnw.cmd desde la ra?z.
- Descargas: revisar Internet, proxy y firewall para Maven/npm.
- JWT ausente: configurar SEATOUR_JWT_SECRET en la terminal del backend;
  Base64 debe representar al menos 32 bytes.
- ADMIN inv?lido: las variables solo crean la cuenta inicial, no la restablecen.
- Demo: requiere contrase?a y est? desactivada salvo habilitaci?n expl?cita.
- Puerto ocupado: identificar primero el proceso; finalizar operaciones y usar
  Ctrl+C en su terminal solo cuando el responsable confirme que puede detenerse.
  No detener procesos ajenos ni borrar bases para liberar puertos.
- CORS: abrir exactamente http://localhost:4200.
- H2 bloqueada: cerrar el otro backend; no borrar la base para desbloquearla.
- Cat?logo vac?o: habilitar la carga ficticia inicial o crear datos manualmente.
- 401: volver a iniciar sesi?n. 403: falta permiso para el rol.
- Sin correo: deshabilitado por defecto; revisar SMTP si lo habilit?.
- Video ausente: no requiere accion; la portada usa el fondo CSS local.

## Alcance de la copia

No se copiaron .git, secretos, bases personales, uploads, dependencias,
compilados ni archivos Codex. No se hicieron commits o publicaciones.
Los sources originales se preservan; la ?nica clase adicional es el
inicializador opcional autorizado, sin cambios en los services de negocio.
MANIFIESTO-FUENTES.json registra hashes de origen; .gitignore se ampli? aqu?.


## Resultado de la verificacion funcional HTTP

El 15 de octubre de 2026 se confirmaron libres 8080/4200 y se arranco solo esta
copia con su base H2 demo existente. No se ejecutaron de nuevo Maven test,
npm audit, npm ci ni npm run build.

- Angular http://localhost:4200: HTTP 200.
- Login ADMIN en POST http://localhost:8080/api/auth/login: HTTP 200, rol ADMIN.
- GET /api/admin/tours, /api/admin/categorias, /api/admin/embarcaciones y
  /api/salidas con JWT: HTTP 200 y dos registros ficticios en cada listado.
- CORS: Access-Control-Allow-Origin: http://localhost:4200 en login y listados;
  OPTIONS de login con origen 4200, POST y content-type: HTTP 200.
- Consola H2 e imagen demo local: HTTP 200.

No hubo navegador conectado: la revision visual de portada, login y pantallas
queda pendiente. La comunicacion se comprobo mediante HTTP, no mediante clics.
Los procesos de esta verificacion se detienen al terminar. Los logs locales
quedan en target/funcional-8080.log y target/funcional-4200.log, excluidos de Git.

Los pasos 2 a 5 son los comandos exactos para ejecutar una entrega nueva. En la
copia ya verificada use las credenciales del ADMIN existente y mantenga la demo
desactivada; no espere que variables nuevas restablezcan cuentas. Si conserva
SERVER_PORT de otra prueba, quite esa variable antes del arranque:

    Remove-Item Env:SERVER_PORT -ErrorAction SilentlyContinue

Para una futura revision visual basta iniciar ambos servicios con los pasos 4
y 5 y abrir exactamente http://localhost:4200. No hace falta repetir la bateria
de pruebas. Confirmar primero puertos libres y finalizar solo procesos propios.

La copia puede prepararse para GitHub con las exclusiones de .gitignore y sin
credenciales ni bases locales. Esto no equivale a aprobar todos los tests o a
resolver las vulnerabilidades ya informadas. Consultar ENTREGA.md.
