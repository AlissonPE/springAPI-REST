package br.com.alibanco.projeto.controller;

import br.com.alibanco.projeto.dto.ContaDTO;
import br.com.alibanco.projeto.form.AtualizaContaForm;
import br.com.alibanco.projeto.form.ContaForm;
import br.com.alibanco.projeto.model.Conta;
import br.com.alibanco.projeto.repository.ContaRepository;
import br.com.alibanco.projeto.repository.TitularRepository;
import java.net.URI;
import java.util.List;
import java.util.function.Supplier;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    ContaRepository contaRepository;
    @Autowired
    TitularRepository titularRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> consultar(@PathVariable Long id) {
        Conta conta =
                contaRepository.findById(id).orElseThrow(ContaNotFoundException());

        return ResponseEntity.ok(ContaDTO.converter(conta));
    }

    @Cacheable(value = "listaDeContas")
    @GetMapping()
    public ResponseEntity<List<ContaDTO>> listar(@RequestParam(required = false) Integer agencia) {
        if (agencia == null) {
            return ResponseEntity.ok(ContaDTO.converterLista(contaRepository.findAll()));
        } else {
            return ResponseEntity.ok(ContaDTO.converterLista(contaRepository.findByAgencia(agencia)));
        }
    }

    @CacheEvict(value = "listaDeContas", allEntries = true )
    @Transactional // delimita transação do banco
    @PostMapping()
    public ResponseEntity<ContaDTO> cadastrar(@RequestBody @Valid ContaForm contaForm, UriComponentsBuilder uriComponentsBuilder) {
        Conta conta = contaForm.converter(titularRepository);
        contaRepository.save(conta);

        var titularId = conta.getTitular().getTitularid();

        URI uri = uriComponentsBuilder.path("/contas/{id}").buildAndExpand(titularId).toUri();
        return ResponseEntity.created(uri).body(new ContaDTO(conta));
    }

    @Transactional // delimita transação do banco
    @CacheEvict(value = "listaDeContas", allEntries = true )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> remover(@PathVariable("id") Long contaId) {

        Conta conta = contaRepository.findById(contaId).orElseThrow(ContaNotFoundException());

        contaRepository.deleteById(contaId);

        return ResponseEntity.noContent().build(); // HTTP 204
    }

    @Transactional
    @CacheEvict(value = "listaDeContas", allEntries = true )
    @PatchMapping("/{id}")
    public ResponseEntity<ContaDTO> atualizar(@PathVariable("id") Long id, @RequestBody AtualizaContaForm atualizaContaForm) {
        Conta conta = contaRepository.findById(id).orElseThrow(ContaNotFoundException());
        atualizaContaForm.atualizaForm(conta, titularRepository);

        return ResponseEntity.ok(ContaDTO.converter(conta));
    }


    public static Supplier<ResponseStatusException> ContaNotFoundException() {
        return () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada");
    }


}
