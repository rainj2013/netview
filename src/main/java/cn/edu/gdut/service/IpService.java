package cn.edu.gdut.service;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.edu.gdut.bean.IpAddress;


@IocBean
public class IpService {

    @Inject
    private NutDao dao;

    public void add(IpAddress IpAddress) {
        dao.insert(IpAddress);
    }

    public IpAddress find(String host) {
        return dao.fetch(IpAddress.class, Cnd.where("host", "=", host));
    }

    public void update(IpAddress IpAddress) {
        dao.update(IpAddress);
    }

    public void delete(IpAddress IpAddress) {
        dao.delete(IpAddress);
    }
}
