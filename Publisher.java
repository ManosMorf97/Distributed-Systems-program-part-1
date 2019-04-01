import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Publisher{

    private static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions = new ArrayList<>();
    private static ArrayList<Route> routes = new ArrayList<>();
    private static HashMap<Integer,Bus> bus = new HashMap<>();

    public static void main(String[] args) throws IOException, ParseException {
        makeMaps();

        ServerSocket Server = new ServerSocket(5001);

        System.out.println("TCPServer Waiting for client on port 5000");

        while (true) {
            Socket connected = Server.accept();
            System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");

            ObjectOutputStream out = new ObjectOutputStream(connected.getOutputStream());
            out.writeObject(bus);
            out.writeObject("Stop");
            connected.close();
        }
    }

    private static void makeMaps() throws IOException, ParseException {
        PubUtilities.CreateRoutes(routes);
        PubUtilities.CreateBusLines(busLines);
        PubUtilities.CreateBusPositions(busPositions);
        int i = 0;
        for(BusLine busLine:busLines){
            for(BusPosition busPosition: busPositions){
                for(Route route: routes){
                    if(busLine.getLineCode().equals(busPosition.getLineCode()) && busPosition.getRouteCode().equals(route.getRouteCode())) bus.put(i++,new Bus(busLine,busPosition,route));
                }
            }
        }
    }
}