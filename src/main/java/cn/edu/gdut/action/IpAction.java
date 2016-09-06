package cn.edu.gdut.action;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;

import cn.edu.gdut.bean.IpAddress;
import cn.edu.gdut.service.IpService;

@IocBean
@At("ip")
@Filters(@By(type = CheckSession.class, args = {"login", "/login.jsp"}))
public class IpAction {
    @Inject
    private IpService ipService;

    @At
    @Ok(">>:/view")
    public void delete(@Param("host") String host) {
        ipService.delete(ipService.find(host));
    }

    @At
    @Ok(">>:/view")
    public void add(@Param("address") String address, @Param("host") String host) {
        IpAddress ipAddress = new IpAddress(address, host);
        ipAddress.setCount(0);
        ipAddress.setLog("");
        ipAddress.setStatus(true);
        ipService.add(ipAddress);
    }
}
