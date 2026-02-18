package lumi.parser;

import lumi.exception.LumiException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateTimeParser {

    // Accept: 2019-10-15
    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    // Accept: 2019-10-15 1800  OR 2019-10-15 18:00
    private static final DateTimeFormatter DATE_TIME_HHMM =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("yyyy-MM-dd HHmm")
                    .toFormatter(Locale.ENGLISH);

    private static final DateTimeFormatter DATE_TIME_COLON =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("yyyy-MM-dd HH:mm")
                    .toFormatter(Locale.ENGLISH);

    // Accept: 2pm, 2:30pm, 2 PM, 2:30 PM
    private static final DateTimeFormatter TIME_AMPM =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("h[:mm]a")
                    .toFormatter(Locale.ENGLISH);

    public static LocalDateTime parseFlexibleDateTime(String raw) throws LumiException {
        String s = raw.trim().toLowerCase();

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Singapore"));

        // keywords
        if (s.equals("today")) {
            return today.atTime(23, 59);
        }
        if (s.equals("tomorrow")) {
            return today.plusDays(1).atTime(23, 59);
        }

        // date only: yyyy-mm-dd (assume end of day)
        try {
            LocalDate d = LocalDate.parse(raw.trim(), DATE);
            return d.atTime(23, 59);
        } catch (DateTimeParseException ignored) {}

        // date + time
        try {
            return LocalDateTime.parse(raw.trim(), DATE_TIME_HHMM);
        } catch (DateTimeParseException ignored) {}

        try {
            return LocalDateTime.parse(raw.trim(), DATE_TIME_COLON);
        } catch (DateTimeParseException ignored) {}

        // time only: "2pm" -> assume today at 2pm
        try {
            LocalTime t = LocalTime.parse(raw.replace(" ", "").trim().toUpperCase(), TIME_AMPM);
            // ^ TIME_AMPM is case-insensitive but we also normalize spacing
            return today.atTime(t);
        } catch (DateTimeParseException ignored) {}

        throw new LumiException("""
                Invalid date/time. Try:
                - yyyy-mm-dd (e.g. 2019-10-15)
                - yyyy-mm-dd HHmm (e.g. 2019-10-15 1800)
                - yyyy-mm-dd HH:mm (e.g. 2019-10-15 18:00)
                - today / tomorrow
                - 2pm / 2:30pm
                """);
    }
}