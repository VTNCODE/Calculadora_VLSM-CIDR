package services;


import java.util.ArrayList;
import java.util.List;

public class SubNetworks {
    private String ipAddress;
    private Integer mask;
    private List<Networks> network = new ArrayList<>();




    public SubNetworks(Integer mask, List<Networks> network) {
        this.mask = mask;
        this.network = network;
    }

    public SubNetworks(String ipAddress, Integer mask, List<Networks> network) {
        if (mask > 32 || mask < 0) {
            throw new InvalidMask("Please enter a mask between 0 and 32.");
        }
        else {
            this.ipAddress = ipAddress;
            this.mask = mask;
            this.network = network;
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Integer getMask() {
        return mask;
    }

    public void setMask(Integer mask) {
        this.mask = mask;
    }
    public List<Networks> getNetwork() {
        return network;
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
                     SubNetworks subNetworks1 = new SubNetworks(z, net);
                     subNetworks.add(subNetworks1);
                     break;
                 }
             }
         }
         return subNetworks;
     }


    @Override
    public String toString () {
        return "IP ADDRESS: " + ipAddress +
                "/" + mask;
    }
}
