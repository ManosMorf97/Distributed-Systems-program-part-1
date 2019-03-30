
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
        socket = serverSocket.accept();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());//accept data
        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
        String line = (String) in.readObject();
        while (!line.equals("Done")) {
            System.out.println(line);
            line = (String) in.readObject();
        }
        System.out.println("Type the buslines you re interessted in.When you done type : bye \n");
        Scanner input = new Scanner(System.in);
        busline = input.nextLine();
        while (!busline.equals("bye")) {
            //socket=new Socket(InetAddress.getByName("127.0.0.1"), port);
            //in=new ObjectInputStream(socket.getInputStream());
            //stage2();

            busline = input.nextLine();
            out.writeObject(busline);
            String answer = (String) in.readObject();
            while (!answer.equals("next")) {
                System.out.println((String) in.readObject());
                answer=(String)in.readObject();
            }
           // out.println(busline);
        }
        System.out.println("Do you want to connect with other broker? 0 for no 1 for yes");
        Scanner input2 = new Scanner(System.in);
        if (input2.nextInt() == 1)
            port = change(port);
    }
    public static void stage2()throws Exception{
        // Socket socket=new Socket(InetAddress.getByName("127.0.0.1"),4321);
        //ObjectInputStream in=new ObjectInputStream(socket.getInputStream());

    }
    public static void main(String[] args) throws Exception {
        stage1();
    }
}
