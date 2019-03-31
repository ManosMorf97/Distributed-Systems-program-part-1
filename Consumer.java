
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Consumer {
    private static String busline;
    private static Socket socket;
    private static int change(int x) {
        if (x == 5056) x = 5057;
        else x = 5056;
        return x;
    }
    public static void stage1()throws Exception {
        int port = 4321;
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            socket = serverSocket.accept();
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while (!line.equals("Done")) {
                System.out.println(line);
                line = br.readLine();
            }
            System.out.println("Type the buslines you re interessted in.When you done type : bye \n");
            Scanner input=new Scanner(System.in);
            busline=input.nextLine()+"\n";
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            while (!busline.equals("bye\n")) {
                //stage2();
                bw.write(busline);
                bw.flush();
                String answer = br.readLine();
                while (!answer.equals("next")) {
                    System.out.println(answer);
                    answer = br.readLine();
                }
                // out.println(busline);
                busline = input.nextLine()+"\n";

            }
            System.out.println("Do you want to connect with other broker? 0 for no 1 for yes");
            Scanner input2 = new Scanner(System.in);
            if (input2.nextInt() == 1)
                port = change(port);

        }
    }
    public static void main(String[] args) throws Exception {
        stage1();
    }
}
