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
        summary = "Eliminar carrito dado un cliente",
        description =  """
        Este endpoint elimina un carrito dado el dni de un cliente.
        Se tiene en cuenta que no se elimina en la db, pero si se elimina para el cliente(estado = CANCELADO).
        
        📌 **Ejemplo de uso:**
        `/api/carrito/eliminar/12345678`
        """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "Carrito eliminado exitosamente.",
                content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = PageCompraResponseDto.class)),
                        examples = @ExampleObject(value = """
                        {}
                """)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Error en el dni",
                content = @Content(mediaType = "application/json", schema = @Schema(
                        example = """
                        {
                            "statusCode": 400,
                            "message": "deleteCarrito.dni: El DNI debe tener 8 dígitos; ",
                            "errorCode": "VALIDATION_ERROR",
                            "details": "uri=/api/carrito/eliminar/2222222",
                            "path": "/api/carrito/eliminar/2222222"
                        }"""
                ))
        ),
        @ApiResponse(
                responseCode = "409",
                description = "El cliente no tiene un carrito ABIERTO",
                content = @Content(mediaType = "application/json", schema = @Schema(
                        example = """
                        {
                            "statusCode": 409,
                            "message": "El cliente no tiene un carrito abierto",
                            "errorCode": "CONFLICT",
                            "details": "uri=/api/carrito/eliminar/22222222",
                            "path": "/api/carrito/eliminar/22222222"
                        }"""
                ))
        ),

})
public @interface DeleteCarritoDoc {
}
