package cn.edu.gdut.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.annotation.Table;

@Table(value = "ipaddress")
@PK( {"address", "host"} )
public class IpAddress {
	@Column
	private Integer count;
	@Column
	private String address;
	@Column
	private String host;
	@Column
	private Boolean status;
	@Column
	private String log;
	private Boolean Ok;
	
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