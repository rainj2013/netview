package cn.edu.gdut.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;

import cn.edu.gdut.bean.IpAddress;

@IocBean
public class PingService {
	@Inject
	private NutDao dao;
	
	public List<IpAddress> view(){
		List<IpAddress> list =  dao.query(IpAddress.class, Cnd.where(null));
		Collections.sort(list, new Comparator<IpAddress>() {

			@Override
			public int compare(IpAddress o1, IpAddress o2) {
				if(o1.getStatus()==false&&o2.getStatus())
					return -1;
				else if(o1.getStatus()&&o2.getStatus()==false)
					return 1;
				else
					return 0;
			}
		});
		return list;
	}
	
	//日志太长的话清理一下，取后面两条就好了
	public void clearLog() {
		List<IpAddress> list =  dao.query(IpAddress.class, Cnd.where(null));
		for(IpAddress ipAddress: list){
			String[] logs = ipAddress.getLog().split("|");
			if(logs.length>=4)
				ipAddress.setLog(logs[2]+logs[3]);
			dao.update(ipAddress);
		}
	}
	
	public void ping(){
		List<IpAddress> list = dao.query(IpAddress.class, Cnd.where(null));
		
		List<IpAddress> subList;
		int count = list.size();
		int page = 30;
		int pageEnd;
		for(int i = 0;i<count;i+=page){
			pageEnd = i+page>count?count:i+page;
			subList = list.subList(i, pageEnd);
			ping(subList);
		}
		
		for(IpAddress ipAddress:list){
			//获取初始状态
			boolean status = ipAddress.getStatus();
		
			//统计ping失败次数,如果成功就重置ping失败次数
			if(ipAddress.getOk())
				ipAddress.setCount(0);
			//失败则增加ping失败次数
			else
				ipAddress.setCount(ipAddress.getCount()+1);
			
			//如果超过三次ping失败，就将状态设置为false
			if(ipAddress.getCount()>=3)
				ipAddress.setStatus(false);
			else
				ipAddress.setStatus(true);
			
			//日志处理，通过前后状态对比判断中断/恢复
			if((status!=(boolean)ipAddress.getStatus())&&(status))
				ipAddress.setLog(ipAddress.getLog()+"|    中断时间："+Times.sDT(new Date()));
			else if((status!=(boolean)ipAddress.getStatus())&&(!status))
				ipAddress.setLog(ipAddress.getLog()+"|    恢复时间："+Times.sDT(new Date()));
			
			dao.update(ipAddress);
		}
		
	}
	
	public void ping(List<IpAddress> ips) {
		ExecutorService service = Executors.newCachedThreadPool();
		for (IpAddress ipAddress : ips) {
			service.submit(new PingTask(ipAddress));
		}
		service.shutdown();
		while (!service.isTerminated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	class PingTask implements Runnable {
		int timeOut = 1500;
		
		private IpAddress ipAddress;
		public PingTask(IpAddress ipAddress) {
			super();
			this.ipAddress = ipAddress;
		}

		public void run(){
			String host = ipAddress.getHost();
			boolean ok = false;
			try {
				ok = InetAddress.getByName(host.trim()).isReachable(timeOut);
			} catch (UnknownHostException e) {
				System.out.println("地址：" + host + "错误！");
			} catch (IOException e) {
				System.out.println("地址：" + host + "连接失败！");
			}
			ipAddress.setOk(ok);
		}
	}
	
}
