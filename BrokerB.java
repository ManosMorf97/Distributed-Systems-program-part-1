import java.io.*;
import java.net.*;
import java.util.ArrayList;
//hash bus lineid
//topic or key LineId
//value busPosition


public class BrokerB {
    private static ArrayList<Route> routes=new ArrayList<>();
    private static ArrayList<BusLine>busLines=new ArrayList<>();
    private static ArrayList<BusLine> responsibleLines=new ArrayList<>();
    private static ArrayList<BusPosition>busPositions=new ArrayList<>();
    private static ArrayList<BusPosition>datafrompublisher=new ArrayList<>();
    public static ArrayList<BusLine> getResponsibleLines(){
        return  responsibleLines;
    }
    private static void CreateRoutes(BufferedReader br) throws IOException {
        String line="";
        while(line!=null){
            String [] characteristics=new String[3];
            line=br.readLine();
            for(int i=0; i<3; i++){
                int pos=line.indexOf(",");
                characteristics[i]=line.substring(0,pos);
                line=line.substring(pos+1);
            }
            int pos2=line.indexOf("[");
            if(pos2<0)pos2=line.length();
            routes.add(new Route(line.substring(0,pos2),characteristics[0],characteristics[1],characteristics[2]));
            line=br.readLine();
        }
    }
    private static void CreateBusLines(BufferedReader br) throws  IOException {
        String line = "";
        while (line != null) {
            String[] characteristics = new String[2];
            line = br.readLine();
            for (int i = 0; i < 2; i++) {
                int pos = line.indexOf(",");
                characteristics[i] = line.substring(0, pos);
                line = line.substring(pos + 1);
            }
            for (Route r : routes) {

                if (r.getRouteDescription().equals(line)) {
                    busLines.add(new BusLine(r, characteristics[1]));
                }

                line = br.readLine();
            }
        }
    }
    private static void CreateBusPositions(BufferedReader br)throws  IOException{
        String line="";
        while(line!=null){
            String [] characteristics=new String[5];
            line=br.readLine();
            int pos=line.indexOf(",");
            line=line.substring(pos+1);
            pos=line.indexOf(",");
            characteristics[0]=line.substring(0,pos);
            line=line.substring(pos+1);
            for(int i=1; i<5; i++){
                pos=line.indexOf(",");
                characteristics[i]=line.substring(0,pos);
                line=line.substring(pos+1);
            }
            for(BusLine bl:busLines){
                if(bl.getRoute().getLineCode().equals(characteristics[0])){
                    busPositions.add(new BusPosition(bl,characteristics[1],Double.parseDouble(characteristics[2]),Double.parseDouble(characteristics[3]),characteristics[4]));
                }
            }
            line=br.readLine();
        }
    }


    public static class ComunicationWithPublisherThread implements Runnable {
        private Socket socket;

        public ComunicationWithPublisherThread (Socket socket) {
            this.socket = socket;
        }

        public void run() {

            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                for(BusLine  b:busLines){
                    if(Integer.parseInt(MD5(b.getLineId()))<Integer.parseInt(MD5(socket.getInetAddress().toString()+"4339"))){
                        responsibleLines.add(b);
                    }
                }
                out.writeObject("I am responsible for these keys:\n");
                for (BusLine  rl:responsibleLines) {
                    out.writeObject(rl.getRoute().getRouteDescription()+"\n");
                    out.writeObject(rl.getLineId()+"\n\n");
                }
                String message = (String) in.readObject();//sypose  that the pub send the data in this way
                while (!message.equals("bye")) {
                    String lineId=(String) in.readObject();
                    int x=(Integer)in.readObject();//check me
                    int y=(Integer)in.readObject();//check me
                    for(BusPosition bp:busPositions){
                        if(bp.getLatitude()==y&&bp.getLongitude()==x&&bp.getLongitude()==x&&bp.getBus().getLineId().equals(lineId)){
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

        public ComunicationWithConsumerThread(Socket socket){
            this.socket=socket;
        }
        public void run(){
            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject("I am responsible for these keys:\n");
                //String message = (String) in.readObject();
                for (BusLine  rl:responsibleLines) {
                    out.writeObject(rl.getRoute().getRouteDescription()+"\n");
                    out.writeObject(rl.getLineId()+"\n\n");
                }
                //the other brokers
                out.writeObject("Broker A is responsible for these keys :\n");
                ArrayList<BusLine> bA=BrokerA.getResponsibleLines();
                for(BusLine rl:bA){
                    out.writeObject(rl.getRoute().getRouteDescription()+"\n");
                    out.writeObject(rl.getLineId()+"\n\n");
                }
                out.writeObject("Broker C is responsible for these keys :\n");
                ArrayList<BusLine> bC=BrokerC.getResponsibleLines();
                for(BusLine rl:bC){
                    out.writeObject(rl.getRoute().getRouteDescription()+"\n");
                    out.writeObject(rl.getLineId()+"\n\n");
                }



                //
                try {
                    out.writeObject("My responsibilities :\n");
                    String lineId = (String) in.readObject();
                    while(!lineId.equals("bye")){
                        for (BusLine bl : responsibleLines)
                            if (bl.getLineId().equals(lineId))//only for my responsibility
                                for (BusPosition bp : busPositions) {
                                    if (bp.getBus().getLineId().equals(lineId)) {
                                        out.writeObject("Bus from line " + lineId + "\n");
                                        out.writeObject("Longitude " + bp.getLongitude() + " Latitude " + bp.getLatitude() + "\n");
                                    }
                                }
                        lineId = (String) in.readObject();
                    }
                }catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
            }catch(IOException  e){
                e.printStackTrace();
            }

        }
    }
    public static void main(String[] args) throws IOException{
        new BrokerB().openServer();
    }

    public void openServer() throws IOException {
        FileReader fr=new FileReader("RouteCodesNew.txt");
        BufferedReader br=new BufferedReader(fr);
        CreateRoutes(br);
        br.close();
        fr.close();
        fr=new FileReader("BusLinesNew.txt");
        br=new BufferedReader(fr);
        CreateBusLines(br);
        br.close();
        fr.close();
        CreateBusPositions(br);
        br.close();
        fr.close();
        ArrayList<Thread> threads = new ArrayList<>();
        ServerSocket providerSocket;
        Socket connection ;
        providerSocket = new ServerSocket(4339);
        try {
            while (true) {
                for (int i = 0; i < 10; i++) {
                    connection = providerSocket.accept();
                    ComunicationWithPublisherThread CWPT = new ComunicationWithPublisherThread(connection);
                    Thread t1 = new Thread(CWPT);
                    t1.start();
                    threads.add(t1);
                }
                for (Thread thr : threads) {
                    try {
                        thr.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                providerSocket = new ServerSocket(5056);
                for (int i = 0; i < 10; i++) {
                    connection = providerSocket.accept();
                    ComunicationWithConsumerThread CWCT = new ComunicationWithConsumerThread(connection);
                    Thread t1 = new Thread(CWCT);
                    t1.start();
                    threads.add(t1);
                }
                for (Thread thr : threads) {
                    try {
                        thr.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
