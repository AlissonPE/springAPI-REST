package br.com.alibanco.projeto.repository;

import br.com.alibanco.projeto.model.Conta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByAgencia(Integer agencia);
}
