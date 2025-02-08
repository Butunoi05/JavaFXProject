package Repository;

import java.util.ArrayList;

public interface Irepository<T> extends Iterable<T> {
    public void adauga(T o) throws RepositoryException;
    public T getById(int id);
    public void stergereId(int id) throws RepositoryException;
    public void stergerePoz(int poz) throws RepositoryException;
    public void Modificare(int id,T ModelNou);
    public T getByPoz(int poz);
    public ArrayList<T> getALL();
    int getSize();
}
