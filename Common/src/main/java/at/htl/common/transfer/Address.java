package at.htl.common.transfer;

import java.io.Serializable;

/**
 * @timeline Address
 * 21.06.2016: MET 005  created class, implementation
 */
public class Address implements Serializable {

    private String ip;
    private int port;

    public Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return String.format("%s/%d", ip, port);
    }
}
