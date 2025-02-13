package micro.board.hotarticle.repository;

import static java.util.stream.Collectors.*;
import static org.springframework.data.redis.connection.ReactiveZSetCommands.ZAddCommand.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Repository
@Slf4j
public class HotArticleRepostitory {

	private final StringRedisTemplate redisTemplate;

	// sorted set 으로 게시글 아읻 ㅣ저장
	// hot-article::list::{yyyyMMdd}
	private static final String KEY_FORMAT = "hot-article::list::%s";

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	/*
	 * limit -> 10건씩 제한
	 * ttl -> 시간 계산
	 */
	public void add(Long articleId, LocalDateTime time, Long score, Long limit, Duration ttl) {
		redisTemplate.executePipelined((RedisCallback<?>) action -> {
			StringRedisConnection conn = (StringRedisConnection) action;
			String key = generateKey(time);
			conn.zAdd(key, score, String.valueOf(articleId));
			conn.zRemRange(key, 0, - limit - 1);
			conn.expire(key, ttl.toSeconds());
			return null;
		});
	}

	public String generateKey(LocalDateTime localDateTime){
		return generateKey(TIME_FORMATTER.format(localDateTime));
	}

	public String generateKey(String dateStr){
		return KEY_FORMAT.formatted(dateStr);
	}

	public List<Long> readAll(String dateStr) {
		return redisTemplate.opsForZSet()
			.reverseRangeWithScores(generateKey(dateStr), 0 , -1)
			.stream()
			.peek(tuple ->
				log.info("[HotArticleRespository.readAll artilceId = {}, scores = {}]", tuple.getValue(), tuple.getScore()))
			.map(ZSetOperations.TypedTuple::getValue)
			.map(Long::valueOf)
			.toList();
	}
	
	public void remove(Long articleId, LocalDateTime localDateTime){
		redisTemplate.opsForZSet()
			.remove(generateKey(localDateTime), String.valueOf(articleId));
	}

}
