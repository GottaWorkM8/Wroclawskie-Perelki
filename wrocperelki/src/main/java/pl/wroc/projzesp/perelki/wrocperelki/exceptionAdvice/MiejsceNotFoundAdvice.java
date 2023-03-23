package pl.wroc.projzesp.perelki.wrocperelki.exceptionAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.wroc.projzesp.perelki.wrocperelki.exceptionAdvice.ZagadkaNotFoundException;


@ControllerAdvice
public class MiejsceNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ZagadkaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(ZagadkaNotFoundException ex) {
        return ex.getMessage();
    }
}
