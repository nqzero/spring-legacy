package foobar;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
    
@RestController
@ResponseBody
@SpringBootApplication
public class SpringLegacy {
    public SpringLegacy() {}

    @Lazy
    @Autowired
    @Qualifier("myBean")
    Object myBean;
    
    @RequestMapping("/dir")
    public Object dir() {
        return "hello world " + myBean;
    }

    public static void main(final String[] args) throws Exception {
        Object obj = new Object();
        SpringApplication app = new SpringApplication(SpringLegacy.class);
        ApplicationListener<ApplicationContextEvent> lis = new ApplicationListener() {
            boolean first = true;
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                if (first & event instanceof ContextRefreshedEvent) {
                    ((GenericApplicationContext) (((ContextRefreshedEvent) event).getApplicationContext())).
                            getBeanFactory().registerSingleton("myBean", obj);
                    first = false;
                }
            }
        };
        app.setListeners(Arrays.asList(lis));
        app.run(args);
    }
}


// recent question asking exactly this:
// https://stackoverflow.com/questions/47911145/injecting-singletons-in-to-a-spring-boot-application-context
