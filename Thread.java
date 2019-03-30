import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public Thread(BrokerA.PubBroker pubBroker) {
    }

    public Thread(Broker.ConBroker conBroker) {
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true) {
            try {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            dis.close();
            dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
