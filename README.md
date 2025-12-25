# FACTOR IT
## Prueba técnica
Para planificar el desarrollo de la aplicación, el primer objetivo fue identificar las entidades más relevantes para la lógica de negocio de la app.
Como resultado se encontraron 3 puntos claves: la entidad Compra, la entidad Carrito y la entidad ProductoCarrito.
Se realizaron las pruebas unitarias sobre los servicios más relevantes utilizando JUnit y Mockito.

Variables de entorno usadas para la db:
DB_HOST, 
DB_PORT,
DB_NAME,
DB_USERNAME,
DB_PASSWORD,
DB_URL

Base de datos: PostgreSql

Documentación realizada con Swagger: http://localhost:8080/swagger-ui/index.html#/

Pruebas realizadas con Postman: https://crimson-escape-498405.postman.co/workspace/2b3f41ce-ed24-4d0b-b9e3-f7740602f19f/collection/26413054-823ee37f-39a7-4aa4-a4de-38f8d2080a09?action=share&source=copy-link&creator=26413054

# Resolución de criterios y decisiones en la Gestión del Carrito
## Crear y Eliminar
Se definió como regla de negocio que un cliente no puede tener más de un carrito activo al mismo tiempo.
Por esta razón, si un cliente intenta crear un carrito, el sistema verifica que no tenga un carrito activo.

## Agregar y Sacar productos
Para la gestión de productos dentro del carrito, se verifica el stock de los productos solicitados para agregar, considerando que los productos son cargados mediante un script.
En este punto se optó por la creación de la entidad ProductoCarrito con el objetivo de separar claramente la información correspondiente
al producto de los agregados/comprados, permitiendo manejar de forma más precisa la información.

## Finalización de un carrito (aplicar descuentos)
La finalización del carrito y la aplicación de los descuentos fue el gran desafío de la prueba técnica.
Para mantener una arquitectura clara y desacoplada, se decidió separar la lógica de descuentos de la lógica del carrito y creación de la compra.
Por esta razón, se implementó el servicio específico del Descuento, donde se encapsuló toda la lógica requerida para la aplicación de los descuentos.
Sobre el descuento VIP, la interpretación es que este puede utilizarse una única vez por mes.