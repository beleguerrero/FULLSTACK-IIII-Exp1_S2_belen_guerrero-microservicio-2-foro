package cl.duoc.mi_foro.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class ForoRequestOptionalDTO {

    @Size (max = 150, message = "El título no puede superar 150 caracteres")
    private String titulo;

    private String contenido;
    
    private Long estadoId;
    
    private Long categoriaId;

    private Boolean activo;

}

