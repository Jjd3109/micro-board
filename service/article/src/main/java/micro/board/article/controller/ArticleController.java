package micro.board.article.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import micro.board.article.service.ArticleService;
import micro.board.article.service.request.ArticleCreateRequest;
import micro.board.article.service.request.ArticleUpdateRequest;
import micro.board.article.service.response.ArticlePageResponse;
import micro.board.article.service.response.ArticleResponse;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ArticleController {

	private final ArticleService articleService;

	@GetMapping("/v1/articles/{articleId}")
	public ArticleResponse read(@PathVariable Long articleId){
		return articleService.read(articleId);
	}

	@GetMapping("/v1/articles")
	public ArticlePageResponse readAll(
		@RequestParam("boardId") Long boardId,
		@RequestParam("page") Long page,
		@RequestParam("pageSize") Long pageSize){
		return articleService.readAll(boardId, page, pageSize);
	}

	@PostMapping("/v1/articles")
	public ArticleResponse create(@RequestBody ArticleCreateRequest articleCreateRequest){
		return articleService.create(articleCreateRequest);
	}

	@PutMapping("/v1/articles/{articleId}")
	public ArticleResponse modify(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest articleUpdateRequest){
		return articleService.update(articleId, articleUpdateRequest);
	}

	@DeleteMapping("/v1/articles/{articleId}")
	public void delete(@PathVariable Long articleId){
		articleService.delete(articleId);
	}
}
