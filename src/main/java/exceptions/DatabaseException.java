package exceptions;
//não checada, não precisa por codigo dentro de um try catch
public class DatabaseException extends RuntimeException {

    public DatabaseException(String msg){
        super(msg);
    }

}
