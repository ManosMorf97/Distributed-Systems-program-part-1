import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class BroUtilities {

    static void ActivateResponsibility(HashMap<String,ArrayList<Bus>> bus,int port) {
        for (Map.Entry<String, ArrayList<Bus>> lineId : bus.entrySet()){
            try {
                if ((MD5(lineId.getKey())).compareTo(MD5(InetAddress.getLocalHost().toString() + port)) < 0) {
                    BrokerA.responsibleLines.put(lineId.getKey(),lineId.getValue());
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
    private static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {}
        return null;
    }
}