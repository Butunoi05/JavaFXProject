package Service;

import Domain.Comanda;
import Domain.Produs;
import Repository.Irepository;
import Repository.RepoGeneric;
import Repository.RepositoryException;

import java.util.ArrayList;
import java.util.Date;

public class ServiceComanda {
    private Irepository<Comanda> Repo;
    public ServiceComanda(Irepository<Comanda> repoGeneric){
        this.Repo=repoGeneric;
    }
    public void addComanda(Comanda comanda) throws RepositoryException{
        if(this.Repo.getById(comanda.getId()) ==null)Repo.adauga(comanda);
        else throw new RepositoryException("Comanda exista deja");
    }
    public Comanda getComandaById(int id) {
        return Repo.getById(id);
    }
    public ArrayList<Comanda> getAllComenzi() {
        return Repo.getALL();
    }
    public void updateComanda(int id, Comanda newComanda) {
        Repo.Modificare(id, newComanda);
    }

    public void deleteComandaById(int id) throws RepositoryException {
        Repo.stergereId(id);
    }
    public void addProdusToComanda(int comandaId, Produs produs) throws RepositoryException {
        if(this.Repo.getById(comandaId)!=null){
        Comanda comanda = Repo.getById(comandaId);
        if (comanda != null) {
            comanda.adaugareProdus(produs);
        }
        else throw new RepositoryException("Produsul dat nu exista");
        }
        else throw new RepositoryException("Comanda data nu exista");
    }
    public void removeProdusFromComanda(int comandaId, Produs produs) throws RepositoryException {
        try {
            if (this.Repo.getById(comandaId) != null) {
                Comanda comanda = Repo.getById(comandaId);
                if (comanda != null) {
                    comanda.stergereProdus(produs);
                } else throw new RepositoryException("Produsul dat nu exista");
            } else throw new RepositoryException("Comanda data nu exista");
        }catch (Exception a){a.printStackTrace();}
    }
    public void changeDataLivrare(int comandaId, Date newDate) {
        Comanda comanda = Repo.getById(comandaId);
        if (comanda != null) {
            comanda.setDataLivrare(newDate);
            Repo.Modificare(comandaId, comanda);
        }
    }
}
