package micro.board.view.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import micro.board.view.entity.ArticleViewCount;
import micro.board.view.repository.ArticleViewCountBackRepository;

@Service
@RequiredArgsConstructor
public class ArticleViewBackService {

	private final ArticleViewCountBackRepository articleViewCountBackRepository;

	@Transactional
	public void backUp(Long articleId, Long viewCount){
		int result = articleViewCountBackRepository.updateCount(articleId, viewCount);


		if(result == 0){
			articleViewCountBackRepository.findById(articleId)
				.ifPresentOrElse(ignored -> { },
					() -> {
						articleViewCountBackRepository.save(
							ArticleViewCount.init(articleId, viewCount)
						);
					}
				);
		}

	}

}
