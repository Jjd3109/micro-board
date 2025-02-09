package micro.board.view.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import micro.board.view.repository.ArticleViewCountRepository;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

	private final ArticleViewCountRepository articleViewCountRepository;
	private final ArticleViewBackService articleViewBackService;
	private static final int BACK_UP_BACK_SIZE = 100;

	public Long increase(Long articleId, Long userId){
		Long count = articleViewCountRepository.increase(articleId);

		System.out.println("increase count 값 : "  + count);

		if(count % BACK_UP_BACK_SIZE == 0){
			System.out.println("내부 count 값 : " + count);
			articleViewBackService.backUp(articleId, count);
		}

		return count;
	}

	public Long count(Long articleId){
		return articleViewCountRepository.read(articleId);
	}



}
