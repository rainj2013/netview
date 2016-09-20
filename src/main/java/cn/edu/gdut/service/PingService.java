package cn.edu.gdut.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.gdut.util.PingUtil;
import org.apache.commons.mail.HtmlEmail;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Times;

import cn.edu.gdut.bean.IpAddress;
import cn.edu.gdut.util.DateUtil;

@IocBean
public class PingService {
    @Inject
    private NutDao dao;
    @Inject("refer:$ioc")
    protected Ioc ioc;

    List<IpAddress> mailIps = new ArrayList<>();

    public List<IpAddress> view() {
        List<IpAddress> list = dao.query(IpAddress.class, Cnd.where(null));
        Collections.sort(list, new Comparator<IpAddress>() {
            @Override
            public int compare(IpAddress o1, IpAddress o2) {
                if (o1.getStatus() == false && o2.getStatus())
                    return -1;
                else if (o1.getStatus() && o2.getStatus() == false)
                    return 1;
                return 0;
            }
        });
        return list;
    }

    //日志太长的话清理一下，取后面两条就好了
    public void clearLog() {
        List<IpAddress> list = dao.query(IpAddress.class, Cnd.where(null));
        for (IpAddress ipAddress : list) {
            String[] logs = ipAddress.getLog().split("[|]");
            if (logs.length >= 4)
                ipAddress.setLog(logs[logs.length-2] + logs[logs.length-1]);
            dao.update(ipAddress);
        }
    }

    public void ping() {
        List<IpAddress> list = dao.query(IpAddress.class, Cnd.where(null));

        List<IpAddress> subList;
        int count = list.size();
        int page = 30;
        int pageEnd;
        //分页ping，一次性ping会很多丢包，网卡好像承受不了那么多连接
        for (int i = 0; i < count; i += page) {
            pageEnd = i + page > count ? count : i + page;
            subList = list.subList(i, pageEnd);
            ping(subList);
        }

        for (IpAddress ipAddress : list) {
            //获取初始状态
            boolean status = ipAddress.getStatus();

            //统计ping失败次数,如果成功就重置状态
            if (ipAddress.getOk()) {
                ipAddress.setCount(0);
                ipAddress.setWarn(false);
            }else
                ipAddress.setCount(ipAddress.getCount() + 1);

            //如果超过三次ping失败，就将状态设置为false
            if (ipAddress.getCount() >= 3)
                ipAddress.setStatus(false);
            else
                ipAddress.setStatus(true);

            //日志处理，通过前后状态对比判断中断/恢复
            if ((status != ipAddress.getStatus()) && status) {
                Date date = new Date();
                ipAddress.setLog(ipAddress.getLog() + "|    中断时间：" + Times.sDT(date));
                ipAddress.setInterruptTime(date);
                ipAddress.setWarn(true);
            } else if ((status != ipAddress.getStatus()) && !status) {
                ipAddress.setLog(ipAddress.getLog() + "|    恢复时间：" + Times.sDT(new Date()));
                ipAddress.setWarn(false);
            }

            //添加到邮件列表
            if (ipAddress.isWarn()) {
                double minutes = DateUtil.minDiff(new Date(), ipAddress.getInterruptTime());
                if (minutes >= 30) {
                    mailIps.add(ipAddress);
                    ipAddress.setWarn(false);//发完就设置一下警告状态，下次不发了
                }
            }
            //发送邮件
            if (mailIps.size() != 0) {
                sendMail(mailIps);
                mailIps.clear();
            }

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

        public void run() {
            String host = ipAddress.getHost();
            boolean ok = false;
            try {
                ok = InetAddress.getByName(host.trim()).isReachable(timeOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //如果上面这种ping不成功就尝试直接调用windows下的cmd
            if(!ok){
                ok = PingUtil.ping(host.trim(),2,timeOut);
            }
            ipAddress.setOk(ok);
        }
    }

    private void sendMail(List<IpAddress> ips) {
        List<Record> emails = dao.query("emails", null);
        if (emails == null || emails.size() == 0)
            return;
        try {
            HtmlEmail sendEmail = ioc.get(HtmlEmail.class);
            sendEmail.setSubject("交换机故障报警");
            String msg = "以下交换机长时间不通：<br>";
            for (IpAddress ip : ips) {
                msg += ip.getAddress() + " IP:" + ip.getHost() + "    <br>";
            }
            sendEmail.setMsg(msg);
            sendEmail.setCharset("UTF-8");
            for (Record record : emails) {
                sendEmail.addTo(record.getString("email"));
            }
            sendEmail.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //备用的一个方法
    private void sendMail(List<String> emails, IpAddress ipAddress) {
        try {
            HtmlEmail sendEmail = ioc.get(HtmlEmail.class);
            sendEmail.setSubject("交换机故障报警");
            sendEmail.setMsg("故障交换机" + ipAddress.getAddress() + " IP:" + ipAddress.getHost());
            sendEmail.setCharset("UTF-8");
            for (String email : emails) {
                sendEmail.addTo(email);
            }

            sendEmail.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        List<IpAddress> list = dao.query(IpAddress.class, null);
        for (IpAddress ip : list) {
            ip.setCount(0);
            ip.setLog("");
            ip.setStatus(true);
            ip.setWarn(false);
            dao.update(ip);
        }
    }
}
