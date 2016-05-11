package cn.edu.gdut.action;

import java.util.List;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import cn.edu.gdut.bean.IpAddress;
import cn.edu.gdut.service.PingService;

@IocBean
public class PingAction {

	@Inject
	private PingService pingService;
	
	@At
	@Ok("jsp:ping")
	public List<IpAddress> view(){
		return pingService.view();
	}
	
	/*@At
	public void ping(){
		pingService.ping();
	}*/
}
