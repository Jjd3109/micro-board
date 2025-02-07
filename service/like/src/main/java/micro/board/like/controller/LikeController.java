package micro.board.like.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import micro.board.like.service.LikeService;
import micro.board.like.service.request.LikeCreateRequest;
import micro.board.like.service.response.LikeResponse;

@RestController
@RequiredArgsConstructor
public class LikeController {

	private final LikeService likeService;

	@GetMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
	public LikeResponse findByArticleIdAndUserId(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId){
		return likeService.read(articleId, userId);
	}


	@GetMapping("/v1/article-likes/articles/{articleId}/count")
	public Long count(@PathVariable("articleId") Long articleId){
		return likeService.count(articleId);
	}

	@PostMapping("/v1/article-likes/articles/pessimistic-lock-1")
	public void likePessimisticLock1(
		@RequestBody LikeCreateRequest likeCreateRequest
	){
		likeService.likePessimisticLock1(likeCreateRequest);
	}

	@DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1")
	public void unlikePessimisticLock1(
		@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId)
	{
		likeService.unLikePessimisticLock1(articleId, userId);
	}

	@PostMapping("/v1/article-likes/articles/pessimistic-lock-2")
	public void likePessimisticLock2(
		@RequestBody LikeCreateRequest likeCreateRequest
	){
		likeService.likePessimisticLock2(likeCreateRequest);
	}

	@DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-2")
	public void unlikePessimisticLock2(
		@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId)
	{
		likeService.unLikePessimisticLock2(articleId, userId);
	}

	@PostMapping("/v1/article-likes/articles/optimistic-lock")
	public void likeoptimisticLock3(
		@RequestBody LikeCreateRequest likeCreateRequest
	){
		likeoptimisticLock3(likeCreateRequest);
	}

	@DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock")
	public void unlikeoptimisticLock3(
		@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId)
	{
		unlikeoptimisticLock3(articleId, userId);
	}


}
