package br.com.alibanco.projeto.handler.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class AlibancoControllerAdvice {


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<String> mensagens = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(b -> mensagens.add(b.getDefaultMessage()));
        return ResponseEntity.badRequest().body(mensagens);

    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResponseStatusException.class)
    public List<String> ResponseStatusException(ResponseStatusException exception) {
        String bindingResult = exception.getReason();

        List<String> mensagens = new ArrayList<>();
        mensagens.add(bindingResult);

        return mensagens;

    }

}
