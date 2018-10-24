package test.com.mariangolea.fintracker.banks.csvparser.api.filters;

import com.mariangolea.fintracker.banks.csvparser.api.filters.TimeSlots;
import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.Collection;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

public class TimeSlotsTest {

    private final UserPreferences userPrefs = UserPreferencesHandler.INSTANCE.getPreferences();
    private final Collection<BankTransaction> transactions = TestUtilities.constructMockDefaultTransactionsForCategorizer(userPrefs);

    @Test
    public void testGetters() {
        TimeSlots calc = new TimeSlots(transactions);
        Collection<YearSlot> slots = calc.getMonthSlots();
        assertTrue(slots != null);
        assertTrue(slots.size() == 4);

        slots = calc.getYearSlots();
        assertTrue(slots != null);
        assertTrue(slots.size() == 4);
    }
}
