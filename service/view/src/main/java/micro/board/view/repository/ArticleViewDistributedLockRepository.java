package micro.board.view.repository;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ArticleViewDistributedLockRepository {

	private final StringRedisTemplate stringRedisTemplate;

	//view::article::{article_id}::user::{user_id}::lock

	private static final String KEY_FORMAT = "view::article::%s::user::%s::lock";

	public boolean lock(Long articleId, Long userId, Duration ttl){
		String key = generatedKey(articleId, userId);
		return stringRedisTemplate.opsForValue().setIfAbsent(key, "", ttl);
	}

	private String generatedKey(Long articleId, Long userId){
		return KEY_FORMAT.formatted(articleId, userId);
	}

}
