import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

class Publisher{
    static private final int PORT = 10000;
    static ArrayList<Value> values = new ArrayList<>();
    private static HashMap<String, HashMap<String, ArrayList<Value>>> test = new HashMap<>();
    private int numConnections = 0;
    private Socket connection;

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        PubUtilities.CreateNames();
        PubUtilities.CreateBuses();
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
                    //busLines = (ArrayList<Topic>) in.readObject();
                    System.out.println("Got client " + broker + " !");





//                    for (Value value : values) {
//                        if(topic.e)
//                        ArrayList<Value> temp = new ArrayList<>();
//                        for (Bus bus : busPositions) {
//                            if (topic.getLineCode().equals(bus.getLineCode()))
//                                for (Route route : routes)
//                                    if (route.getRouteCode().equals(bus.getRouteCode()))
//                                        temp.add(new Value(topic, bus, route));
//                        }
//                        bus.put(topic.getLineId().trim(), temp);
//                    }
//                    test.put(broker,bus);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}