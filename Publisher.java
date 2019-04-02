import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Publisher{

    private static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions = new ArrayList<>();
    private static ArrayList<Route> routes = new ArrayList<>();
    private static HashMap<String,ArrayList<Bus>> bus = new HashMap<>();

    public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException {
        makeMaps();

        for (Map.Entry<String, ArrayList<Bus>> lineId : bus.entrySet()) {
            ArrayList<Bus> bus1 = lineId.getValue();
            for(int i = 0;i < bus1.size();i++){
                System.out.println(bus1.get(i).getBusLine().getDescription());
            }
        }


        ServerSocket Server = new ServerSocket(5000);

        System.out.println("TCPServer Waiting for client on port 5000");

        Object inFromServer;

        while (true) {
            Socket connected = Server.accept();
            System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");
            ObjectInputStream in = new ObjectInputStream(connected.getInputStream());
            inFromServer = in.readObject();
            if(inFromServer.equals("Sending Lines")){
                ObjectOutputStream out = new ObjectOutputStream(connected.getOutputStream());
                busLines = (ArrayList<BusLine>) in.readObject();
                for (BusLine busLine : busLines) {
                    ArrayList<Bus> temp = new ArrayList<>();
                    for (BusPosition busPosition : busPositions)
                        if (busLine.getLineCode().equals(busPosition.getLineCode()))
                            for (Route route : routes)
                                if(route.getRouteCode().equals(busPosition.getRouteCode()))
                                    temp.add(new Bus(busLine, busPosition, route));
                    bus.put(busLine.getLineId().trim(), temp);
                }
                out.writeObject(bus);
                out.writeObject("Stop");
                connected.close();
            }
        }
    }

    private static void makeMaps() throws IOException, ParseException {
        PubUtilities.CreateRoutes(routes);
        PubUtilities.CreateBusLines(busLines);
        PubUtilities.CreateBusPositions(busPositions);

    }
}