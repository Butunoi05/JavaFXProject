package Domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Comanda extends Entitate {
    private ArrayList<Domain.Produs> produsList;
    private Date DataLivrare;
    public Comanda(String filename) {
        super(filename);
    }

    public Comanda(int id, ArrayList<Domain.Produs> produses, Date date) {
        super(id);
        this.produsList=produses;
        this.DataLivrare=date;
    }
    public void adaugareProdus(Domain.Produs produs){
        this.produsList.add(produs);
    }
    public void stergereProdus(Domain.Produs produs){
        this.produsList.remove(produs);
    }

    public Date getDataLivrare() {
        return DataLivrare;
    }

    public void setDataLivrare(Date dataLivrare) {
        DataLivrare = dataLivrare;
    }

    public List<Domain.Produs> getProdusList() {
        return produsList;
    }

    public void setProdusList(ArrayList<Domain.Produs> produsList) {
        this.produsList = produsList;
    }

    @Override
    public String toString() {
        return "Lista cu id:"+this.getId()+" numar produse:"+produsList.size()+" data livrare:"+DataLivrare;
    }
}
