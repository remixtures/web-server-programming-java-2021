package calculations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CalculationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculationsApplication.class, args);
    }
}
