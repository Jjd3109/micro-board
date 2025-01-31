package micro.board.article.service.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;
import micro.board.article.entity.Article;

@Getter
@ToString
public class ArticleResponse {

	private Long articleId;
	private String title;
	private String content;
	private Long boardId; // shard key
	private Long writerId;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public static ArticleResponse from(Article article){
		ArticleResponse respones = new ArticleResponse();
		respones.articleId = article.getArticleId();
		respones.title = article.getTitle();
		respones.content = article.getContent();
		respones.boardId = article.getBoardId();;
		respones.writerId = article.getWriterId();
		respones.createdAt = article.getCreatedAt();
		respones.modifiedAt = article.getModifiedAt();
		return respones;
	}
}
