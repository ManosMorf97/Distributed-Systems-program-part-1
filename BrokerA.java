import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;


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

        public void startCommunication() throws IOException {
            while (true) {
                Socket connected = Server.accept();
                System.out.println(" THE CLIENT" + " " + connected.getInetAddress()
                        + ":" + connected.getPort() + " IS CONNECTED ");

                BufferedReader inFromUser = new BufferedReader(
                        new InputStreamReader(System.in));

                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(connected.getInputStream()));

                PrintWriter outToClient = new PrintWriter(
                        connected.getOutputStream(), true);
                 while(true){
                outToClient.println("I am broker A and I am responsible for these keys");
                Set<String> lineIds = bus.keySet();
                for (String lineId : lineIds) {
                    System.out.println(lineId);
                }
                outToClient.println("Broker B is responsible for these Keys");
                outToClient.println("Broker C is responsible for these Keys");
                outToClient.println("Done");
                String inputLineId = inFromClient.readLine();
                while (!inputLineId.equals("bye")) {
                    ArrayList<Bus> buses = bus.get(inputLineId);
                    int i = 0;
                    for (Bus bus_ : buses) {
                        outToClient.println("Latitude: " + bus_.getBusPosition().getLatitude() + " Longitude: " + bus_.getBusPosition().getLongitude() + "Time: " + bus_.getBusPosition().getTime().toString());
                        i++;
                    }
                    if (i == 0) System.out.println("No buses for me");
                    outToClient.println("next");
                }
            }
            }
        }
    }
        public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
            Socket clientSocket = new Socket("localhost", 5000);
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                try {
                    bus = (HashMap<String, ArrayList<Bus>>) in.readObject();
                    String stop = (String) in.readObject();
                    BroUtilities.ActivateResponsibility(bus, 4321);
                    if (stop.equals("Stop")) break;
                } catch (EOFException ignored) {


                }
            }
                clientSocket.close();
                System.out.println("sd");
                ServerSocket Server=new ServerSocket(4321);
                 ComunicationWithConsumerThread cwct = new ComunicationWithConsumerThread(Server);
                 Thread t = new Thread(cwct);
                 t.start();
                 t.join();
                 System.out.println("TCPServer Waiting for client on port 4321");

        }
    }


