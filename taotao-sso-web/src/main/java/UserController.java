import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * 用户处理Controller
 * <p>Title: UserController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class UserController {
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkUserData(@PathVariable String param, @PathVariable Integer type) {
		TaotaoResult result = userService.checkData(param, type);
		return result;
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	public TaotaoResult regitster(TbUser user) {
		TaotaoResult result = userService.register(user);
		return result;
	}
	
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login (String username, String password, HttpServletRequest request, HttpServletResponse response) {
//		接受两个参数
//		调用service登录
		TaotaoResult result = userService.login(username, password);
		if(result.getStatus() == 200) {
//			从返回结果去token，写入cookie，cookie要跨域
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
		}

		return result;
	}
}
