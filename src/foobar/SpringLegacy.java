package foobar;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
    
@RestController
@SpringBootApplication
/**
 * example of injecting a non-spring object into a spring application, eg to support a legacy application
 * https://stackoverflow.com/questions/47911145/injecting-singletons-in-to-a-spring-boot-application-context
 */
public class SpringLegacy {
    public SpringLegacy() {}

    @Lazy
    @Autowired
    @Qualifier("legacyBean")
    Object myBean;
    
    @RequestMapping("/dir")
    public Object dir() {
        return "hello world " + myBean;
    }
    
    static class LegacyObject {}

    public static void main(final String[] args) throws Exception {
        Object obj = new LegacyObject();
        SpringApplication app = new SpringApplication(SpringLegacy.class);
        ApplicationListener<ApplicationContextEvent> lis = new ApplicationListener() {
            boolean first = true;
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                if (first & event instanceof ContextRefreshedEvent) {
                    ((GenericApplicationContext) (((ContextRefreshedEvent) event).getApplicationContext())).
                            getBeanFactory().registerSingleton("legacyBean", obj);
                    first = false;
                }
            }
        };
        app.setListeners(Arrays.asList(lis));
        app.run(args);
    }
}


