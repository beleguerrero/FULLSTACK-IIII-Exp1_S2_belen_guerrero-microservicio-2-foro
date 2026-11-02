package cl.duoc.mi_foro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.mi_foro.entity.CategoriaEntity;

@Repository 
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
        Optional<CategoriaEntity> findByIdAndActivo(Long id, Integer activo);
}
