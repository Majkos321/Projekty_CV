import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Wiadomosc implements Serializable {


    Integer operacja;//1 to wiadomosc
    Integer IDS;
    String mess;
    Integer klient;

    Wiadomosc(Integer IDS,Integer klient , Integer operacja,String mess) {
        this.operacja = operacja;
        this.IDS = IDS;
        this.mess = mess;
        this.klient = klient;
    }

    public void wysli(){

        try(Socket socket = new Socket("localhost", 5000);){
            OutputStream OS = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(OS);// do wysylania objektow slozy ObjectOutputStream
            oos.writeObject(this);
            oos.flush();// flush sluzy do wypchniecia bufora

                if(operacja.equals(2)){ //jesli chcemy odczytac wiadomosci to  2
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); //czekanie na dane od serwera
                    ArrayList<String> historia = (ArrayList<String>) in.readObject();
                    System.out.println("Historia chatu z uzytkownikiem" + klient);
                    for(String h :  historia){
                        System.out.println(h);
                    }
                }
        }catch(Exception e){
            System.out.println("Bład Wiadomosc");
        }
    }

}







