package micro.board.hotarticle.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import kuke.board.common.event.Event;
import kuke.board.common.event.EventPayload;
import kuke.board.common.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import micro.board.hotarticle.client.ArticleClient;
import micro.board.hotarticle.repository.HotArticleRepostitory;
import micro.board.hotarticle.service.eventhandler.EventHandler;
import micro.board.hotarticle.service.response.HotArticleResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotArticleService {

	private final ArticleClient articleClient;
	private final List<EventHandler> eventHandlers;
	private final HotArticleScoreCalculator hotArticleScoreCalculator;
	private final HotArticleScoreUpdater hotArticleScoreUpdater;
	private final HotArticleRepostitory hotArticleRepostitory;

	public void handleEvent(Event<EventPayload> event){
		EventHandler<EventPayload> eventHandler = findEventHandler(event);

		if(eventHandler == null){
			return;
		}


		if(isArticleCreatedOrDeleted(event)){
			eventHandler.handle(event);
		}else{
			hotArticleScoreUpdater.update(event, eventHandler);
		}
	}

	private EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {

		return eventHandlers.stream()
			.filter(eventHandler -> eventHandler.supports(event))
			.findAny()
			.orElse(null);
	}

	private boolean isArticleCreatedOrDeleted(Event<EventPayload> event) {
		return EventType.ARTICLE_CREATED == event.getType() || EventType.ARTICLE_DELETED == event.getType();
	}

	public List<HotArticleResponse> readAll(String dateStr){
		// 날짜 정보를 받아서 구해오기 !

		return hotArticleRepostitory.readAll(dateStr).stream()
			.map(articleClient::read)
			.filter(Objects::nonNull)
			.map(HotArticleResponse::from)
			.toList();
	}

}

