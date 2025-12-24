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
        summary = "Vaciar de productos el carrito",
        description =  """
        Este endpoint elimina todos los productos de un carrito.
        
        📌 **Ejemplo de uso:**
        `/api/carrito/vaciar/1`
        """
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "Carrito vaciado exitosamente.",
                content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = PageCompraResponseDto.class)),
                        examples = @ExampleObject(value = """
                        {}
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
                            "details": "uri=/api/carrito/vaciar/2",
                            "path": "/api/carrito/vaciar/2"
                        }"""
                ))
        ),
        @ApiResponse(
                responseCode = "409",
                description = "El carrito ya esta vacio",
                content = @Content(mediaType = "application/json", schema = @Schema(
                        example = """
                        {
                            "statusCode": 409,
                            "message": "No se puede vaciar, el carrito ya esta vacio",
                            "errorCode": "CONFLICT",
                            "details": "uri=/api/carrito/vaciar/1",
                            "path": "/api/carrito/vaciar/1"
                        }"""
                ))
        ),
})
public @interface EmptyCarritoDoc {
}
