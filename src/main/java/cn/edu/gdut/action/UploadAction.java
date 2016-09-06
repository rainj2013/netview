package cn.edu.gdut.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import cn.edu.gdut.bean.IpAddress;
import cn.edu.gdut.service.IpService;
import cn.edu.gdut.util.XlsUtil;
import jxl.read.biff.BiffException;

@IocBean
@Filters(@By(type = CheckSession.class, args = {"login", "/login.jsp"}))
public class UploadAction {

    @Inject
    private IpService ipService;

    @At("upload")
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:myUpload"})
    @Ok(">>:/view")
    public void upload(@Param("xls") TempFile tf) throws ServletException {
        try {
            List<String[]> list = XlsUtil.readExcel(tf.getInputStream());
            for (String[] strs : list) {
                IpAddress ipAddress = new IpAddress(strs[0], strs[1]);
                ipAddress.setCount(0);
                ipAddress.setLog("");
                ipAddress.setStatus(true);
                ipService.add(ipAddress);
            }

        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

    }
}
