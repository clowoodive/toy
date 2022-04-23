package com.kakaopay.coding.investingapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.kakaopay.coding.investingapi.error.ResultCode.InternalServerError;

@RestControllerAdvice
public class InvestingExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvestingException.class)
    protected ResponseEntity<InvestingException.ResultBody> handleInvestingException(InvestingException iex) {

        System.out.println("ResultCode : " + iex.resultCode + ", ResultMessage : " + iex.resultMessage);

        InvestingException.ResultBody resultBody = new InvestingException.ResultBody(iex.resultCode, iex.resultMessage);

        return new ResponseEntity<>(resultBody, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<InvestingException.ResultBody> handleRuntimeException(Exception ex) {

        System.out.println("class : " + ex.getClass().getName() + ", message : " + ex.getMessage());

        InvestingException.ResultBody resultBody = new InvestingException.ResultBody(InternalServerError, null);

        return new ResponseEntity<>(resultBody, HttpStatus.FORBIDDEN);
    }
}
