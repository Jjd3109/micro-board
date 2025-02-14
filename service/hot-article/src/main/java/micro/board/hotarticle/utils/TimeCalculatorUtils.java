package micro.board.hotarticle.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeCalculatorUtils {

	/*
	 * 자정과 지금까지의 시간 나타나주기
	 */
	public static Duration calculatorDuraionToMidnight(){
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midNight = now.plusDays(1).with(LocalTime.MIDNIGHT);
		return Duration.between(now, midNight);
	}
}
