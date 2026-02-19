import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Serwer {

    HashMap<String, PrintWriter> Client = new HashMap<>();
    public ArrayList<Rang> konwersacje = new ArrayList<>();
    static String line;
    static Integer ID;
    static Integer IDK;


    public static void main(String[] args) throws Exception {//przy serwer socket moze wystapic IOEXCEPTION

        Serwer serwer = new Serwer();
        //serwer.ControList(serwer.konwersacje);

        ServerSocket server = new ServerSocket(5000);
        System.out.println("Server czeka na połaczenie....");


        while (true) {
            Socket socket = server.accept();
            System.out.println("Klient sie połączył");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Wiadomosc wiad = (Wiadomosc) ois.readObject();

            //serwer.ControList(serwer.konwersacje);
            line = wiad.mess;
            ID = wiad.IDS;
            IDK = wiad.klient;

            if (ID.equals(IDK)) {
                throw new ChatInitializationException("BŁĄD: Użytkownik " + ID + " próbował pisać do samego siebie!");
            }

            /**
             * czemu nie uporzadkuje globalnie ktory jest mniejszy
             * a ktory wiekszy ID popniewaz moze to potem mi sie pogubic
             * narazie latwo mi sledziec co od kogo przyszlo ale nie zawsze
             * musi tak byc !!!
             */

            if (wiad.operacja == 1) {
                serwer.savelogs(ID, line, IDK);//wysylanie info o chacie
                if (line.equals("END")) {
                    System.out.println("Proces Serwera został zakończony");
                    break;
                }
                if (line != null) {
                    System.out.println(line);
                    System.out.println("Obsłuzono kliencta czekam na nastepnego ");
                }
            } else if (wiad.operacja == 2) {
                if (ID > IDK) {
                    int temp = ID;
                    ID = IDK;
                    IDK = temp;
                }
                ObjectOutputStream histry = new ObjectOutputStream(socket.getOutputStream());
                ArrayList<String> histry_chat = new ArrayList<>();
                if (serwer.ifChat(ID, IDK)) {
                    histry_chat = serwer.FindChat(ID, IDK);
                } else {
                    histry_chat.add("Przykro mi konwersacja nie istnieje");
                }
                histry.writeObject(histry_chat);
                histry.close();
            } else {
                throw new NotFoundOperationException("Uzytkownik wybral zla operacje");
            }


            socket.close();


        }
    }

    void savelogs(Integer ID, String massage, Integer KlientID) throws Exception {

        String file = this.everwrite(ID, IDK);
        if (file == null) {
            throw new IOException();
        }
        try (FileWriter FW = new FileWriter(file, true);//Potem tworzywmy indywidual chaty do kazdego uzytkwonika
             BufferedWriter out = new BufferedWriter(FW)) {

            out.write("ID" + ID + ": " + massage);
            out.newLine();
        } catch (IOException e) {
            System.out.println("Błąd " + e.getMessage());
        }
    }

    String everwrite(Integer ID1, Integer ID2) throws ChatInitializationException {//logika tworzenia nowych kownersacji

        int id_min = ID1;
        int id_max = ID2;

        if (id_min > id_max) {
            int temp = id_max;
            id_max = id_min;
            id_min = temp;
        }

        if (konwersacje.isEmpty()) {
            Rang chat1 = new Rang(id_min, id_max);
            chat1.setName(id_min + "Chat" + id_max);
            konwersacje.add(chat1);
            System.out.println("To pierwsza wiadomosc do tego uzytkownika");
            try (FileWriter Fw = new FileWriter(id_min + "Chat" + id_max)) {
                return id_min + "Chat" + id_max;
            } catch (IOException e) {
                System.out.println("Nie udalo sie utworzyc pliku jestesmy w Serwer");
            }
        } else {

            for (Rang g : konwersacje) {
                if (g.name.equals(id_min + "Chat" + id_max)) {
                    return id_min + "Chat" + id_max;
                }
            }
            System.out.println("To pierwsza wiadomosc do tego uzytkownika");
            try (FileWriter Fw = new FileWriter(id_min + "Chat" + id_max, true)) {
                Rang r = new Rang(id_min, id_max);
                r.setName(id_min + "Chat" + id_max);
                konwersacje.add(r);
                return id_min + "Chat" + id_max;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            /**
             * mamy tak 1.Brak czego kolwiek napewno musimy utworzyc pod warunkiem ze to nie jest ta sama osoba
             * 2.teraz tak jesli cos mamy i nie sa to te dwa same ID to mozemy miec nazwe niestety moga bc one tworzone roznie ale no
             * jak znjadziemy to zwracamy nazwe ktora jest  bo napewno tak sie nazywa
             */


        }
        return null;
    }

    public Boolean ifChat(Integer ID_1, Integer ID_2) {

        Integer id_min = ID_1;
        Integer id_max = ID_2;
        if (id_min > id_max) {
            int temp = id_max;
            id_max = id_min;
            id_min = temp;
        }
        for (Rang g : konwersacje) {
            if (g.name.equals(id_min + "Chat" + id_max)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> FindChat(Integer ID_1, Integer ID_2) {

        String name_Chat = ID_1 + "Chat" + ID_2;
        ArrayList<String> chat = new ArrayList<>();
        try (BufferedReader bf = new BufferedReader(new FileReader(name_Chat))) {
            String line;
            while ((line = bf.readLine()) != null) {

                chat.add(line);

            }
            return chat;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void ClientWork(Socket socket) {

    }


    private void ControList(ArrayList<Rang> list) {

        if (list.isEmpty()) {
            System.out.println("Lista jest pusta");
        }
        for (Rang r : list) {
            System.out.println(r.name);
        }
    }
}

// Wyjątki
class ChatInitializationException extends Exception {
    public ChatInitializationException(String message) {
        super(message);
    }
}

class NotFoundOperationException extends Exception {
    public NotFoundOperationException(String message) {
        super(message);
    }
}

