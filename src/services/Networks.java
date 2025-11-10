package services;

public class Networks implements Comparable<Networks> {
    private final String name;
    private Integer hosts;

    public Networks(String name) {
        this.name = name;
    }

    public Networks(String name, Integer hosts) {
        this.name = name;
        this.hosts = hosts;
    }

    public String getName() {
        return name;
    }

    public Integer getHosts() {
        return hosts;
    }

    @Override
    public String toString() {
        return "Network: " + name + " " + "Hosts: " + hosts;
    }

    @Override
    public int compareTo(Networks o) {
        return o.getHosts() - this.hosts;
    }
}

