

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

public class HelloServer {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8080);

        while (true) {
            Socket socket = server.accept();
            
            Scanner input = new Scanner(socket.getInputStream());
            
            if (input.nextLine().contains("/quit")) {
                break;
            }
            
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println("HTTP/1.1 200 OK");
            writer.println();
            
            Files.lines(Paths.get("index.html")).forEach(writer::println);
            writer.flush(); 
            
            writer.close();
            input.close();
            socket.close();
            server.close();
        }
    }
}
