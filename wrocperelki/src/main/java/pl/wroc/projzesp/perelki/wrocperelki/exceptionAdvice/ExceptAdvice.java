package pl.wroc.projzesp.perelki.wrocperelki.exceptionAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ExceptAdvice {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String employeeNotFoundHandler(Exception ex) {
        pl.szajsjem.SimpleLog.log("Wyjątek przetwarzania:"+ex.toString());
        System.out.println(ex.getMessage());
        return "internal error";
    }
}
