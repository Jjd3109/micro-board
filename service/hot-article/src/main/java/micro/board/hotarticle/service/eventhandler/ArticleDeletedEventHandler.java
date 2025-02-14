package micro.board.hotarticle.service.eventhandler;

import org.springframework.stereotype.Component;

import kuke.board.common.event.Event;
import kuke.board.common.event.EventType;
import kuke.board.common.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import micro.board.hotarticle.repository.ArticleCreatedTimeRepository;
import micro.board.hotarticle.repository.HotArticleRepostitory;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload>  {

	private final HotArticleRepostitory hotArticleRepostitory;
	private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

	@Override
	public void handle(Event<ArticleDeletedEventPayload> event) {
		ArticleDeletedEventPayload payload = event.getPayload();
		articleCreatedTimeRepository.delete(payload.getArticleId());
		hotArticleRepostitory.remove(
			payload.getArticleId(),
			payload.getCreatedAt()
		);

	}

	@Override
	public boolean supports(Event<ArticleDeletedEventPayload> event) {
		return EventType.ARTICLE_DELETED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleDeletedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
