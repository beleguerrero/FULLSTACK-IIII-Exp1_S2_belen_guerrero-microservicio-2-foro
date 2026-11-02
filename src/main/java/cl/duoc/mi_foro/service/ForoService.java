package cl.duoc.mi_foro.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import cl.duoc.mi_foro.Constants;
import cl.duoc.mi_foro.dto.ForoRequestDTO;
import cl.duoc.mi_foro.dto.ForoRequestOptionalDTO;
import cl.duoc.mi_foro.dto.ForoResponseDTO;
import cl.duoc.mi_foro.entity.CategoriaEntity;
import cl.duoc.mi_foro.entity.EstadoForoEntity;
import cl.duoc.mi_foro.entity.ForoEntity;
import cl.duoc.mi_foro.entity.UsuarioEntity;
import cl.duoc.mi_foro.jwt.JwtUtil;
import cl.duoc.mi_foro.repository.CategoriaRepository;
import cl.duoc.mi_foro.repository.EstadoForoRepository;
import cl.duoc.mi_foro.repository.ForoRepository;
import cl.duoc.mi_foro.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;


@Service 
public class ForoService {

    private final ForoRepository foroRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstadoForoRepository estadoForoRepository;
    private final UsuarioRepository usuarioRepository;

    private final JwtUtil jwtUtil;

    public ForoService(
        ForoRepository foroRepository,
        CategoriaRepository categoriaRepository,
        EstadoForoRepository estadoForoRepository,
        UsuarioRepository usuarioRepository,
        JwtUtil jwtUtil
    ) {
        this.foroRepository = foroRepository;
        this.categoriaRepository = categoriaRepository;
        this.estadoForoRepository = estadoForoRepository;
        this.usuarioRepository = usuarioRepository;

        this.jwtUtil = jwtUtil;
    }


    // get
    public ApiResponse<List<ForoResponseDTO>> getForos() {
        List<ForoResponseDTO> foros = foroRepository.findByActivo(1).stream().map(this::toDTO).toList();
        return new ApiResponse<>(HttpStatus.OK, foros);
    }


    // get id
    public ApiResponse<?> getForoById(Long id) {
        Optional<ForoEntity> foro = foroRepository.findByIdAndActivo(id, 1);
        if(foro.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, List.of("Foro no encontrado"));
        }
        return new ApiResponse<>(HttpStatus.OK, foro.map(this::toDTO));
    }


    // do
    public ApiResponse<?> doForo(ForoRequestDTO foroRequest, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader(Constants.HEADER_AUTHORIZATION_KEY).substring(Constants.TOKEN_BEARER_PREFIX.length());
        Long usuarioAutenticadoId = jwtUtil.extractUserId(token);

        String titulo       = foroRequest.getTitulo();
        String contenido    = foroRequest.getContenido();
        Long estadoId       = foroRequest.getEstadoId();
        Long categoriaId    = foroRequest.getCategoriaId();
        
        List<String> errores = new ArrayList<>();

        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(usuarioAutenticadoId);
        if(usuarioOpt.isEmpty()) errores.add("usuario no encontrado");

        if(titulo != null && titulo.isBlank()) errores.add("Título no puede estar vacío");
        if(contenido != null && contenido.isBlank()) errores.add("Contenido no puede estar vacío");
        
        Optional<CategoriaEntity> categoriaOpt = categoriaRepository.findById(categoriaId);
        if(categoriaOpt.isEmpty()) errores.add("Categoría no encontrada");
        
        Optional<EstadoForoEntity> estadoOpt = estadoForoRepository.findById(estadoId);
        if(estadoOpt.isEmpty()) errores.add("Estado no encontrado");

        if(!errores.isEmpty()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, errores);
        }
        
        
        UsuarioEntity usuario = usuarioOpt.get();
        CategoriaEntity categoria = categoriaOpt.get();
        EstadoForoEntity estado = estadoOpt.get();
        
        ForoEntity foro = new ForoEntity();
        foro.setTitulo(titulo);
        foro.setContenido(contenido);
        foro.setFecha(LocalDateTime.now());
        foro.setFechaActualizacion(LocalDateTime.now());
        // foro.setUsuarioId(foroRequest.getUsuarioId());
        foro.setUsuario(usuario);
        foro.setEstado(estado);
        foro.setCategoria(categoria);
        foro.setActivo(1);

        ForoEntity createdForo = foroRepository.save(foro);
        return new ApiResponse<>(HttpStatus.CREATED, List.of(toDTO(createdForo)));
    }


    // set
    public ApiResponse<?> setForo(Long id, ForoRequestOptionalDTO foroRequest, HttpServletRequest httpRequest) {
        Optional<ForoEntity> foroOptional = foroRepository.findById(id);
        if(foroOptional.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, List.of("Foro no encontrado"));
        }
        ForoEntity foro = foroOptional.get();


        // solo puede actualizar un admin o el mismo usuario logueado
        String token = httpRequest.getHeader(Constants.HEADER_AUTHORIZATION_KEY).substring(Constants.TOKEN_BEARER_PREFIX.length());

        boolean esAdmin = "ROLE_ADMIN".equals(jwtUtil.extractRole(token));
        Long usuarioAutenticadoId = jwtUtil.extractUserId(token);
        boolean esMismoUsuario = usuarioAutenticadoId.equals(usuarioAutenticadoId);

        if(!esAdmin && !esMismoUsuario) {
            return new ApiResponse<>(HttpStatus.FORBIDDEN, List.of("No tienes permiso para modificar este foro"));
        }
        
        String titulo       = foroRequest.getTitulo();
        String contenido    = foroRequest.getContenido();
        Long estadoId       = foroRequest.getEstadoId();
        Long categoriaId    = foroRequest.getCategoriaId();
        Boolean activo      = foroRequest.getActivo();

        if(
            titulo      == null && 
            contenido   == null && 
            estadoId    == null && 
            categoriaId == null && 
            activo      == null
        ) {
            return new ApiResponse<>(HttpStatus.CONFLICT, List.of("No hay datos para actualizar"));
        }


        // validar datos
        List<String> errores = new ArrayList<>();

        if(titulo != null && titulo.isBlank()) errores.add("Título no puede estar vacío");
        if(contenido != null && contenido.isBlank()) errores.add("Contenido no puede estar vacío");
        
        EstadoForoEntity estado = null;
        if(estadoId != null) {
            Optional<EstadoForoEntity> estadoOpt = estadoForoRepository.findById(estadoId);
            estado = estadoOpt.orElse(null);
            if(estado == null) errores.add("Estado no encontrado");
        }
        
        CategoriaEntity categoria = null;
        if(categoriaId != null) {
            Optional<CategoriaEntity> categoriaOpt = categoriaRepository.findById(categoriaId);
            categoria = categoriaOpt.orElse(null);
            if(categoria == null) errores.add("Categoría no encontrada");
        }


        if(!errores.isEmpty()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, errores);
        }
        
        
        if(titulo != null) foro.setTitulo(titulo);
        if(contenido != null) foro.setContenido(contenido);
        if(estadoId != null) foro.setEstado(estado);
        if(categoriaId != null) foro.setCategoria(categoria);
        if(activo != null) foro.setActivo(activo ? 1 : 0);
        foro.setFechaActualizacion(LocalDateTime.now());

        ForoEntity savedForo = foroRepository.save(foro);
        return new ApiResponse<>(HttpStatus.OK, List.of(toDTO(savedForo)));
    }


    // delete
    public ApiResponse<?> deleteForo(Long id, boolean deactivate) {
        Optional<ForoEntity> foroOptional = foroRepository.findById(id);
        if(foroOptional.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, List.of("Foro no encontrado"));
        }

        foroRepository.deleteById(id);
        return new ApiResponse<>(HttpStatus.OK, List.of("Foro eliminado"));
    }



    private ForoResponseDTO toDTO(ForoEntity foro) {
        return new  ForoResponseDTO(
            foro.getId(),
            foro.getTitulo(),
            foro.getContenido(),
            foro.getFecha(),
            foro.getFechaActualizacion(),
            // foro.getUsuarioId().toString(),
            foro.getUsuario().getUsuario(),
            foro.getEstado().getNombre(),
            foro.getCategoria().getNombre()
        );
    }

}
