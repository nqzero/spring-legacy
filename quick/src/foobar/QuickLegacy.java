package foobar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
    
@RestController
@SpringBootApplication
public class QuickLegacy {
    public QuickLegacy() {}
    
    LegacyData2 legacy;

    
    public static class LegacyData2 {}
    
    @RequestMapping("/dir")
    public Object dir() {
        return "hello world" + legacy;
    }

    public static void main(final String[] args) throws Exception {
        LegacyData2 obj = new LegacyData2();
        ConfigurableApplicationContext runner = SpringApplication.run(QuickLegacy.class,args);
        QuickLegacy quick = runner.getAutowireCapableBeanFactory().getBean("quickLegacy",QuickLegacy.class);
        quick.legacy = obj;
    }
}


