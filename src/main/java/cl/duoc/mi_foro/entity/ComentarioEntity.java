package cl.duoc.mi_foro.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "comentario")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class ComentarioEntity {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Lob
    @Column(nullable = false, name = "c_contenido")
    private String contenido;

    @Column(nullable = false, name = "c_fecha")
    private LocalDateTime fecha;

    @Column(nullable = false, name = "c_fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "c_padre")
    private ComentarioEntity comentarioPadre;

    // @Column(nullable = false, name = "u_id")
    // private Long usuarioId;
    @ManyToOne
    @JoinColumn(nullable = false, name = "u_id")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(nullable = false, name = "f_id")
    private ForoEntity foro;

    @Column(nullable = false, name = "c_activo")
    private Integer activo;

}
