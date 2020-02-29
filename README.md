
## Introduction
I have implemented packet filtering through firewall. The system admin initially inputs a set of security rules for firewall as an input (csv file). The firewall rules are defined by direction (inbound or outbound), protocol (tcp or udp), ports (either a single port or a range of ports) and IP address (individual or range of IPs). These rules will define whether the incoming packet will be accepted or not. It is assumed that if the packet is not accepted then it will be blocked automatically. The firewall class provides a constructor to accept the file provided by the sysadmin and creates a set of rules internally. The accept_packet will take a request and return a Boolean which decided if the packet will be accepted or not. 

## Assumption
All the incoming packets are assumed formatted as "direction", "protocol",port(/s), "IP addresses"
</br>
where direction = inbound or outbound</br>
protocol = tcp or udp </br>
port = 1-65535 and range is seperated by '-' </br>
IP address = 0.0.0.0-255.255.255.255 and range is seperated by '-'

## Design
I have used Java 12 for this assignment. I created a driver class “Application” which takes in the security rules as well as the packet to be tested. The firewall class starts building rules via buildRules() method and new rule is added through addRule() method of the Rules class. 

The incoming packets can be of 4 types viz inbound tcp packet, inbound udp, outbound tcp, outbound udp. Each of these packet types are stores in 4 different hash maps namely inboundTcp, outboundTcp, inboundUdp and outboundUdp. Since the number of rules will be large to search from, hashmaps ensures a fast lookup. Each of these hashmaps store port number as key and IP addresses as values. Each rule can accept range of ports and IP- addresses. For every rule, if there are a set of IPs that are allowed by that rule, I am storing the value of first IP and last IP of the range. After some research I found that each of the IP is stored as an integer value and this will save significant space over storing Strings. 

Whenever a new rule arrives which matches to an existing port, this new IP address is appended to the list of existing allowed IPs. If the rule contains several ports on a particular set of IP, then I am storing the IPs for each of the port. Eg we have this rule “inbound, tcp, 1-5, 0.0.0.0”, so hashmap inboundTcp will store value 0.0.0.0 at keys 1,2,3,4,5. Here the tradeoff is done between time which is required for searching in a range against increasing data redundancy.  

Once the keys are stored and all the hash tables are ready, I am sorting the list of keys based on the range of IP addresses. This sorting will allow me to use binary search to search for the IP addresses. Binary search on set of IPs and single IP are done in order of the range of IPs. 

## Design Decisions 
1. The number of IP addresses are lot to store, so I decided to store only the start and end IP of the range of IPs as this will allow us to compact the space that is required to store the IPs.
2. Binary search is used to search among IPs which reduces the search time to log n against linear search. Although binary search needs the elements to be sorted, this will just be one time operation that will be done after rules are build and will help in saving time.
3. IPs are stored into integer format rather than string to reduce time needed for comparison when a new packet arrives.
4.  For a range of ports in rules, I am choosing time over space. Storing the same IP for each port in a range does consume additional space. Since the incoming packet will belong to a particular port, searching will be fast.


## Complexity
m - number of accepted ports in firewall </br>
n - number of IPs accepted on each port

Building Rules:
* Time Complexity: O(mnlog(n))
* Space Complexity: O(mn)

Accept Packet:
* Time Complexity: O(log n)
* Space Complexity: O(1)



## Testing
I have written test cases using Junit. Two files rules.csv and big_rules.csv are used for testing data. Rules.csv was used to test for edge and specific cases whereas big_rules.csv was used to test against 172k lines. 
The dataset was obtained from https://github.com/datasets/geoip2-ipv4?files=1 for testing.

The larger dataset took 50 ns to run through "big_rules.csv"
#### Cases tested
1. The IPs in incoming packet lie on the boundary of range of IP.
2. More than one rule match an incoming packet
3. Performance on rules size of more than 100k 

## Future Scope
If I had more time to solve I would have created a custom data structure to store values as ranges for O(1) lookup 

