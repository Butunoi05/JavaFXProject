package Repository;

public class RepositoryException extends  Exception{
    public RepositoryException(String mesaj){
        super(mesaj);
    }
    public RepositoryException(String mesaj,Throwable cauza){
        super(mesaj,cauza);
    }
}