package test.com.mariangolea.fintracker.banks.pdfparser.parsers;

import com.mariangolea.fintracker.banks.pdfparser.parsers.INGParser;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests individual methods in INGParser class.
 *
 * @author mariangolea@gmail.com
 */
public class INGParserTest {

    INGParserLocal parser = new INGParserLocal();

    @Test
    public void testMonthsDateParser() {
        String input;
        Date output;
        Calendar calendar = Calendar.getInstance(INGParser.ROMANIAN_LOCALE);
        Map<String, Integer> monthNames = calendar.getDisplayNames(Calendar.MONTH, Calendar.LONG, INGParser.ROMANIAN_LOCALE);
        for (Entry<String, Integer> entry : monthNames.entrySet()) {
            //try to verify day, month and year based on month values.
            int year = 2000 + entry.getValue();
            int day = 1 + entry.getValue(); //Month indices start with 0, sa when constructing days add 1.
            int month = entry.getValue();
            input = day + " " + entry.getKey() + " " + year;
            output = parser.parseCompletedDate(input);
            calendar.setTime(output);
            assertEquals("Month value not as expected.", month, calendar.get(Calendar.MONTH));
            assertEquals("Day value not as expected.", day, calendar.get(Calendar.DAY_OF_MONTH));
            assertEquals("Year value not as expected.", year, calendar.get(Calendar.YEAR));
        }
    }

    /**
     * Allows calling individual protected methods directly.
     */
    private class INGParserLocal extends INGParser {

        @Override
        protected double parseAmount(String amountString) {
            return super.parseAmount(amountString);
        }

        @Override
        protected Date parseCompletedDate(String dateString) {
            return super.parseCompletedDate(dateString);
        }

        @Override
        protected Date parseStartDate(String dateString) {
            return super.parseStartDate(dateString);
        }
    }
}
