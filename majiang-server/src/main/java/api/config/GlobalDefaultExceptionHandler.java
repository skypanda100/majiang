package api.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	private Logger mLog = Logger.getLogger(this.getClass());

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public String defaultExceptionHandler(HttpServletRequest request, Exception e) {
		e.printStackTrace();
		mLog.error(e.getMessage());
		return "服务器繁忙，请稍后再试！";
	}
}
