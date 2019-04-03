import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

class Publisher{
    static private final int PORT = 10000;

    static ArrayList<Value> values = new ArrayList<>();
    private static HashMap<Topic, ArrayList<Value>> test = new HashMap<>();
    private int numConnections = 0;
    private ArrayList<String> BrokerList = new ArrayList<>();
    private static ArrayList<Topic> topics = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        BroUtilities.CreateBusLines(topics);
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
                Socket connection = providerSocket.accept();
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
                    System.out.println("Got client " + broker + " !");

                    for (Topic topic: topics){
                        ArrayList<Value> temp = new ArrayList<>();
                        for (Value value : values) {
                            if (topic.getLineId().equals(value.getBus().getBuslineId())){
                                temp.add(value);
                            }
                        }
                        test.put(topic,temp);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public ArrayList<String> getBrokerList(){
        if(BrokerList.size() == 0){
            BrokerList.add("Broker A");
            BrokerList.add("Broker B");
            BrokerList.add("Broker C");
        }
        return BrokerList;
    }
}