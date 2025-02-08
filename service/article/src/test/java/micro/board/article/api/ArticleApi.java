package micro.board.article.api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import micro.board.article.entity.Article;
import micro.board.article.service.response.ArticlePageResponse;
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
	void readAlltest(){
		//given
		ArticlePageResponse articlePageResponse = readAll(1L, 30L, 10L);

		//when

		//then
		System.out.println("response = " + articlePageResponse);
	}

	@Test
	void readAllInfiniteScroll(){
		//given
		List<ArticleResponse> articleResponse = readAllInfiniteScroll(1L, 5L);
		Long lastArticleId = articleResponse.getLast().getArticleId();
		List<ArticleResponse> articleResponses = readAllInfiniteScroll(1L, 5L, lastArticleId);

		//when

		//then
		System.out.println("첫번째 시작");
		for(ArticleResponse articleResponse1 : articleResponse){
			System.out.println(articleResponse1.getArticleId());
		}
		System.out.println("두번째 시작");
		for(ArticleResponse articleResponse2 : articleResponses){
			System.out.println(articleResponse2.getArticleId());
		}

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

	ArticlePageResponse readAll(Long boardId, Long page, Long pageSize){
		return restClient.get()
			.uri("/v1/articles?boardId={boardId}&page={page}&pageSize={pageSize}",boardId,page,pageSize)
			.retrieve()
			.body(ArticlePageResponse.class);
	}


	ArticleResponse update(Long articleId, ArticleUpdateRequest articleUpdateRequest){
		return restClient.put()
			.uri("/v1/articles/{articleId}", articleId)
			.body(articleUpdateRequest)
			.retrieve()
			.body(ArticleResponse.class);
	}


	List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long limit){
		return restClient.get()
			.uri("/v1/articles/infinite-scroll?boardId={boardId}&limit={limit}",boardId,limit)
			.retrieve()
			.body(new ParameterizedTypeReference<List<ArticleResponse>>() {
			});
	}

	List<ArticleResponse> readAllInfiniteScroll(Long boardId, Long limit, Long lastArticleId){
		return restClient.get()
			.uri("/v1/articles/infinite-scroll?boardId={boardId}&limit={limit}&lastArticleId={lastArticleId}",boardId,limit,lastArticleId)
			.retrieve()
			.body(new ParameterizedTypeReference<List<ArticleResponse>>() {
			});
	}

	void delete(Long articleId){
		restClient.delete()
			.uri("/v1/articles/{articleId}", articleId)
			.retrieve()
			.body(ArticleResponse.class);
	}

	@Test
	void countTest(){
		ArticleResponse articleResponse = create(new ArticleCreateRequest("h1", "content", 1L, 2L));

		Long count = restClient.get()
			.uri("/v1/articles/boards/{boardId}/count", articleResponse.getBoardId())
			.retrieve()
			.body(Long.class);

		restClient.delete()
			.uri("/v1/articles/{articleId}", articleResponse.getArticleId())
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
