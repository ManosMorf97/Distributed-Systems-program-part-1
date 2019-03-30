import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

class Publisher{

    private static HashMap<String, BusPosition> Keys;

    private ArrayList<Publisher> registeredPublishers;

    public static void main(String[] args) throws IOException {
        new Publisher().openServer();
    }

    private void openServer() {
        ServerSocket providerSocket = null; //arxikopoihsh
        Socket connection;
        String message = null;
        try {
            providerSocket = new ServerSocket(4321);

            while(true) { //o server lamvanei aithmata apo client mexri na termatistei h synthikh

                connection = providerSocket.accept(); //server socket

                //epistrefei neo socket
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream()); //diauloi epikoinwnias
                ObjectInputStream in= new ObjectInputStream(connection.getInputStream());      //etc data input stream, file input stream

                out.writeObject("Connection successful!"); //mesw output stream
                out.flush(); //steile kateutheian to mhnyma

                do {
                    try {
                        message = (String) in.readObject(); //perimenw na parw kati typou string, casting!!
                        System.out.println(connection.getInetAddress().getHostAddress()+">"+message); //termatizei otan lavei bye

                    } catch (ClassNotFoundException classnot) {
                        System.err.println("Data received in unknown format");
                    }
                } while (!message.equals("bye")); //termatizei otan lavei bye
                in.close(); //kleinw ta streams
                out.close();
                connection.close(); //kleinw to socket
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(providerSocket).close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
}

    private HashMap<String,BusPosition> getKeys(){
        return Keys;
    }

}