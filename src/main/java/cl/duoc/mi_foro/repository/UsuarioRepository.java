package cl.duoc.mi_foro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.mi_foro.entity.UsuarioEntity;

@Repository 
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

}
