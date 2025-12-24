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
        summary = "Eliminar uno o mas productos del carrito",
        description =  """
        Este endpoint elimina uno o mas productos dado el id del carrito.
        
        📌 **Ejemplo de url esperada:**
        ```url
            /api/carrito/vaciar/1
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
                        responseCode = "200",
                        description = "Producto eliminado del carrito exitosamente",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(example = """
                                        {
                                            "id": 1,
                                            "totalPago": 150.00
                                        }
                """)
                        )
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
                                    "details": "uri=/api/carrito/sacar-producto/2",
                                    "path": "/api/carrito/sacar-producto/2"
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
                                    "details": "uri=/api/carrito/sacar-producto/2",
                                    "path": "/api/carrito/sacar-producto/2"
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "El producto no esta en el carrito",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 404,
                                    "message": "El producto no esta en el carrito",
                                    "errorCode": "RESOURCE_NOT_FOUND",
                                    "details": "uri=/api/carrito/sacar-producto/1",
                                    "path": "/api/carrito/sacar-producto/1"
                                }"""
                        ))
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Cantidad equivocada",
                        content = @Content(mediaType = "application/json", schema = @Schema(
                                example = """
                                {
                                    "statusCode": 409,
                                    "message": "La cantidad a eliminar supera la cantidad actual",
                                    "errorCode": "CONFLICT",
                                    "details": "uri=/api/carrito/sacar-producto/1",
                                    "path": "/api/carrito/sacar-producto/1"
                                }"""
                        ))
                ),
        }
)
public @interface DeleteProductoDoc {
}
