package micro.board.comment.api;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import micro.board.comment.service.response.CommentPageResponse;
import micro.board.comment.service.response.CommentResponse;
import org.assertj.core.api.Assertions.*;
import org.testng.Assert;

@Slf4j
public class commentApiTest {

	RestClient restClient = RestClient.create("http://localhost:9001");

	@Test
	@DisplayName("댓글 생성")
	void create(){
		//given

		CommentResponse commentResponse1 = create(new CommentCreateRequest("content1", 1L, null, 1L));
		CommentResponse commentResponse2 = create(new CommentCreateRequest("content2", 1L,
			commentResponse1.getCommentId(), 1L));
		CommentResponse commentResponse3 = create(new CommentCreateRequest("content3", 1L,
			commentResponse1.getCommentId(), 1L));

		//when

		//then
		log.info("commentId : " + commentResponse1.getCommentId());
		log.info("\tcommentId : " + commentResponse2.getCommentId());
		log.info("\tcommentId : " + commentResponse3.getCommentId());
	}

	@Test
	@DisplayName("댓글 보기")
	void read(){
		//given
		CommentResponse read = read(144334239902371840L);

		//when

		//then
		System.out.println(read);
	}

	@Test
	@DisplayName("댓글 삭제")
	void delete(){

		//144334239902371840
		//144334240615403520
		//144334240686706688


		//given
		delete(144334239902371840L);
		CommentResponse read = read(144334239902371840L);

		System.out.println(read);
		//when


		//then


	}

	@Test
	void readAll(){
		CommentPageResponse commentPageResponse = restClient.get()
			.uri("/v1/comments?articleId=1&page=50000&pageSize=10")
			.retrieve()
			.body(CommentPageResponse.class);

		for(CommentResponse commentResponse : commentPageResponse.getCommentResponseList()){
			if(!commentResponse.getCommentId().equals(commentResponse.getParentCommentId())){
				System.out.print("\t");
			}
			System.out.println("comment.getCommentId = " + commentResponse.getCommentId());
		}
	}


	CommentResponse create(CommentCreateRequest commentCreateRequest){
		return restClient.post()
			.uri("/v1/comments")
			.body(commentCreateRequest)
			.retrieve()
			.body(CommentResponse.class);
	}

	CommentResponse read(Long commentId){
		return restClient.get()
			.uri("/v1/comments/{commentId}" ,commentId)
			.retrieve()
			.body(CommentResponse.class);
	}

	void delete(Long commentId){
		 restClient.delete()
			.uri("/v1/comments/{commentId}", commentId)
			.retrieve()
			 .toBodilessEntity();
	}



	@Getter
	@AllArgsConstructor
	public static class CommentCreateRequest {
		private String content;
		private Long articleId; // shard key
		private Long parentCommentId;
		private Long writerId;

	}
}
