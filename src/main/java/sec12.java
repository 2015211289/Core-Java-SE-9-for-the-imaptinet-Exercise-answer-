import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.util.function.Predicate;

import static java.time.temporal.ChronoField.OFFSET_SECONDS;

public class sec12 {

    //exercise1
    void exercise1() {
        LocalDate progarammerDay = LocalDate.of(2020, 1, 1)
                .plus(Period.ofDays(256));
    }

    //exercise2
    /*
    增加一年，返回2001-2-28。
    一次增加4年，返回2004-2-29。
    4次增加一年，返回2004-2-28。
     */

    //exercise3
    public static TemporalAdjuster next(Predicate<LocalDate> p) {
        TemporalAdjuster Next = w -> {
            LocalDate result = (LocalDate) w;
            while (!p.test(result)) {
                result = result.plusDays(1);
            }
            return result;
        };
        return Next;
    }

    //exercise4
    static void Cal(int month, int year) {
        LocalDate date = LocalDate.of(year, month, 1);
        int value = date.getDayOfWeek().getValue();
        for (int i = 0; i < value - 1; i++) System.out.print("   ");
        while (date.getMonthValue() == month) {
            System.out.printf("%2d ", date.getDayOfMonth());
            if (date.getDayOfWeek().getValue() == 7) System.out.println();
            date = date.plusDays(1);
        }
    }

    //exercise5
    void exercise5() {
        LocalDate birth = LocalDate.of(1997, 4, 30);
        LocalDate now = LocalDate.now();
        long days = birth.until(now, ChronoUnit.DAYS);
        System.out.println(days);
    }

    //exercise6
    void exercise6() {
        LocalDate date = LocalDate.of(1900, 1, 13);
        while (date.getYear() < 2000) {
            if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                System.out.println(date);
            }
            date = date.plusMonths(1);
        }
    }

    //exercise7
    class TimeInterval {
        private LocalDate date;
        private LocalTime start;
        private LocalTime end;

        public TimeInterval(LocalDate d, LocalTime s, LocalTime e) {
            date = d;
            start = s;
            end = e;
        }

        public LocalDate getDate() {
            return date;
        }

        public LocalTime getStart() {
            return start;
        }

        public LocalTime getEnd() {
            return end;
        }

        public boolean isOverlap(TimeInterval o) {
            if (!date.isEqual(o.getDate())) return false;
            if (start.isAfter(o.getEnd())) return false;
            if (end.isBefore(o.getStart())) return false;
            return true;
        }
    }

    //exercise8
    void exercise8() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("OOOO");
        ZoneId.getAvailableZoneIds().stream().forEach(id -> {
            ZonedDateTime dateTime = ZonedDateTime.of(LocalDate.now(),
                    LocalTime.now(), ZoneId.of(id));
            System.out.println(formatter.format(dateTime));
        });
    }

    //exercise9
    void exercise9() {
        ZoneId.getAvailableZoneIds().stream().forEach(id -> {
            ZonedDateTime dateTime = ZonedDateTime.of(LocalDate.now(),
                    LocalTime.now(), ZoneId.of(id));
            if (dateTime.getOffset().get(OFFSET_SECONDS) % 3600 != 0)
                System.out.println(id);
        });
    }

    //exercise10
    static LocalTime exercise10(String fromId, String toId, LocalDate date,
                                LocalTime start, Period time) {
        ZonedDateTime s = ZonedDateTime.of(date, start, ZoneId.of(fromId));
        s = s.plus(time).withZoneSameInstant(ZoneId.of(toId));
        return s.toLocalTime();
    }

    //exercise11
    static Duration exercise11(String fromId, String toId,
                               LocalDate sDate, LocalTime start,
                               LocalDate tDAte, LocalTime end) {
        ZonedDateTime s = ZonedDateTime.of(sDate, start, ZoneId.of(fromId));
        ZonedDateTime e = ZonedDateTime.of(tDAte, end,
                ZoneId.of(toId));
        long minutes = s.until(e, ChronoUnit.MINUTES);
        return Duration.ofMinutes(minutes);
    }

    static void exercise12(ZonedDateTime... meetings) {
        ZonedDateTime now = ZonedDateTime.now();
        for (ZonedDateTime meeting : meetings) {
            long minutes = now.until(meeting, ChronoUnit.MINUTES);
            if (minutes <= 60) System.out.println(meeting);
        }
    }

}
