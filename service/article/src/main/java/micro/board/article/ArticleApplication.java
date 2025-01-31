package micro.board.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class ArticleApplication {
	public static void main(String[] args) {
		SpringApplication.run(ArticleApplication.class, args);
	}
}
