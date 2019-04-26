/*
Ονο/υμο                 Αμ
Πάνος Ευάγγελος         3150134
Μορφιαδάκης Εμμανουήλ   3150112
Μπρακούλιας Φίλιππος    3140137

 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

class Publisher{
    static ArrayList<Value> values = new ArrayList<>();
    private static HashMap<Topic, ArrayList<Value>> output = new HashMap<>();
    private static ArrayList<Topic> topics = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParseException {
        BroUtilities.CreateBusLines(topics);
        PubUtilities.CreateNames();
        PubUtilities.CreateBuses();
        System.out.println("Waiting for clients to connect...");
        boolean temp = true;
        int i = 0;
        while (temp) {
            try (Socket clientSocket = new Socket("localhost", 4321)) {
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject("Publisher");
                temp = false;
                String broker;
                Object inFromServer;
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                inFromServer = in.readObject();
                if(inFromServer.toString().startsWith("Broker")) {
                    broker = inFromServer.toString().substring(6);
                    System.out.println("Got client " + broker + " !");
                    for (Topic topic: topics){
                        ArrayList<Value> temp2 = new ArrayList<>();
                        for (Value value : values) if (topic.getLineId().equals(value.getBus().getBuslineId())) temp2.add(value);
                        output.put(topic,temp2);
                    }
                }
                out.writeObject(output);
                out.writeObject("Stop");
            } catch (IOException | ClassNotFoundException e) {
                if (i == 30) {
                    System.out.println("Connection with server timed out, we couldn't find what you asked for.");
                    break;
                }
                System.out.println("Waiting for Publisher!");
                i++;
            }
        }
    }
}