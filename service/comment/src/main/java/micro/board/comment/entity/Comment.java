package micro.board.comment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "comment")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

	@Id
	private Long commentId;
	private String content;
	private Long articleId; // shard key
	private Long parentCommentId;
	private Long writerId;
	private Boolean deleted;
	private LocalDateTime createdAt;

	public static Comment create(Long commentId, String content, Long articleId, Long parentCommentId, Long writerId){
		Comment comment = new Comment();
		comment.commentId = commentId;
		comment.content = content;
		comment.articleId = (articleId != null) ? articleId : 1L;
		comment.parentCommentId = parentCommentId == null ? commentId : parentCommentId;
		comment.writerId = writerId;
		comment.deleted = false;
		comment.createdAt = LocalDateTime.now();
		return comment;
	}

	public boolean isRoot(){
		return parentCommentId.longValue() == commentId;
	}

	public void delete(){
		deleted = true;
	}
}
