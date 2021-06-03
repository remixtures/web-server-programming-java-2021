
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HelloRedirectLoop {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8080);
        
        int numberOfRequests = 1;
        
        while (true) {
            Socket socket = server.accept();
            System.out.println("Request number " + numberOfRequests);
            Scanner input = new Scanner(socket.getInputStream());
            String path = input.nextLine();
            numberOfRequests++;
            
            if (path.contains("quit")) {
                break;
            }
            
            PrintWriter writer = new PrintWriter(socket.getOutputStream()); 
            writer.println("HTTP/1.1 302 OK");
            writer.println("Location: http://localhost:8080/");
            
            writer.flush();
            input.close();
            writer.close();
            socket.close();
            server.close();
        }
    }
}
