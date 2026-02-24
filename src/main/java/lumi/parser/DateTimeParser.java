package lumi.parser;

import lumi.exception.LumiException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Utility class for parsing flexible, user-friendly date and time inputs
 * <p>
 * Supports multiple formats such as :
 * <ul>
 *     <li>ISO date: {@code yyyy-MM-dd}</li>
 *     <li>Date + time: {@code yyyy-MM-dd HHmm}</li>
 *     <li>Date + time: {@code yyyy-MM-dd HH:mm}</li>
 *     <li>Natural keywords: {@code today}, {@code tomorrow}</li>
 *     <li>Time-only inputs: {@code 2pm}, {@code 2:30pm}</li>
 * </ul>
 * All time-only inputs are assumed to refer to the current date in the
 * {@code Asia/Singapore timezone}
 */
public class DateTimeParser {

    /**
     * Formatter for ISO local date format (yyyy-MM-dd).
     */
    private static final DateTimeFormatter DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Formatter for date and time in compact format (yyyy-MM-dd HHmm).
     */
    private static final DateTimeFormatter DATE_TIME_HHMM =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("yyyy-MM-dd HHmm")
                    .toFormatter(Locale.ENGLISH);

    /**
     * Formatter for date and time with colon separator (yyyy-MM-dd HH:mm)
     */
    private static final DateTimeFormatter DATE_TIME_COLON =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("yyyy-MM-dd HH:mm")
                    .toFormatter(Locale.ENGLISH);

    /**
     * Formatter for 12-hour AM/PM time inputs (e.g., 2pm, 2:30pm).
     */
    private static final DateTimeFormatter TIME_AMPM =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("h[:mm]a")
                    .toFormatter(Locale.ENGLISH);

    /**
     * Parses raw date/time string into a {@link LocalDateTime}
     * This method attempts multiple formats in order and applies the following rules:
     * <ul>
     *   <li>Date-only inputs default to 23:59 (end of day).</li>
     *   <li>Time-only inputs default to today's date.</li>
     *   <li>{@code today} and {@code tomorrow} default to 23:59.</li>
     * </ul>
     * @param raw Raw user input string representing date and/or time
     * @return Parsed {@link LocalDateTime}
     * @throws LumiException If the input does not match any supported format
     */
    public static LocalDateTime parseFlexibleDateTime(String raw) throws LumiException {
        String s = raw.trim().toLowerCase();

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Singapore"));

        // Natural keywords
        if (s.equals("today")) {
            return today.atTime(23, 59);
        }
        if (s.equals("tomorrow")) {
            return today.plusDays(1).atTime(23, 59);
        }

        // Date only (assume end of day 2359)
        try {
            LocalDate d = LocalDate.parse(raw.trim(), DATE);
            return d.atTime(23, 59);
        } catch (DateTimeParseException ignored) {}

        // Date + time (HHmm)
        try {
            return LocalDateTime.parse(raw.trim(), DATE_TIME_HHMM);
        } catch (DateTimeParseException ignored) {}

        // Date + time (HH:mm)
        try {
            return LocalDateTime.parse(raw.trim(), DATE_TIME_COLON);
        } catch (DateTimeParseException ignored) {}

        // Time only (assume today)
        try {
            LocalTime t = LocalTime.parse(raw.replace(" ", "").trim().toUpperCase(), TIME_AMPM);
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