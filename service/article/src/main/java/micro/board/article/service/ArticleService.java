package micro.board.article.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import micro.board.article.entity.Article;
import micro.board.article.entity.BoardArticleCount;
import micro.board.article.repository.ArticleRepository;
import micro.board.article.repository.BoardArticleCountRepository;
import micro.board.article.service.request.ArticleCreateRequest;
import micro.board.article.service.request.ArticleUpdateRequest;
import micro.board.article.service.response.ArticlePageResponse;
import micro.board.article.service.response.ArticleResponse;

@Service
@RequiredArgsConstructor
@Log4j2
public class ArticleService {
	private final Snowflake snowflake = new Snowflake();
	private final ArticleRepository articleRepository;
	private final BoardArticleCountRepository boardArticleCountRepository;


	@Transactional
	public ArticleResponse create(ArticleCreateRequest request){
		Article article = articleRepository.save(
			Article.create(snowflake.nextId(), request.getTitle(), request.getContent(), request.getBoardId(),
				request.getWriterId())
		);

		int result = boardArticleCountRepository.increase(request.getBoardId());

		if(result == 0){
			boardArticleCountRepository.save(
				BoardArticleCount.init(request.getBoardId(), 1L)
			);
		}

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
		Article article = articleRepository.findById(articleId).orElseThrow();
		articleRepository.delete(article);
		boardArticleCountRepository.decrease(article.getBoardId());
	}

	public ArticlePageResponse readAll(Long boardId, Long page, Long pageSize){
		return ArticlePageResponse.of(
			articleRepository.findAll(boardId, (page-1) * pageSize, pageSize).stream()
				.map(ArticleResponse::from)
				.toList(),
			articleRepository.count(
				boardId,
				PageLimitCalculator.calculatePageLimit(page, pageSize, 10L)
			)
		);
	}

	public List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long limit, Long lastArticleId){
		List<Article> articles = lastArticleId == null ?
			articleRepository.findAllInfiniteScroll(boardId, limit) :
			articleRepository.findAllInfiniteScroll(boardId, limit, lastArticleId);

		return articles.stream().map(ArticleResponse::from).toList();
	}

	public Long count(Long boardId){
		return boardArticleCountRepository.findById(boardId)
			.map(BoardArticleCount::getArticleCount)
			.orElse(0L);

	}

}
