package services;


import java.util.ArrayList;
import java.util.List;

public class SubNetworks {
    private String ipAddress;
    private Integer mask;
    private List<Networks> network = new ArrayList<>();

    public SubNetworks() {
    }

    public SubNetworks(String ipAddress, Integer mask) {
        this.ipAddress = ipAddress;
        this.mask = mask;
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

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
    public Integer necessaryHosts() {
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
}
