package cl.duoc.mi_foro.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;

@Entity 
@Table(name = "foro")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class ForoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "f_id")
    private Long id;

    @Column(nullable = false, length = 150, name = "f_titulo")
    private String titulo;

    @Lob
    @Column(nullable = false, name = "f_contenido")
    private String contenido;

    @Column(nullable = false, name = "f_fecha")
    private LocalDateTime fecha;

    @Column(nullable = false, name = "f_fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // @Column(nullable = false, name = "u_id")
    // private Long usuarioId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "u_id")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(nullable = false, name = "ef_id")
    private EstadoForoEntity estado;

    @ManyToOne
    @JoinColumn(nullable = false, name = "c_id")
    private CategoriaEntity categoria;

    @Column(nullable = false, name = "f_activo")
    private Integer activo;

}
