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
            /api/carrito/cerrar/1
        ```

        """
)
@ApiResponses(
        value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Cierre de carrito exitosamente",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(example = """
                                        {
                                            "id": 1,
                                            "fechaCierre": "2025-12-22T16:21:36.1168025",
                                            "totalPago": 11900.00,
                                            "carritoId": 1
                                        }
                """)
                        )
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
                                    "details": "uri=/api/carrito/cerrar/3",
                                    "path": "/api/carrito/cerrar/3"
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "No se puede cerrar un carrito que no esta abierto",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 409,
                                    "message": "El carrito no esta ABIERTO",
                                    "errorCode": "CONFLICT",
                                    "details": "uri=/api/carrito/cerrar/2",
                                    "path": "/api/carrito/cerrar/2"
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "El carrito esta vacio",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 409,
                                    "message": "No se puede cerrar un carrito vacio",
                                    "errorCode": "CONFLICT",
                                    "details": "uri=/api/carrito/cerrar/3",
                                    "path": "/api/carrito/cerrar/3"
                                }"""
                        ))
                ),
        }
)
public @interface CloseCarritoDoc {
}
