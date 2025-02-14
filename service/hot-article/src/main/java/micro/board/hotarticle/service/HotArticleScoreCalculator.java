package micro.board.hotarticle.service;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import micro.board.hotarticle.repository.ArticleCommentCountRepository;
import micro.board.hotarticle.repository.ArticleLikeCountRepository;
import micro.board.hotarticle.repository.ArticleViewCountRepository;

@Component
@RequiredArgsConstructor
public class HotArticleScoreCalculator {

	private final ArticleLikeCountRepository articleLikeCountRepository;
	private final ArticleViewCountRepository articleViewCountRepository;
	private final ArticleCommentCountRepository articleCommentCountRepository;

	private static final long ARTICLE_LIKE_COUNT_WEIGHT = 3;
	private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 2;
	private static final long ARTICLE_VIEW_COUNT_WEIGHT = 1;

	public long calculate(Long articleId) {
		Long articleLikeCount = articleLikeCountRepository.read(articleId);
		Long articleViewCount = articleViewCountRepository.read(articleId);
		Long articleCommentCount = articleCommentCountRepository.read(articleId);

		return ARTICLE_LIKE_COUNT_WEIGHT * articleLikeCount
			+ ARTICLE_COMMENT_COUNT_WEIGHT * articleCommentCount
			+ ARTICLE_VIEW_COUNT_WEIGHT * articleViewCount;

	}
}
