package Service;

import Domain.Produs;
import Repository.DuplicateObjectException;
import Repository.Irepository;
import Repository.RepoGeneric;
import Repository.RepositoryException;

import java.util.ArrayList;

public class ServiceProdus {
    public Irepository<Produs> ServiceProdus;
    public ServiceProdus(Irepository<Produs> repoGeneric){
        this.ServiceProdus=repoGeneric;
    }
    public void AddProdus(Produs produs) throws IllegalAccessException, RepositoryException {
        if(produs==null)throw new IllegalAccessException();
        if(ServiceProdus.getById(produs.getId())!= null)
            throw new DuplicateObjectException("Exista deja o masina cu id ul dat");
        ServiceProdus.adauga(produs);
    }
    public void stergereId(int id) throws RepositoryException {
        if(ServiceProdus.getById(id) == null)
            throw new RuntimeException("Nu exista o masina cu id ul dat");
        else ServiceProdus.stergereId(id);
    }
    public ArrayList<Produs> getAll(){
        return this.ServiceProdus.getALL();
    }

    public Produs getById(int id) throws RepositoryException {
        Produs produs=null;
        for(int i=0;i< ServiceProdus.getSize();++i)
            if(ServiceProdus.getByPoz(i).getId()==id)
                produs=ServiceProdus.getByPoz(i);
        if(produs!=null)return produs;
        else throw new RepositoryException("Id ul dat nu exista");
    }
    public void ModificaProdusById(int id,int pret,String nume,String categorie) throws RepositoryException {
        boolean ok=false;
        Produs produs=new Produs(id,categorie,nume,pret);
        for(int i=0;i< ServiceProdus.getSize();++i)
            if(ServiceProdus.getByPoz(i).getId()==id){
                this.ServiceProdus.Modificare(id,produs);
                ok=true;
            }
        if(!ok)throw new RepositoryException("Id ul dat nu exista");
    }
}
