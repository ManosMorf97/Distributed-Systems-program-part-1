import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


class BroUtilities {
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

    static void CreateBusLines(ArrayList<Topic> topics) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("BusLinesNew.txt"));
        String line = in.readLine();
        String [] characteristics = new String[3];
        while(line != null){
            int i = 0;
            for (String word : line.split(",")) {
                characteristics[i] = word;
                i++;
            }
            topics.add(new Topic(characteristics[1].trim()));
            line = in.readLine();
        }
        in.close();
    }
}