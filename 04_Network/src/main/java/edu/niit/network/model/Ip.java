package edu.niit.network.model;

import java.io.Serializable;

public class Ip implements Serializable {
    private int code;
    private IpData data;


    public Ip(int code, IpData data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public IpData getData() {
        return data;
    }

    public void setData(IpData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Ip{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
