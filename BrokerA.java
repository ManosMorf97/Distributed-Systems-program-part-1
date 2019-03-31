import java.io.*;
import java.net.*;
import java.util.ArrayList;
//hash buslineid
//topic or key LineId
//value busPosition

public  class BrokerA{
   private static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusLine> responsibleLines = new ArrayList<>();
    private static ArrayList<BusPosition> datafrompublisher = new ArrayList<>();
public static ArrayList<BusLine> getBusLines(){
    return busLines;
}
    static ArrayList<BusLine> getResponsibleLines(){
        return  responsibleLines;
    }

    public static class ComunicationWithPublisherThread implements Runnable {
        private Socket socket;

        ComunicationWithPublisherThread (Socket socket) {
            this.socket = socket;
        }

        public void run() {

            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                for(BusLine  b:busLines){
                    if(Integer.parseInt(Utilities.MD5(b.getLineId()))<Integer.parseInt(Utilities.MD5(socket.getInetAddress().toString() + "4321"))){
                        responsibleLines.add(b);
                    }
                }
                out.writeObject("I am responsible for these keys:\n");
                for (BusLine  rl:Utilities.getResponsibleLines()) {
                    out.writeObject(rl.getRouteDescription()+"\n");
                    out.writeObject(rl.getLineId()+"\n\n");
                }
                String message = (String) in.readObject();//sypose  that the pub send the data in this way
                System.out.println(message);
                while (!message.equals("bye")) {
                   String lineId=(String) in.readObject();
                   int x=(Integer)in.readObject();//check me
                    int y=(Integer)in.readObject();//check me
                    for(BusPosition bp:Utilities.getBusPositions()){
                       if(bp.getLatitude() == y&&bp.getLongitude() == x){//we will see that after publisher is done
                            datafrompublisher.add(bp);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public static class ComunicationWithConsumerThread implements  Runnable{
        private Socket socket;
        private ServerSocket providerSocket;
        ComunicationWithConsumerThread(Socket socket){
            this.socket=socket;
        }
        public void run(){

            try {
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write("I am broker A and I am responsible for these keys:\n");
                bw.flush();
                //String message = (String) in.readObject();
                for (BusLine rl : Utilities.getResponsibleLines()) {
                    for (Route r2 : Utilities.getRoutes()) {
                        if(rl.getLineCode()==r2.getLineCode())
                        bw.write(rl.getRouteDescription() + "\n");
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
                        System.out.println(Utilities.getResponsibleLines().size());
                        //only for my responsibility
                            for(BusLine bl:Utilities.getResponsibleLines()) {
                                if(bl.getLineId().equals((lineId))){
                                    String linecode=bl.getLineCode();
                                    for(BusPosition bp:Utilities.getBusPositions()){
                                        if(bp.getLineCode().equals(linecode)){
                                            bw.write("Bus from line " + lineId + "\n");
                                            bw.flush();
                                            bw.write("Longitude " + bp.getLongitude() + " Latitude " + bp.getLatitude() + "\n");
                                            bw.flush();
                                        }
                                    }
                                }



                            }

                        lineId = br.readLine();
                            System.out.println("Get");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
    }
    public static void main(String[] args) throws IOException{
        new Utilities().openServer(4321);
    }


}

