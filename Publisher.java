import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Publisher{
    static private final int PORT = 10000;

    private static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions = new ArrayList<>();
    private static ArrayList<Route> routes = new ArrayList<>();
    private static HashMap<String,ArrayList<Bus>> bus = new HashMap<>();


    public static void main(String[] args) throws IOException, ParseException {
        makeMaps();
        new Publisher().push();
    }


    private void push(){
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);
        Runnable serverTask = () -> {
            System.out.println("TCPServer Waiting for client on port 5000");
            try {
                int i = 0;
                System.out.println("Waiting for clients to connect...");
                while (true) {
                    ServerSocket Server = new ServerSocket(PORT + i);
                    i = i + 1000;
                    Socket connected = Server.accept();
                    clientProcessingPool.submit(new ClientTask(connected));
                }
            } catch (IOException e) {
                throw new RuntimeException("Not able to open the port 8080", e);
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }

    private class ClientTask implements Runnable {
        private final Socket clientSocket;

        private ClientTask(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                System.out.println("Got a client !");
                Object inFromServer;
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                inFromServer = in.readObject();

                if(inFromServer.equals("Sending Lines")) {
                    busLines = (ArrayList<BusLine>) in.readObject();

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

                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                    out.writeObject(bus);
                    out.writeObject("Stop");
                    clientSocket.close();
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