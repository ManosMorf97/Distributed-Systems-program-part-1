import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

class Utilities {


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


    static void CreateBusPositions(BufferedReader br, ArrayList<BusLine> busLines, ArrayList<BusPosition> busPositions)throws IOException {
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


    static void CreateRoutes(BufferedReader br, ArrayList<Route> routes) throws IOException {
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
    static void CreateBusLines(BufferedReader br, ArrayList<Route> routes, ArrayList<BusLine> busLines) throws  IOException{
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



}
