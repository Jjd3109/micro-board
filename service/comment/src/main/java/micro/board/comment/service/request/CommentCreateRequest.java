package micro.board.comment.service.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateRequest {
	private String content;
	private Long articleId; // shard key
	private Long parentCommentId;
	private Long writerId;

}
