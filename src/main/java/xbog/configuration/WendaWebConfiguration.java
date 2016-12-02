package xbog.configuration;

import xbog.interceptor.LoginRequiredInterceptor;
import xbog.interceptor.PassportInterceptor;
import xbog.interceptor.QuestionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;
    //下面的loginRequiredInterceptor用到了passportInterceptor中hostholder，所以先注册
    @Autowired
    QuestionInterceptor questionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
        registry.addInterceptor(questionInterceptor).addPathPatterns("/questionForm");
        super.addInterceptors(registry);
    }
}
