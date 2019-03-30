import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Utilities {
    private static ArrayList<Route> routes = new ArrayList<>();
    private static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusLine> responsibleLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions = new ArrayList<>();
    private static ArrayList<BusPosition> datafrompublisher = new ArrayList<>();

    static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {}
        return null;
    }


    private static void CreateBusPositions(BufferedReader br, ArrayList<BusLine> busLines, ArrayList<BusPosition> busPositions)throws IOException {
        String line="";
        while(line!=null){
            String [] characteristics=new String[5];
            line=br.readLine();
            int pos=line.indexOf(",");
            line = line.substring(pos + 1);
            pos = line.indexOf(",");
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


    private static void CreateRoutes(BufferedReader br, ArrayList<Route> routes) throws IOException {
        String line = "";
        while(line != null){
            String [] characteristics = new String[3];
            line = br.readLine();
            for(int i=0; i < 3; i++){
                int pos = line.indexOf(",");
                characteristics[i] = line.substring(0,pos);
                line = line.substring(pos+1);
            }
            int pos2 = line.indexOf("[");
            if(pos2<0)pos2 = line.length();//if ([) does not exist
            routes.add(new Route(line.substring(0,pos2),characteristics[0],characteristics[1],characteristics[2]));
            line = br.readLine();
        }
    }
    private static void CreateBusLines(BufferedReader br, ArrayList<Route> routes, ArrayList<BusLine> busLines) throws  IOException{
        String line="";
        while(line!=null){
            String [] characteristics=new String[2];
            line=br.readLine();
            for(int i=0; i<2; i++){
                int pos=line.indexOf(",");
                characteristics[i]=line.substring(0,pos);
                line=line.substring(pos+1);
            }
            for(Route r:routes){
                if(r.getRouteDescription().equals(line)){
                    busLines.add(new BusLine(r,characteristics[1]));
                }
            }
            line=br.readLine();
        }
    }

    void openServer(int port) throws IOException {
        FileReader fr = new FileReader("RouteCodesNew.txt");
        BufferedReader br = new BufferedReader(fr);
        CreateRoutes(br,routes);
        br.close();
        fr.close();
        fr = new FileReader("BusLinesNew.txt");
        br = new BufferedReader(fr);
        CreateBusLines(br,routes,busLines);
        br.close();
        fr.close();
        CreateBusPositions(br,busLines,busPositions);
        br.close();
        fr.close();
        ArrayList<Thread> threads = new ArrayList<>();
        ServerSocket providerSocket;
        Socket connection ;
        providerSocket = new ServerSocket(4321);
        try {
            while (true) {
                for (int i = 0; i < 10; i++) {
                    connection = providerSocket.accept();
                    BrokerA.ComunicationWithPublisherThread CWPT = new BrokerA.ComunicationWithPublisherThread(connection);
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

                providerSocket = new ServerSocket(port);
                for (int i = 0; i < 10; i++) {
                    connection = providerSocket.accept();
                    BrokerA.ComunicationWithConsumerThread CWCT = new BrokerA.ComunicationWithConsumerThread(connection);
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
