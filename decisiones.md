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