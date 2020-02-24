import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class IPExtract {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(new File(IPExtract.class.getClassLoader().getResource("Ipv4.csv").getFile()));
        FileWriter myWriter = new FileWriter("big_rules.csv");
                //IPExtract.class.getClassLoader().getResource("big_rules.csv").getFile());


        scanner.nextLine();

        while(scanner.hasNext()) {
            String line = scanner.nextLine();
            String ipPort = line.split(",")[0];
            ipPort = ipPort.substring(1, ipPort.length() - 1);
            String ip = ipPort.split("/")[0];
            String port = ipPort.split("/")[1];
            //System.out.println("inbound,tcp,"+ip+","+port);
            myWriter.write("inbound,tcp,15,"+ip+"\n");
        }
        myWriter.close();

    }
}
