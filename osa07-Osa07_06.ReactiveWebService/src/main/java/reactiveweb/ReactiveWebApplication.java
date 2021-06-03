package reactiveweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveWebApplication.class, args);
        
        GreetingWebClient gwc = new GreetingWebClient();
        System.out.println(gwc.getResult());
    }
}
