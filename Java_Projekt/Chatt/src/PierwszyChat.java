import java.io.*;
import java.net.Socket;

public class PierwszyChat {

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 5000)){

            System.out.println("Połacznono z serwrerm");
            OutputStream output = socket.getOutputStream();
            PrintWriter wiadomosc = new PrintWriter(output,true);

            wiadomosc.println("Witaj");
            System.out.println("Wiadomość wysłana. Zamykam połączenie.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
