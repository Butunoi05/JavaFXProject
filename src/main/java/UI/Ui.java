package UI;

import Domain.Comanda;
import Domain.Produs;
import Repository.DuplicateObjectException;
import Repository.RepositoryException;
import Service.ServiceComanda;
import Service.ServiceProdus;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Ui {
    public ServiceProdus serviceProdus;
    public ServiceComanda serviceComanda;
    public Ui(ServiceProdus serviceProdus,ServiceComanda serviceComanda){
        this.serviceProdus=serviceProdus;
        this.serviceComanda=serviceComanda;
    }
    public void adaugareProdus()throws RepositoryException{
        Scanner scanner=new Scanner(System.in);
        int pret;
        String categorie,nume;
        int id;
        System.out.println("Dati id ul \n");
        id=scanner.nextInt();
        System.out.println("Dati pretul \n");
        pret=scanner.nextInt();
        System.out.println("Dati numele produsului \n");
        nume=scanner.next();
        System.out.println("Dati categoria produsului \n");
        categorie=scanner.next();
        Produs produs=new Produs(id,categorie,nume,pret);
        try {
            this.serviceProdus.AddProdus(produs);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        }
    }
    public void stergereProdus(){
        Scanner scanner=new Scanner(System.in);
        int id;
        System.out.println("Dati idul produsului care trebuie sters");
        id=scanner.nextInt();

        try {
            this.serviceProdus.stergereId(id);
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }
    void adaugaProduse()throws RepositoryException{
        Produs produs=new Produs(1,"frigider","",800);
        Produs produs1=new Produs(2,"telefon","",2000);
        Produs produs2=new Produs(3,"telefon","",1000);
        Produs produs3=new Produs(4,"tv","",700);
        Produs produs4=new Produs(5,"tv","",900);
        try {
            this.serviceProdus.AddProdus(produs);
            this.serviceProdus.AddProdus(produs1);
            this.serviceProdus.AddProdus(produs2);
            this.serviceProdus.AddProdus(produs3);
            this.serviceProdus.AddProdus(produs4);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (DuplicateObjectException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Produs> arrayList=new ArrayList<>();
        arrayList.add(produs);

        ArrayList<Produs> arrayList1=new ArrayList<>();
        arrayList1.add(produs);
        arrayList1.add(produs1);
        arrayList1.add(produs4);

        ArrayList<Produs> arrayList2=new ArrayList<>();
        arrayList2.add(produs);
        arrayList2.add(produs1);
        arrayList2.add(produs2);
        arrayList2.add(produs3);
        arrayList2.add(produs4);

        ArrayList<Produs> arrayList3=new ArrayList<>();
        arrayList3.add(produs1);
        arrayList3.add(produs1);
        arrayList3.add(produs2);
        arrayList3.add(produs2);
        Date date=new Date();
        Comanda comanda=new Comanda(1,arrayList,date);
        Comanda comanda1=new Comanda(2,arrayList1,date);
        Comanda comanda2=new Comanda(3,arrayList2,date);
        Comanda comanda3=new Comanda(4,arrayList3,date);
        this.serviceComanda.addComanda(comanda);
        this.serviceComanda.addComanda(comanda1);
        this.serviceComanda.addComanda(comanda2);
        this.serviceComanda.addComanda(comanda3);

    }

    public void adaugareComanda() throws RepositoryException {
        Scanner scanner=new Scanner(System.in);
        ArrayList<Produs> produses=new ArrayList<>();
        while (true) {
            System.out.println("1.Adaugare produs in comanda\n2.Finalizare comanda");
            int x=scanner.nextInt();

            if(x==1)
            {
                System.out.println("Dati idul produsului care doriti sa il cumparati");
                int id=scanner.nextInt();
                Produs produs=this.serviceProdus.getById(id);
                produses.add(produs);
            }

            if(x==2)break;
        }
        if(produses.size()>0) {
            System.out.println("Dati id ul \n");
            int id=scanner.nextInt();
            Date date=new Date();
            Comanda comanda=new Comanda(id,produses,date);
            this.serviceComanda.addComanda(comanda);
        }
    }
    public void stergereComanda() throws RepositoryException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Dati idul comenzii care trebuie sters");
        int id=scanner.nextInt();
        this.serviceComanda.deleteComandaById(id);

    }
    public void ModificareProdus(){
        Scanner scanner=new Scanner(System.in);
        int id;
        System.out.println("Dati idul produsului care trebuie modificat");
        id=scanner.nextInt();

        int pret;
        String categorie,nume;
        System.out.println("Dati noul pret \n");
        pret=scanner.nextInt();
        System.out.println("Dati noul nume al produsului \n");
        nume=scanner.next();
        System.out.println("Dati noua categorie a produsului \n");
        categorie=scanner.next();
        try {
            this.serviceProdus.ModificaProdusById(id,pret,nume,categorie);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    public void afisare(){
        ArrayList<Produs> arrayList=this.serviceProdus.getAll();
        for(int i=0;i<arrayList.size();++i)System.out.println(arrayList.get(i).toString());
    }
    public void afisare1(){
        ArrayList<Comanda> arrayList=this.serviceComanda.getAllComenzi();
        for(int i=0;i<arrayList.size();++i)System.out.println(arrayList.get(i).toString());
    }

    public void Afisare(){
        System.out.println("1.AdaugareProdus ");
        System.out.println("2.StergereProdus ");
        System.out.println("3.ModificaProdus ");
        System.out.println("4.AfiseazaProduse ");
        System.out.println("5.Adaugare comanda ");
        System.out.println("6.Afiseaza comenzi ");
        System.out.println("7.Stergere comanda ");
        System.out.println("8.Adaugare produs in comanda ");
        System.out.println("9.Stergere produs din comanda ");
        System.out.println("10.iesire program ");
    }
    public void adaugareProdusLista() throws RepositoryException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Dati idul comenzii care trebuie modificata");
        int y=scanner.nextInt();
        System.out.println("Dati idul produsului care trebuie adaugat in comanda");
        int x=scanner.nextInt();
        Produs produs=this.serviceProdus.getById(x);
        this.serviceComanda.addProdusToComanda(y,produs);

    }
    public void stergereProdusLista() throws RepositoryException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Dati idul comenzii care trebuie modificata");
        int y=scanner.nextInt();
        System.out.println("Dati idul produsului care trebuie sters din comanda");
        int x=scanner.nextInt();
        Produs produs=this.serviceProdus.getById(x);
        this.serviceComanda.removeProdusFromComanda(y,produs);

    }
    public void start() throws RepositoryException{
        Scanner scanner=new Scanner(System.in);
        //this.adaugaProduse();
        while(true){
            Afisare();
            System.out.println("Dati varianta");
            int x=scanner.nextInt();
            if(x==1)try{
                this.adaugareProdus();
            }catch (Exception a){a.printStackTrace();}
            if(x==2)try {
                this.stergereProdus();
            }catch (Exception a){a.printStackTrace();}
            if(x==3)try {
                this.ModificareProdus();}
            catch (Exception a){a.printStackTrace();}
            if(x==4)try {
                this.afisare();
            }catch (Exception a){a.printStackTrace();}
            if(x==5)try {
                this.adaugareComanda();
            }catch (Exception a){a.printStackTrace();}
            if(x==6)try {
                this.afisare1();
            }catch (Exception a){a.printStackTrace();}
            if(x==7)try {
                this.stergereComanda();
            }catch (Exception a){a.printStackTrace();}
            if(x==8)try {
                this.adaugareProdusLista();
            }catch (Exception a){a.printStackTrace();}
            if(x==9)try {
                this.stergereProdusLista();
            }catch (RepositoryException a){a.printStackTrace();}
            if(x==10)break;
        }
    }
}
