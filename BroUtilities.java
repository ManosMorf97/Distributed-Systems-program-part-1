import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;


class BroUtilities {
        static void CreateBusLines(ArrayList<Topic> topics) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("BusLinesNew.txt"));
        String line = in.readLine();
        String [] characteristics = new String[3];
        while(line != null){
            int i = 0;
            for (String word : line.split(",")) {
                characteristics[i] = word;
                i++;
            }
            topics.add(new Topic(characteristics[1].trim()));
            line = in.readLine();
        }
        in.close();
    }

    static HashMap<String, ArrayList<Topic>> MD5(ArrayList<Topic> topics) {
        HashMap<String,ArrayList<Topic>> hashed = new HashMap<>();
        ArrayList<Topic> A = new ArrayList<>();
        ArrayList<Topic> B = new ArrayList<>();
        ArrayList<Topic> C = new ArrayList<>();
        int temp = 0;
        for (Topic topic: topics){
            try {
                temp = ipToLong(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            int num = Integer.parseInt(topic.getLineId()) + temp + 4321;

            if(num%3 == 0){
                A.add(topic);
            }else if(num%3 == 1){
                B.add(topic);
            }else{
                C.add(topic);
            }
        }
        hashed.put("BrokerA",A);
        hashed.put("BrokerB",B);
        hashed.put("BrokerC",C);
        return hashed;
    }

    HashMap<Topic, ArrayList<Value>> pull(Socket clientSocket) throws IOException, ClassNotFoundException{
        HashMap<Topic, ArrayList<Value>> input = null;
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            while (true) {
                try {
                    Object inFromServer;
                    inFromServer = in.readObject();
                    if(!inFromServer.equals("Stop")){
                        input = (HashMap<Topic, ArrayList<Value>>) inFromServer;
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
        return input;
    }

    private static int ipToLong(String ipAddress) {

        String[] ipAddressInArray = ipAddress.split("\\.");

        int result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {

            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);

        }

        return result;
    }

}