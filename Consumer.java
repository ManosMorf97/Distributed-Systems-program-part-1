import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Consumer {

    public static void main(String[] args) throws Exception {
        Consumer server = new Consumer();
        ServerSocket providerSocket = new ServerSocket(4321, 3);
        System.out.println("Waiting for clients to connect...");
        while (true){
        server.run(providerSocket);
        }
    }


    private void run(ServerSocket providerSocket) throws InterruptedException {
        try {
            while (true) {
                Socket connection = providerSocket.accept();
                Thread t = new Thread(new server(connection));
                t.start();
                t.join();
            }
        } catch (IOException e) {
            throw new RuntimeException("Not able to open the port", e);
        }
    }


    private class server  implements Runnable {
        private final Socket clientSocket;

        private server(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                Scanner input = new Scanner(System.in);
                String line = inFromServer.readLine();

                while(true){
                    PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);

                    while (!line.equals("Done")) {
                        System.out.println(line);
                        line = inFromServer.readLine();
                    }
                    System.out.println("Type the buslines you re interessted in. When you done type : bye \n");

                    String busline = input.nextLine() + "\n";

                    outToServer.println(busline);
                    String answer = inFromServer.readLine();

                    while (!answer.equals("next")) {
                        System.out.println(answer);
                        answer = inFromServer.readLine();
                    }
                    line = inFromServer.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
