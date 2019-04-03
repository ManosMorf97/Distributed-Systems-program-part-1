import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public  class BrokerA {
        private static HashMap<Topic,  ArrayList<Value>> input = new HashMap<>();
    private static ArrayList<Topic> topics = new ArrayList<>();
    private static HashMap<String,ArrayList<Topic>> hashed = new HashMap<>();


    public static class ComunicationWithConsumerThread implements Runnable {
        private ServerSocket Server;
        private HashMap<String, ArrayList<Topic>> temp2;

        ComunicationWithConsumerThread(ServerSocket Server) {
            this.Server = Server;
        }

        public void run() {
            try {
                startCommunication();
            }catch(IOException | NoSuchAlgorithmException e){
                e.printStackTrace();
            }
        }

        void startCommunication() throws IOException, NoSuchAlgorithmException {
            while (true) {
                Socket connected = Server.accept();
                System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");

                PrintWriter outToClient = new PrintWriter(connected.getOutputStream(), true);

                while(true){
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));


                    hashed = BroUtilities.MD5(topics);

                    outToClient.println("I am broker A and I am responsible for these keys");

                    for (Topic topic : hashed.get("BrokerA")) {
                        outToClient.println(topic.getLineId());
                    }

                    outToClient.println("Broker B is responsible for these Keys");
                    for (Topic topic : hashed.get("BrokerB")) outToClient.println(topic.getLineId());

                    outToClient.println("Broker C is responsible for these Keys");
                    for (Topic topic : hashed.get("BrokerC")) outToClient.println(topic.getLineId());

                    outToClient.println("Done");

                    String inputLineId = inFromClient.readLine();
                    ArrayList<Value> values;

                    try {
                        for(Topic topic:input.keySet()){
                            if(topic.getLineId().equals(inputLineId)){
                                values = input.get(topic);
                                values.sort(Comparator.comparing(o -> o.getBus().getTime()));
                                for (Value bus_2_ : values) outToClient.println("The bus with id " + bus_2_.getBus().getVehicleId() + " was last spotted at [" + bus_2_.getBus().getTime() + "] at \nLatitude: " + bus_2_.getLatitude() + "\nLongitude: " + bus_2_.getLongitude() + "\nRoute: " + bus_2_.getBus().getLineName() + "\n-----------------------------------------------------------\n") ;
                            }
                        }
                    }catch(NullPointerException e){
                        outToClient.println("No values for me");
                    }
                    outToClient.println("next");

                }
            }
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        BroUtilities.CreateBusLines(topics);
        Socket clientSocket = new Socket("localhost", 10000);

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

        out.writeObject("BrokerA");

        input = new BroUtilities().pull(clientSocket);

        ServerSocket Server = new ServerSocket(4321);

        while (true) {
            ComunicationWithConsumerThread cwct = new ComunicationWithConsumerThread(Server);
            Thread t = new Thread(cwct);
            t.start();
            t.join();
        }
    }
}


