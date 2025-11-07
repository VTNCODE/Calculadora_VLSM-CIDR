package services;

public class Networks {
    private String name;
    private Integer hosts;

    public Networks() {
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHosts() {
        return hosts;
    }

    public void setHosts(Integer hosts) {
        this.hosts = hosts;
    }

    @Override
    public String toString() {
        return "Network: " + name + " " + "Hosts: " + hosts;
    }
}

