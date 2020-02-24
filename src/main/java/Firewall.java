import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.List;

public class Firewall {

    private String filePath;
    private Rules rules;

    Firewall(String filePath) {
        this.filePath = filePath;
        buildRules();
    }

    /**
     * @param direction string inbound or outbound
     * @param protocol string either tcp or udp
     * @param port integer in range [1, 65535]
     * @param ipAddr string well-formed IPv4 address
     * @return boolean true if accept the packet else false
     */
    public boolean accept_packet(String direction, String protocol, int port, String ipAddr) {
        
        long ip = IPUtils.getLongValueOfIP(ipAddr);
       
        Map<Integer, List<long[]>> ruleMap = rules.getRules(direction, protocol);
        
        if(!ruleMap.containsKey(port))
            return false;
        
        List<long[]> list = ruleMap.get(port);

        return search(list, ip);
    }

    private boolean search(List<long[]> list, long ip) {

        int l = 0, r = list.size() - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
  
            long[] range = list.get(m);

            if(range.length == 1) {
                if (range[0] == ip) 
                    return true; 
                if (range[0] < ip) 
                    l = m + 1; 
                else
                    r = m - 1;
            }
            else {
                if (range[0] <= ip && range[1] >= ip)
                    return true; 
                if (range[0] < ip) 
                    l = m + 1; 
                else
                    r = m - 1;
            }
             
        } 

        return false;
    }
    
    private void buildRules() {
    
        try {
            Scanner scanner = new Scanner(new File(this.filePath));
            rules = new Rules();

            while(scanner.hasNext()) {
                String[] rule = scanner.nextLine().split(",");
                //System.out.println(rule[0]+" " + rule[1]+" "+rule[2]+" "+ rule[3]);
                rules.addRule(rule[0], rule[1], rule[2], rule[3]);
            }

            rules.sortAllMaps();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    }

}