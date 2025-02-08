package Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractRepository<T> implements Irepository<T> {
    protected List<T> repo=new ArrayList<>();
    @Override
    public Iterator<T> iterator(){
        return new ArrayList<T>(repo).iterator();
    }
}