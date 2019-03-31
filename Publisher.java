import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Publisher{

    static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions = new ArrayList<>();
    private static ArrayList<Route> routes = new ArrayList<>();
    static ArrayList<BusLine> responsibleLines = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        makeArrays();
        String fromclient;
        String toclient;

        ServerSocket Server = new ServerSocket(5000);

        System.out.println("TCPServer Waiting for client on port 5000");

        while (true) {
            Socket connected = Server.accept();
            System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connected.getInputStream()));

            PrintWriter outToClient = new PrintWriter(connected.getOutputStream(), true);

            while (true) {
                System.out.println("SEND(Type Q or q to Quit):");
                toclient = inFromUser.readLine();
                if (toclient.equals("q") || toclient.equals("Q")) {
                    outToClient.println(toclient);
                    connected.close();
                    break;
                } else outToClient.println(toclient);

                fromclient = inFromClient.readLine();

                if (fromclient.equals("q") || fromclient.equals("Q")) {
                    connected.close();
                    break;
                } else System.out.println("RECEIVED:" + fromclient);
            }
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