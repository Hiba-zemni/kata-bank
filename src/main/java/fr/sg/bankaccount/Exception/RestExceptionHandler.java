package fr.sg.bankaccount.Exception;

import fr.sg.bankaccount.model.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo balanceExceptionHandler(BalanceException ex) {
        ErrorInfo errorInfo = ErrorInfo.builder().errorMessage(ex.getMessage()).build();
        return errorInfo;
    }

    @ExceptionHandler(NotExistAccountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo notExistAccountExceptionHandler(NotExistAccountException ex) {
        ErrorInfo errorInfo = ErrorInfo.builder().errorMessage(ex.getMessage()).build();
        return errorInfo;
    }


}
