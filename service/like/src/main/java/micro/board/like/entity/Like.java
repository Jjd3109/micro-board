package micro.board.like.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "article_like")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

	@Id
	private Long articleLikeId;
	private Long articleId;
	private Long userId;
	private LocalDateTime createdAt;

	public static Like create(Long articleLikeId, Long articleId, Long userId, LocalDateTime createdAt) {
		Like like = new Like();
		like.articleLikeId = articleLikeId;
		like.articleId = articleId;
		like.userId = userId;
		like.createdAt = createdAt;
		return like;
	}
}
