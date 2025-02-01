package micro.board.article.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PageLimitCalculatorTest {

	@Test
	void calculatorTest(){
		calculatorTest(1L, 30L, 10L, 301L);
		calculatorTest(1L, 30L, 20L, 601L);
	}

	static void calculatorTest(Long page, Long pageSize, Long moveablePageCount, Long expected){
		Long result = PageLimitCalculator.calculatePageLimit(page, pageSize, moveablePageCount);
		assertThat(result).isEqualTo(expected);
	}
}