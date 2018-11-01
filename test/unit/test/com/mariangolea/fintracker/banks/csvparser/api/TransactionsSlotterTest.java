package test.com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.transaction.TransactionsSlotter;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferencesHandler;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;

public class TransactionsSlotterTest {

    private Collection<BankTransaction> transactions;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void init() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory(TestUtilities.createFolder(folder, "prefsTest"));
        UserPreferencesHandlerInterface userPrefsHandler = factory.getUserPreferencesHandler();
        transactions = TestUtilities.constructMockDefaultTransactionsForCategorizer(userPrefsHandler.getPreferences());
    }

    @Test
    public void testGetSlotYear() {
        TransactionsSlotter slotter = getSlotter(UserPreferences.Timeframe.YEAR);
        YearSlot slot = slotter.getSlot(TestUtilities.createDate(2018));
        assertNotNull(slot);
        assertEquals(slot, new YearSlot(2018));

        //second time around result is cached!
        YearSlot slot2 = slotter.getSlot(TestUtilities.createDate(2018));
        assertTrue(slot == slot2);
    }

    @Test
    public void testGetSlotMonth() {
        TransactionsSlotter slotter = getSlotter(UserPreferences.Timeframe.MONTH);
        YearSlot slot = slotter.getSlot(TestUtilities.createDate(1, 2018));
        assertNotNull(slot);
        assertEquals(new MonthSlot(1, 2018), slot);

        //second time around result is cached!
        YearSlot slot2 = slotter.getSlot(TestUtilities.createDate(1, 2018));
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
}
