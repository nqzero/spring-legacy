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

    @Lazy
    @Autowired
    @Qualifier("myBean")
    DummyObject myBean;
    
    @RequestMapping("/dir")
    public Object dir() {
        return "hello world " + myBean;
    }

    public SpringLegacy() {}
    public static class DummyObject {}
    
    public static void main(final String[] args) throws Exception {
        
        
        DummyObject obj = new DummyObject();
        DummyObject other = new DummyObject();
        SpringApplication app = new SpringApplication(SpringLegacy.class);
        ApplicationListener<ApplicationContextEvent> lis = new ApplicationListener() {
            boolean first = true;
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                if (first & event instanceof ContextRefreshedEvent) {
                    ApplicationContext c3 = ((ContextRefreshedEvent) event).getApplicationContext();
                    GenericApplicationContext c4 = (GenericApplicationContext) c3;
                    c4.getBeanFactory().registerSingleton("myBean", obj);
                    c4.getBeanFactory().registerSingleton("otherBean", other);
                    System.out.println(c3);
                    first = false;
                }
            }
        };
        app.setListeners(Arrays.asList(lis));
        app.run(args);

        
    }
}


// application events:
// https://stackoverflow.com/questions/8686507/how-to-add-a-hook-to-the-application-context-initialization-event
// https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2


// recent question asking exactly this:
// https://stackoverflow.com/questions/47911145/injecting-singletons-in-to-a-spring-boot-application-context

// legacy apps, ie registerSingleton:
// http://www.captaindebug.com/2012/03/integrating-spring-into-legacy.html#.Wl2hL3WnHSU
