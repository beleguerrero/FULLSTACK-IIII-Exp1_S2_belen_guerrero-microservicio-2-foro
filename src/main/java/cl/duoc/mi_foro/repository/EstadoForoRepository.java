package cl.duoc.mi_foro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.mi_foro.entity.EstadoForoEntity;

@Repository 
public interface  EstadoForoRepository extends JpaRepository<EstadoForoEntity, Long> {
    Optional<EstadoForoEntity> findByIdAndActivo(Long id, Integer activo);

}
