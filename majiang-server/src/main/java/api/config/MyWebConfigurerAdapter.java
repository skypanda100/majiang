package api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**   
 * @ClassName:  MYWebAdapter   
 * @Description: 注入拦截器
 */
@Configuration
public class MyWebConfigurerAdapter extends WebMvcConfigurerAdapter  {
	
	@Bean // 注入定义拦截器
	public HandlerInterceptor getMyInterceptor() {
		return new URLInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 多个拦截器组成一个拦截器链
		registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("PUT", "DELETE", "GET", "POST", "OPTIONS")
        .allowedHeaders("*")
        .exposedHeaders("access-control-allow-headers",
                "access-control-allow-methods",
                "access-control-allow-origin",
                "access-control-max-age",
                "X-Frame-Options")
        .allowCredentials(false).maxAge(3600);
    }
}