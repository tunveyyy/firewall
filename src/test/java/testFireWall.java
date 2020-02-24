import static org.junit.jupiter.api.Assertions.assertEquals;
 import org.junit.jupiter.api.Test;

import java.io.File;

public class testFireWall {

    @Test
    public void fireWallTest() {
        String filePath = new File(IPExtract.class.getClassLoader().getResource("big_rules.csv").getFile()).getAbsolutePath();
        Firewall firewall = new Firewall(filePath);

        boolean result = firewall.accept_packet("inbound","tcp",10,"100.100.100.1");



        // assert statements
        assertEquals(false, firewall.accept_packet("inbound","tcp",10,"255.255.255.255")); //end of range
        assertEquals(false, firewall.accept_packet("inbound","tcp",65535,"100.100.100.40")); //lying between two ranges
        assertEquals(false, firewall.accept_packet("inbound","tcp",1,"255.255.255.255")); //start port same
        assertEquals(true, firewall.accept_packet("inbound","tcp",1240,"0.0.0.0")); //matching start index of IP

        assertEquals(true, firewall.accept_packet("outbound","tcp",1240,"0.0.0.0")); //should accept everything acc to rule 3

        assertEquals(true, firewall.accept_packet("inbound","udp",15,"41.197.0.0")); //beginning of IP range
        assertEquals(true, firewall.accept_packet("inbound","udp",12,"41.197.255.255")); //penultimate element of range of IP
        assertEquals(true, firewall.accept_packet("inbound","udp",12,"42.0.0.0")); //boundary element of range

        assertEquals(false, firewall.accept_packet("outbound", "udp", 30,"0.0.0.0"));


    }
}