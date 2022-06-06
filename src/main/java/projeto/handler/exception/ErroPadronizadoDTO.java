package br.com.alibanco.projeto.handler.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@NoArgsConstructor
public class ErroPadronizadoDTO {
    private final List<String> mensagens = new ArrayList<>();

    public void adicionarErro(FieldError fieldError) {

        String mensagemDeErro = String.format("Campo %s %$s.", fieldError.getField(), fieldError.getDefaultMessage());

        mensagens.add(mensagemDeErro);
    }

}
