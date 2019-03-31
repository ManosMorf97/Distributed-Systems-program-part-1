import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

class BroUtilities {

    public static void ActivateResponsibility(ArrayList<BusLine> busLines) {
        for (BusLine b : busLines) {
            try {
                if ((MD5(b.getLineId())).compareTo(MD5(InetAddress.getLocalHost().toString() + "4321")) < 0) {
                    BrokerA.responsibleLines.add(b);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
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
}