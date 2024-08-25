package exceptions;
//não checada, não precisa por codigo dentro de um try catch
public class DatabaseIntegrityException extends RuntimeException {

    public DatabaseIntegrityException(String msg){
        super(msg);
    }

}
