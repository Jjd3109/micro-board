package micro.board.hotarticle.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import kuke.board.common.event.Event;
import kuke.board.common.event.EventPayload;
import lombok.RequiredArgsConstructor;
import micro.board.hotarticle.repository.ArticleCreatedTimeRepository;
import micro.board.hotarticle.repository.HotArticleRepostitory;
import micro.board.hotarticle.service.eventhandler.EventHandler;

@Component
@RequiredArgsConstructor
public class HotArticleScoreUpdater {
	private final HotArticleRepostitory hotArticleRepostitory;
	private final HotArticleScoreCalculator hotArticleScoreCalculator;
	private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

	private static long HOT_ARTICLE_COUNT = 10;
	private static Duration HOT_ARTICLE_TTL =Duration.ofDays(10);

	public void update(Event<EventPayload> event, EventHandler<EventPayload> eventHandler) {
		Long articleId = eventHandler.findArticleId(event);
		LocalDateTime createdTime = articleCreatedTimeRepository.read(articleId);

		if(!isArticleCreatedToday(createdTime)){
			return;
		}

		eventHandler.handle(event);

		long score = hotArticleScoreCalculator.calculate(articleId);
		hotArticleRepostitory.add(
			articleId,
			createdTime,
			score,
			HOT_ARTICLE_COUNT,
			HOT_ARTICLE_TTL
		);



	}

	private boolean isArticleCreatedToday(LocalDateTime createdTime) {
		return createdTime != null && createdTime.toLocalDate().equals(LocalDate.now());
	}
}
