package br.com.alibanco.projeto.controller;

import br.com.alibanco.projeto.dto.TitularDTO;
import br.com.alibanco.projeto.form.AtualizaTitularForm;
import br.com.alibanco.projeto.model.Titular;
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
@RequestMapping("/titulares")
public class TitularController {

    @Autowired
    TitularRepository titularRepository;


    @GetMapping("/{id}")
    public ResponseEntity<TitularDTO> consultar(@PathVariable Long id) {
        Titular titular = titularRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Titular não cadastrado no sistema."));


        return ResponseEntity.ok(TitularDTO.converter(titular));
    }

    @Cacheable(value = "listaDeTitulares")
    @GetMapping()
    public List<TitularDTO> listar(@RequestParam(required = false) String cpf) {
        if (cpf == null) {
            return TitularDTO.converterLista(titularRepository.findAll());
        } else {
            return TitularDTO.converterLista(titularRepository.findByCpf(cpf));
        }
    }

    @Transactional
    @CacheEvict(value = "listaDeTitulares", allEntries = true)
    @PostMapping()
    public ResponseEntity<TitularDTO> cadastrar(@RequestBody @Valid Titular titular, UriComponentsBuilder uriComponentsBuilder) {
        titularRepository.save(titular);

        URI uri = uriComponentsBuilder.path("/titulares/{id}").buildAndExpand(titular.getTitularid()).toUri();
        return ResponseEntity.created(uri).body(new TitularDTO(titular));
    }

    @Transactional
    @CacheEvict(value = "listaDeTitulares", allEntries = true)
    @PatchMapping("/{id}")
    public ResponseEntity<TitularDTO> atualizar(@PathVariable("id") Long id, @RequestBody AtualizaTitularForm atualizaTitularForm) {
        Titular titular = titularRepository.findById(id).orElseThrow(TitularNotFoundException());
        atualizaTitularForm.atualizar(titular, titularRepository);

        return ResponseEntity.ok(TitularDTO.converter(titular));
    }

    @Transactional
    @CacheEvict(value = "listaDeTitulares", allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover (@PathVariable("id") Long id) {
        Titular titular = titularRepository.findById(id).orElseThrow(TitularNotFoundException());
        titularRepository.deleteById(id);

        return ResponseEntity.noContent().build(); // HTTP 204
    }


    public static Supplier<ResponseStatusException> TitularNotFoundException() {
        return () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Titular não encontrado");
    }
}
