import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

class Publisher{
    static private final int PORT = 10000;
    static private final int PORT2 = 11000;

    private static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions = new ArrayList<>();
    private static ArrayList<Route> routes = new ArrayList<>();
    private static HashMap<String,ArrayList<Bus>> bus = new HashMap<>();
    private static HashMap<String, HashMap<String, ArrayList<Bus>>> test = new HashMap<>();
    private int numConnections = 0;
    Socket connection;

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        makeMaps();
        Publisher server = new Publisher();
        ServerSocket providerSocket = new ServerSocket(PORT, 3);
        System.out.println("Waiting for clients to connect...");
        while (true){
            server.run(providerSocket);
        }
    }


    private void run(ServerSocket providerSocket) throws InterruptedException {
        try {
            while (numConnections < 3) {
                connection = providerSocket.accept();
                Thread t = new Thread(new ConnectionHandler(connection));
                t.start();
                t.join();
                Thread t1 = new Thread(new push(connection));
                t1.start();
                t1.join();
            }
        } catch (IOException e) {
            throw new RuntimeException("Not able to open the port", e);
        }
    }
    private class push  implements Runnable {
        private final Socket connection;

        private push(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                out.writeObject(test);
                out.writeObject("Stop");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectionHandler  implements Runnable {
        private final Socket connection;

        private ConnectionHandler(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                String broker;
                numConnections++;
                Object inFromServer;
                ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
                inFromServer = in.readObject();
                if(inFromServer.toString().startsWith("Broker")) {
                    broker = inFromServer.toString().substring(6);
                    busLines = (ArrayList<BusLine>) in.readObject();
                    System.out.println("Got client " + broker + " !");
                    for (BusLine busLine : busLines) {
                        ArrayList<Bus> temp = new ArrayList<>();
                        for (BusPosition busPosition : busPositions) {
                            if (busLine.getLineCode().equals(busPosition.getLineCode()))
                                for (Route route : routes)
                                    if (route.getRouteCode().equals(busPosition.getRouteCode()))
                                        temp.add(new Bus(busLine, busPosition, route));
                        }
                        bus.put(busLine.getLineId().trim(), temp);
                    }
                    test.put(broker,bus);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void makeMaps() throws IOException, ParseException {
        PubUtilities.CreateRoutes(routes);
        PubUtilities.CreateBusLines(busLines);
        PubUtilities.CreateBusPositions(busPositions);
    }
}


//push(topic,value) -> [broker]
//pull(topic,[broker]) -> [topic,value]