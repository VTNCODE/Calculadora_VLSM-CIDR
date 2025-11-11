package services;


import java.util.*;

public class SubNetworks {
    private String ipAddress;
    private Integer mask;
    private List<Networks> network = new ArrayList<>();

    private SubNetworks(String ipAddress) {
        verifyIpAddress(ipAddress);
        this.ipAddress = ipAddress;
    }
    private SubNetworks(Integer mask, List<Networks> network) {
        this.mask = mask;
        this.network = network;
    }

    private SubNetworks(String ipAddress, Integer mask) {
        verifyIpAddress(ipAddress);
        this.ipAddress = ipAddress;
        this.mask = mask;
    }

    protected SubNetworks(String ipAddress, List<Networks> network) {
        verifyIpAddress(ipAddress);
        this.ipAddress = ipAddress;
        this.network = network;
    }

    public SubNetworks(String ipAddress, Integer mask, List<Networks> network) {

        verifyIpAddress(ipAddress);
        verifyMask(ipAddress, mask);

        Collections.sort(network);
        this.ipAddress = ipAddress;
        this.mask = mask;
        this.network = network;

    }

    public List<Networks> getNetwork() {
        return network;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Integer getMask() {
        return mask;
    }
    public Integer totalHosts(Integer mask) {
        return (int) Math.pow(2, (32 - mask)) - 2;
    }
    public Integer usedHosts() {
        int n = 0;
        for (Networks networks : network) {
            n += networks.getHosts();
        }
        if (n > totalHosts(mask)) {
            throw new ExceededHosts("Number of hosts exceeded total hosts.");
        }
        else {
            return n;
        }
    }
    public List<Networks> availableHosts() {
        List<Networks> networksList = new ArrayList<>();

        for (Networks networks : network) {
            for (int z = 0; z <= 32; z++) {
                if (networks.getHosts() > Math.pow(2, (32 - z)) - 2) {
                    Integer hosts = (int)Math.pow(2, (32 - z + 1)) - 2;
                    Networks networks1 = new Networks(networks.getName(), hosts);
                    networksList.add(networks1);
                    break;
                }
            }
        }
        return networksList;
    }

    public List<Networks> nonUsedHosts() {
        List<Networks> networksList = new ArrayList<>();
        for (int i = 0; i < network.size(); i++ ) {
            Integer hosts = availableHosts().get(i).getHosts() - network.get(i).getHosts();
            Networks networks = new Networks(network.get(i).getName(), hosts);
            networksList.add(networks);
        }
        return networksList;
    }
     public List<SubNetworks> prefix() {
         List<SubNetworks> subNetworks = new ArrayList<>();
         List<Networks> net = new ArrayList<>();
         for (Networks networks : network) {
             for (int z = 0; z <= 32; z++) {
                 if (networks.getHosts() > Math.pow(2, (32 - z)) - 2) {
                     Networks networks1 = new Networks(networks.getName());
                     net.add(networks1);
                     SubNetworks subNetworks1 = new SubNetworks(z-1, net);
                     subNetworks.add(subNetworks1);
                     break;
                 }
             }
         }
         return subNetworks;
     }

     public List<SubNetworks> mask() {
         List<SubNetworks> subNetworks = new ArrayList<>();

         for (int i = 0; i < network.size(); i++) {
             String mask;
             if (prefix().get(i).getMask() >= 24 && prefix().get(i).getMask() <= 30) {
                 int lastBits = 254 - ((int)Math.pow(2, (32 - prefix().get(i).getMask())) - 2);
                 mask = "255.255.255." + lastBits;
                 SubNetworks subNetworks1 = new SubNetworks(mask, prefix().get(i).getMask());
                 subNetworks.add(subNetworks1);
             }
             else if (prefix().get(i).getMask() >= 16 && prefix().get(i).getMask() < 24) {
                 int lastBits = 256 - (int)Math.pow(2, (24 - prefix().get(i).getMask()));
                 mask = "255.255." + lastBits + ".0";
                 SubNetworks subNetworks1 = new SubNetworks(mask, prefix().get(i).getMask());
                 subNetworks.add(subNetworks1);
             }
             else if (prefix().get(i).getMask() >= 8 && prefix().get(i).getMask() < 16) {
                 int lastBits = 256 - (int)Math.pow(2, (16 - prefix().get(i).getMask()));
                 mask = "255." + lastBits + ".0.0";
                 SubNetworks subNetworks1 = new SubNetworks(mask, prefix().get(i).getMask());
                 subNetworks.add(subNetworks1);
             }
             else {
                 int lastBits = 256 - (int)Math.pow(2, (8 - prefix().get(i).getMask()));
                 mask = lastBits + ".0.0.0";
                 SubNetworks subNetworks1 = new SubNetworks(mask, prefix().get(i).getMask());
                 subNetworks.add(subNetworks1);
             }
         }


        return subNetworks;
     }
     public List<SubNetworks> wildCardMask() {
         List<SubNetworks> subNetworks = new ArrayList<>();

         for (int i = 0; i < network.size(); i++) {
             StringBuilder wildCardMask = new StringBuilder();
             String []bits = mask().get(i).getIpAddress().split("\\.");
             for (int z =0; z < bits.length; z++ ) {
                 int val = Integer.parseInt(bits[z]);
                 int changes = 255 - val;
                 if (z == 3) {
                     wildCardMask.append(changes);
                 }
                 else {
                     wildCardMask.append(changes).append(".");
                 }

             }
             SubNetworks subNetworks1 = new SubNetworks(wildCardMask.toString());
             subNetworks.add(subNetworks1);

         }
        return subNetworks;
     }
     public List<SubNetworks> broadCast() {
        List<SubNetworks> subNetworks = new ArrayList<>();
        List<Networks> networks = new ArrayList<>();

        String [] ipAddress = getIpAddress().split("\\.");
        String [] mask;

        for (int z = 0; z < network.size(); z++) {
             StringBuilder broadcast = new StringBuilder();
             mask = mask().get(z).getIpAddress().split("\\.");

             for (int y = 0; y < mask.length; y++) {

                 int val = Integer.parseInt(mask[y]);
                 int ch = Integer.parseInt(ipAddress[y]);


                 if (ch == 255 && val != 0 && z != 0) {
                     ch = -1;
                 }
                 if (val == 255 && Objects.equals(ipAddress[y + 1], "255")) {
                     ch += 1;
                 }

                 if (z != 0) {
                     if (val != 255 && val != 0) {
                         int change = ch + (255 - val) + 1;
                         if (y == 3) {
                             broadcast.append(change);
                         } else {
                             broadcast.append(change).append(".");
                         }
                     } else {
                         if (y == 3) {
                             broadcast.append(ch);
                         }
                         else {
                             broadcast.append(ch).append(".");
                         }
                     }
                 }
                 else {
                     if (val != 255) {
                         int change = ch + (255 - val);
                         if (y == 3) {
                             broadcast.append(change);
                         } else {
                             broadcast.append(change).append(".");
                         }
                     } else {
                         broadcast.append(ch).append(".");
                     }
                 }
             }
             ipAddress = broadcast.toString().split("\\.");



             SubNetworks subNetworks1 = new SubNetworks(broadcast.toString(), networks);
             subNetworks.add(subNetworks1);
        }

        return subNetworks;
     }

     public List<SubNetworks> networkAddress() {
         List<SubNetworks> subNetworks = new ArrayList<>();
         String [] ipAddress1;
         String [] mask;

         SubNetworks subNetworks1 = new SubNetworks(getIpAddress());
         subNetworks.add(subNetworks1);


         for (int i = 0; i < network.size()-1; i++ ){
             StringBuilder ipAdd = new StringBuilder();
             mask = mask().get(i).getIpAddress().split("\\.");
             ipAddress1 = broadCast().get(i).getIpAddress().split("\\.");

             for (int z = 0; z < mask.length; z++) {
                 int val = Integer.parseInt(mask[z]);
                 int ch = Integer.parseInt(ipAddress1[z]);

                 if (ch == 255) {
                     ch = 0;
                 }
                 if (val == 255 && Objects.equals(ipAddress1[z + 1], "255")) {
                     ch += 1;
                 }
                 if (val != 255 && val != 0 && !Objects.equals(ipAddress1[z], "255")) {
                     ch += 1;
                 }
                 if (z == 3) {
                     ipAdd.append(ch);
                 } else {
                     ipAdd.append(ch).append(".");
                 }
             }
             subNetworks1 = new SubNetworks(ipAdd.toString());
             subNetworks.add(subNetworks1);
         }

         return subNetworks;
     }
    public List<SubNetworks> usableRangeFirstAddress() {
        List<SubNetworks> subNetworks = new ArrayList<>();
        String [] firstAddress;

        for (int i = 0; i < network.size(); i++ ) {
            StringBuilder networkAddress = new StringBuilder();
            firstAddress = networkAddress().get(i).getIpAddress().split("\\.");

            for (int z = 0; z < firstAddress.length; z++) {
                int ch = Integer.parseInt(firstAddress[z]);
                if (z == 3) {
                    ch += 1;
                }
                if (z == 3) {
                    networkAddress.append(ch);
                }
                else {
                    networkAddress.append(ch).append(".");
                }

            }
            SubNetworks subNetworks1 = new SubNetworks(networkAddress.toString());
            subNetworks.add(subNetworks1);

        }
        return subNetworks;
    }

    public List<SubNetworks> usableRangeFinalAddress() {
        List<SubNetworks> subNetworks = new ArrayList<>();
        String [] firstAddress;

        for (int i = 0; i < network.size(); i++ ) {
            StringBuilder networkAddress = new StringBuilder();
            firstAddress = broadCast().get(i).getIpAddress().split("\\.");

            for (int z = 0; z < firstAddress.length; z++) {
                int ch = Integer.parseInt(firstAddress[z]);
                if (z == 3) {
                    ch -= 1;
                }
                if (z == 3) {
                    networkAddress.append(ch);
                }
                else {
                    networkAddress.append(ch).append(".");
                }

            }
            SubNetworks subNetworks1 = new SubNetworks(networkAddress.toString());
            subNetworks.add(subNetworks1);
        }
        return subNetworks;
    }

    public void verifyIpAddress(String ipAddress) {
        String [] ip = ipAddress.split("\\.");
        int lastEight = Integer.parseInt(ip[3]);
        int lastSixteen = Integer.parseInt(ip[2]);
        int last24 = Integer.parseInt(ip[1]);
        int last32 = Integer.parseInt(ip[0]);

        if (last32 > 255 || last32 < 0) {
            throw new InvalidAddress("This ip address does not seems valid.");
        }
        else if (last24 > 255 || last24 < 0) {
            throw new InvalidAddress("This ip address does not seems valid.");
        }
        else if (lastSixteen > 255 || lastSixteen < 0) {
            throw new InvalidAddress("This ip address does not seems valid.");
        }
        else if (lastEight > 255 || lastEight < 0) {
            throw new InvalidAddress("This ip address does not seems valid.");
        }
    }
    public void verifyMask(String ipAddress, Integer mask) {
        String [] ip = ipAddress.split("\\.");
        int lastEight = Integer.parseInt(ip[3]);
        int lastSixteen = Integer.parseInt(ip[2]);
        int last24 = Integer.parseInt(ip[1]);
        int last32 = Integer.parseInt(ip[0]);


        if (mask > 32 || mask < 0) {
            throw new InvalidMask("Please enter a mask between 0 and 32.");
        }
        else if (mask >= 24 && lastEight != 0) {
            throw new InvalidAddress("This ip can not be fit into this mask.");
        }
        else if (mask >= 16 && mask < 24 && lastSixteen != 0) {
            throw new InvalidAddress("This ip can not be fit into this mask.");
        }
        else if (mask >= 8 && mask < 16  && last24 !=0) {
            throw new InvalidAddress("This ip can not be fit into this mask.");
        }
        else if (mask < 8 && last32 !=0) {
            throw new InvalidAddress("This ip can not be fit into this mask.");
        }
    }

    public void impressResults() {
        for (int i = 0; i < network.size(); i++) {
            System.out.println("Name: " + network.get(i).getName());
            System.out.println("Hosts Needed: " + network.get(i).getHosts());
            System.out.println("Hosts Available: " + availableHosts().get(i).getHosts());
            System.out.println("Unused Hosts: " + nonUsedHosts().get(i).getHosts());
            System.out.println("Network Address: " + networkAddress().get(i).getIpAddress());
            System.out.println("Slash: /" + prefix().get(i).getMask());
            System.out.println("Mask: " + mask().get(i).getIpAddress());
            System.out.println("Usable Range: " + usableRangeFirstAddress().get(i).getIpAddress() + " - " + usableRangeFinalAddress().get(i).getIpAddress());
            System.out.println("Broadcast: " + broadCast().get(i).getIpAddress());
            System.out.println("WildCard: " + wildCardMask().get(i).getIpAddress());
            System.out.println();

        }
    }
    @Override
    public String toString () {

        return "IP ADDRESS: " + ipAddress +
                "/" + mask;
    }


}
