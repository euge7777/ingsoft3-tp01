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

### 4. Uso de IA


