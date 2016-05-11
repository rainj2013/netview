package cn.edu.gdut;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
				service.ping();
			}
		}, 0, 20*1000);
		
		//清理日志任务
		clearLogTimer = new Timer("clearLog");
		final String time = "00:00:00";  
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + time); 
		Date startTime = null;
		try {
			startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	
		clearLogTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				service.clearLog();
			}
		}, startTime, 24*3600*1000);
		
	}
	
}
