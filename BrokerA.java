import java.io.*;
import java.net.*;
import java.util.ArrayList;


public  class BrokerA {
    private static ArrayList<BusLine> busLines;
    static ArrayList<BusLine> responsibleLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions;
    private static ArrayList<Route> routes;




    public static class ComunicationWithConsumerThread implements Runnable {
        private Socket socket;

        ComunicationWithConsumerThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            while (true) {
                try {
                    OutputStream os = socket.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw);
                    bw.write("I am broker A and I am responsible for these keys:\n");
                    bw.flush();
                    //String message = (String) in.readObject();
                    for (BusLine rl : responsibleLines) {
                        for (Route r2 : routes) {
                            if (rl.getLineCode().equals(r2.getLineCode()))
                                bw.write(rl.getDescription() + "\n");
                            bw.flush();
                            bw.write(rl.getLineId() + "\n\n");
                            bw.flush();
                        }
                    }
                    //the other brokers
                    bw.write("Broker B is responsible for these keys :\n");
                    bw.flush();
                    // ArrayList<BusLine> bB = BrokerB.getResponsibleLines();
                    //for (BusLine rl : bB) {
                    //  bw.write(rl.getRoute().getRouteDescription() + "\n");
                    //bw.flush();
                    //bw.write(rl.getLineId() + "\n\n");
                    //bw.flush();
                    //}
                    bw.write("Broker C is responsible for these keys :\n");
                    bw.flush();
                    //ArrayList<BusLine> bC = BrokerC.getResponsibleLines();
                    //for (BusLine rl : bC) {
                    //  bw.write(rl.getRoute().getRouteDescription() + "\n");
                    //bw.flush();
                    //bw.write(rl.getLineId() + "\n\n");
                    //bw.flush();
                    // }
                    bw.write("Done" + "\n");
                    bw.flush();
                    //
                    //Get the return message from the server
                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String lineId = br.readLine();
                    System.out.println(lineId.length());
                    bw.write("My responsibilities :\n");
                    bw.flush();
                    while (!lineId.equals("bye")) {
                        System.out.println(responsibleLines.size());
                        //only for my responsibility
                        for (BusLine bl : responsibleLines) {
                            if (bl.getLineId().equals((lineId))) {
                                String linecode = bl.getLineCode();
                                for (BusPosition bp : busPositions) {
                                    if (bp.getLineCode().equals(linecode)) {
                                        bw.write("Bus from line " + lineId + "\n");
                                        bw.flush();
                                        bw.write("Longitude " + bp.getLongitude() + " Latitude " + bp.getLatitude() + " Time " + bp.getTime() + "\n");
                                        bw.flush();
                                    }
                                }
                            }


                        }
                        bw.write("next\n");
                        bw.flush();
                        lineId = br.readLine();
                        System.out.println("Get");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Socket clientSocket = new Socket("localhost", 5000);
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        while (true) {
            try {
                routes = (ArrayList<Route>) in.readObject();
                busPositions = (ArrayList<BusPosition>) in.readObject();
                busLines = (ArrayList<BusLine>) in.readObject();
                String stop = (String) in.readObject();
                if (stop.equals("Stop")) break;
            } catch (EOFException ignored) {


            }

            //routes =(ArrayList<Route>) in.readObject();
            //busPositions=(ArrayList<BusPosition>)in.readObject();
            //busLines=(ArrayList<BusLine>)in.readObject();

            //in.readObject();
            //ActivateResponsibility();

            //clientSocket.close();
            //comuniction with client

            //ServerSocket Server = new ServerSocket(4321);
           // Socket connected = Server.accept();
           // ComunicationWithConsumerThread cwct = new ComunicationWithConsumerThread(connected);
           // Thread t = new Thread(cwct);
           // t.start();
           // t.join();
           // System.out.println("TCPServer Waiting for client on port 4321");
        }
    }
}

