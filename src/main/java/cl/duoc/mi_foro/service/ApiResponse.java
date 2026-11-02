package cl.duoc.mi_foro.service;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class ApiResponse<T> {
    private HttpStatus estado;
    private T respuesta;
}
