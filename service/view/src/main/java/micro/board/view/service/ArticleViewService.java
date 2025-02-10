package micro.board.view.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import micro.board.view.repository.ArticleViewCountRepository;
import micro.board.view.repository.ArticleViewDistributedLockRepository;

@Service
@RequiredArgsConstructor
public class ArticleViewService {

	private final ArticleViewCountRepository articleViewCountRepository;
	private final ArticleViewBackService articleViewBackService;
	private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;

	private static final int BACK_UP_BACK_SIZE = 100;
	private static final Duration VIEW_TTL = Duration.ofMinutes(10);

	public Long increase(Long articleId, Long userId){
		if(!articleViewDistributedLockRepository.lock(articleId, userId, VIEW_TTL)){
			return articleViewCountRepository.read(articleId);
		}

		Long count = articleViewCountRepository.increase(articleId);

		if(count % BACK_UP_BACK_SIZE == 0){

			articleViewBackService.backUp(articleId, count);
		}

		return count;
	}

	public Long count(Long articleId){
		return articleViewCountRepository.read(articleId);
	}



}
