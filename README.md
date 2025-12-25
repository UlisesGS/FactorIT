# FACTOR IT
## Prueba tecnica
Para planificar el desarrollo de la aplicacion, el primer objetivo fue identificar las entidades mas relevantes para
la logica de negocio de la app. Como resultado se encontraron 3 puntos claves: la entidad Compra, la entidad Carrito y la entidad ProductoCarrito.
Se realizaron las pruebas unitarias sobre los servicios mas relevantes utilizando JUnit y Mockito.

Documentacion realizada con swagger: http://localhost:8080/swagger-ui/index.html#/.

Pruebas realizadas con Postman: https://crimson-escape-498405.postman.co/workspace/2b3f41ce-ed24-4d0b-b9e3-f7740602f19f/collection/26413054-823ee37f-39a7-4aa4-a4de-38f8d2080a09?action=share&source=copy-link&creator=26413054.

# Resolución de criterios y decisiones en la Gestión del Carrito
## Crear y Eliminar
Se definio como regla de negocio que un cliente no puede tener mas de un carrito activo al mismo tiempo.
Por esta razon, si un cliente intenta crear un carrito, el sistema verifica que no tenga un carrito activo.

## Agregar y Sacar productos
Para la gestion de productos dentro del carrito, se verifica el stock de los productos solicitados para agregar, considerando que los productos son cargados
mediante un script.
En este punto se opto por la creacion de la entidad ProductoCarrito con el objetivo de separar claramente la informacion correspondiente al producto de los
agregados/comprados, permitiendo manejar de forma mas precisa la informacion.

## Finalización de un carrito (aplicar descuentos)
La finalizacion del carrito y la aplicacion de los descuentos fue el gran desafio de la prueba tecnica. Para mantener una arquitectura clara y desacoplada,
se decidio separar la logica de descuentos de la logica del carrito y creacion de la compra. Por esta razon, se implemento el servicio especifico del Descuento, donde
se encapsulo toda la logica requerida para la aplicacion de los descuentos.
Sobre el descuento VIP, la interpretacion es que este puede utilizarse una unica vez por mes.