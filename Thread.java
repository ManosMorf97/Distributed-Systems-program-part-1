import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Thread extends java.lang.Thread {
    static DataInputStream dis;
    static DataOutputStream dos;
    static Socket s;


    // Constructor
    public Thread(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        Thread.s = s;
        Thread.dis = dis;
        Thread.dos = dos;
    }


}
