package micro.board.hotarticle.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kuke.board.common.event.Event;
import lombok.extern.slf4j.Slf4j;
import micro.board.hotarticle.repository.ArticleCommentCountRepository;
import micro.board.hotarticle.repository.ArticleCreatedTimeRepository;
import micro.board.hotarticle.repository.ArticleLikeCountRepository;
import micro.board.hotarticle.repository.ArticleViewCountRepository;
import micro.board.hotarticle.repository.HotArticleRepostitory;
import micro.board.hotarticle.service.eventhandler.EventHandler;

@ExtendWith(MockitoExtension.class)
@Slf4j
class HotArticleScoreUpdaterTest {

	@InjectMocks
	HotArticleScoreUpdater hotArticleScoreUpdater;

	@Mock
	ArticleLikeCountRepository articleLikeCountRepository;
	@Mock
	ArticleViewCountRepository articleViewCountRepository;
	@Mock
	ArticleCommentCountRepository articleCommentCountRepository;
	@Mock
	ArticleCreatedTimeRepository articleCreatedTimeRepository;
	@Mock
	HotArticleRepostitory hotArticleRepostitory;

	@Test
	void updateIfArticleNotCreatedTodayTest(){
		//given

		Long articleId = 1L;
		Event event = mock(Event.class);
		EventHandler eventHandler = mock(EventHandler.class);

		given(eventHandler.findArticleId(event)).willReturn(articleId);

		LocalDateTime cretedTime = LocalDateTime.now().minusDays(1);
		given(articleCreatedTimeRepository.read(1L)).willReturn(cretedTime);


		//when
		hotArticleScoreUpdater.update(event, eventHandler);

		//then
		verify(eventHandler, never()).handle(event);
		verify(hotArticleRepostitory, never())
			.add(anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class));

	}

	@Test
	void updateCreatedTodayTest(){
		//given

		Long articleId = 1L;
		Event event = mock(Event.class);
		EventHandler eventHandler = mock(EventHandler.class);

		given(eventHandler.findArticleId(event)).willReturn(articleId);

		LocalDateTime cretedTime = LocalDateTime.now();
		given(articleCreatedTimeRepository.read(1L)).willReturn(cretedTime);


		//when
		hotArticleScoreUpdater.update(event, eventHandler);

		//then
		verify(eventHandler).handle(event);
		verify(hotArticleRepostitory)
			.add(anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class));

	}


}