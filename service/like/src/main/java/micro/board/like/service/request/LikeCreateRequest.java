package micro.board.like.service.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class LikeCreateRequest {

	private Long articleId;
	private Long userId;
	private LocalDateTime createdAt;


}
