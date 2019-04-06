import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

class Publisher{
    static private final int PORT = 10000;

    static ArrayList<Value> values = new ArrayList<>();
    private static HashMap<Topic, ArrayList<Value>> output = new HashMap<>();
    private static ArrayList<Topic> topics = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        BroUtilities.CreateBusLines(topics);
        PubUtilities.CreateNames();
        PubUtilities.CreateBuses();
        ServerSocket providerSocket = new ServerSocket(PORT, 3);
        System.out.println("Waiting for clients to connect...");
        try {
            while (true) {
                Socket connection = providerSocket.accept();
                Thread t = new Thread(new push(connection));
                t.start();
                t.join();
            }
        } catch (IOException e) {
            throw new RuntimeException("Not able to open the port", e);
        }
    }

    private static class push  implements Runnable {
        private final Socket connection;

        private push(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                String broker;
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
                        output.put(topic,temp);
                    }
                }
                ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                out.writeObject(output);
				out.writeObject("Stop");
                connection.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}