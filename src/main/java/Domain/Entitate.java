package Domain;


import java.io.*;

public class Entitate implements Serializable {
    private static final long serialVersionUID=1L;
    private String filename;
    private int id;
    public Entitate(String filename){
        this.filename=filename;
        this.id=LoadId();
        generareId();
    }
    public Entitate(){}

    public Entitate(int id){
        this.id=id;
    }
    private int LoadId(){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            return Integer.parseInt(bufferedReader.readLine());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
    private void WriteId(){
        try(BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(filename))) {
            bufferedWriter.write(Integer.toString(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void generareId(){
        this.id++;
        WriteId();

    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){this.id=id;}

}


