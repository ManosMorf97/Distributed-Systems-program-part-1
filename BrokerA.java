import java.io.*;
import java.net.*;
import java.util.*;


public  class BrokerA {
    static HashMap<String,ArrayList<Bus>> responsibleLines = new HashMap<>();
    private static HashMap<String,ArrayList<Bus>>  bus = new HashMap<>();


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

                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));


                PrintWriter outToClient = new PrintWriter(connected.getOutputStream(), true);

                while(true){
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
                    outToClient.println("I am broker A and I am responsible for these keys");
                    Set<String> lineIds = bus.keySet();
                    for (String lineId : lineIds) {
                        outToClient.println(lineId);
                    }
                    outToClient.println("Broker B is responsible for these Keys");
                    outToClient.println("Broker C is responsible for these Keys");
                    outToClient.println("Done");
                    String inputLineId = inFromClient.readLine();
                    //while (!inputLineId.equals("bye")) {
                    ArrayList<Bus> buses;
                    try {
                        buses = bus.get(inputLineId);
                        buses.sort(Comparator.comparing(o -> o.getBusPosition().getTime()));
                        for (Bus bus_ : buses)
                            outToClient.println("The bus with id " + bus_.getBusPosition().getVehicleId() + " was last spotted at " + bus_.getBusPosition().getTime() + " on the position with Latitude: " + bus_.getBusPosition().getLatitude() + " and  Longitude: " + bus_.getBusPosition().getLongitude() + " at route " + bus_.getRoute().getDescription()) ;

                    }catch(NullPointerException e){
                        outToClient.println("No buses for me");
                    }


                    outToClient.println("next");
                    //inputLineId = inFromClient.readLine();
                //}
                }
            }
        }
    }
        public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
            Socket clientSocket = new Socket("localhost", 5000);
            Object inFromUser;
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                try {
                    inFromUser = in.readObject();
                    if (inFromUser.equals("Stop")) break;
                    else{
                        bus = (HashMap<String, ArrayList<Bus>>) inFromUser;
                        BroUtilities.ActivateResponsibility(bus, 4321);
                    }
                } catch (EOFException ignored) {


                }
            }
            clientSocket.close();
            System.out.println("sd");
            ServerSocket Server = new ServerSocket(4321);
            while (true) {
                ComunicationWithConsumerThread cwct = new ComunicationWithConsumerThread(Server);
                Thread t = new Thread(cwct);
                t.start();
                t.join();
            }

            //System.out.println("TCPServer Waiting for client on port 4321");

        }
    }


