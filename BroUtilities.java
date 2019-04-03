import java.io.*;
import java.net.BindException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


class BroUtilities {
        static void CreateBusLines(ArrayList<Topic> topics) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("BusLinesNew.txt"));
        String line = in.readLine();
        String [] characteristics = new String[3];
        while(line != null){
            int i = 0;
            for (String word : line.split(",")) {
                characteristics[i] = word;
                i++;
            }
            topics.add(new Topic(characteristics[1].trim()));
            line = in.readLine();
        }
        in.close();
    }

    HashMap<Topic, ArrayList<Value>> pull(Socket clientSocket) throws IOException, ClassNotFoundException{
        HashMap<Topic, ArrayList<Value>> bus = null;
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                try {
                    Object inFromServer;
                    inFromServer = in.readObject();
                    if(!inFromServer.equals("Stop")){
                        bus = (HashMap<Topic, ArrayList<Value>>) inFromServer;
                    }else{
                        break;
                    }
                } catch (EOFException ignored) {

                }
            }
            clientSocket.close();
        }catch (BindException | ConnectException e){
            System.out.println("Couldn't connect to server");
        }
        return bus;
    }
}