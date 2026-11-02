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
public class ForoResponseDTO {

    private Long id;
    private String titulo;
    private String contenido;
    private LocalDateTime fecha;
    private LocalDateTime fecha_actualizacion;
    // private Long u_id;
    private String usuario;
    // private Long ef_id;
    private String estado;
    // private Long c_id;
    private String categoria;
    // private Integer activo;
}
