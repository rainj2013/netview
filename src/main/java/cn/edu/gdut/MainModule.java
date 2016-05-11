// 				_ooOoo_ 
// 		 	 o8888888o 
// 	  	  	      88" . "88 
//               	  (| -_- |) 
//		 		O\ = /O 
//             ____/`---*\____ 
//               . * \\| |// `. 
//             / \\||| : |||// \ 
//           / _||||| -:- |||||- \ 
//             | | \\\ - /// | | 
//            | \_| **\---/** | | 
//           \  .-\__ `-` ___/-. / 
//            ___`. .* /--.--\ `. . __ 
//        ."" *< `.___\_<|>_/___.* >*"". 
//      | | : `- \`.;`\ _ /`;.`/ - ` : | | 
//         \ \ `-. \_ __\ /__ _/ .-` / / 
//======`-.____`-.___\_____/___.-`____.-*=======
// 			     佛祖保佑，程序无BUG
//............................................... 
package cn.edu.gdut;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@Modules(scanPackage=true)
@Encoding(input = "UTF-8", output = "UTF-8")
@IocBy(type=ComboIocProvider.class, args={"*org.nutz.ioc.loader.json.JsonLoader", "dao.js",
	"*org.nutz.ioc.loader.annotation.AnnotationIocLoader",
	"cn.edu.gdut","ioc/"})
@SetupBy(AppContext.class)
public class MainModule {
	@At("hello")
	public boolean doHello() {
		System.out.println("hello,World");
		return true;
	}
}
