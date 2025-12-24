package com.carrito.factorit.documentation.carrito;

import com.carrito.factorit.models.persistence.compra.dto.PageCompraResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
        summary = "Obtener de un carrito su estado y todos los productos que contiene",
        description =  """
        Este endpoint devuelve el estado y los productos de un carrito.

        📌 **Ejemplo de uso:**
        `/api/carrito/consultar-estado/1`
        """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Lista de compras obtenida correctamente.",
                content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = PageCompraResponseDto.class)),
                        examples = @ExampleObject(value = """
                        {
                            "productoCarritoResponseDtoList": [
                                {
                                    "productoResponseDtoList": [
                                        {
                                            "nombre": "Producto A",
                                            "codigoProducto": "PROD-A"
                                        }
                                    ],
                                    "cantidad": 2
                                },
                                {
                                    "productoResponseDtoList": [
                                        {
                                            "nombre": "Producto B",
                                            "codigoProducto": "PROD-B"
                                        }
                                    ],
                                    "cantidad": 2
                                }
                            ],
                            "estado": "ABIERTO",
                            "totalProductos": 4
                        }
                """)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Carrito no encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(
                        example = """
                        {
                            "statusCode": 404,
                            "message": "Carrito no encontrado",
                            "errorCode": "RESOURCE_NOT_FOUND",
                            "details": "uri=/api/carrito/consultar-estado/1",
                            "path": "/api/carrito/consultar-estado/1"
                        }
                """
                ))
        ),
})
public @interface CheckStatusCarritoDoc {
}
