import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Uzytkownik implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer ID;
    String Name;
    private Integer quantity;
    private transient Integer pass; //transient powduje brak serializacji

    Uzytkownik(Integer ID, String Name,Integer pass) {
        this.ID = ID;
        this.Name = Name;
        this.pass = pass;
    }

    Integer getID(){
        return ID;
    }
    String getName(){
        return Name;
    }
    Integer getQuantity(){
        return quantity;
    }


    void zapisz(){
        //ObjectOutputStream Save = ;
    }

    public static void main(String[] args) {
        Uzytkownik jasiek =  new  Uzytkownik(6,"Jasiek",1);
        Uzytkownik zdzis =  new  Uzytkownik(10,"Zdzis",1);

        Wiadomosc wiadomosc = new Wiadomosc(23,90,1,"Siemka");
        wiadomosc.wysli();
        Wiadomosc wiadomosc1 = new Wiadomosc(90,23,2 ,"Halo halo tu londyn");
        wiadomosc1.wysli();
    }

}
