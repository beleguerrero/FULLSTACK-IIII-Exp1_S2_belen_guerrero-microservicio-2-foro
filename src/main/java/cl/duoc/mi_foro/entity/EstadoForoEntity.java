package cl.duoc.mi_foro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.GenerationType;

@Entity 
@Table(name = "estado_foro")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class EstadoForoEntity {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "ef_id")
    private Long id;

    @Column (nullable = false, length = 150, name = "ef_nombre")
    private String nombre;

    @Column (nullable = false, length = 1, name = "ef_activo")
    private Integer activo;
    
}

