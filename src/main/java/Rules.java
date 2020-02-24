import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Rules {
    private Map<Integer, List<long[]>> inboundTcpMap;
    private Map<Integer, List<long[]>> outboundTcpMap;
    private Map<Integer, List<long[]>> inboundUdpMap;
    private Map<Integer, List<long[]>> outboundUdpMap;

    Rules() {
        inboundTcpMap = new HashMap<>();
        outboundTcpMap = new HashMap<>();
        inboundUdpMap = new HashMap<>();
        outboundUdpMap = new HashMap<>();
    }

    public void addRule(String direction, String protocol, String port, String ipAddr) {
        Map<Integer, List<long[]>> map = getRules(direction, protocol);

        int beginPort = 0;
        int endPort = 0;

        if(port.contains("-")) {
            String[] ports = port.split("-");
            beginPort = Integer.valueOf(ports[0]);
            endPort = Integer.valueOf(ports[1]);

        }
        else {
            beginPort = Integer.valueOf(port);
            endPort = Integer.valueOf(port);
        }

        addPortIPMap(beginPort, endPort, map, ipAddr);
        
    }
    
    private void addPortIPMap(int beginPort, int endPort, Map<Integer, List<long[]>> map, String ip) {

        for(int port = beginPort; port <= endPort; port++) {
            
            List<long[]> list = map.get(port);
            
            if(list == null) {
                list = new ArrayList<>();
                map.put(port, list);
            }
            
            long[] arr = new long[ip.contains("-") ? 2 : 1];
            
            if(ip.contains("-")) {
                arr[0] = IPUtils.getLongValueOfIP(ip.split("-")[0]);
                arr[1] = IPUtils.getLongValueOfIP(ip.split("-")[1]);
                list.add(arr);
            }
            else {

                arr[0] = IPUtils.getLongValueOfIP(ip);
                list.add(arr);

            }

        }
    }

    public Map<Integer, List<long[]>> getRules(String direction, String protocol) {
        if (direction.equals("inbound") && protocol.equals("tcp")) {
            return inboundTcpMap;
        }
        else if (direction.equals("inbound") && protocol.equals("udp")) {
            return inboundUdpMap;
        }
        else if (direction.equals("outbound") && protocol.equals("tcp")) {
            return outboundTcpMap;
        }
        else {
            return outboundUdpMap;
        }
    }

    public void sortAllMaps() {
        sortMap(inboundTcpMap);
        sortMap(inboundUdpMap);
        sortMap(outboundTcpMap);
        sortMap(outboundUdpMap);
    }
    
    private void sortMap(Map<Integer, List<long[]>> map) {
        for(Map.Entry<Integer, List<long[]>> entry : map.entrySet()) {
            List<long[]> list = entry.getValue();
            Collections.sort(list, (a, b) -> {

                if(a.length == 2 && b.length == 2)
                    return a[1] - b[1] <= 0l ? -1 : 1;
                else if(a.length == 2)
                    return a[1] - b[0] <= 0l ? -1 : 1;
                else if(b.length == 2)
                    return a[0] - b[1] <= 0l ? -1 : 1;
                else
                    return a[0] - b[0] <= 0l ? -1 : 1;
            });
        }
    }
}