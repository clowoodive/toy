package clowoodive.toy.investing.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class InvestingExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvestingException.class)
    protected ResponseEntity<InvestingException.ResultBody> handleInvestingException(InvestingException iex) {

        System.out.println("ResultCode : " + iex.getCode() + ", ResultMessage : " + iex.getMessage());

        InvestingException.ResultBody resultBody = new InvestingException.ResultBody(iex.getCode().getCode(), iex.getMessage());

        return new ResponseEntity<>(resultBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<InvestingException.ResultBody> handleRuntimeException(Exception ex) {

        System.out.println("class : " + ex.getClass().getName() + ", message : " + ex.getMessage());

        InvestingException.ResultBody resultBody = new InvestingException.ResultBody(ResultCode.InternalServerError.getCode(), null);

        return new ResponseEntity<>(resultBody, HttpStatus.FORBIDDEN);
    }
}
