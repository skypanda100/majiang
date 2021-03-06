package api.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName: URLInterceptor
 * @Description: 请求拦截器
 */
public class URLInterceptor implements HandlerInterceptor {
	private Logger mLog = Logger.getLogger(this.getClass());

	// 在请求处理之前进行调用（Controller方法调用之前）
	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
			throws Exception {
		getIpAddress(httpServletRequest);
		return true;
	}

	// 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
	}

	// 在整个请求结束之后被调用
	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {
	}

	/**
	 * 获取请求地址，
	 */
	public void getIpAddress(HttpServletRequest request) {
		mLog.info(request.getRemoteAddr() + "_" + request.getHeader("User-Agent") + "_" + request.getRequestURL());
	}
}