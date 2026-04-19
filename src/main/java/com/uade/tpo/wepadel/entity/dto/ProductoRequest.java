package com.uade.tpo.wepadel.entity.dto;
import java.math.BigDecimal;
import com.uade.tpo.wepadel.entity.CategoriaEnum;
import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class ProductoRequest {
    @NotBlank
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal precio;

    @NotNull
    private CategoriaEnum categoria;

    @NotNull
    private Boolean estaHabilitado;
}
