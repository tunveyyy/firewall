import java.util.Arrays;

public class IPUtils {
    
    public static long getLongValueOfIP(String ip) {
        
        String[] ips = ip.split("\\.");
        //System.out.println(Arrays.toString(ips));
        long A = Long.valueOf(ips[0].trim());
        long B = Long.valueOf(ips[1].trim());
        long C = Long.valueOf(ips[2].trim());
        long D = Long.valueOf(ips[3].trim());

        return D + (C * 256) + (B * 256 * 256) + (A * 256 * 256 * 256);
    }

}