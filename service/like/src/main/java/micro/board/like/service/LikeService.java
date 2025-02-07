package micro.board.like.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import micro.board.like.entity.ArticleLikeCount;
import micro.board.like.entity.Like;
import micro.board.like.repository.ArticleLikeCountRepository;
import micro.board.like.repository.LikeRepository;
import micro.board.like.service.request.LikeCreateRequest;
import micro.board.like.service.response.LikeResponse;

@Service
@RequiredArgsConstructor
public class LikeService {

	private final LikeRepository likeRepository;
	private final Snowflake snowflake = new Snowflake();
	private final ArticleLikeCountRepository articleLikeCountRepository;

	@Transactional
	public LikeResponse save(LikeCreateRequest likeCreateRequest) {
		Like save = likeRepository.save(
			Like.create(
				snowflake.nextId(),
				likeCreateRequest.getArticleId(),
				likeCreateRequest.getUserId()
			)
		);
		return LikeResponse.from(save);
	}

	/*
	 * update 구문
	 */
	@Transactional
	public void likePessimisticLock1(LikeCreateRequest likeCreateRequest) {
		likeRepository.save(
			Like.create(
				snowflake.nextId(),
				likeCreateRequest.getArticleId(),
				likeCreateRequest.getUserId()
			)
		);

		int result = articleLikeCountRepository.increase(likeCreateRequest.getArticleId());

		if(result == 0 ){
			articleLikeCountRepository.save(
				ArticleLikeCount.init(likeCreateRequest.getArticleId(), 1L)
			);
		}
	}


	@Transactional
	public void unLikePessimisticLock1(Long articleId, Long userId) {
		likeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(articleLike -> {
				likeRepository.delete(articleLike);
				articleLikeCountRepository.decrease(articleId);
			});
	}

	/*
	 * update select
	 */
	@Transactional
	public void likePessimisticLock2(LikeCreateRequest likeCreateRequest) {
		likeRepository.save(
			Like.create(
				snowflake.nextId(),
				likeCreateRequest.getArticleId(),
				likeCreateRequest.getUserId()
			)
		);

		ArticleLikeCount articleLikeCount = articleLikeCountRepository.findLockedByArticleId(
				likeCreateRequest.getArticleId())
			.orElseGet(() -> ArticleLikeCount.init(likeCreateRequest.getArticleId(), 0L)
			);

		articleLikeCount.increase();
		articleLikeCountRepository.save(articleLikeCount);
	};



	@Transactional
	public void unLikePessimisticLock2(Long articleId, Long userId) {
		likeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(articleLike -> {
				likeRepository.delete(articleLike);
				ArticleLikeCount articleLikeCount = articleLikeCountRepository.findLockedByArticleId(articleId).orElseThrow();
				articleLikeCount.decrease();
			});
	}

	/*
	 * 낙관적 락
	 */

	@Transactional
	public void likeOptimisticLock(LikeCreateRequest likeCreateRequest) {
		likeRepository.save(
			Like.create(
				snowflake.nextId(),
				likeCreateRequest.getArticleId(),
				likeCreateRequest.getUserId()
			)
		);

		ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(likeCreateRequest.getArticleId())
			.orElseGet(
				() -> ArticleLikeCount.init(likeCreateRequest.getArticleId(), 0L)
			);

		articleLikeCount.increase();
		articleLikeCountRepository.save(articleLikeCount);
	}


	@Transactional
	public void unLikePessimisticLock3(Long articleId, Long userId) {
		likeRepository.findByArticleIdAndUserId(articleId, userId)
			.ifPresent(entity -> {
				likeRepository.delete(entity);
				ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId).orElseThrow();
				articleLikeCount.decrease();
			});
	}




	public LikeResponse find(Long articleLikeId){
		return likeRepository.findById(articleLikeId)
			.map(LikeResponse::from)
			.orElseThrow();
	}

	public LikeResponse read(Long articleId, Long userId) {
		return likeRepository.findByArticleIdAndUserId(articleId, userId)
			.map(LikeResponse::from)
			.orElseThrow();
	}

	public Long count(Long articleId){
		return articleLikeCountRepository.findById(articleId)
			.map(ArticleLikeCount::getLikeCount)
			.orElse(0L);
	}


}
