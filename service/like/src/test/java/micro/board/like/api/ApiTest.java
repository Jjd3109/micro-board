package micro.board.like.api;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import jakarta.transaction.Transactional;
import micro.board.like.service.request.LikeCreateRequest;
import micro.board.like.service.response.LikeResponse;

public class ApiTest {

	RestClient restClient = RestClient.create("http://localhost:9002");

	@Test
	@DisplayName("생성 테스트")
	@Transactional
	void create(){
		//given
		LikeCreateRequest likeCreateRequest = new LikeCreateRequest(1L, 1L);

		//when

		LikeResponse body = restClient.post()
			.uri("/v1/article-likes/articles")
			.body(likeCreateRequest)
			.retrieve()
			.body(LikeResponse.class);


		//then

		assertThat(body.getArticleId()).isEqualTo(1l);
	}


	@Test
	@DisplayName("삭제 테스트")
	void delete(){
		//given
		LikeCreateRequest likeCreateRequest = new LikeCreateRequest(5L, 5L);

		//when

		LikeResponse body = restClient.post()
			.uri("/v1/article-likes/articles")
			.body(likeCreateRequest)
			.retrieve()
			.body(LikeResponse.class);

		System.out.println("Article ID: " + body.getArticleId());
		System.out.println("User ID: " + body.getUserId());


		restClient.delete()
			.uri("/v1/article-likes/articles/{articleId}/users/{userId}", body.getArticleId(), body.getUserId())
			.retrieve()
			.toBodilessEntity();


		//then


	}


}
