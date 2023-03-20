package pl.wroc.projzesp.perelki.wrocperelki.exceptionAdvice;

public class ZagadkaNotFoundException extends RuntimeException{
    public ZagadkaNotFoundException(Long id) {
        super("Nie znaleziono zagadki o numerze: " + id);
    }
}
