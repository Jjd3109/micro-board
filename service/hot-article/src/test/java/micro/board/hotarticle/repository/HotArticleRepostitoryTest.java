package micro.board.hotarticle.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HotArticleRepostitoryTest {

	@Autowired
	HotArticleRepostitory hotArticleRepostitory;

	@Test
	void addTest() throws InterruptedException {
		//given
		LocalDateTime now = LocalDateTime.of(2025, 2, 13, 0, 0);
		long limit = 3;

		//when
		hotArticleRepostitory.add(1L, now, 2L, limit, Duration.ofSeconds(3));
		hotArticleRepostitory.add(2L, now, 3L, limit, Duration.ofSeconds(3));
		hotArticleRepostitory.add(3L, now, 1L, limit, Duration.ofSeconds(3));
		hotArticleRepostitory.add(4L, now, 5L, limit, Duration.ofSeconds(3));
		hotArticleRepostitory.add(5L, now, 6L, limit, Duration.ofSeconds(3));


		//then

		List<Long> articleIdList = hotArticleRepostitory.readAll("20250213");

		Assertions.assertThat(articleIdList).hasSize(Long.valueOf(limit).intValue());
		Assertions.assertThat(articleIdList.get(0)).isEqualTo(5L);
		Assertions.assertThat(articleIdList.get(1)).isEqualTo(4L);
		Assertions.assertThat(articleIdList.get(2)).isEqualTo(2L);

		TimeUnit.SECONDS.sleep(5);

		Assertions.assertThat(hotArticleRepostitory.readAll("20250213")).isEmpty();

	}
}