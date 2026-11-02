package cl.duoc.mi_foro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.duoc.mi_foro.entity.ComentarioEntity;

@Repository 
public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Long> {

    // List<ComentarioEntity> findByForo_IdActivo(Long foroId, Integer activo);

    @Query("""
        SELECT c
        FROM ComentarioEntity c
        WHERE c.foro.id = :foroId
        AND c.activo = :activo
    """)
    List<ComentarioEntity> getByForoIdAndActivo(@Param("foroId") Long foroId, @Param("activo") Integer activo);
}
