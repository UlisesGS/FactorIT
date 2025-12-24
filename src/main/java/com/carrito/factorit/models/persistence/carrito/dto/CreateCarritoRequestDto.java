package com.carrito.factorit.models.persistence.carrito.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarritoRequestDto {

    @NotBlank(message = "El DNI del cliente es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String clienteDni;

    @NotNull(message = "Debe especificarse si el carrito es especial o no")
    private Boolean esEspecial;

}
