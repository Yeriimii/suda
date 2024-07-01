package yelly.suda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SudaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SudaApplication.class, args);
	}

}
