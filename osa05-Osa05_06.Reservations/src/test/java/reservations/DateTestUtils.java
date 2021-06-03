package reservations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class DateTestUtils {

    public static LocalDate getRandomLocalDateBetween(int startYear, int endYear) {
        Random random = new Random();
        int min = (int) LocalDate.of(startYear, 1, 1).toEpochDay();
        int max = (int) LocalDate.of(endYear, 1, 1).toEpochDay();
        long randomDay = min + random.nextInt(max - min);

        return LocalDate.ofEpochDay(randomDay);
    }

    public static Date getRandomDateBetween(int startYear, int endYear) {
        return Date.from(getRandomLocalDateBetween(startYear, endYear).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
