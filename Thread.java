import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Thread extends java.lang.Thread {
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket s;


    // Constructor
    public Thread(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public Thread(Broker.PubBroker pubBroker) {
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
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
