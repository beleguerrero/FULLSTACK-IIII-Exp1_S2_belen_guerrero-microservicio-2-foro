package cl.duoc.mi_foro.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import cl.duoc.mi_foro.Constants;
import cl.duoc.mi_foro.dto.ComentarioRequestDTO;
import cl.duoc.mi_foro.dto.ComentarioRequestOptionalDTO;
import cl.duoc.mi_foro.dto.ComentarioResponseDTO;
import cl.duoc.mi_foro.entity.ComentarioEntity;
import cl.duoc.mi_foro.entity.ForoEntity;
import cl.duoc.mi_foro.entity.UsuarioEntity;
import cl.duoc.mi_foro.jwt.JwtUtil;
import cl.duoc.mi_foro.repository.ComentarioRepository;
import cl.duoc.mi_foro.repository.ForoRepository;
import cl.duoc.mi_foro.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service 
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ForoRepository foroRepository;
    private final UsuarioRepository usuarioRepository;

    private final JwtUtil jwtUtil;

    public ComentarioService(
        ComentarioRepository comentarioRepository,
        ForoRepository foroRepository,
        UsuarioRepository usuarioRepository,
        JwtUtil jwtUtil
    ) {
        this.comentarioRepository = comentarioRepository;
        this.foroRepository = foroRepository;
        this.usuarioRepository = usuarioRepository;

        this.jwtUtil = jwtUtil;
    }


    // get
    public ApiResponse<?> getComentariosByForo(Long foroId) {
        Optional<ForoEntity> foroOpt = foroRepository.findById(foroId);
        if(foroOpt.isEmpty()) return new ApiResponse<>(HttpStatus.NOT_FOUND, List.of("Foro no encontrado"));
        
        List<ComentarioResponseDTO> comentarios = comentarioRepository.getByForoIdAndActivo(foroId, 1).stream().map(this::toDTO).toList();
        return new ApiResponse<>(HttpStatus.OK, comentarios);
    }


    // do
    public ApiResponse<?> doComentario(Long foroId, ComentarioRequestDTO comentarioRequest, HttpServletRequest httpRequest) {
        Optional<ForoEntity> foroOpt = foroRepository.findById(foroId);
        if(foroOpt.isEmpty()) return new ApiResponse<>(HttpStatus.NOT_FOUND, List.of("Foro no encontrado"));


        String token = httpRequest.getHeader(Constants.HEADER_AUTHORIZATION_KEY).substring(Constants.TOKEN_BEARER_PREFIX.length());
        Long usuarioAutenticadoId = jwtUtil.extractUserId(token);
        
        // lista errores
        List<String> errores = new ArrayList<>();
        
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(usuarioAutenticadoId);
        // if(usuarioOpt.isEmpty()) errores.add("usuario no encontrado");
        // UsuarioEntity usuario = usuarioOpt.get();
        UsuarioEntity usuario = usuarioOpt.orElse(null);
        if (usuario == null) errores.add("Usuario no encontrado");


        ComentarioEntity comentarioPadre = null;
        if(comentarioRequest.getComentarioPadreId() != null) {
            Optional<ComentarioEntity> comentarioPadreOpt = comentarioRepository.findById(comentarioRequest.getComentarioPadreId());
            comentarioPadre = comentarioPadreOpt.orElse(null);
            if(comentarioPadre == null) errores.add("Comentario padre no encontrado");
            if(comentarioPadre.getActivo() != 1) errores.add("No se puede agregar respuesta");
        }

        ForoEntity foro = foroOpt.get();
        if(foro.getActivo() != 1) errores.add("No se puede agregar comentarios a este foro");

        String contenido = comentarioRequest.getContenido();
        if(contenido != null && contenido.isBlank()) errores.add("Contenido no puede estar vacío");


        if(!errores.isEmpty()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, errores);
        }

        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setContenido(contenido);
        comentario.setFecha(LocalDateTime.now());
        comentario.setFechaActualizacion(LocalDateTime.now());
        comentario.setComentarioPadre(comentarioPadre);
        comentario.setUsuario(usuario);
        comentario.setForo(foro);
        comentario.setActivo(1);

        ComentarioEntity createdComentario = comentarioRepository.save(comentario);
        return new ApiResponse<>(HttpStatus.CREATED, List.of(toDTO(createdComentario)));
    }


    // set
    public ApiResponse<?> setCometario(Long id, ComentarioRequestOptionalDTO comentarioRequest, HttpServletRequest httpRequest) {
        Optional<ComentarioEntity> comentarioOpt = comentarioRepository.findById(id);
        if(comentarioOpt.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, List.of("Comentario no encontrado"));
        }

        ComentarioEntity comentario = comentarioOpt.get();

        Optional<ForoEntity> foroOptional = foroRepository.findById(comentario.getForo().getId());
        if(foroOptional.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, List.of("Foro no encontrado"));
        }


        // solo puede actualizar un admin o el mismo usuario logueado
        String token = httpRequest.getHeader(Constants.HEADER_AUTHORIZATION_KEY).substring(Constants.TOKEN_BEARER_PREFIX.length());

        boolean esAdmin = "ROLE_ADMIN".equals(jwtUtil.extractRole(token));
        Long usuarioAutenticadoId = jwtUtil.extractUserId(token);

        boolean esMismoUsuario = usuarioAutenticadoId.equals(comentario.getUsuario().getId());

        if(!esAdmin && !esMismoUsuario) {
            return new ApiResponse<>(HttpStatus.FORBIDDEN, List.of("No tienes permiso para modificar este comentario"));
        }
        
        
        String contenido = comentarioRequest.getContenido();
        Boolean activo = comentarioRequest.getActivo();

        if (contenido == null && activo == null) {
            return new ApiResponse<>(HttpStatus.CONFLICT, List.of("No hay datos para actualizar"));
        }        

        if(contenido != null && contenido.isBlank()) {
            return new ApiResponse<>(HttpStatus.CONFLICT, List.of("Contenido no puede estar vacío"));
        }
        
        
        if(contenido != null) comentario.setContenido(contenido);
        if(activo != null) comentario.setActivo(activo ? 1 : 0);
        comentario.setFechaActualizacion(LocalDateTime.now());

        ComentarioEntity savedComentario = comentarioRepository.save(comentario);
        return new ApiResponse<>(HttpStatus.OK, List.of(toDTO(savedComentario)));
    }


    // delete
    public ApiResponse<?> deleteComentario(Long id, HttpServletRequest httpRequest) {
        Optional<ComentarioEntity> comentarioOpt = comentarioRepository.findById(id);
        if(comentarioOpt.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, List.of("Comentario no encontrado"));
        }

        ComentarioEntity comentario = comentarioOpt.get();
        
        // solo puede actualizar un admin o el mismo usuario logueado
        String token = httpRequest.getHeader(Constants.HEADER_AUTHORIZATION_KEY).substring(Constants.TOKEN_BEARER_PREFIX.length());

        boolean esAdmin = "ROLE_ADMIN".equals(jwtUtil.extractRole(token));
        Long usuarioAutenticadoId = jwtUtil.extractUserId(token);
        boolean esMismoUsuario = usuarioAutenticadoId.equals(comentario.getUsuario().getId());

        if(!esAdmin && !esMismoUsuario) {
            return new ApiResponse<>(HttpStatus.FORBIDDEN, List.of("No tienes permiso para modificar este comentario"));
        }
        
        if(comentario.getActivo() != 1) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, List.of("Comentario ya eliminado"));
        }
        
        comentario.setActivo(0);
        comentarioRepository.save(comentario);
        return new ApiResponse<>(HttpStatus.OK, List.of("Comentario eliminado"));
    }



    private ComentarioResponseDTO toDTO(ComentarioEntity comentario) {
        return new ComentarioResponseDTO(
            comentario.getId(),
            comentario.getContenido(),
            comentario.getFecha(),
            comentario.getFechaActualizacion(),
            comentario.getComentarioPadre() != null ? comentario.getComentarioPadre().getId() : null,
            comentario.getUsuario().getUsuario()
            // comentario.getUsuarioId().toString()
        );
    }

}
