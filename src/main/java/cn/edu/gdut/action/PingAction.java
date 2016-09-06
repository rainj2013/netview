package cn.edu.gdut.action;

import java.util.List;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.filter.CheckSession;

import cn.edu.gdut.bean.IpAddress;
import cn.edu.gdut.service.PingService;

@IocBean
@Filters(@By(type = CheckSession.class, args = {"login", "/login.jsp"}))
public class PingAction {

    @Inject
    private PingService pingService;

    @At
    @Ok("jsp:ping")
    public List<IpAddress> view() {
        return pingService.view();
    }

    @At
    public void init() {
        pingService.init();
    }
}
