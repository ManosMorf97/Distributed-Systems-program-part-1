import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public  class BrokerC {
    private static ArrayList<BusLine> responsibleLines = new ArrayList<>();
    private static HashMap<String, HashMap<String, ArrayList<Bus>>>  bus = new HashMap<>();
    private static ArrayList<BusLine> busLines = new ArrayList<>();


    public static class ComunicationWithConsumerThread implements Runnable {
        private ServerSocket Server;

        ComunicationWithConsumerThread(ServerSocket Server) {
            this.Server = Server;
        }

        public void run() {
            try {
                startCommunication();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        void startCommunication() throws IOException {
            while (true) {
                Socket connected = Server.accept();
                System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");

                PrintWriter outToClient = new PrintWriter(connected.getOutputStream(), true);

                while(true){
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));

                    outToClient.println("I am broker C and I am responsible for these keys");
                    for (String lineId : bus.get("C").keySet()) outToClient.println(lineId);

                    outToClient.println("Broker A is responsible for these Keys");
                    for (String lineId : bus.get("A").keySet()) outToClient.println(lineId);

                    outToClient.println("Broker B is responsible for these Keys");
                    for (String lineId : bus.get("B").keySet()) outToClient.println(lineId);

                    outToClient.println("Done");

                    String inputLineId = inFromClient.readLine();
                    ArrayList<Bus> buses;
                    try {
                        buses = bus.get("C").get(inputLineId);
                        buses.sort(Comparator.comparing(o -> o.getBusPosition().getTime()));
                        for (Bus bus_ : buses)
                            outToClient.println("The bus with id " + bus_.getBusPosition().getVehicleId() + " was last spotted at [" + bus_.getBusPosition().getTime() + "] at \nLatitude: " + bus_.getBusPosition().getLatitude() + "\nLongitude: " + bus_.getBusPosition().getLongitude() + "\nRoute: " + bus_.getRoute().getDescription() + "\n-----------------------------------------------------------\n") ;

                    }catch(NullPointerException e){
                        outToClient.println("No buses for me");
                    }
                    outToClient.println("next");

                }
            }
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        BroUtilities.CreateBusLines(busLines);

        for(BusLine busLine: busLines){
            if(BroUtilities.MD5(busLine.getLineId()).compareTo(BroUtilities.MD5(InetAddress.getLocalHost().toString() + 10000)) < 0){
                responsibleLines.add(busLine);
            }
        }

        Socket clientSocket = new Socket("localhost", 10000);

        Object outToServer;

        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        outToServer = responsibleLines;
        out.writeObject("BrokerC");
        out.writeObject(outToServer);

        new BrokerC().run(clientSocket);

        //ServerSocket Server = new ServerSocket(4321);

//        while (true) {
//
//            ComunicationWithConsumerThread cwct = new ComunicationWithConsumerThread(Server);
//            Thread t = new Thread(cwct);
//            t.start();
//            t.join();
//        }
    }



    private void run(Socket clientSocket) throws IOException, ClassNotFoundException{
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                try {
                    Object inFromServer;
                    inFromServer = in.readObject();
                    if(!inFromServer.equals("Stop")){
                        bus = (HashMap<String, HashMap<String, ArrayList<Bus>>>) inFromServer;
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
    }
}


