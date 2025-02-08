package Repository;

import Domain.Entitate;

import java.io.IOException;
import java.util.ArrayList;

public class RepoGeneric<T extends Entitate> extends AbstractRepository<T>{

    public ArrayList<T> repo= new ArrayList<>();
    public RepoGeneric(){};
    public void adauga(T ent)  throws RepositoryException {
        repo.add(ent);
    }
    public int getSize(){
        return repo.size();
    }
    public T getById(int id){
        for(int i=0;i<repo.size();i++)
            if(repo.get(i).getId()==id)
                return repo.get(i);
        return null;
    }
    public T getByPoz(int poz){
        return repo.get(poz);
    }
    public void stergereId(int id){
        for(int i=0;i<repo.size();i++)
            if(repo.get(i).getId()==id)
                repo.remove(i);
    }
    public void stergerePoz(int poz){
        repo.remove(poz);}

    public void Modificare(int id,T ent)
    {
        for(int i=0;i<this.repo.size();i++)
            if(this.repo.get(i).getId()==id)
            {this.repo.set(i,ent);}

    }
    public ArrayList<T> getALL(){
        return new ArrayList<>(repo);
    }
}

