package cn.edu.gdut.action;

import javax.servlet.http.HttpSession;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Lang;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import cn.edu.gdut.service.ManagerService;

@IocBean
public class LoginAction {
	
	@Inject
	private ManagerService managerService;
	
	@At
	@POST
	@Ok("json")
	public Object login(@Param("password") String password,HttpSession session){
		if(Lang.md5(password).equals(managerService.getPassword())){
			session.setAttribute("login", true);
			return true;
		}else
			return false;
	}
	
	@At
	@GET
	@Ok("jsp:/login.jsp")
	public void login(){}
}
