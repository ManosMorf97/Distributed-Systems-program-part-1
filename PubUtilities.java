import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

class PubUtilities {
    static void CreateBusPositions(ArrayList<BusPosition> busPositions) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(new FileReader("BusPositionsNew.txt"));
        String line = in.readLine();
        String [] characteristics = new String[6];
        int j = 0;
        while(line != null){
            int i = 0;
            for(String word : line.split(",")){
                characteristics[i] = word;
                i++;
            }
            String string = characteristics[5];
            DateFormat format = new SimpleDateFormat("MMM  d yyyy HH:mm:ss:SSSa", Locale.ENGLISH);
            Date date = format.parse(string);
            busPositions.add(new BusPosition(Integer.parseInt(characteristics[0].trim()),Integer.parseInt(characteristics[1].trim()),Integer.parseInt(characteristics[2].trim()),Double.parseDouble(characteristics[3].trim()),Double.parseDouble(characteristics[4].trim()), date));
            line = in.readLine();
        }
        in.close();
    }

    static void CreateRoutes(ArrayList<Route> routes) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("RouteCodesNew.txt"));
        String line = in.readLine();
        String [] characteristics = new String[4];
        while(line != null){
            int i = 0;
            for (String word : line.split(",")) {
                characteristics[i] = word;
                i++;
            }
            routes.add(new Route(Integer.parseInt(characteristics[0].trim()),Integer.parseInt(characteristics[1].trim()),Integer.parseInt(characteristics[2].trim()),characteristics[3].trim()));
            line = in.readLine();
        }
        in.close();
    }

    static void CreateBusLines(ArrayList<BusLine>  busLines) throws  IOException{
        BufferedReader in = new BufferedReader(new FileReader("BusLinesNew.txt"));
        String line = in.readLine();
        String [] characteristics = new String[3];
        while(line != null){
            int i = 0;
            for (String word : line.split(",")) {
                characteristics[i] = word;
                i++;
            }
            busLines.add(new BusLine(Integer.parseInt(characteristics[0].trim()),Integer.parseInt(characteristics[1].trim()),characteristics[2].trim()));
            line = in.readLine();
        }
        in.close();
    }
}