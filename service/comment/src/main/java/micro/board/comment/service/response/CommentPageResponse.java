package micro.board.comment.service.response;

import java.util.List;

import lombok.Getter;

@Getter
public class CommentPageResponse {

	private List<CommentResponse> commentResponseList;
	private Long commentCount;

	public static CommentPageResponse of(List<CommentResponse> commentResponseList, Long commentCount){
		CommentPageResponse commentPageResponse = new CommentPageResponse();
		commentPageResponse.commentResponseList = commentResponseList;
		commentPageResponse.commentCount = commentCount;
		return commentPageResponse;
	}
}
