package micro.board.like.api;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


	@Test
	void like(Long articleId, Long userId, String lockType){

		//given
		LikeCreateRequest likeCreateRequest = new LikeCreateRequest(articleId, userId);

		//when

		LikeResponse body = restClient.post()
			.uri("/v1/article-likes/articles/" + lockType)
			.body(likeCreateRequest)
			.retrieve()
			.body(LikeResponse.class);

		//then

	}

	@Test
	@DisplayName(value = "")
	void likePerformanceTest() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		likePerformanceTest(executorService, 1111L, "pessimistic-lock-1");
		likePerformanceTest(executorService, 2222L, "pessimistic-lock-2");
		likePerformanceTest(executorService, 3333L, "optimistic-lock");
	}


	void likePerformanceTest(ExecutorService executorService, Long articleId, String locktype) throws
		InterruptedException {

		CountDownLatch countDownLatch = new CountDownLatch(3000);
		System.out.println(locktype + "start");

		like(articleId, 1L, locktype);

		long start = System.nanoTime();

		for(int i = 0; i < 3000; i++){
			long userId = i + 2;
				executorService.submit(() -> {
					like(articleId, userId, locktype);
					countDownLatch.countDown();
				}
			);
		}

		countDownLatch.await();

		long end = System.nanoTime();

		System.out.println("lockType = " + locktype + ", time = " + (end - start) / 1000000 + "ms");
		System.out.println("end");

		Long count = restClient.get()
			.uri("/v1/article-likes/articles/{articleId}/count", articleId)
			.retrieve()
			.body(Long.class);

		System.out.println("count = " + count);
	}






}
