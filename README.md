# Expense Tracker

La aplicación Expense Tracker funciona para llevar un registro de sus ingresos y gastos

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
Todos los comandos a partir de ahora se deben hacer en la raiz del proyecto. Como todavía no estamos en la raiz del proyecto, para ingresar haga:
```bash
cd ingsoft3.tp01
```

## Configurar variables de entorno

El proyecto utiliza PostgresSQL como base de datos. No es necesario instalarlo ya que Docker ya se encarga de descargar la imagen, levantar el contenedor y configurar la base de datos

El archivo `.env` contiene las variables que Docker necesita para configurar la base de datos

El repositorio incluye un `.env.example` que debe copiar y completar con sus propios valores

```bash
cp .env.example .env
```
Ahora usted tiene su propio archivo `.env` en su carpeta que debe completar por cuenta propia. Para ingresar al archivo no va a aparecer en su gestor de archivos asi que para abrirlo debe modificarlo directamente desde la terminal

Al realizar 
```bash
nano .env
```
En su pantalla debería aparecer algo como:

```env
POSTGRES_DB=expense_tracker
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
```
Ahora usted mismo lo debe modificar donde dice `you_password` con la contraseña que desee 

Para guardar sus cambios debe hacer:
```bash
ctrl + O
enter
ctrl + X
```

## Levantar la aplicacion

Ahora para levantar la aplicación tiene dos caminos posibles:

1. Si quiere descargar las imagenes de Docker en su máquina local (lo que puede llevar tiempo) continue con las instrucciones de abajo
2. Si prefiere utilizar las imagenes subidas previamente a Docker Hub (lo que puede llevar considerablemente menos tiempo) puede continuar donde dice *Levantar el proyecto con las imagenes de registry* y seguir con esas instrucciones 

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

