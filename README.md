# Expense Tracker

La aplicación Expense Tracker funciona para llevar un registro de tus ingresos y gastos y los clasifica por categoría

La aplicación está compuesta por:

- Frontend: React + TypeSript + Vite, servido con Nginx
- Backend: Spring Boot + Java17
- Base de datos: PostgreSQL

Para ejecutar el proyecto se necesita:

- Git
- Docker
- Docker compose

Podes probar tu versión de Docker con:

```bash
docker --version
docker compose version
```
Luego cloná el repositorio con:

```bash
git clone https://github.com/euge7777/ingsoft3-tp01.git
```
Todos los comandos a partir de ahora se deben hacer en la raíz del proyecto. Como todavía no estamos en la raíz del proyecto, para ingresar haga:
```bash
cd ingsoft3-tp01
```

## Configurar variables de entorno

El proyecto utiliza PostgresSQL como base de datos. No es necesario tenerlo instalado para el proyecto, Docker ya se encarga de descargar la imagen, levantar el contenedor y configurar la base de datos

El archivo `.env` contiene las variables que Docker necesita para configurar la base de datos

El repositorio incluye un `.env.example` que debe copiar y completar con sus propios valores

Para eso primero haga:

```bash
cp .env.example .env
```
Ahora usted tiene su propio archivo `.env` en la carpeta del proyecto en su propia máquina. Este tipo de archivo no va a aparecer si usted entra a la carpeta del repositorio por su gestor de archivos asi que debe modificarlo directamente en la terminal

Para modificar el archivo haga: 
```bash
nano .env
```
En su pantalla debería aparecer algo como:

```env
POSTGRES_DB=expense_tracker
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
```
Ahora modifique su contraseña (donde dice `your_password`) y escriba la contraseña que desee 

Para guardar sus cambios debe hacer:
```bash
ctrl + O
enter
ctrl + X
```

## Levantar la aplicación

Ahora para levantar la aplicación tiene dos caminos posibles:

1. Si quiere descargar las imágenes de Docker en su máquina local 
2. Si prefiere utilizar las imágenes subidas previamente a Docker Hub

Si prefiere utilizas las imágenes del Docker Hub (opción 2) dirijase a la sección *Levantar el proyecto con las imágenes del registry*

Si prefier descargar las imágenes en su máquina local continúe con estas instrucciones:

Desde la raiz del proyecto ejecute:
```bash
docker compose up
```
Docker está levantando y contruyendo tres servicios:
- Backend
- Frontend
- Base de datos 

Una vez que termine puede comprobar que todos los servicios estén funcionando con:
```bash
docker compose ps
```
## Acceder a la aplicación

Cuando ya haya levantado docker debe entrar desde cualquier navegador a:
```text
http://localhost:3000
```
Ya puede registrar su usuario, iniciar sesión y usa Expense Tracker

## Detener la aplicación

Mejor si no dejas la aplicación corriendo asi que para frenarla lo único que debe hacer es:
```bash
docker compose down
```
Este comando elimina los contenedores, pero todavía conserva el volumne de PostgreSQL por lo que los datos siguen disponibles por si quiere levantar la aplicación en algún otro momento

Para volver a levantar la aplicación debe hacer:
```bash
docker compose up -d
```
Si quiere eliminar los contenedores y el volumen de datos de PostgreSQL debe ejecutar:
```bash
docker compose down -v
```
Y asi los datos guardados en PostgreSQL también son eliminados

## Levantar el proyecto con las imagenes de registry

El repositorio ya cuenta con un `docker-compose.registry.yml` que usaremos para levantar la aplicación

Lo que hace este archivo es que levanta el proyecto usando las imágenes publicadas en Docker Hub en lugar de contruirlas localmente en su máquina

Primero descargue las imágenes haciendo: 
```bash
docker compose -f docker-compose.registry.yml pull
```
Luega levante los servicios con:
```bash
docker compose -f docker-compose.registry.yml up -d
```
Puede comprobar el estado de las imágenes con:
```bash
docker compose -f docker-compose.registry.yml ps
```
## Acceder a la aplicación 

Para ingresar a la aplicación abra en cualquier navegador:  
```text
http://localhost:3000
```
## Detener la aplicación

Para detener los servicios debe hacer desde terminal:
```bash
docker compose -f docker-compose.registry.yml down
```
Y para detener los servicios y también borrar el volumen de PostgresSQL puede hacer:
```bash
docker compose -f docker-compose.registry.yml down -v
```

