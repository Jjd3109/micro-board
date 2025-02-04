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

	@PostMapping("/v1/article-likes/articles")
	public LikeResponse createLike(
		@RequestBody LikeCreateRequest likeCreateRequest
	){
		return likeService.save(likeCreateRequest);
	}

	@DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
	public void deleteLike(
		@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId)
	{
		likeService.delete(articleId, userId);
	}
}
