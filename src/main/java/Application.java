import java.io.File;

public class Application {
    public static void main(String[] args) {
        String filePath = new File(IPExtract.class.getClassLoader().getResource("big_rules.csv").getFile()).getAbsolutePath();

        Firewall firewall = new Firewall(filePath);

        long start = System.nanoTime();
        boolean result = firewall.accept_packet("inbound","tcp",10,"100.100.100.1");
        long end = System.nanoTime();


        //System.out.println("Time for Execution is "+(end - start) / 1000);
        System.out.println(result);

    }

}