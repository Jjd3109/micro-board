package micro.board.hotarticle.service.response;

import java.time.LocalDateTime;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.ToString;
import micro.board.hotarticle.client.ArticleClient;

@Getter
@ToString
public class HotArticleResponse {

	private Long articleId;
	private String title;
	private LocalDateTime createdAt;

	public static HotArticleResponse from(ArticleClient.ArticleResponse articleResponse){
		HotArticleResponse hotArticleResponse = new HotArticleResponse();
		hotArticleResponse.articleId = articleResponse.getArticleId();
		hotArticleResponse.title = articleResponse.getTitle();
		hotArticleResponse.createdAt = articleResponse.getCreatedAt();

		return hotArticleResponse;
	}
}
