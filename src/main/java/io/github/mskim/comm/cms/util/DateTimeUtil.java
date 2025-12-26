package io.github.mskim.comm.cms.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

/**
 * 날짜/시간 유틸리티
 *
 * <p>날짜 및 시간 관련 공통 기능을 제공합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
public class DateTimeUtil {

    private DateTimeUtil() {
        // 유틸리티 클래스는 인스턴스화 방지
    }

    /**
     * 현재 날짜 조회
     *
     * @return 현재 날짜 (LocalDate)
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 현재 날짜/시간 조회
     *
     * @return 현재 날짜/시간 (LocalDateTime)
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 이번 달 시작일 조회
     *
     * @return 이번 달 1일
     */
    public static LocalDate startOfCurrentMonth() {
        return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 이번 달 마지막일 조회
     *
     * @return 이번 달 마지막 날
     */
    public static LocalDate endOfCurrentMonth() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 특정 월의 시작일 조회
     *
     * @param year 년
     * @param month 월 (1-12)
     * @return 해당 월의 1일
     */
    public static LocalDate startOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    /**
     * 특정 월의 마지막일 조회
     *
     * @param year 년
     * @param month 월 (1-12)
     * @return 해당 월의 마지막 날
     */
    public static LocalDate endOfMonth(int year, int month) {
        return YearMonth.of(year, month).atEndOfMonth();
    }

    /**
     * 날짜 범위 계산 (일 수)
     *
     * @param start 시작 날짜
     * @param end 종료 날짜
     * @return 일 수 (end - start + 1)
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
    }

    /**
     * 오늘 날짜가 특정 날짜 범위 내에 있는지 확인
     *
     * @param start 시작 날짜
     * @param end 종료 날짜
     * @return 범위 내에 있으면 true
     */
    public static boolean isTodayBetween(LocalDate start, LocalDate end) {
        LocalDate today = today();
        return !today.isBefore(start) && !today.isAfter(end);
    }

    /**
     * 특정 날짜가 오늘인지 확인
     *
     * @param date 확인할 날짜
     * @return 오늘이면 true
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(today());
    }

    /**
     * 특정 날짜가 과거인지 확인
     *
     * @param date 확인할 날짜
     * @return 과거면 true
     */
    public static boolean isPast(LocalDate date) {
        return date != null && date.isBefore(today());
    }

    /**
     * 특정 날짜가 미래인지 확인
     *
     * @param date 확인할 날짜
     * @return 미래면 true
     */
    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(today());
    }
}
