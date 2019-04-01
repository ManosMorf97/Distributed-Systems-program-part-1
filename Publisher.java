import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

class Publisher{

    private static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions = new ArrayList<>();
    private static ArrayList<Route> routes = new ArrayList<>();
    private static HashMap<String,ArrayList<Bus>> bus = new HashMap<>();

    public static void main(String[] args) throws IOException, ParseException {
        makeMaps();

        ServerSocket Server = new ServerSocket(5001);

        System.out.println("TCPServer Waiting for client on port 5001");

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
        for (BusLine busLine : busLines) {
            ArrayList<Bus> temp = new ArrayList<>();
            for (BusPosition busPosition : busPositions)
                if (busLine.getLineCode().equals(busPosition.getLineCode()))
                    for (Route route : routes)
                        if(route.getRouteCode().equals(busPosition.getRouteCode()))
                            temp.add(new Bus(busLine, busPosition, route));
            bus.put(busLine.getLineId().trim(), temp);
        }
    }
}