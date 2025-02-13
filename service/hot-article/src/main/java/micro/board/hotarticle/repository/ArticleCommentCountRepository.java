package micro.board.hotarticle.repository;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ArticleCommentCountRepository {

	private final StringRedisTemplate redisTemplate;

	// hot-article::article::{articleId}::comment-count
	private static final String KEY_FORMAT = "hot-article::article::%s::comment-count";

	public void createOrUpdate(Long articleId, Long commentCount, Duration ttl){
		redisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(commentCount), ttl); // 없으면 생성 아니면 updatate
	}

	public Long read(Long articleId){
		String result = redisTemplate.opsForValue().get(generateKey(articleId));
		return result == null ? 0L : Long.valueOf(result);
	}

	private String generateKey(Long articleId){
		return KEY_FORMAT.formatted(articleId);
	}
}
