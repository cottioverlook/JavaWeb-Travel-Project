package top.potatohub.ctrip.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.potatohub.ctrip.backend.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Value("${login.validate:true}")
    private boolean loginValidate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (loginValidate) {
            registry.addInterceptor(loginInterceptor)
                    .addPathPatterns("/**")  // Intercept all paths
                    .excludePathPatterns(
                            "/auth/**",  // Exclude all auth related paths
                            "/alipay/**",
                            "/Weixin%20Image_20251218164216.png",
                            "/static/**",
                            "/attractions/**",
                            "/scenery/**",
                            "/api/hotels/**",
                            "/api/flights/**",
                            "/api/trains/**",
                            "/api/locations/**",
                            "/api/reviews/product/**",
                            "/error"
                    );
        }
    }
}