package pl.wroc.projzesp.perelki.wrocperelki.exceptionAdvice;

public class MiejsceNotFoundException extends RuntimeException{
    public MiejsceNotFoundException(Long id) {
        super("Nie znaleziono zagadki o numerze: " + id);
    }
}
