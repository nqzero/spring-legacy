package foobar;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
        // https://fraaargh.wordpress.com/2013/10/24/add-an-existing-beanobject-to-a-spring-applicationcontext-using-javaconfig/
        // ApplicationContextAware
        // https://stackoverflow.com/questions/9657961/best-practise-of-injecting-applicationcontext-in-spring3
        
        
        DummyObject obj = new DummyObject();
        DummyObject other = new DummyObject();
        SpringApplication app = new SpringApplication(SpringLegacy.class);
        boolean stuff = false;
        if (stuff)
            SpringApplication.run(SpringLegacy.class, args);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ConfigurableApplicationContext c2 = null;
        context.getBeanFactory().registerSingleton("myBean", obj);
        ApplicationListener<ApplicationContextEvent> lis = new ApplicationListener() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                if (event instanceof ContextRefreshedEvent) {
                    ApplicationContext c3 = ((ContextRefreshedEvent) event).getApplicationContext();
                    GenericApplicationContext c4 = (GenericApplicationContext) c3;
                    c4.getBeanFactory().registerSingleton("myBean", obj);
                    c4.getBeanFactory().registerSingleton("otherBean", other);
                    System.out.println(c3);
                }
            }
        };

        // https://stackoverflow.com/questions/8686507/how-to-add-a-hook-to-the-application-context-initialization-event
        // https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2


        // recent question asking exactly this:
        // https://stackoverflow.com/questions/47911145/injecting-singletons-in-to-a-spring-boot-application-context

        // legacy apps:
        // http://www.captaindebug.com/2012/03/integrating-spring-into-legacy.html#.Wl2hL3WnHSU
        
        app.setListeners(Arrays.asList(lis));
        app.run(args);
//        app.setApplicationContextClass(context);
//        context.register(SpringMatter.class);
//        context.refresh();
//        System.out.println(context.getBean("myBean"));
//        new SpringApplicationBuilder(SpringMatter.class)
//                        .properties("foo=bar").run(args);
    }
}
