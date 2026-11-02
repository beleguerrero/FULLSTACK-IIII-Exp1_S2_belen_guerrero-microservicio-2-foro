package cl.duoc.mi_foro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.mi_foro.entity.ForoEntity;

@Repository 
public interface ForoRepository extends JpaRepository<ForoEntity, Long> {
    
    List<ForoEntity> findByActivo(Integer activo);
    
    Optional<ForoEntity> findByIdAndActivo(Long id, Integer activo);
}
