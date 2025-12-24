package com.carrito.factorit.documentation.compra;

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
        summary = "Obtener todas las compras de un cliente",
        description =  """
        Este endpoint devuelve una lista paginada y con filtros de las compras de un cliente.

        Permite filtrar por fechas y con orden(fecha-monto).

        - **dni**: busca todas las compras pertenecientes a ese dni
        - **desde**: filtra las compras desde x fecha (mientras sea antes de la actual)
        - **hasta**: filtra las compras hasta la fecha actual por deffault(se le puede  ingresar despues de a la fecha desde y antes de la actual)
        - **orden**: filtra las compras por oden de fecha o monto
        - **Pageable**: permite manejar la paginación (número de página, tamaño de página y ordenamiento).
          Este último tiene las siguientes opciones:
            - **page=0** (número de página)
            - **size=10** (cantidad de elementos por página)

        El unico parametro si o si necesario es el dni, los demas son opcionales.

        📌 **Ejemplo de uso con todos los filtros:**
        `/api/compras/listar-compras/11111111?desde=2025-12-10&hasta=2025-12-18&orden=FECHA&page=0&size=5`
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
                         "content": [
                             {
                                 "id": 2,
                                 "fechaCierre": "2025-12-12T16:57:13.237812",
                                 "totalPago": 370.00,
                                 "carritoId": 2
                             },
                             {
                                 "id": 3,
                                 "fechaCierre": "2025-12-16T16:57:13.239049",
                                 "totalPago": 450.00,
                                 "carritoId": 3
                             }
                         ],
                         "pageNumber": 0,
                         "pageSize": 5,
                         "totalElements": 2,
                         "totalPages": 1
                     }
                """)
                )
        )
})
public @interface ListAllCompraDoc {
}
