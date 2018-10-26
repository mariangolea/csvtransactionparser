package test.com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.TransactionsSlotter;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

public class TransactionsSlotterTest {

    private Collection<BankTransaction> transactions;
    private Calendar calendar;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void init() {
        transactions = TestUtilities.constructMockDefaultTransactionsForCategorizer(UserPreferencesHandler.INSTANCE.getPreferences());
        calendar = Calendar.getInstance();
    }

    @Test
    public void testGetSlotYear() {
        TransactionsSlotter slotter = getSlotter(UserPreferences.Timeframe.YEAR);
        YearSlot slot = slotter.getSlot(createDate(calendar, 2018));
        assertNotNull(slot);
        assertEquals(slot, new YearSlot(2018));

        //second time around result is cached!
        YearSlot slot2 = slotter.getSlot(createDate(calendar, 2018));
        assertTrue(slot == slot2);
    }

    @Test
    public void testGetSlotMonth() {
        TransactionsSlotter slotter = getSlotter(UserPreferences.Timeframe.MONTH);
        YearSlot slot = slotter.getSlot(createDate(calendar, 1, 2018));
        assertNotNull(slot);
        assertEquals(slot, new MonthSlot(1, 2018));

        //second time around result is cached!
        YearSlot slot2 = slotter.getSlot(createDate(calendar, 1, 2018));
        assertTrue(slot == slot2);
    }

    @Test
    public void testGetTimeSlotsYear() {
        TransactionsSlotter slotter = getSlotter(UserPreferences.Timeframe.YEAR);
        Collection<YearSlot> slots = slotter.getTimeSlots();
        assertNotNull(slots);

        //check caching.
        Collection<YearSlot> slots2 = slotter.getTimeSlots();
        assertTrue(slots == slots2);
        assertEquals(slots, slots2);

        //test transactions are made during 4 years range.
        assertEquals(slots.size(), 4);
    }

    @Test
    public void testGetTimeSlotsMonth() {
        TransactionsSlotter slotter = getSlotter(UserPreferences.Timeframe.MONTH);
        Collection<YearSlot> slots = slotter.getTimeSlots();
        assertNotNull(slots);

        //check caching.
        Collection<YearSlot> slots2 = slotter.getTimeSlots();
        assertTrue(slots == slots2);
        assertEquals(slots, slots2);

        //test transactions are made during 4 months.
        assertEquals(slots.size(), 5);
    }

    private TransactionsSlotter getSlotter(UserPreferences.Timeframe timeframe) {
        return new TransactionsSlotter(timeframe, transactions);
    }

    private Date createDate(Calendar calendar, int month, int year) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return calendar.getTime();

    }

    private Date createDate(Calendar calendar, int year) {
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }
}
