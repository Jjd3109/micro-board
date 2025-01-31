package micro.board.article.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import micro.board.article.entity.Article;
import micro.board.article.repository.ArticleRepository;
import micro.board.article.service.request.ArticleCreateRequest;
import micro.board.article.service.request.ArticleUpdateRequest;
import micro.board.article.service.response.ArticleResponse;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArticleService {
	private final Snowflake snowflake = new Snowflake();
	private final ArticleRepository articleRepository;


	@Transactional
	public ArticleResponse create(ArticleCreateRequest request){
		Article article = articleRepository.save(
			Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(),
				request.getWriterId())
		);

		return ArticleResponse.from(article);
	}

	@Transactional
	public ArticleResponse update(Long articleId, ArticleUpdateRequest request){
		Article article = articleRepository.findById(articleId).orElseThrow();
		article.update(request.getTitle(), request.getContent());
		return ArticleResponse.from(article);

	}

	public ArticleResponse read(Long artcleId){
		return ArticleResponse.from(articleRepository.findById(artcleId).orElseThrow());
	}

	@Transactional
	public void delete(Long articleId){
		articleRepository.deleteById(articleId);
	}

}
