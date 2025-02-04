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
	@DisplayName("좋아요")
	void create(){
		//given

		Long likeId = 1L;
		Long articleId = 100L;
		Long userId = 200L;
		LocalDateTime createdAt = LocalDateTime.now();

		LikeCreateRequest request = new LikeCreateRequest(articleId, userId, createdAt);
		Like like = Like.create(likeId, articleId, userId, createdAt);

		when(likeRepository.save(any(Like.class))).thenReturn(like);

		// when
		LikeResponse response = likeService.save(request);

		// then
		assertThat(response).isNotNull();



	}



}