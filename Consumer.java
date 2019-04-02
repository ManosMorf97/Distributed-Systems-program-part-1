
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


        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));
        Scanner input=new Scanner(System.in);
        String line = inFromServer.readLine();
        while(true){
            PrintWriter outToServer = new PrintWriter(
                    clientSocket.getOutputStream(), true);

            while (!line.equals("Done")) {
                System.out.println(line);
                line = inFromServer.readLine();
            }
                System.out.println("Type the buslines you re interessted in.When you done type : bye \n");

                String busline = input.nextLine() + "\n";
                System.out.println("DO "+busline);
                outToServer.println(busline);
                String answer = inFromServer.readLine();
                while (!answer.equals("next")) {
                    System.out.println(answer);
                    answer = inFromServer.readLine();
                }
                //System.out.println("Do you want to connect with other broker? 0 for no 1 for yes");
                line = inFromServer.readLine();

        }
    }
    public static void main(String[] args) throws Exception {
        stage1();
    }
}
