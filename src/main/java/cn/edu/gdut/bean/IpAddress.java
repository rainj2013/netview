package cn.edu.gdut.bean;

import java.util.Date;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table(value = "ipaddress")
public class IpAddress {
    @Column
    private Integer count;
    @Column
    private String address;
    @Name
    private String host;
    @Column
    private Boolean status;
    @Column
    private String log;
    @Column
    private boolean warn;
    @Column
    private Date interruptTime;
    private Boolean Ok;

    public Date getInterruptTime() {
        return interruptTime;
    }

    public void setInterruptTime(Date interruptTime) {
        this.interruptTime = interruptTime;
    }

    public boolean isWarn() {
        return warn;
    }

    public void setWarn(boolean warn) {
        this.warn = warn;
    }

    public IpAddress() {
        super();
    }

    public IpAddress(String address, String host) {
        super();
        this.address = address;
        this.host = host;
    }

    public Boolean getOk() {
        return Ok;
    }

    public void setOk(Boolean ok) {
        Ok = ok;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "IpAddress [count=" + count + ", address=" + address + ", host=" + host + ", status=" + status + ", log="
                + log + ", Ok=" + Ok + "]";
    }

}
