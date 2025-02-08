package Repository;

public class DuplicateObjectException extends RepositoryException{
    public DuplicateObjectException(String mesaj){
        super(mesaj);
    }
}
