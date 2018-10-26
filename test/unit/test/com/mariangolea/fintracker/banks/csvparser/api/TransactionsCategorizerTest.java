package test.com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionAbstractGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.TransactionsCategorizedSlotter;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javafx.util.Pair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

public class TransactionsCategorizerTest {

    private UserPreferences userPrefs;
    private Collection<BankTransaction> transactions;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void init() {
        userPrefs = UserPreferencesHandler.INSTANCE.getPreferences();
        TestUtilities.populateUserPrefsWithCompanyAndGroupData(userPrefs);
        transactions = TestUtilities.constructMockDefaultTransactionsForCategorizer(userPrefs);
    }

    @Test
    public void testUncategorized() {
        Extension calc = new Extension(transactions, userPrefs);
        Collection<BankTransactionGroupInterface> uncategorized = calc.getUnmodifiableUnCategorized();
        assertNotNull(uncategorized);
        assertEquals(uncategorized.size(), 1);
    }

    @Test
    public void testSlottedCategorized() throws IOException {
        TransactionsCategorizedSlotter calc = new TransactionsCategorizedSlotter(transactions, userPrefs);
        Map<YearSlot, Collection<BankTransactionGroupInterface>> result = calc.getUnmodifiableSlottedCategorized();
        assertTrue(result.keySet().size() == 4);

        Collection<String> topMostCategories = userPrefs.getTopMostCategories();
        final int topMostCategoriesSize = topMostCategories.size();

        result.keySet().forEach(slot -> {
            assertEquals(result.get(slot).size(), topMostCategoriesSize);
        });
    }

    @Test
    public void testCategorizeByCompanyNamesYearSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Extension calc = new Extension(transactions, userPrefs);
        Map<Pair<YearSlot, String>, BankTransactionCompanyGroup> result = calc.getUnmodifiableSlottedCompanyGroups();
        assertNotNull(result);
        assertEquals(result.keySet().size(), 6);

        BankTransactionAbstractGroup test = result.get(new Pair(new YearSlot(2016), "Carrefour"));
        assertNotNull(test);
        test = result.get(new Pair(new YearSlot(2017), "Auchan"));
        assertNotNull(test);
        test = result.get(new Pair(new YearSlot(2018), "Petrom"));
        assertNotNull(test);
        test = result.get(new Pair(new YearSlot(2018), "Employer"));
        assertNotNull(test);
        test = result.get(new Pair(new YearSlot(2018), "Auchan"));
        assertNotNull(test);
        test = result.get(new Pair(new YearSlot(2019), "Employer"));
        assertNotNull(test);
    }

    @Test
    public void testCategorizeByCompanyNamesMonthSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        Extension calc = new Extension(transactions, userPrefs);
        Map<Pair<YearSlot, String>, BankTransactionCompanyGroup> result = calc.getUnmodifiableSlottedCompanyGroups();
        assertNotNull(result);
        assertEquals(result.keySet().size(), 6);

        BankTransactionAbstractGroup test = result.get(new Pair(new MonthSlot(6, 2016), "Carrefour"));
        assertNotNull(test);
        test = result.get(new Pair(new MonthSlot(1, 2017), "Auchan"));
        assertNotNull(test);
        test = result.get(new Pair(new MonthSlot(5, 2018), "Petrom"));
        assertNotNull(test);
        test = result.get(new Pair(new MonthSlot(5, 2018), "Employer"));
        assertNotNull(test);
        test = result.get(new Pair(new MonthSlot(5, 2018), "Auchan"));
        assertNotNull(test);
        test = result.get(new Pair(new MonthSlot(2, 2019), "Employer"));
        assertNotNull(test);
    }

    @Test
    public void testCreateSlottedGroupYearSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Extension calc = new Extension(transactions, userPrefs);
        BankTransactionGroupInterface group = calc.createSlottedGroupLocal(new YearSlot(2018), "Revenues");
        assertNotNull(group);
        assertEquals("Revenues", group.getCategoryName());
        assertEquals(group.getGroupsNumber(), 1);
        ArrayList<BankTransactionGroupInterface> subGroups = new ArrayList<>(group.getContainedGroups());
        BankTransactionGroupInterface subGroup = subGroups.get(0);
        assertNotNull(subGroup);
        assertEquals("Employer", subGroup.getCategoryName());
        assertEquals(subGroup.getGroupsNumber(), 0);
    }

    @Test
    public void testCreateSlottedGroupMonthSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        Extension calc = new Extension(transactions, userPrefs);
        BankTransactionGroupInterface group = calc.createSlottedGroupLocal(new MonthSlot(5, 2018), "Revenues");
        assertNotNull(group);
        assertEquals("Revenues", group.getCategoryName());
        assertEquals(group.getGroupsNumber(), 1);
        ArrayList<BankTransactionGroupInterface> subGroups = new ArrayList<>(group.getContainedGroups());
        BankTransactionGroupInterface subGroup = subGroups.get(0);
        assertNotNull(subGroup);
        assertEquals("Employer", subGroup.getCategoryName());
        assertEquals(subGroup.getGroupsNumber(), 0);
    }

    @Test
    public void testCreateSlottedGroupsYearSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Extension calc = new Extension(transactions, userPrefs);
        Map<YearSlot, BankTransactionGroupInterface> groups = calc.createSlottedGroupsLocal("Employer");
        assertNotNull(groups);
        assertEquals(groups.keySet().size(), 2);
    }

    @Test
    public void testCreateSlottedGroupsMonthSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        Extension calc = new Extension(transactions, userPrefs);
        Map<YearSlot, BankTransactionGroupInterface> groups = calc.createSlottedGroupsLocal("Employer");
        assertNotNull(groups);
        assertEquals(groups.keySet().size(), 2);
    }

    private class Extension extends TransactionsCategorizedSlotter {

        public Extension(final Collection<BankTransaction> transactions, final UserPreferences userPrefs) {
            super(transactions, userPrefs);
        }

        @Override
        public Map<YearSlot, Collection<BankTransactionGroupInterface>> getUnmodifiableSlottedCategorized() {
            return super.getUnmodifiableSlottedCategorized();
        }

        @Override
        protected Map<Pair<YearSlot, String>, BankTransactionCompanyGroup> getUnmodifiableSlottedCompanyGroups() {
            return super.getUnmodifiableSlottedCompanyGroups();
        }

        @Override
        public Collection<BankTransactionGroupInterface> getUnmodifiableUnCategorized() {
            return super.getUnmodifiableUnCategorized();
        }

        protected final BankTransactionGroupInterface createSlottedGroupLocal(final YearSlot timeSlot, final String category) {
            return createSlottedGroup(timeSlot, category);
        }
        protected final Map<YearSlot, BankTransactionGroupInterface> createSlottedGroupsLocal(final String category) {
            return createSlottedGroups(category);
        }
        
    }
}
