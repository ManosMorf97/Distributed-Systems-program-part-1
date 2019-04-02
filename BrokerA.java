import java.io.*;
import java.net.*;
import java.util.*;


public  class BrokerA {
    private static ArrayList<BusLine> responsibleLines = new ArrayList<>();
    private static HashMap<String,ArrayList<Bus>>  bus = new HashMap<>();
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

                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));


                PrintWriter outToClient = new PrintWriter(connected.getOutputStream(), true);

                while(true){
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));
                    outToClient.println("I am broker A and I am responsible for these keys");

                    for (String lineId : bus.keySet()) {
                        outToClient.println(lineId);
                    }
                    outToClient.println("Broker B is responsible for these Keys");
                    outToClient.println("Broker C is responsible for these Keys");
                    outToClient.println("Done");

                    String inputLineId = inFromClient.readLine();
                    ArrayList<Bus> buses;
                    try {
                        buses = bus.get(inputLineId);
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
            Socket clientSocket = new Socket("localhost", 5000);
            Object inFromServer;
            Object outToServer;

            while (true) {
                try {
                    BroUtilities.CreateBusLines(busLines);
                    for(BusLine busLine: busLines){
                        if(BroUtilities.MD5(busLine.getLineId()).compareTo(BroUtilities.MD5(InetAddress.getLocalHost().toString() + 5000)) < 0){
                            responsibleLines.add(busLine);
                        }
                    }
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                    outToServer = responsibleLines;
                    out.writeObject("Sending Lines");
                    out.writeObject(outToServer);
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    inFromServer = in.readObject();
                    if (inFromServer.equals("Stop")) break;
                    else{
                        bus = (HashMap<String, ArrayList<Bus>>) inFromServer;
                        inFromServer = in.readObject();
                        if (inFromServer.equals("Stop")) break;
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


