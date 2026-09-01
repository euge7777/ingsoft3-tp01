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
