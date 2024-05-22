package br.com.topsorteio.exceptions.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class RestErrorMensagem {
    private HttpStatus error;
    private String mensagem;
    private boolean status;
}
