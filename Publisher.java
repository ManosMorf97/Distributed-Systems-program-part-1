import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Publisher{

    static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions = new ArrayList<>();
    private static ArrayList<Route> routes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        makeArrays();

        ServerSocket Server = new ServerSocket(5000);

        System.out.println("TCPServer Waiting for client on port 5000");

        while (true) {
            Socket connected = Server.accept();
            System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");

            ObjectOutputStream out = new ObjectOutputStream(connected.getOutputStream());

            out.writeObject(routes);
            out.writeObject(busPositions);
            out.writeObject(busLines);
            out.writeObject("Stop");
            connected.close();
            break;
        }
    }

    private static void makeArrays() throws IOException {
        FileReader fr = new FileReader("RouteCodesNew.txt");
        BufferedReader br = new BufferedReader(fr);
        PubUtilities.CreateRoutes(br,routes);
        br.close();
        fr.close();
        fr = new FileReader("BusLinesNew.txt");
        br = new BufferedReader(fr);
        PubUtilities.CreateBusLines(br,busLines);
        br.close();
        fr.close();
        fr = new FileReader("BusPositionsNew.txt");
        br = new BufferedReader(fr);
        PubUtilities.CreateBusPositions(br,busPositions);
        br.close();
        fr.close();
    }
}