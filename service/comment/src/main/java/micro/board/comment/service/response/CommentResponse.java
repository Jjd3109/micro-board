package micro.board.comment.service.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;
import micro.board.comment.entity.Comment;

@Getter
@ToString
public class CommentResponse {

	private Long commentId;
	private String content;
	private Long articleId; // shard key
	private Long parentCommentId;
	private Long writerId;
	private Boolean deleted;
	private LocalDateTime createdAt;

	public static CommentResponse from(Comment comment){
		CommentResponse commentResponse = new CommentResponse();
		commentResponse.commentId = comment.getCommentId();
		commentResponse.content = comment.getContent();
		commentResponse.articleId = comment.getArticleId();
		commentResponse.parentCommentId = comment.getParentCommentId();
		commentResponse.writerId = comment.getWriterId();
		commentResponse.deleted = comment.getDeleted();
		commentResponse.createdAt = comment.getCreatedAt();

		return commentResponse;
	}
}
