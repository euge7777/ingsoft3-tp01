# Decisiones del TP1

### 1. Conflicto a la hora de mergear
Github no podía resolvereuge el conflicto solo porque, al estar siendo modificada la misma linea del archivo, Github no reconoce cuál es el cambio correcto. Github no entiende la lógica de lo que dice el texto, solo sabe que hubo un cambio. Por eso deja los marcadores para mostrar las partes que coinciden y que la persona decida qué dejar o qué quitar

### 2. Problemas encontrados
La primera vez que hicimos el merge que muestran el comando en terminal para trear los cambios al repo local salió bien pero me olvidé de hacerlo para la segunda vez que hicimos merge (cuando resolvíamos el conflicto) entonces cuando quise hacer el tag desde la terminal me salía que mi repo local estaba desactualizado y no entendía por qué

### 3. Uso de IA
Utilicé IA cuando no entendí el error que me salió en la terminal cuando quise hacer el tag, me daba una solución pero la verdad no entendía lo que quería que hiciera asi que directamente borré mi repo local y lo volví a clonar directamente. La verdad no me había dado cuenta de que cada vez que creo una pull request y despues mergeo debo llevar esos cambios a mi repositorio local también

# Decisiones del TP2

### 1. App elegida
La aplicación de usada se llama **Expense Tracker** que sirve para el registro y manejo de gastos e ingresos

La elegí por su arquitectura es simple, maneja el server en Spring Boot y Java 17, la capa de client en React, Vite y TypeScript y la parte de la base de datos en PostgreSQL

### 2. Estrategia de dockerización
Mantuve las tres capas que ya maneja la aplicación: frontend (client), backend (server) y la base de datos y administro los tres por el Docker Compose

#### Backend
La aplicación ya venía con un dockerfile para esta parte pero también tenía su propio docker-compose por lo que le quité el docker-compose para después hacer uno solo y me basé en el dockerfile que ya tenía el repo antes

Para el backend usaba una imagen OpenJDK pero decidí cambiarla por Eclipse Temurin que es mucho más vigente y ademas que OpenJDK daba error por no encontrarse disponible. Luego se descargan las dependencias, se crea el archivo JAR y se pasa a la otra etapa donde se copia ese archivo JAR a la imagen final y se ejecuta

#### Frontend
También me basé en el Dockerfila que venía con el repo pero le quité el docker-compose que tenía, mantuve la misma imagen, que es node:18-alpine

Luedo en la segunda etapa nginx se encarga de servir la aplicación y actúa como proxy hacia el backend 

#### PostgresSQL
El docker-compose directamente crea y configura el contenro de PostgresSQL utilizando las variables del .env

En el repo original la configuración de PostgresSQL estaba definida directamente en un archivo pero decidí modificarlo para poder utilizar directamente varibles de entorno. También se agregó un volumen de datos para que aunque se bajen los contendores los datos persistan

El repo original no contaba con un healthcheck a la base de datos asi que también lo agregué

Al final dejé un docker-compose en la raiz del proyecto en lugar de uno para el front y otro para el back como estaba en el repo original

### 3. Problemas encontrados
Aparte de los problemas mencionados antes occurió que el backend al principio permitía solicitudes únicamente a localhost:5173 pero dentro del Docker, el frotend se encuentra en localhost:3000 por lo que las solicitudes de registro e inicio de sesión daban error

### 4. Uso de IA
Se utilizó ChatGPT para consultar sobre la esctructura de los dockerfile tanto del back como el front para que sea multi-stage como pedía el tp. También para la parte de configurar el proxy de Nginx 

# Decisiones del TP3 

### 1. Duración del Sprint
Elegí una duración de los sprints de una semana ya que se adapta al ritmo de los trabajos prácticos presentados en la materia y para mantener un mejor ciclo de planificación

### 2. Límite de trabajo en progeso
Elegí un límite de trabajo en progreso de 2 tareas como la guía aconseja (cantidad de personas + 1). Esto me permite trabajar sobre una tarea y tener otra en segundo lugar si la primera queda bloqueada por alguna razón. Un límite demasiado alto ya perdería la finalidad de colocar un límite a las tareas en progreso

### 3. Diagnóstico de la historia mal escrita
La historia presentada está mal escrita porque decribe más una tarea técnica antes que una funcionalidad que le agregue valor al proyecto. Si la tengo que reescrbir pondría algo como "Como usuario quiero regitrarme en la aplicación para poder guardar mi información y acceder a ella"

### 4. Problemas encontrados
El único problema que encontré era que los comandos en la guía me daban error cuando los copiaba en mi terminal. Fue ahí cuando usé IA para que me dijera el error, como los comandos estaban con una sintaxis de Bash y yo estaba trabajando desde mi PowerShell me iban a dar error

### 5. Uso de IA
Para pasar algunos comandos y que no me dieran error al ejecutarlos en mi PowerShell, la mayoría del trabajao práctico lo hice desde la misma web de Github pero para crear las issues con sus etiquetas correspondientes y después crear la épica, la HU, las dos tareas y el bug 

# Decisiones del TP4

### 1. Estructura del pipeline
Decidí seguir con la misma estructura propuesta en la guía de práctica del tp4

Decidií usar dos jobs independientes, uno para el back y otro para el front aprovechando cada uno cuenta con su propio dockerfile. Además se ejecutan en paralelo ya que la construcción de una imagen no depende de la otra. 

### 2. Caché del pipeline
El pipeline utiliza cache de capas de Docker mediante GitHub Actions. Para evitar que el cache del backend y del frontend se mezclen, se utilizaron scopes diferentes

La primera ejecución construye las capas de las imágenes y las almacena en el cache de GitHub Actions y asi en ejecuciones posteriores se pueden reutilizar las capas que no hayan cambiado

Si el cache desaparece, el pipeline sigue funcionando. La diferencia es que Docker tiene que reconstruir nuevamente las capasO

### 3. Pipeline con Dockerfile
Se utilizan los dockerfiles del tp2 para que de esta manera axista una única definición del proceso de construcción de cada imagen de la app.

Si el pipeline compilara el backend y el frontend mediante comandos propios, existirían dos formas diferentes de construir la aplicación que podrían dejar de coincidir con el tiempo. Utilizando los dockerfiles, el mismo procedimiento de construcción utilizado durante el desarrollo es el que se verifica automáticamente con ci 

### 4. Problemas encontrados
En este tp no tuve problemas, pude seguir la guía fácilmente para la resolución del tp cumpliendo con todos los checkpoints 

### 5. Uso de IA
Si entendí la estructura del workflow presentado en la guía pero utilicé inteligencia artificial para corroborar que los cambios que estaba haciendo a la hora de adaptar la estructura base a mi propio proyecto estuviera correcta 

# Decisiones del TP5

### 1. Lógica elegida para testear

#### Backend

Se elegió testear lógica de servicios y componenetes relacionadas con reglas críticas de la aplicación tales como: 
- Registro de usuarios
- Validacón del mail duplicado
- Actalizacion de categorías propias
- Rechazo de categrías ajenas
- Filtrao de transacciones por categoría y fechas
- Rechazo de transacciones con categoría inexistente
- Cálculo de reportes mensuales: ingresos, egresos y balance
- Consulta de perfil de usuario existente e inexistente
- Generación y validación de JWT
- Obtención del usuario autenticado desde el contexto de seguridad

Estas reglas fueron elegidas porque un error en ellas afecta directamente partes sensibles de la app: autenticación, autorización, transacciones, reportes y seguridad del usuario

#### Frontend

Se elegió testear lógica unitaria sin DOM como decía la consigna: 
- formatAmout: formateo de mmontos con moneda
- formatDate: generación de fechas usadas para filtros diarios y mensuales
- api.ts: configuración del cliente Axios, agregado de token y manejo de errores de autenticación

#### Reglas de negocio

No fue necesario inventar reglas de negocio nuevas para llegar al mínimo de tests

#### Refactor para poder mockear

Tanto en el backend como en el frontend no fue necesario un refactor grande para mockear, porque los servicios ya recibían sus dependencias por constructor o por inyección de dependencias de Spring. Eso permitió reemplazar repositorios y dependencias con Mockito en los unit tests, sin usar una base de datos real y en el front  en api.test.ts se mockearon los módulos axios y useAuthStore con vi.mock() y vi.fn(). De esa manera se pudo probar el comportamiento del cliente HTTP sin hacer llamadas reales a la API ni depender del store real

### 2. Umbrales de coverage

#### Backend 

En el backend se configuró JaCoCo para generar el reporte y ejecutar jacoco:check

El umbral elegido fue: 
- Lines: 30%
- Branches: 35%

Se elegió un umbral inicial moderaod por el backedn ya tenía códig previo de TPs ateriores sin tests unitarios. El objetivo fue que el gate bloquee regresiones y código nuevo sin cobertura, sin imponer de golpe un porcentaje artificialmente alto sobre una base histórica

#### Frontend

En el frontend se configuró Vitest con V8 coverage

El umbral elegido fue:
- Lines: 80%
- Branches: 80%

### 3. Qué entra y qué queda fuera de cada cobertura

#### Backend 

En el backend se excluyeron de la medición las entidades y DTOs ya que son clases de datos sin reglas de negocio, además los mappers generados pro MapStruct y el ServerApplication correspondiente al arranque de Spring Boot

#### Frontend

En el frontend se configuró el coverage para que midiera el api.ts, que contiene la lógica del cliente HTTO, token y manejo de erroes de autenticación, y todo los archivos ts dentro de utils que contiene la lpogica de formato de montos y fechas

### 4. Por qué coverage alto no garantiza calidad

El coverage mide ejecución, no verificación

Por ejemplo, en este proyecto un test podría llamar a formatAmount(1234.5, 'ARS') y no hacer ningun expect. Eso subiría la cobertura porque ejecuta la función, pero no verificaría si el formato, la moneda o los decimales son correctos

Por eso los tests agreados no solo ejecutan funciones, sino que verifican resultados concretos:
- Que el monto teng separador argentino 
- Qye se respete el símbolo de moneda
- Que se agregue Authorization: Bearer <token> cuando hay sesión
- Que el reporte mensual calcule correctamente ingresos, egresos y balance

### 5. Pull Requests 

#### PR 1 - Implementación de tests, coverage y pipeline

Pull Request mergeado: 

https://github.com/euge7777/ingsoft3-tp01/pull/34

Corrida verde del pipeline:

https://github.com/euge7777/ingsoft3-tp01/actions/runs/36807226691

En este PR se agregó:
- Suite de tests del backend
- Suite de tests del frontend
- Coverage en backend con JaCoCo
- Coverage en frontend con Vitest
- Etapas de tests en los Dockerfiles
- Publicación de artifacts de coverage
- Summary en GitHub Actions

Durante el trabajo se detectó un problema en frontend: el Dockerfile usaba node:18-alpine, pero Vitest 5 requiere Node 22.12, Node 24 o superior. Por eso el pipeline fallaba aunque los tests funcionaban localmente. Se actualizó la imagen a node:24-alpine para alinear el entorno Docker con las herramientas de testing usadas

#### PR 2 - Prueba del quality gate

Pull Request abierto y en rojo:

https://github.com/euge7777/ingsoft3-tp01/pull/35

Corrida roja del pipeline:

https://github.com/euge7777/ingsoft3-tp01/actions/runs/36808534887

Este PR agrega código nuevo sin tests en formatDate.ts para demostrar que el quality gate bloquea el merge

Resultado local:
- npm test: verde
- npm run build: verde
- npm run test:coverage: rojo

El check que queda en rojo es build-frontend

La métrica que falló fue coverage de frontend:
- Functions: 75%, debajo del threshold 80%.
- Branches: 75%, debajo del threshold 80%.

Esto demuestra que el PR no falla por compilación ni por tests rotos, sino porque la cobertura quedó por debajo del umbral definido

### 6. Rama sin cubrir

Para este ejercicio revisé el reporte de cobertura generado por Vitest en el frontend, especialmente el PR #34, que se dejó abierto y en rojo para demostrar el funcionamiento del quality gate

El camino sin cubrir elegido está en el archivo: client/src/utils/formatDate.ts

En el reporte aparece como no cubierto el bloque agregado en las líneas aproximadas 52-59, correspondiente a la función buildDateRangeLabel

La rama de código sin cubrir es esta condición:
if (startText > endText) {
  return 'rango inválido'
}

Esa rama se ejecutaría cuando la fecha de inicio sea posterior a la fecha de fin. Por ejemplo, una entrada concreta que recorrería ese camino sería:
buildDateRangeLabel(
  new Date(2026, 9, 5),
  new Date(2026, 8, 30)
)

En ese caso, startText sería mayor que endText, por lo que la función debería devolver que el rango es inválido

Decidí no aregar el test en ese PR proque el objetivo del PR era cerrar la suite principal y los tests que planteé ya cubren las reglas críticas de la aplicación. Esa rama detectada correponde a un caso adicinal de validación de rango de fechas. Es un caso válido para mejorar la suite en una iteración posterior pero no neceraria para el primer quality gate ya que le umbral definido ya se cumplía y las reglas princiaples estaban testeadas

### 7. Problemas encontrados

Problemas encontrados y cómo se resolvieron
- En backend, el test generado por Spring Boot intentaba levantar el contexto completo y fallaba por configuración de base de datos. Se eliminó porque no era un unit test de lógica de negocio
- Se configuró JaCoCo para generar reporte y aplicar jacoco:check
- En frontend, se agregó Vitest y coverage con V8
- Al principio el frontend pasaba localmente, pero fallaba en GitHub Actions porque el Dockerfile usaba Node 18 y Vitest 5 requiere una versión más nueva. Se resolvió cambiando la imagen a node:24-alpine

### 8. Uso de IA

Se utilizó inteligencia artificial para organizar los pasos a llevar a cabo de este trabajo práctico ya que era bastante extenso, encontrar una estructura para los tests y revisar errores

La verificaci´´on se hizo ejecuntando los comandos localmente y en GitHub Actions: 
- Backend: mvn clean verify
- Frontend: nmp test, npm run test:coverage, npm run build
- Pipeline: checks build-backend y build-frontend en GitHub Actions

Los tests fueron revisados para ver si había errores, entender bien el comportamiento de cada uno y qué casos quedaban sin cubrir 

# Decisiones del TP6

### Enlaces de este tp

- Paquete backend GHCR: https://github.com/euge7777/ingsoft3-tp01/pkgs/container/ingsoft3-tp01-backend
- Paquete frontend GHCR: https://github.com/euge7777/ingsoft3-tp01/pkgs/container/ingsoft3-tp01-frontend
- PR con "Entrar al registry" salteado: https://github.com/euge7777/ingsoft3-tp01/actions/runs/36901113196/job/110500227246
- Corrida de main con publicación al final del job: https://github.com/euge7777/ingsoft3-tp01/actions/runs/37403794571/job/112081043601
- QA Front: https://expense-fornt-qa.onrender.com
- QA API: https://expense-api-qa.onrender.com
- PROD Front: https://expense-front-prod.onrender.com
- PROD API: https://expense-api-prod.onrender.com

### 1. Artefactos

El pipeline publica dos artefactos Docker en GHCR: una imagen para el backend y otra para el frontend. Ambas quedan etiquetadas con el SHA del commit que las produjo.

La publicación se realiza únicamente cuando el cambio llega a main y después de que la verificación del job finalizó correctamente. En los Pull Requests se construye y verifica, pero no se publica.

Esto permite que el registry tenga un significado confiable: una imagen publicada representa un cambio que pasó por la verificación previa. Si se publicaran imágenes aun cuando los tests fallaran, dejaría de ser cierto que lo publicado corresponde a una versión validada.

La cadena se sostiene en tres puntos:
1. Los cambios pasan por el pipeline antes de llegar a main.
2. Solo los push a main publican imágenes.
3. El paso de publicación está al final del job, después de los tests y reportes.

### 2. Continous Delivery

En este TP se implementó Continuous Delivery.

Cada cambio integrado a main llega automáticamente al entorno QA después de que el CI queda en verde. Producción no se despliega de manera automática: requiere una aprobación humana explícita mediante el environment production.

No se implementó Continuous Deployment porque en ese modelo el paso a producción también sería automático, sin intervención humana.

En este proyecto se decidió mantener aprobación manual porque permite revisar el estado de QA antes de promover el cambio a producción y evita que cualquier cambio verificado llegue directamente a usuarios finales.

### 3. Enviroments y secrets

Se crearon dos environments en GitHub:
- qa
- production

El job deploy-qa depende del build del backend y del frontend mediante needs, por lo que QA solo se despliega si ambos jobs terminan correctamente.

Además, los deploys están condicionados a github.ref == 'refs/heads/main' por lo que los Pull Requests verifican, pero no despliegan.

Los secrets están separados por environment.

QA utiliza:
RENDER_HOOK_API_QA
RENDER_HOOK_FRONT_QA

Producción utiliza:
RENDER_HOOK_API_PROD
RENDER_HOOK_FRONT_PROD

Esto limita el alcance de las credenciales: los jobs de QA no necesitan ni pueden utilizar los deploy hooks de producción.
El environment production tiene además required reviewer, por lo que el job queda pausado hasta recibir aprobación.

### 4. Criterios del gate

Antes de aprobar un deploy a producción se revisa:
- build-backend haya terminado correctamente
- build-frontend haya terminado correctamente
- deploy a QA haya finalizado
- smoke test de QA esté en verde
- commit que se está promoviendo sea el esperado
- que no haya errores relevantes en los logs o en la corrida
  
La aprobación no se usa solo como un botón formal, sino como una compuerta antes de producción.

También se realizó un rechazo real con motivo escrito para comprobar que el gate puede bloquear un deploy. En ese caso, el job de producción no continúa y el cambio no llega a PROD.

### 5. Free tier

Se utilizaron los planes gratuitos de Render y Neon.

Una limitación importante de Render Free es que los servicios pueden entrar en estado de suspensión cuando están inactivos. Esto provoca cold starts, por lo que la primera solicitud puede tardar varios segundos o incluso cerca de un minuto. 

Por este motivo los smoke tests no hacen una única llamada, sino que utilizan reintentos con espera entre intentos y --max-time para evitar que un request quede colgado indefinidamente.

También existe un límite de horas de instancia y minutos de build en Render, por lo que no conviene mantener servicios artificialmente despiertos ni generar deploys innecesarios.

El pipeline contempla estas limitaciones mediante reintentos y tiempos de espera.

### 6. Render

Render se utiliza como plataforma de ejecución de los cuatro servicios:
- backend QA
- frontend QA
- backend PROD
- frontend PROD

Cada servicio tiene Auto-Deploy desactivado. Esto es importante porque el deploy debe ser disparado por GitHub Actions, no directamente por Render.

El pipeline utiliza Deploy Hooks y agrega el commit mediante &ref=$GITHUB_SHA para pedirle a Render que despliegue el mismo commit que fue verificado.

Sin embargo, existe una limitación importante, Render reconstruye la aplicación desde el repositorio. Por lo tanto, aunque se despliega el mismo commit, no se ejecuta exactamente la misma imagen publicada previamente en GHCR.

La garantía que se pierde es la de identidad binaria: el código corresponde al mismo commit, pero la imagen que corre en Render es una reconstrucción nueva.

### 7. Smoke tests

Después de cada deploy se ejecuta un smoke test.
En QA y PROD se comprueba /health y /api/v1/health/db donde /health verifica que el backend esté vivo, mientras qye /api/v1/health/db ejecuta una consulta contra PostgreSQL y permite comprobar que el backend puede comunicarse con la base. 

La URL raíz del frontend verifica que Nginx y la aplicación web estén disponibles.

El smoke test prueba que los componentes básicos del sistema responden después del despliegue, pero no prueba toda la lógica de negocio.

Tampoco demuestra por sí solo qué versión exacta está corriendo: comprueba disponibilidad, no identidad del artefacto desplegado.

### 8. Deployment pattern

Para una producción real elegiría blue-green deployment, si el costo de infraestructura lo permite.

La estrategia consiste en mantener dos entornos completos: uno activo y otro donde se despliega la nueva versión. Una vez validada, el tráfico se cambia hacia el nuevo entorno.

La ventaja principal es que el rollback puede ser muy rápido: si aparece un problema, se vuelve a dirigir el tráfico hacia el entorno anterior.

Para estrategias como canary sería necesario contar con mejor observabilidad, por ejemplo métricas, logs centralizados, alertas y medición del comportamiento de la nueva versión.

El plan de rollback actual consiste en volver a desplegar una versión anterior conocida como estable desde Render.

Se realizó una prueba real:
Commit restaurado: af36073
Estado final: Live
Duración medida: 2 min 11 s

El rollback revierte el código desplegado, pero no deshace automáticamente modificaciones realizadas en los datos de PostgreSQL.

### 9. Problemas encontrados. Uso de IA

Durante el desarrollo del TP se encontraron varios problemas:
- Al cambiar de computadora faltaban variables locales de PostgreSQL
- El frontend QA tenía una URL incorrecta
- El servicio frontend QA había sido creado con un typo en el nombre
- El smoke test fallaba porque se estaba utilizando una URL incorrecta
- Uno de los Deploy Hooks de producción estaba mal configurado
- Los cold starts de Render provocaron timeouts durante los primeros intentos del smoke test.
  
Los problemas se resolvieron revisando logs de Render, probando manualmente los endpoints con curl, verificando las URLs reales de los servicios, corrigiendo los secrets de GitHub y reejecutando los jobs.

Se utilizó IA como asistencia para adaptar la guía del TP al stack del proyecto, configurar los health checks, Nginx y una parte de los smoke tests, también para consultar por los problemas que me fueron surgiendo a lo largo del tp.

Las sugerencias fueron verificadas mediante:
- Ejecución local con Docker
- Corridas reales de GitHub Actions
- Logs de Render
- Pruebas con curl
- Comprobación de los servicios QA y PROD
- Descarga pública de las imágenes desde GHCR

# Decisiones del TP7

### 1. Build once, deploy many

En el TP6, el proceso de despliegue dependía de que Render construyera la aplicación a partir del código fuente. Esto generaba una separación entre el artefacto validado por GitHub Actions y el artefacto que finalmente ejecutaba cada entorno.

El problema de este enfoque es que dos construcciones realizadas a partir del mismo código no necesariamente producen el mismo resultado. Pueden existir diferencias en dependencias, configuración, entorno de construcción o versiones de herramientas.

Por ejemplo, en mi app, el frontend y el backend podían superar sus pruebas en GitHub Actions, pero después Render realizaba otra construcción para QA o producción. En consecuencia, no teníamos la garantía de estar ejecutando exactamente el artefacto que había pasado las verificaciones.

En el TP7 se implementa el principio de Build once, deploy many: GitHub Actions construye una única imagen Docker del backend y una del frontend para cada commit de main. Estas imágenes se publican en GitHub Container Registry (GHCR) y posteriormente se utilizan para desplegar QA y PROD.

De esta manera, la imagen Docker pasa a ser la unidad de despliegue. No se vuelve a construir la aplicación en cada entorno, sino que reutilizamos el mismo artefacto, modificando únicamente la configuración necesaria.

### 2. Estrategia de versionado: SHA y release

Utilzo dos mecanismos de identificación que cumplen funciones diferentes.

#### Etiquetas SHA en GHCR

Las imágenes publicadas se etiquetan utilizando el identificador completo del commit de Git:

ghcr.io/euge7777/ingsoft3-tp01-backend:sha-<commit>

ghcr.io/euge7777/ingsoft3-tp01-frontend:sha-<commit>

El SHA permite relacionar directamente una imagen con la versión del código fuente que originó su construcción.

Esta estrategia evita la ambigüedad de etiquetas genéricas y facilita identificar qué versión se desplegó, comparar QA con PROD y recuperar una versión anterior.

La etiqueta identifica la versión del código, mientras que el digest de Docker identifica el contenido concreto de la imagen. Para una comprobación estricta de identidad del artefacto, el digest es la referencia más fuerte.

#### Etiqueta v7.0.0 en Git

La etiqueta anotada v7.0.0 identifica la versión entregada del TP7 en el historial de Git.

A diferencia de sha-<commit>, que utilizamos para localizar imágenes en el registro, v7.0.0 funciona como referencia de publicación y permite encontrar el commit correspondiente a la entrega.

#### Por qué no publicamos latest

No utilizo la etiqueta latest como mecanismo de despliegue porque es mutable: hoy puede identificar una imagen y mañana otra diferente.

Si QA y PROD utilizaran latest, sería difícil asegurar que están ejecutando exactamente la misma versión. Además, un nuevo push podría modificar la imagen a la que apunta esa etiqueta.

Con sha-<commit>, cada ejecución del pipeline utiliza una referencia específica al commit que fue construido y probado.

#### Configuración inicial de Render

Los servicios de Render fueron configurados con la modalidad Existing Image, utilizando imágenes alojadas en GHCR.

La imagen indicada al configurar inicialmente el servicio permitió establecer su origen y realizar el primer despliegue.

Sin embargo, no significa que esa versión inicial sea la que permanece ejecutándose. En el pipeline, los deploy hooks de Render reciben el parámetro imgURL con la etiqueta correspondiente al SHA de cada nuevo commit.

### 3. Cómo pasar de v7.0.0 a la imagen desplegable

La relación entre la release y la imagen se obtiene a partir del commit al que apunta la etiqueta.

Se utiliza:

git rev-list -n 1 v7.0.0

Este comando devuelve el SHA completo del commit etiquetado.

A continuación, ese mismo identificador se utiliza en las referencias de GHCR:

ghcr.io/euge7777/ingsoft3-tp01-backend:sha-<SHA>
ghcr.io/euge7777/ingsoft3-tp01-frontend:sha-<SHA>

De esta forma podemos pasar de la versión de entrega a las imágenes correspondientes sin depender de latest ni reconstruir el código.

La relación es:

Release v7.0.0 -> commit SHA -> imágenes Docker de ese SHA -> despliegue en Render

### 4. Cómo comprobar que se ejecuta la imagen correcta

Comprobar que una aplicación responde correctamente no es lo mismo que comprobar qué artefacto se encuentra ejecutando.

En el pipeline se utilizan los smoke tests contra los servicios de QA y PROD, verificando que el frontend responde y que el backend dispone de endpoints de salud, incluyendo una comprobación de conectividad con la base de datos.

Estos controles sirven para detectar problemas básicos, como un servicio caído, una API inaccesible o un fallo de conexión con PostgreSQL.

Sin embargo, un smoke test no demuestra por sí solo que el servicio esté ejecutando la imagen correspondiente al commit recién construido.

Por ejemplo, una versión anterior de Expense Tracker podría continuar funcionando y responder correctamente a /health, aunque el nuevo despliegue hubiera fallado.

Para comprobar la versión desplegada, se revisa la ejecución de GitHub Actions y los eventos o detalles de despliegue de Render, contrastando la referencia de imagen utilizada con el SHA esperado.

En una verificación más estricta también se compara el digest de la imagen que está ejecutando el entorno con el digest publicado en GHCR.

Desde la perspectiva de un cliente HTTP externo, una respuesta de salud solamente confirma disponibilidad. Para comprobar desde afuera la versión exacta sería necesario exponer información confiable de versión o digest desde la aplicación y contrastarla con el registro y el despliegue.

Por eso se distinguen dos verificaciones:

- Disponibilidad: comprobar que los endpoints responden correctamente
- Identidad del artefacto: comprobar que el despliegue corresponde a la imagen publicada para el SHA esperado

Ambas son necesarias y ninguna reemplaza a la otra.

### 5. Pruebas de integración

En el tp implemento tres pruebas en client/e2e/api.spec.ts, utilizando APIRequestContext de Playwright para enviar solicitudes HTTP reales al backend desplegado en QA.

#### Prueba 1: Crear una categoría

La prueba crea una categoría, consulta nuevamente las categorías para comprobar su persistencia y finalmente elimina el registro creado.

Verifica la integración entre la autenticación, el controlador HTTP, la lógica de negocio y PostgreSQL.

#### Prueba 2: Crear y eliminar un gasto

La prueba registra un gasto mediante la API, comprueba que quedó almacenado y posteriormente lo elimina.

Elegí este flujo porque registrar gastos es una funcionalidad central de Expense Tracker. Si se rompe, la aplicación pierde una parte importante de su utilidad.

#### Prueba 3: Modificar una categoría

La tercera prueba modifica una categoría existente creada para la prueba y comprueba que sus nuevos datos se guardaron correctamente.

Elegí esta operación porque complementa las pruebas de creación y eliminación. Permite detectar problemas en las actualizaciones de datos y en su persistencia, que no serían descubiertos únicamente comprobando altas y bajas.

#### Qué no incluye integración

No se incluyen interacciones visuales con botones, formularios, modales o navegación del navegador.

Esos aspectos corresponden a las pruebas E2E.

Tampoco se repiten todas las validaciones internas del backend, porque las reglas de negocio aisladas y los casos de borde deben cubrirse principalmente mediante pruebas unitarias.

De esta forma evitamos duplicar responsabilidades entre los distintos niveles de la pirámide de pruebas.

#### Responsabilidad ante fallos

Si falla alguna de estas pruebas, la primera responsabilidad de diagnóstico corresponde al área de desarrollo del backend y persistencia, ya que la prueba está verificando el comportamiento de la API y su comunicación con PostgreSQL.

Esto no significa que cualquier fallo sea necesariamente un defecto del backend: también podría deberse a problemas del entorno QA, autenticación, conectividad o datos de prueba.

En el proyecto, el responsable inicial de revisar estos fallos es el equipo que mantiene la API y el pipeline.

### 6. Pruebas E2E

Implementé tres pruebas en client/e2e/expense.spec.ts, utilizando Playwright con Chromium.

A diferencia de las pruebas de integración, estas pruebas reproducen acciones de un usuario real sobre el frontend desplegado en QA.

#### Prueba 1: Crear un ingreso con una categoría nueva

La prueba inicia sesión, abre el formulario de transacciones, crea una categoría desde la interfaz y registra un ingreso asociado a esa categoría.

Después comprueba que el ingreso aparece correctamente y que sus datos fueron persistidos.

Elegí este flujo porque permite recorrer varias partes del sistema: autenticación, formularios, creación de categorías, selección de relaciones y registro de ingresos.

#### Prueba 2: Crear un gasto

La prueba inicia sesión, crea un gasto desde el navegador y verifica que aparece en el dashboard con el importe esperado.

ELegí este caso porque el registro de gastos es la funcionalidad principal de Expense Tracker. Una falla en este recorrido afecta directamente al usuario.

#### Prueba 3: Eliminar un gasto

La prueba crea un gasto, identifica su fila por el título generado para ese test y utiliza el botón Delete de esa misma fila.

Después comprueba que el registro desaparece tanto de la interfaz como de la base de datos.

Elegí este recorrido porque verifica una operación destructiva y permite detectar errores en la asociación entre una fila de la tabla y su botón de eliminación.

En particular, evité seleccionar simplemente el primer botón Delete, porque eso podría eliminar un gasto distinto del creado por la prueba.

#### Qué no incluye E2E

No incorporé todas las validaciones posibles de campos, cálculos internos ni combinaciones de datos.

Tampoco utilicé el navegador para verificar cada operación individual de la API.

Estas pruebas son más costosas porque requieren iniciar Chromium, navegar por la aplicación e interactuar con distintos componentes. Por eso solo se seleccionan tres recorridos representativos.

Siguiendo la pirámide de pruebas, dejo las comprobaciones más pequeñas para los tests unitarios, las interacciones entre componentes para integración y los principales recorridos de usuario para E2E.

#### Responsabilidad ante fallos

Si se rompe un recorrido E2E, el primer responsable de revisar el problema es el equipo que mantiene el frontend y la experiencia de usuario.

También puede ser necesaria la intervención de quienes mantienen el backend o el entorno, ya que las pruebas atraviesan el sistema completo.

Los fallos aparecen en GitHub Actions y quedan acompañados por los reportes HTML y, cuando corresponde, capturas y trazas de Playwright.

### 7. Integración y E2E: por qué no son equivalentes

Las dos suites verifican partes diferentes del funcionamiento de Expense Tracker.

| Integración | E2E |
| ----------- | --- |
| Envía solicitudes HTTP directamente a la API | Interactúa con la aplicación desde Chromium |
| Comprueba autenticación, endpoints y persistencia | Comprueba formularios, botones, navegación y resultados visibles |
| No depende del texto de un botón | Sí puede detectar que un botón dejó de tener el nombre esperado |
| Es más acotada y rápida | Recorre más componentes y puede ser más lenta | 
| Ayuda a diagnosticar fallos de backend y BD | Detecta defectos funcionales visibles para el usuario |
| Demostración de integración verde y E2E roja | Para verificar el comportamiento del gate de calidad realizamos una modificación intencional en CreateTransactionForm.tsx. |

Cambiamos el texto del botón de 'Create transaction' a 'Save transaction'

La aplicación continuó compilando y los endpoints del backend siguieron funcionando correctamente.

Por eso las tres pruebas de integración terminaron en verde.

Sin embargo, las pruebas E2E intentaban localizar el botón utilizando su nombre accesible original:

page.getByRole('button', {
  name: 'Create transaction'
})

Como el texto había cambiado, Playwright no encontraba el elemento esperado y las pruebas que dependían de él fallaban.

El resultado fue:

Build verde -> QA desplegado -> integración en verde -> E2E en rojo -> PROD omitido

Esta demostración permitió comprobar que una aplicación puede tener una API funcional y, al mismo tiempo, presentar un defecto detectable en los recorridos de usuario.

También confirmó que los dos tipos de prueba son complementarios y que el gate funciona correctamente: al fallar E2E, GitHub Actions no habilitó el despliegue de producción.

Posteriormente restauré el texto original, se ejecuta nuevamente el pipeline y asi recupero los resultados verdes.

### 8. Integración amplia frente a integración estrecha

Realizo integración amplia: los tests envían solicitudes a la API de QA ya desplegada y utilizan su base de datos PostgreSQL real.

No levanto una instancia aislada del backend y una base de datos temporal exclusivamente para estas pruebas.

#### Ventajas de la integración amplia

La principal ventaja es que puedo calidar el sistema en condiciones más cercanas al despliegue real.

Las pruebas comprueban que el backend publicado, su configuración de entorno, la autenticación, las rutas HTTP y su conexión con PostgreSQL funcionan conjuntamente.

Además, verifican el servicio que efectivamente fue desplegado en QA, en lugar de verificar solamente una instancia construida dentro del runner de GitHub Actions.

#### Desventajas

Las pruebas dependen de que QA esté disponible y de que su base de datos funcione.

También pueden verse afectadas por latencia de red, cold starts, datos compartidos o cambios concurrentes en el entorno.

Por ese motivo, los resultados pueden ser más lentos y menos aislados que los de integración estrecha.

#### Qué aportaría la integración estrecha

Una alternativa sería levantar el backend junto con una base PostgreSQL exclusiva para las pruebas, por ejemplo mediante Docker Compose o Testcontainers.

Esto mejoraría el aislamiento, permitiría controlar mejor los datos iniciales y reduciría la dependencia de un entorno remoto.

Sin embargo, no comprobaría necesariamente que la versión desplegada en Render tenga una configuración correcta ni que QA sea accesible desde afuera.

Uso integración amplia porque uno de los objetivos centrales del TP7 era verificar el despliegue real antes de permitir el paso a producción.

### 9. Cold start, timeouts, retries y pruebas flaky

El entorno QA está alojado en Render y puede experimentar períodos de inactividad.

En planes gratuitos, un servicio suspendido puede necesitar un tiempo adicional para reactivarse. Esto se conoce como cold start.

El problema es que una prueba automatizada podría interpretar la demora como un fallo funcional cuando, en realidad, el servicio todavía se está iniciando.

Para reducir este riesgo utilizamos varias medidas.

#### Smoke test previo

El job deploy-qa ejecuta comprobaciones de disponibilidad antes de iniciar las pruebas de integración y E2E.

Realiza varios intentos de conexión a los endpoints de salud y al frontend, esperando entre intentos.

De esta manera se reduce la probabilidad de iniciar las suites mientras alguno de los servicios todavía no responde.

#### Timeouts

COnfiguré Playwright con un timeout general de 60 segundos por prueba.

En el recorrido de creación de ingreso, que incluye más interacciones, utilizamos un límite de 90 segundos.

También utilizo timeouts específicos para determinadas operaciones HTTP y esperas de elementos de la interfaz.

#### Retries

La configuración de Playwright contempla un reintento en caso de fallo.

Este mecanismo puede ayudar a distinguir errores transitorios relacionados con condiciones de ejecución, aunque no debe utilizarse para ocultar defectos reales.

Por ejemplo, cuando cambié el texto del botón de creación, repetir la prueba no podía solucionar el problema porque el defecto estaba presente en el frontend.

#### Qué es un test flaky

Una prueba flaky es aquella que, utilizando el mismo código y las mismas condiciones previstas, algunas veces pasa y otras falla sin que exista necesariamente un cambio funcional en la aplicación.

Esto puede producirse por condiciones de carrera, respuestas lentas, problemas de red, dependencia entre pruebas o selectores inestables.

Para reducir estos problemas:

- Utilicé selectores específicos, como select[name="color"], en lugar de seleccionar un elemento genérico ambiguo
- Generé identificadores únicos para categorías y transacciones
- Elimino los datos de prueba al finalizar
- Comrpuebo resultados mediante esperas y aserciones de Playwright, evitando pausas fijas innecesarias
- Ejecuto las suites con un solo worker para disminuir interferencias entre pruebas sobre el mismo usuario de QA

Los retries son una protección adicional, pero la solución principal es escribir pruebas deterministas y aislar correctamente los datos.

### 10. Una misma imagen de frontend para QA y PROD

El frontend de la aplicación utiliza React y Vite. La aplicación se compila durante la construcción de su imagen Docker y después se sirve mediante Nginx.

Para aplicar Build once, deploy many, la imagen del frontend debe ser reutilizable en QA y PROD sin volver a ejecutar la compilación de Vite.

Esto exige separar el contenido construido de la configuración específica del entorno.

#### Qué contiene la imagen

La imagen contiene los archivos estáticos generados por Vite, el servidor Nginx y la configuración necesaria para servir la aplicación y redirigir las solicitudes correspondientes al backend.

Estos archivos son los mismos al desplegar una determinada imagen en QA o en PROD.

#### Qué depende del entorno

La ubicación del backend al que debe comunicarse el frontend depende del entorno:

QA utiliza https://expense-api-qa.onrender.com.

PROD utiliza https://expense-api-prod.onrender.com.

Esa diferencia debe resolverse mediante configuración de ejecución, sin generar dos bundles de frontend diferentes.

En el proyecto utilizo un archivo de configuración de Nginx basado en una plantilla, que permite separar la configuración utilizada por el servidor del contenido estático del frontend.

Con la configuración de ejecución correspondiente, ambos entornos pueden consumir la misma imagen del frontend, mientras Nginx utiliza el destino de API adecuado.

### 11. Problemas encontrados y soluciones

#### Despliegue de imágenes en Render

Inicialmente fue necesario adaptar el mecanismo de despliegue utilizado en el TP6.

Al pasar a servicios configurados mediante Existing Image, modifiqué GitHub Actions para enviar a Render la URL de la imagen mediante imgURL, en lugar de utilizar el parámetro ref asociado al despliegue desde el repositorio.

Esto permitió indicar explícitamente la imagen del SHA correspondiente a cada ejecución.

#### Error 508 de Nginx

Durante las pruebas en QA encontré un error HTTP 508 relacionado con un bucle de enrutamiento.

Revisando la configuración del proxy Nginx, modifiqué el encabezado Host, reemplazando el valor anterior por $proxy_host para que la solicitud hacia el destino configurado utilizara el host correspondiente.

Esto permitió resolver el problema de enrutamiento.

#### Error 403 de CORS

Posteriormente, la aplicación devolvía 403 Invalid CORS request al intentar registrar un usuario desde QA.

El problema era que la configuración CORS del backend no contemplaba los orígenes públicos de los frontends desplegados en Render.

Agregué los dominios de QA y PROD a los orígenes permitidos en WebConfig.java.

Con esta corrección pude registrar e iniciar sesión con el usuario de pruebas.

#### Problemas de selectores en Playwright

Durante la implementación de las E2E tenía errores porque algunos botones no estaban disponibles directamente en el dashboard.

También encontramos un problema de modo estricto de Playwright al seleccionar el color de una categoría mediante getByRole('combobox'), ya que la página tenía varios selectores.

Lo solucionamos utilizando select[name="color"], que identifica de forma precisa el elemento requerido.

Estas correcciones hicieron que las tres pruebas E2E terminaran correctamente tanto en la computadora local como en GitHub Actions.

#### Demostración del fallo E2E

INtroduje temporalmente un cambio en el nombre del botón de creación de transacciones.

Aunque las pruebas de integración continuaron pasando, las E2E detectaron el cambio y fallaron.

El job de producción quedó omitido automáticamente debido a la dependencia needs: e2e.

Luego revertí el cambio para confirmar que el pipeline volvió a pasar.

### 12. Declaración de uso de inteligencia artificial

Durante el desarrollo del TP7 se utilizó IA como herramienta de asistencia técnica.

Se empleó para comprender los requisitos de la consigna, orientar la configuración de GitHub Actions y Docker, preparar las pruebas de integración y E2E con Playwright, interpretar mensajes de error y proponer correcciones.

También se utilizó como apoyo para organizar y redactar la documentación de las decisiones técnicas.

Las propuestas se contrastaron mediante la ejecución de comandos, pruebas locales, resultados de GitHub Actions y verificaciones sobre la aplicación desplegada.

Los problemas encontrados durante la implementación se analizaron y corrigieron de forma iterativa, utilizando los errores observados como evidencia.

La herramienta de IA funcionó como apoyo al desarrollo y a la documentación, mientras que la verificación del comportamiento del proyecto se realizó mediante las ejecuciones y resultados obtenidos.
