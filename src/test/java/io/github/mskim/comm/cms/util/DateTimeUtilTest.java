package io.github.mskim.comm.cms.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DateTimeUtilTest {

    @Test
    void startOfMonthReturnsFirstDay() {
        assertThat(DateTimeUtil.startOfMonth(2026, 2)).isEqualTo(LocalDate.of(2026, 2, 1));
    }

    @Test
    void endOfMonthReturnsLastDayForLeapYear() {
        assertThat(DateTimeUtil.endOfMonth(2024, 2)).isEqualTo(LocalDate.of(2024, 2, 29));
    }

    @Test
    void daysBetweenReturnsInclusiveDayCount() {
        long days = DateTimeUtil.daysBetween(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 10));

        assertThat(days).isEqualTo(10);
    }

    @Test
    void daysBetweenReturnsZeroWhenDateIsNull() {
        assertThat(DateTimeUtil.daysBetween(null, LocalDate.of(2026, 2, 10))).isZero();
    }
}
