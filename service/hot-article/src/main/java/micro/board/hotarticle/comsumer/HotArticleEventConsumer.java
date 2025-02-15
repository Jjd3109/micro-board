package micro.board.hotarticle.comsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import kuke.board.common.event.Event;
import kuke.board.common.event.EventPayload;
import kuke.board.common.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import micro.board.hotarticle.service.HotArticleService;

@Component
@Slf4j
@RequiredArgsConstructor
public class HotArticleEventConsumer {

	private final HotArticleService hotArticleService;

	@KafkaListener(topics = {
		EventType.Topic.KUKE_BOARD_ARTICLE,
		EventType.Topic.KUKE_BOARD_COMMENT,
		EventType.Topic.KUKE_BOARD_LIKE,
		EventType.Topic.KUKE_BOARD_VIEW,
	})
	public void listen(String message, Acknowledgment ack) {
		log.info("[HotArticleEventConsumer] message: {}]", message);
		Event<EventPayload> event = Event.fromJson(message);

		if(event != null){
			hotArticleService.handleEvent(event);
		}

		ack.acknowledge();

	}

}
