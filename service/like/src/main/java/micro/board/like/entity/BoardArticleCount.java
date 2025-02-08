package micro.board.like.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "article_like_count")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardArticleCount {
    @Id
    private Long articleId; // shard key
    private Long likeCount;
    @Version
    private Long version;

    public static BoardArticleCount init(Long articleId, Long likeCount) {
        BoardArticleCount boardArticleCount = new BoardArticleCount();
        boardArticleCount.articleId = articleId;
        boardArticleCount.likeCount = likeCount;
        boardArticleCount.version = 0L;
        return boardArticleCount;
    }

    public void increase() {
        this.likeCount++;
    }

    public void decrease() {
        this.likeCount--;
    }
}
