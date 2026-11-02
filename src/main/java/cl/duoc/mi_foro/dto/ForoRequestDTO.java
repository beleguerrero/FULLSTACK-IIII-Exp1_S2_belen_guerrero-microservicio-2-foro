package cl.duoc.mi_foro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ForoRequestDTO {

    @NotBlank(message = "Falta título")
    @Size(max = 150, message = "El título no puede superar 150 caracteres")
    private String titulo;

    @NotBlank(message = "Falta contenido")
    private String contenido;

    // @NotNull(message = "Falta usuario")
    // private Long usuarioId;

    @NotNull(message = "Falta estado")
    private Long estadoId;

    @NotNull(message = "Falta categoría")
    private Long categoriaId;
}

