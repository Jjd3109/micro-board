package micro.board.like.service.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;
import micro.board.like.entity.Like;

@Getter
@ToString
public class LikeResponse {

	private Long articleLikeId;
	private Long articleId;
	private Long userId;
	private LocalDateTime createdAt;

	public static LikeResponse from(Like like) {
		LikeResponse likeResponse = new LikeResponse();
		likeResponse.articleLikeId = like.getArticleLikeId();
		likeResponse.articleId = like.getArticleId();
		likeResponse.userId = like.getUserId();
		likeResponse.createdAt = like.getCreatedAt();
		return likeResponse;
	}

}
