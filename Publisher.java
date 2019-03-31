import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class Publisher{

    private static ArrayList<BusLine> busLines = new ArrayList<>();
    private static ArrayList<BusPosition> busPositions = new ArrayList<>();
    private static ArrayList<Route> routes = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("RouteCodesNew.txt");
        BufferedReader br = new BufferedReader(fr);
        Utilities.CreateRoutes(br,routes);
        br.close();
        fr.close();
        fr = new FileReader("BusLinesNew.txt");
        br = new BufferedReader(fr);
        Utilities.CreateBusLines(br,routes,busLines);
        br.close();
        fr.close();
        Utilities.CreateBusPositions(br,busLines,busPositions);
        br.close();
        fr.close();
        new Utilities().openServer(16409);
    }



}