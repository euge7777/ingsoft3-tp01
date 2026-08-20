# Expense Tracker

La aplicación esta compuesta por:

- Frontend: React + TypeSript + Vite, servido con Nginx
- Backend: Spring Boot + Java17
- Base de datos: PostgreSQL

Para ejecutar el proyecto se necesita:

- Git
- Docker
- Docker compose

Comprobar el Docker con

```bash
docker --version
docker compose version
```
Luego clonar el repositorio con

```bash
git clone https://github.com/euge7777/ingsoft3-tp01.git
```
## Configurar varibles de entorno

El proyecto utiliza PostgresSQL como base de datos. No es necesario instalarlo ya que Docker ya se encarga de descargar la imagen, levantar el contenedor y configurar la base de datos
El archivo `.env` contiene las variables que Docker necesita para configurar la base de datos
El repositorio incluye un `.env.example` que debe copiar y completar con sus propios valores

Para crear el archivo debe hacer desde la raiz del proyecto:

```bash
cp .env.example .env
```
El archivo contiene las siguientes variables:

```env
POSTGRES_DB=expense_tracker
POSTGRES_USER=your_user 
POSTGRES_PASSWORD=your_password
```
Su usuario suele ser `postgres` y la contraseña usted la elige 

## Levantar la aplicacion

Una vez que termine con lo anterior ya puede levantar el proyecto

Desde la raiz del proyecto ejecute:
```bash
docker compose up
```
Docker está levantando y contruyendo tres servicios:
- Backend
- Frontend
- Base de datos 

Una vez que termine puede comprobar que todos los servicios estan funcionando con:
```bash
docker compose ps
```
## Acceder a la aplicación

Cuando ya haya levantado docker debe entrar desde cualquier navegador a:
```text
http://localhost:3000
```
Ya puede registrar su usuario, iniciar sesión y usa Expense Tracker

## Detener la aplicacion

Mejor si no dejas la aplicación corriendo asi que cuando ya quiera frenarla lo único que debe hacer es:
```bash
docker compose down
```
Este comando elimina los contenedores, pero todavía conserva el volumne de PostgreSQL por lo que los datos siguen disponibles por si quiere levantar la aplicación de nuevo
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

Ademas del `docker-compose.yml` el repositorio cuenta con un `docker-compose.registry.yml`
Lo que hace este archivo es que levanta el proyecto usando las imagenes publicadas en Docker Hub en lugar de contruirlas localmente en su máquina

Primero puede descagar las imágenes haciendo: 
```bash
docker compose -f docker-compose.registry.yml pull
```
Luega levanta los servicios con:
```bash
docker compose -f docker-compose.registry.yml up -d
```
Puede comprobar el estado de las imagenes con:
```bash
docker compose -f docker-compose.registry.yml ps
```
La aplicacion vuelve a queda disponible en el mismo 
```text
http://localhost:3000
```
Para detener los servicios debe hacer:
```bash
docker compose -f docker-compose.registry.yml down
```
Y para deterne los servicios y también borrar el volumen hace:
```bash
docker compose -f docker-compose.registry.yml down -v
```

