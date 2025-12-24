package com.carrito.factorit.documentation.carrito;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Agregar uno o mas productos al carrito",
        description =  """
        Este endpoint agrega uno o mas productos dado el id del carrito.
        Se tiene en cuenta que el producto ya esta creado.
        
        📌 **Ejemplo de url esperada:**
        ```url
            /api/carrito/agregar-producto/1
        ```
        
        📌 **Ejemplo de datos JSON esperados:**
        ```json
                {
                    "productoId":1,
                    "cantidad":1
                }
        ```

        📌 **Reglas de validación:**
        - **productoId:** obligatorio.
        - **cantidad:** obligatorio, mayor que 0.
        """
)
@ApiResponses(
        value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Producto/s agregado al carrito exitosamente",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "id": 1,
                                    "totalPago": 150.00
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Cantidad es menor o igual a 0",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 400,
                                    "message": "La cantidad debe ser mayor o igual a 1",
                                    "errorCode": "VALIDATION_ERROR",
                                    "details": "uri=/api/carrito/agregar-producto/1",
                                    "path": "/api/carrito/agregar-producto/1"
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Carrito no existe",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 404,
                                    "message": "Carrito no encontrado",
                                    "errorCode": "RESOURCE_NOT_FOUND",
                                    "details": "uri=/api/carrito/agregar-producto/2",
                                    "path": "/api/carrito/agregar-producto/2"
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Producto no existe",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 404,
                                    "message": "Producto no encontrado",
                                    "errorCode": "RESOURCE_NOT_FOUND",
                                    "details": "uri=/api/carrito/agregar-producto/1",
                                    "path": "/api/carrito/agregar-producto/1"
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Conflicto: title duplicado",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 409,
                                    "message": "Stock insuficiente para el producto",
                                    "errorCode": "CONFLICT",
                                    "details": "uri=/api/carrito/agregar-producto/1",
                                    "path": "/api/carrito/agregar-producto/1"
                                }"""
                        ))
                ),

        }
)
public @interface AddProductDoc {
}
