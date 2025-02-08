package Domain;

public class Produs extends Entitate {
    private int id;
    private String categorie;
    private String nume;
    private int pret;


    public Produs(int id,String categorie,String nume,int pret){
        this.id=id;
        this.pret=pret;
        this.nume=nume;
        this.categorie=categorie;

    }

    public Produs(){}

    public Produs(String nume, int pret) {
        this.nume=nume;
        this.pret=pret;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }

    public int getPret() {
        return pret;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Produsul cu id:"+ this.getId()+" categorie:"+categorie+" nume:"+nume+" pret:"+pret;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
