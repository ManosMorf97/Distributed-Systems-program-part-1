import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public  class BrokerA {
    private static ArrayList<Topic> responsibleLines = new ArrayList<>();
    private static HashMap<Topic,  ArrayList<Value>>  bus = new HashMap<>();
    private static ArrayList<Topic> topics = new ArrayList<>();


//    public static class ComunicationWithConsumerThread implements Runnable {
//        private ServerSocket Server;
//
//        ComunicationWithConsumerThread(ServerSocket Server) {
//            this.Server = Server;
//        }
//
//        public void run() {
//            try {
//                startCommunication();
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//
//        void startCommunication() throws IOException {
//            while (true) {
//                Socket connected = Server.accept();
//                System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");
//
//                PrintWriter outToClient = new PrintWriter(connected.getOutputStream(), true);
//
//                while(true){
//                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
//
//                    outToClient.println("I am broker A and I am responsible for these keys");
//                    for (String lineId : bus.keySet()) outToClient.println(lineId);
//
//                    outToClient.println("Broker B is responsible for these Keys");
//                    for (String lineId : bus.get("B").keySet()) outToClient.println(lineId);
//
//                    outToClient.println("Broker C is responsible for these Keys");
//                    for (String lineId : bus.get("C").keySet()) outToClient.println(lineId);
//
//                    outToClient.println("Done");
//
//                    String inputLineId = inFromClient.readLine();
//                    ArrayList<Value> values;
//                    try {
//                        values = bus.get("A").get(inputLineId);
//                        values.sort(Comparator.comparing(o -> o.getBus().getTime()));
////                        for (Value bus_2_ : values)
////                            outToClient.println("The bus with id " + bus_2_.getBus().getVehicleId() + " was last spotted at [" + bus_2_.getBus().getTime() + "] at \nLatitude: " + bus_2_.getBus().getLatitude() + "\nLongitude: " + bus_2_.getBus().getLongitude() + "\nRoute: " + bus_2_.getRoute().getDescription() + "\n-----------------------------------------------------------\n") ;
//
//                    }catch(NullPointerException e){
//                        outToClient.println("No values for me");
//                    }
//                    outToClient.println("next");
//
//                }
//            }
//        }
//    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        BroUtilities.CreateBusLines(topics);
        Socket clientSocket = new Socket("localhost", 10000);
        Object outToServer;

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

        out.writeObject("BrokerA");

        bus = new BroUtilities().pull(clientSocket);

        ServerSocket Server = new ServerSocket(4321);

//        while (true) {
//
//            ComunicationWithConsumerThread cwct = new ComunicationWithConsumerThread(Server);
//            Thread t = new Thread(cwct);
//            t.start();
//            t.join();
//        }
    }
}


