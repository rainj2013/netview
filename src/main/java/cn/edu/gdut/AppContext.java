package cn.edu.gdut;

import java.util.Timer;
import java.util.TimerTask;

import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import cn.edu.gdut.service.PingService;

public class AppContext implements Setup{
	Timer pingTimer;
	Timer clearLogTimer;
	@Override
	public void destroy(NutConfig arg0) {
		pingTimer.cancel();
		clearLogTimer.cancel();
	}

	@Override
	public void init(NutConfig config) {
		final PingService service =	config.getIoc().getByType(PingService.class);
		
		//ping任务
		pingTimer = new Timer("ping");
		
		pingTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("ping");
				service.ping();
			}
		}, 0, 20*1000);
		
		//清理日志任务
		clearLogTimer = new Timer("clearLog");
	
		clearLogTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				service.clearLog();
			}
		}, 0, 12*3600*1000);
		
	}
	
}
