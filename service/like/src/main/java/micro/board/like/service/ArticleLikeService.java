package micro.board.like.service;


import java.util.Optional;

import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import micro.board.like.entity.ArticleLike;
import micro.board.like.entity.BoardArticleCount;
import micro.board.like.repository.ArticleLikeCountRepository;
import micro.board.like.repository.ArticleLikeRepository;
import micro.board.like.service.response.ArticleLikeResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final Snowflake snowflake = new Snowflake();
    //private final OutboxEventPublisher outboxEventPublisher;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    public ArticleLikeResponse read(Long articleId, Long userId) {
        return articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .map(ArticleLikeResponse::from)
                .orElseThrow();
    }

    /**
     * update 구문
     */
    @Transactional
    public void likePessimisticLock1(Long articleId, Long userId) {
        ArticleLike articleLike = articleLikeRepository.save(
            ArticleLike.create(snowflake.nextId(), articleId, userId)
        );

        // PESSIMISTIC_WRITE 락을 걸어서 동시성 문제 방지
        Optional<BoardArticleCount> likeCountOpt = articleLikeCountRepository.findLockedByArticleId(articleId);

        if (likeCountOpt.isPresent()) {
            BoardArticleCount likeCount = likeCountOpt.get();
            likeCount.increase();
        } else {
            articleLikeCountRepository.save(BoardArticleCount.init(articleId, 1L));
        }

        // outboxEventPublisher.publish(
        //         EventType.ARTICLE_LIKED,
        //         ArticleLikedEventPayload.builder()
        //                 .articleLikeId(articleLike.getArticleLikeId())
        //                 .articleId(articleLike.getArticleId())
        //                 .userId(articleLike.getUserId())
        //                 .createdAt(articleLike.getCreatedAt())
        //                 .articleLikeCount(count(articleLike.getArticleId()))
        //                 .build(),
        //         articleLike.getArticleId()
        // );
    }

    @Transactional
    public void unlikePessimisticLock1(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    articleLikeCountRepository.decrease(articleId);
                    // outboxEventPublisher.publish(
                    //         EventType.ARTICLE_UNLIKED,
                    //         ArticleUnlikedEventPayload.builder()
                    //                 .articleLikeId(articleLike.getArticleLikeId())
                    //                 .articleId(articleLike.getArticleId())
                    //                 .userId(articleLike.getUserId())
                    //                 .createdAt(articleLike.getCreatedAt())
                    //                 .articleLikeCount(count(articleLike.getArticleId()))
                    //                 .build(),
                    //         articleLike.getArticleId()
                    // );
                });
    }

    /**
     * select ... for update + update
     */
    @Transactional
    public void likePessimisticLock2(Long articleId, Long userId) {
        articleLikeRepository.save(
                ArticleLike.create(
                        snowflake.nextId(),
                        articleId,
                        userId
                )
        );
        BoardArticleCount boardArticleCount = articleLikeCountRepository.findLockedByArticleId(articleId)
                .orElseGet(() -> BoardArticleCount.init(articleId, 0L));
        boardArticleCount.increase();
        articleLikeCountRepository.save(boardArticleCount);
    }

    @Transactional
    public void unlikePessimisticLock2(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    BoardArticleCount boardArticleCount = articleLikeCountRepository.findLockedByArticleId(articleId).orElseThrow();
                    boardArticleCount.decrease();
                });
    }

    @Transactional
    public void likeOptimisticLock(Long articleId, Long userId) {
        articleLikeRepository.save(
                ArticleLike.create(
                        snowflake.nextId(),
                        articleId,
                        userId
                )
        );

        BoardArticleCount boardArticleCount = articleLikeCountRepository.findById(articleId)
                .orElseGet(() -> BoardArticleCount.init(articleId, 0L));
        boardArticleCount.increase();
        articleLikeCountRepository.save(boardArticleCount);
    }

    @Transactional
    public void unlikeOptimisticLock(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> {
                    articleLikeRepository.delete(articleLike);
                    BoardArticleCount boardArticleCount = articleLikeCountRepository.findById(articleId).orElseThrow();
                    boardArticleCount.decrease();
                });
    }

    public Long count(Long articleId) {
        return articleLikeCountRepository.findById(articleId)
                .map(BoardArticleCount::getLikeCount)
                .orElse(0L);
    }
}
