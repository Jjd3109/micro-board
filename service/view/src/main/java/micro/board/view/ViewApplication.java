package micro.board.view;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "micro.board")
@EnableJpaRepositories(basePackages = "micro.board")
public class ViewApplication {
	public static void main(String[] args) {
		SpringApplication.run(ViewApplication.class, args);
	}
}
