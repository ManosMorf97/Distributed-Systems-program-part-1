import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Consumer {

    public static void main(String[] args){
        int i = 0;
        while (true){
            try (Socket clientSocket = new Socket("localhost", 4321)){
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
            } catch (ConnectException e) {
                if (i == 30) {
                    System.out.println("Connection with broker timed out, we couldn't find any broker.");
                    break;
                }
                System.out.println("Waiting for Broker!");
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
