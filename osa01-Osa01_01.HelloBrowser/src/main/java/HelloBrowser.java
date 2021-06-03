
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class HelloBrowser {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        
        System.out.println("================\n" +
			" THE INTERNETS! \n" +
                        "================");

        System.out.print("Where to? ");
        String address = input.nextLine();
        int port = 80;

	Socket socket = new Socket(address, port);

	PrintWriter writer = new PrintWriter(socket.getOutputStream());
	writer.println("GET / HTTP/1.1");
	writer.println("Host: " + address);
	writer.println();
	writer.flush();

	System.out.println("==========\n" +
			"RESPONSE\n" +
                        "==========");

	Scanner reader = new Scanner(socket.getInputStream());
	while (reader.hasNext()) {
            System.out.println(reader.nextLine());
	}
    }
}
