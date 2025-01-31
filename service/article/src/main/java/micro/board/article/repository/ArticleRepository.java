package micro.board.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import micro.board.article.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
