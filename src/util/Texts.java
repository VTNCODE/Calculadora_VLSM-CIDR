package util;

import services.SubNetworks;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Texts {

    String path;
    SubNetworks subNetworks;

    public Texts(String path, SubNetworks subNetworks) {
        this.path = path;
        this.subNetworks = subNetworks;
    }

    public void fileText() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("The network " + subNetworks.getIpAddress() + " has "+subNetworks.totalHosts(subNetworks.getMask()) + " hosts.");
            bw.newLine();
            bw.write("Your subnets need " + subNetworks.usedHosts() + " hosts.");
            bw.newLine();
            for (int i = 0; i < subNetworks.getNetwork().size(); i++) {
                if (subNetworks.addressMayNotFit(subNetworks.networkAddress().get(i).getIpAddress())) {
                    bw.write("WARNING! It looks like this subnet won't fit into your network.");
                }
                bw.newLine();
                bw.write("Name: " + subNetworks.getNetwork().get(i).getName());
                bw.newLine();
                bw.write("Hosts needed: " + subNetworks.getNetwork().get(i).getHosts());
                bw.newLine();
                bw.write("Hosts Available: " + subNetworks.availableHosts().get(i).getHosts());
                bw.newLine();
                bw.write("Unused Hosts: " + subNetworks.nonUsedHosts().get(i).getHosts());
                bw.newLine();
                bw.write("Network Address: " + subNetworks.networkAddress().get(i).getIpAddress());
                bw.newLine();
                bw.write("Slash: /" + subNetworks.prefix().get(i).getMask());
                bw.newLine();
                bw.write("Mask: " + subNetworks.mask().get(i).getIpAddress());
                bw.newLine();
                bw.write("Usable Range: " + subNetworks.usableRangeFirstAddress().get(i).getIpAddress() + " - " + subNetworks.usableRangeFinalAddress().get(i).getIpAddress());
                bw.newLine();
                bw.write("Broadcast: " + subNetworks.broadCast().get(i).getIpAddress());
                bw.newLine();
                bw.write("WildCard: " + subNetworks.wildCardMask().get(i).getIpAddress());
                bw.newLine();
                bw.newLine();
            }

        }
        catch (IOException e) {
            System.out.println("Error while writing: " + e.getMessage());
        }
    }

}
