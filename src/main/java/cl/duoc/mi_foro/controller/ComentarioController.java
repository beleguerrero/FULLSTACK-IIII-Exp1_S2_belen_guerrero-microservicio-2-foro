package cl.duoc.mi_foro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.mi_foro.dto.ComentarioRequestDTO;
import cl.duoc.mi_foro.dto.ComentarioRequestOptionalDTO;
import cl.duoc.mi_foro.dto.ComentarioResponseDTO;
import cl.duoc.mi_foro.service.ApiResponse;
import cl.duoc.mi_foro.service.ComentarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/mi-foro")
public class ComentarioController {

    private final ComentarioService service;

    public ComentarioController(ComentarioService service) {
        this.service = service;
    }


    // get
    @GetMapping("/foros/{foroId}/comentarios")
    public ResponseEntity<?> getComentariosForo(@PathVariable Long foroId) {
        ApiResponse<?> r = service.getComentariosByForo(foroId);
        return ResponseEntity.status(r.getEstado()).body(r.getRespuesta());
    }


    // post
    @PostMapping("/foros/{foroId}/comentarios")
    public ResponseEntity<?> doComentario(
        @PathVariable Long foroId,
        @Valid @RequestBody ComentarioRequestDTO request,
        HttpServletRequest httpRequest
    ) {
        ApiResponse<?> r = service.doComentario(foroId, request, httpRequest);
        return ResponseEntity.status(r.getEstado()).body(r.getRespuesta());
    }


    // put
    @PutMapping("/comentarios/{id}")
    public ResponseEntity<?> setComentario(
        @PathVariable Long id,
        @Valid @RequestBody ComentarioRequestOptionalDTO request,
        HttpServletRequest httpRequest
    ) {
        ApiResponse<?> r = service.setCometario(id, request, httpRequest);
        return ResponseEntity.status(r.getEstado()).body(r.getRespuesta());
    }


    // delete
    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<?> deleteComentario(@PathVariable Long id, HttpServletRequest httpRequest) {
        ApiResponse<?> r = service.deleteComentario(id, httpRequest);
        return ResponseEntity.status(r.getEstado()).body(r.getRespuesta());
    }

}
