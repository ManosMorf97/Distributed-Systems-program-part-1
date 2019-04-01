
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Consumer {

    private static int change(int x) {
        if (x == 5056) x = 5057;
        else x = 5056;
        return x;
    }

    private static void stage1()throws Exception {
        int port = 4321;
        String FromServer;
        String ToServer;

        Socket clientSocket = new Socket("localhost", port);

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
                System.in));

        PrintWriter outToServer = new PrintWriter(
                clientSocket.getOutputStream(), true);

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));
        while(true){
            String line = inFromServer.readLine();
            while (!line.equals("Done")) {
                System.out.println(line);
                line = inFromServer.readLine();
                System.out.println("Type the buslines you re interessted in.When you done type : bye \n");
                Scanner input=new Scanner(System.in);
                String busline = input.nextLine() + "\n";
                outToServer.println(busline);
                String answer = inFromServer.readLine();
                while (!answer.equals("next")) {
                    System.out.println(answer);
                    answer = inFromServer.readLine();
                }
                System.out.println("Do you want to connect with other broker? 0 for no 1 for yes");

            }
        }
    }
    public static void main(String[] args) throws Exception {
        stage1();
    }
}
