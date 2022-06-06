package br.com.alibanco.projeto.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idConta")
    private Long id;

    @OneToOne
    private Titular Titularid;

    private Integer agencia;

    private Integer numero;
    private BigDecimal saldo;

    public Conta(Integer agencia, Integer numero, BigDecimal saldo, Titular titularid) {
        this.Titularid = titularid;
        this.agencia = agencia;
        this.numero = numero;
        this.saldo = saldo;
    }

    public Titular getTitular() {
        return Titularid;
    }

    public void setTitular(Titular titular) {
        this.Titularid = titular;
    }


    public void adiciona(BigDecimal valor) {
        this.setSaldo(this.saldo.add(valor));
    }

    @Override
    public String toString() {
        return "Conta {"
                +
                "\nProprietário= " + this.getTitular().getNome()
                +
                "\nAgencia= " + agencia
                +
                "\nNumero= " + numero
                +
                "\nSaldo= " + saldo
                +
                "\n}";
    }
}
