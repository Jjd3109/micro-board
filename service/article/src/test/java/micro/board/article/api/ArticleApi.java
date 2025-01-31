package micro.board.article.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import micro.board.article.service.response.ArticleResponse;


public class ArticleApi {

	RestClient restClient = RestClient.create("http://localhost:9000");

	@Test
	void createTest(){
		//given
		ArticleResponse articleResponse = create(new ArticleCreateRequest("h1", "my content", 1L, 1L));
		//when

		//then
		System.out.println("response = " + articleResponse);
	}

	@Test
	void readTest(){
		//given
		ArticleResponse articleResponse = read(143598649147011072L);

		//when

		//then
		System.out.println("response = " + articleResponse);
	}

	@Test
	void articleUpdateTest(){
		//given
		ArticleResponse articleResponse = update(143598649147011072L ,new ArticleUpdateRequest("바꾸기", "테스트"));

		//when

		//then
		System.out.println("response = " + articleResponse);

	}

	@Test
	void articleDeleteTest(){
		//given
		delete(143598649147011072L);
		//when

		//then
	}

	ArticleResponse create(ArticleCreateRequest articleCreateRequest){
		return restClient.post()
			.uri("/v1/articles")
			.body(articleCreateRequest)
			.retrieve()
			.body(ArticleResponse.class);
	}

	ArticleResponse read(Long articleId){
		return restClient.get()
			.uri("/v1/articles/{articleId}", articleId)
			.retrieve()
			.body(ArticleResponse.class);
	}

	ArticleResponse update(Long articleId, ArticleUpdateRequest articleUpdateRequest){
		return restClient.put()
			.uri("/v1/articles/{articleId}", articleId)
			.body(articleUpdateRequest)
			.retrieve()
			.body(ArticleResponse.class);
	}

	void delete(Long articleId){
		restClient.delete()
			.uri("/v1/articles/{articleId}", articleId)
			.retrieve()
			.body(ArticleResponse.class);
	}


	@Getter
	@AllArgsConstructor
	static class ArticleCreateRequest {
		private String title;
		private String content;
		private Long writerId;
		private Long boardId;
	}


	@Getter
	@AllArgsConstructor
	static class ArticleUpdateRequest {
		private String title;
		private String content;

	}



}
