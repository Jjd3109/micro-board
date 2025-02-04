package micro.board.like.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import micro.board.like.entity.Like;
import micro.board.like.repository.LikeRepository;
import micro.board.like.service.request.LikeCreateRequest;
import micro.board.like.service.response.LikeResponse;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;
	private final Snowflake snowflake = new Snowflake();

	@Transactional
	public LikeResponse save(LikeCreateRequest likeCreateRequest) {
		Like save = likeRepository.save(
			Like.create(
				snowflake.nextId(),
				likeCreateRequest.getArticleId(),
				likeCreateRequest.getUserId(),
				likeCreateRequest.getCreatedAt()
			)
		);
		return LikeResponse.of(save);
	}

}
