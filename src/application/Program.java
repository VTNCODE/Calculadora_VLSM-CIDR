package application;

import services.Networks;
import services.SubNetworks;
import util.Screen;
import util.Texts;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main (String[]args){

        Scanner sc = new Scanner(System.in);
        List<Networks> networks = new ArrayList<>();

        Screen.resetScreen();

        System.out.print("Enter de ip Address: ");
        String ipAddress = sc.nextLine();
        System.out.print("Enter the mask: ");
        Integer mask = sc.nextInt();


        System.out.print("Enter the number of hosts: ");
        int subNetworks = sc.nextInt();
        System.out.println();

        for (int i = 0; i < subNetworks; i++) {
            System.out.printf("Name of sub-network %d: ", i + 1);
            sc.nextLine();
            String name = sc.nextLine();
            System.out.printf("Number of hosts %d: ", i + 1);
            Integer hosts = sc.nextInt();
            networks.add(new Networks(name, hosts));
        }
        SubNetworks network = new SubNetworks(ipAddress, mask, networks);

        System.out.println();
        System.out.println("Subnet Total Hosts: " + network.totalHosts(mask));
        System.out.println("Subnet hosts needed: " + network.usedHosts());
        System.out.println();
        network.impressResults();

        Texts texts = new Texts("C:\\Users\\Desktop\\Desktop\\Calculadora_VLSM-CIDR-main", network);
        texts.fileText();

        sc.close();
    }

}
