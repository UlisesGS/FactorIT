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
        summary = "Crear carrito dado un cliente",
        description =  """
        Este endpoint crea un carrito dado el dni de un cliente.
        Se tiene en cuenta que el cliente no puede tener mas de un carrito ABIERTO al mismo tiempo.
        
        📌 **Ejemplo de datos JSON esperados:**
        ```json
        {
          "clienteDni": "12345678",
          "esEspecial": false
        }
        ```

        📌 **Reglas de validación:**
        - **clienteDni:** obligatorio, 8 caracteres.
        - **esEspecial:** obligatorio.
        """
)
@ApiResponses(
        value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Carrito creado exitosamente",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "fechaCreacion": "2025-12-20T17:09:02.6075268",
                                    "esEspecial": false,
                                    "estado": "ABIERTO",
                                    "totalPago": 0
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Error en el dni",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 400,
                                    "message": "El DNI debe tener 8 dígitos",
                                    "errorCode": "VALIDATION_ERROR",
                                    "details": "uri=/api/carrito/crear",
                                    "path": "/api/carrito/crear"
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Cliente no encontrado",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 404,
                                    "message": "Cliente no encontrado",
                                    "errorCode": "RESOURCE_NOT_FOUND",
                                    "details": "uri=/api/carrito/crear",
                                    "path": "/api/carrito/crear"
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Conflicto: cliente ya tiene carrito",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 409,
                                    "message": "El cliente ya tiene un carrito abierto",
                                    "errorCode": "CONFLICT",
                                    "details": "uri=/api/carrito/crear",
                                    "path": "/api/carrito/crear"
                                }"""
                        ))
                ),

        }
)
public @interface CreateCarritoDoc {
}
