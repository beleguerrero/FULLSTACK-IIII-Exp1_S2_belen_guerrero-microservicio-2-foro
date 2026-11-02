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

import cl.duoc.mi_foro.dto.ForoRequestDTO;
import cl.duoc.mi_foro.dto.ForoRequestOptionalDTO;
import cl.duoc.mi_foro.dto.ForoResponseDTO;
import cl.duoc.mi_foro.service.ApiResponse;
import cl.duoc.mi_foro.service.ForoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/mi-foro/foros")
public class ForoController {

    private final ForoService service;

    public ForoController(ForoService service) {
        this.service = service;
    }


    // get
    @GetMapping
    public ResponseEntity<List<ForoResponseDTO>> getForos() {
        ApiResponse<List<ForoResponseDTO>> r = service.getForos();
        return ResponseEntity.status(r.getEstado()).body(r.getRespuesta());
    }


    // get id
    @GetMapping("/{id}")
    public ResponseEntity<?> getForo(@PathVariable Long id) {
        ApiResponse<?> r = service.getForoById(id);
        return ResponseEntity.status(r.getEstado()).body(r.getRespuesta());
    }


    // post
    @PostMapping
    public ResponseEntity<?> doForo(@Valid @RequestBody ForoRequestDTO request, HttpServletRequest httpRequest) {
        ApiResponse<?> r = service.doForo(request, httpRequest);
        return ResponseEntity.status(r.getEstado()).body(r.getRespuesta());

    }


    // put
    @PutMapping("/{id}")
    public ResponseEntity<?> setForo(
        @PathVariable Long id,
        @Valid @RequestBody ForoRequestOptionalDTO request,
        HttpServletRequest httpRequest
    ) {
        ApiResponse<?> r = service.setForo(id, request, httpRequest);
        return ResponseEntity.status(r.getEstado()).body(r.getRespuesta());
    }


    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteForo(@PathVariable Long id) {
        ApiResponse<?> r = service.deleteForo(id, false);
        return ResponseEntity.status(r.getEstado()).body(r.getRespuesta());
    }

}
