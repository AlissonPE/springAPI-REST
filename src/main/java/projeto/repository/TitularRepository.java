package br.com.alibanco.projeto.repository;

import br.com.alibanco.projeto.model.Titular;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitularRepository extends JpaRepository<Titular, Long> {
    List<Titular> findByCpf(String cpf);
}
