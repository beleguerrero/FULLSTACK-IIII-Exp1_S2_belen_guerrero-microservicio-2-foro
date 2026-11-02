package cl.duoc.mi_foro.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ComentarioRequestDTO {

    @NotBlank(message = "Falta contenido")
    private String contenido;

    private Long comentarioPadreId;

    // @NotNull(message = "Falta usuario")
    // private Long UsuarioId;
}
