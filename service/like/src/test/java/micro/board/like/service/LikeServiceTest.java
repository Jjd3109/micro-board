package micro.board.like.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.Assert;

import kuke.board.common.snowflake.Snowflake;
import lombok.Getter;
import lombok.ToString;
import micro.board.like.entity.Like;
import micro.board.like.repository.LikeRepository;
import micro.board.like.service.request.LikeCreateRequest;
import micro.board.like.service.response.LikeResponse;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

	@Mock
	private LikeRepository likeRepository;

	@Mock
	private Snowflake snowflake;

	@InjectMocks
	private LikeService likeService;


	@Test
	@DisplayName("좋아요 생성")
	void create(){
		//given

		Long likeId = 1L;
		Long articleId = 100L;
		Long userId = 200L;

		LikeCreateRequest request = new LikeCreateRequest(articleId, userId);
		Like like = Like.create(likeId, articleId, userId);

		when(likeRepository.save(any(Like.class))).thenReturn(like);

		// when
		LikeResponse response = likeService.save(request);

		// then
		assertThat(response).isNotNull();

	}

	@Test
	@DisplayName("좋아요 조회")
	void findById() {
		// given
		Long articleLikeId = 1L;
		Long articleId = 100L;
		Long userId = 200L;
		Like like = Like.create(articleLikeId, articleId, userId);

		// LikeRepository가 findById 호출 시 해당 Like 객체를 반환하도록 설정
		when(likeRepository.findById(articleLikeId)).thenReturn(java.util.Optional.of(like));

		// when
		LikeResponse response = likeService.find(articleLikeId);

		// then
		assertThat(response).isNotNull();  // 응답이 null이 아님을 확인
		assertThat(response.getArticleId()).isEqualTo(articleId);  // Article ID가 올바른지 확인
		assertThat(response.getUserId()).isEqualTo(userId);  // User ID가 올바른지 확인
	}


}