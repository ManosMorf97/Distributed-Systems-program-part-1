import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

class PubUtilities {
    static void CreateBusPositions(BufferedReader br,ArrayList<BusPosition> busPositions)throws IOException {
        String line = br.readLine();
        while(line != null){
            int pos;
            String [] characteristics = new String[6];
            for(int i = 0; i < 6; i++){
                pos = line.indexOf(",");
                if(pos < 0)pos = line.length();
                characteristics[i] = line.substring(0, pos);
                line = line.substring(0, pos + 1);
            }
            busPositions.add(new BusPosition(characteristics[0],characteristics[1],characteristics[2],Double.parseDouble(characteristics[3]),Double.parseDouble(characteristics[4]),characteristics[5]));
            line = br.readLine();
        }
    }

    static void CreateRoutes(BufferedReader br,ArrayList<Route> routes) throws IOException {
        String line = "";
        while(line != null){
            String [] characteristics = new String[4];
            line = br.readLine();
            for(int i=0; i < 3; i++){
                int pos = line.indexOf(",");
                characteristics[i] = line.substring(0,pos);
                line = line.substring(pos+1);
            }
            int pos2 = line.indexOf("[");
            if(pos2 < 0) pos2 = line.length();//if ([) does not exist
            characteristics[3] = line.substring(0,pos2);
            routes.add(new Route(characteristics[0],characteristics[1],characteristics[2],characteristics[3]));
            line = br.readLine();
        }
    }

    static void CreateBusLines(BufferedReader br,ArrayList<BusLine> busLines) throws  IOException{
        String line = "";
        while(line != null){
            String [] characteristics = new String[3];
            line = br.readLine();
            for(int i=0; i<2; i++){
                int pos = line.indexOf(",");
                characteristics[i] = line.substring(0,pos);
                line = line.substring(pos+1);
            }
            busLines.add(new BusLine(characteristics[0],characteristics[1],characteristics[2]));

            line = br.readLine();
        }
    }
}