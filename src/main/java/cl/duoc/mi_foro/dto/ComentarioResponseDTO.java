package cl.duoc.mi_foro.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ComentarioResponseDTO {

    private Long id;
    private String contenido;
    private LocalDateTime fecha;
    private LocalDateTime fechaActualizacion;
    private Long comentarioPadreId;
    // private Long usuarioId;
    private String usuario;
    // private Long f_id;
    // private Integer activo;
}
