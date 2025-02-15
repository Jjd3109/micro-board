package micro.board.hotarticle.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import micro.board.hotarticle.service.HotArticleService;
import micro.board.hotarticle.service.response.HotArticleResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HotArticleController {

	private final HotArticleService hotArticleService;

	@GetMapping("/v1/hot-articles/articles/data/{dateStr}")
	public List<HotArticleResponse> getHotArticles(
		@PathVariable("dateStr") String dateStr) {
		return hotArticleService.readAll(dateStr);
	}


}
