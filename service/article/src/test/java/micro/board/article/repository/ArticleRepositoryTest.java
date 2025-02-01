package micro.board.article.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import micro.board.article.entity.Article;

@SpringBootTest
@Slf4j
class ArticleRepositoryTest {

	@Autowired
	ArticleRepository articleRepository;

	@Test
	void findAllTest(){
		List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);
		log.info("articles 사이즈 : " + articles.size());

		for(Article article : articles){
			log.info("article 값 : " + article);
		}
	}

	@Test
	void countTest(){
		Long count = articleRepository.count(1L, 30L);
		log.info("count 값 : " + count);
	}


}