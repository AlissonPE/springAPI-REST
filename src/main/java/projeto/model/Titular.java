package br.com.alibanco.projeto.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
public class Titular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Titularid;

    @NotNull
    private String nome;
    @NotNull
    private String cpf;
    @NotNull
    private String nomeDaMae;

    public Titular() {
    }

    public Titular(String cpf, String nome, String nomeDaMae) {
        this.nome = nome;
        this.cpf = cpf;
        this.nomeDaMae = nomeDaMae;
    }
}
