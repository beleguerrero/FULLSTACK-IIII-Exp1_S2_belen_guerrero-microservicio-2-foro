package cl.duoc.mi_foro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "usuario")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class UsuarioEntity {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "u_id")
    private Long id;

    @Column (nullable = false, length = 150, name = "u_usuario")
    private String usuario;

    @Column (nullable = false, length = 255, name = "u_password")
    private String password;

    @Column (nullable = false, length = 150, name = "u_nombre")
    private String nombre;

    @Column (nullable = true, length = 150, name = "u_correo")
    private String correo;

    @ManyToOne 
    @JoinColumn (nullable = false, name = "tu_id")
    private TipoUsuarioEntity tipoUsuario;

    @Column (nullable = false, length = 1, name = "u_activo")
    private Integer activo;
}