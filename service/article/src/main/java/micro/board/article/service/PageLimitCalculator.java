package micro.board.article.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PageLimitCalculator {

	public static Long calculatePageLimit(Long page, Long pageSize, Long moveablePageCount){
		return ((page - 1) / moveablePageCount + 1) * pageSize * moveablePageCount + 1;
	}
}
