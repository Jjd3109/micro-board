package micro.board.article.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import micro.board.article.entity.BoardArticleCount;
@Repository
public interface BoardArticleCountRepository extends JpaRepository<BoardArticleCount, Long> {

	// select ... for update
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<BoardArticleCount> findLockedByBoardId(Long boardId);  // 수정된 메서드 이름

	@Query(
		value = "update board_article_count "
			+ "set article_count = article_count + 1 "
			+ "where board_id = :boardId",  // 수정된 쿼리
		nativeQuery = true
	)
	@Modifying
	int increase(@Param("boardId") Long boardId);

	@Query(
		value = "update board_article_count "
			+ "set article_count = article_count - 1 "
			+ "where board_id = :boardId",  // 수정된 쿼리
		nativeQuery = true
	)
	@Modifying
	int decrease(@Param("boardId") Long boardId);
}
