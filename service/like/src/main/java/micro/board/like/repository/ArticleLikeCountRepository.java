package micro.board.like.repository;

import jakarta.persistence.LockModeType;
import micro.board.like.entity.BoardArticleCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<BoardArticleCount, Long> {
    // select ... for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BoardArticleCount> findLockedByArticleId(Long articleId);


    @Modifying
    @Query("UPDATE board_article_count a SET a.likeCount = a.likeCount + 1 WHERE a.articleId = :articleId")
    int increase(@Param("articleId") Long articleId);

    @Query(
            value = "update article_like_count set like_count = like_count - 1 where article_id = :articleId",
            nativeQuery = true
    )
    @Modifying
    int decrease(@Param("articleId") Long articleId);
}
