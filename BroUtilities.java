import java.net.InetAddress;
import java.net.UnknownHostException;

class BroUtilities {

    private void ActivateResponsibility(){
        for(BusLine  b:Publisher.busLines){
            try {
                if ((BroUtilities.MD5(b.getLineId())).compareTo(BroUtilities.MD5(InetAddress.getLocalHost().toString() + "4321")) < 0) {
                    BrokerA.responsibleLines.add(b);
                }
            }catch(UnknownHostException e){
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