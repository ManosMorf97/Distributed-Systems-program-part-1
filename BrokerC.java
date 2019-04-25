/*
Ονο/υμο                 Αμ
Πάνος Ευάγγελος         3150134
Μορφιαδάκης Εμμανουήλ   3150112
Μπρακούλιας Φίλιππος    3140137

 */

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public  class BrokerC {

    private static HashMap<Topic,  ArrayList<Value>> input = new HashMap<>();
    private static ArrayList<Topic> topics = new ArrayList<>();


    public static void main(String[] args) throws IOException{
        BroUtilities.CreateBusLines(topics);
        ServerSocket providerSocket = new ServerSocket(7654, 3);
        System.out.println("Waiting for consumers to connect...");
        try {
            while (true) {
                Socket connection = providerSocket.accept();
                Thread t = new Thread(new ComunicationWithConsumerThread(connection));
                t.start();
            }
        } catch (IOException e) {
            throw new RuntimeException("Not able to open the port", e);
        }
    }

    public static class ComunicationWithConsumerThread implements Runnable {
        private Socket connected;

        ComunicationWithConsumerThread(Socket connected) {
            this.connected = connected;
        }

        public void run() {
            try {
                ObjectInputStream in = new ObjectInputStream(connected.getInputStream());
                Object inFromServer = in.readObject();
                if (inFromServer.toString().equals("Consumer")) {
                    while (true) {
                        System.out.println("THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");
                        PrintWriter outToClient = new PrintWriter(connected.getOutputStream(), true);
                        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
                        HashMap<String, ArrayList<Topic>> hashed = BroUtilities.MD5(topics);
                        outToClient.println("\n--------------------------------------------------------------------------\n");
                        outToClient.println("I am broker C and I am responsible for these keys");
                        for (Topic topic : hashed.get("BrokerC")) outToClient.println(topic.getLineId());
                        outToClient.println("Broker A is responsible for these Keys");
                        for (Topic topic : hashed.get("BrokerA")) outToClient.println(topic.getLineId());
                        outToClient.println("Broker B is responsible for these Keys");
                        for (Topic topic : hashed.get("BrokerB")) outToClient.println(topic.getLineId());
                        outToClient.println("Done");
                        String inputLineId = inFromClient.readLine();
                        boolean temp2 = false;
                        for (Topic topic : hashed.get("BrokerC")) if (topic.getLineId().equals(inputLineId)) temp2 = true;
                        if (temp2) {
                            ArrayList<Value> values;
                            outToClient.println("Trying to establish connection with the server. Please be patient...");
                            try {
                                for (Topic topic : input.keySet()) {
                                    if (topic.getLineId().equals(inputLineId)) {
                                        values = input.get(topic);
                                        values.sort(Comparator.comparing(o -> o.getBus().getTime()));
                                        for (Value bus_2_ : values)
                                            outToClient.println("The bus with id " + bus_2_.getBus().getVehicleId() + " was last spotted at [" + bus_2_.getBus().getTime() + "] at \nLatitude: " + bus_2_.getLatitude() + "\nLongitude: " + bus_2_.getLongitude() + "\nRoute: " + bus_2_.getBus().getLineName() + "\n-----------------------------------------------------------\n");
                                        if (values.size() == 0)
                                            System.out.println("We couldn't find any buses on that line, please try other broker.");
                                    }
                                }
                            } catch (NullPointerException e) {
                                outToClient.println("No values for me");
                            }
                            outToClient.println("next");
                        } else if (!inputLineId.toLowerCase().equals("bye")) {
                            outToClient.println("I don't have information for the specific line, try a different broker.");
                            outToClient.println("next");
                        }
                    }
                }else if(inFromServer.toString().equals("Publisher")){
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(connected.getOutputStream());
                        out.writeObject("BrokerC");
                        input = new BroUtilities().pull(in);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}


