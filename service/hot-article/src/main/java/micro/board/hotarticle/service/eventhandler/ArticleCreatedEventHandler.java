package micro.board.hotarticle.service.eventhandler;

import org.springframework.stereotype.Component;

import kuke.board.common.event.Event;
import kuke.board.common.event.EventType;
import kuke.board.common.event.payload.ArticleCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import micro.board.hotarticle.repository.ArticleCreatedTimeRepository;
import micro.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload>{

	private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

	@Override
	public void handle(Event<ArticleCreatedEventPayload> event) {
		ArticleCreatedEventPayload payload = event.getPayload();
		articleCreatedTimeRepository.createOrUpdate(
			payload.getArticleId(),
			payload.getCreatedAt(),
			TimeCalculatorUtils.calculatorDuraionToMidnight()
		);



	}

	@Override
	public boolean supports(Event<ArticleCreatedEventPayload> event) {
		return EventType.ARTICLE_CREATED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleCreatedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
