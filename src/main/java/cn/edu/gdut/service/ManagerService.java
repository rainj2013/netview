package cn.edu.gdut.service;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class ManagerService {
	@Inject
	private NutDao dao;
	
	public String getPassword(){
		return dao.query("password",null).get(0).getString("password");
	}
}
