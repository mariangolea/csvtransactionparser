package test.com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionAbstractGroup;
import com.mariangolea.fintracker.banks.csvparser.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.transaction.TransactionsCategorizedSlotter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javafx.util.Pair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;

public class TransactionsCategorizerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private UserPreferencesInterface userPrefs;
    private Collection<BankTransaction> transactions;

    @Before
    public void init() {
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UserPreferencesHandlerInterface userPrefsHandler = factory.getUserPreferencesHandler();
        userPrefs = userPrefsHandler.getPreferences();
        TestUtilities.populateUserPrefsWithCompanyAndGroupData(userPrefs);
        transactions = TestUtilities.constructMockDefaultTransactionsForCategorizer(userPrefs);
    }

    @Test
    public void testUncategorized() {
        Extension calc = new Extension(transactions, userPrefs);
        Collection<BankTransaction> uncategorized = calc.getUnmodifiableUnCategorized();
        assertNotNull(uncategorized);
        assertEquals(1, uncategorized.size());
    }

    @Test
    public void testSlottedCategorized() throws IOException {
        TransactionsCategorizedSlotter calc = new TransactionsCategorizedSlotter(transactions, userPrefs);
        Map<YearSlot, Collection<BankTransactionGroupInterface>> result = calc.getUnmodifiableSlottedCategorized();
        assertEquals(4, result.keySet().size());

        Collection<String> topMostCategories = userPrefs.getTopMostCategories();
        final int topMostCategoriesSize = topMostCategories.size();

        result.keySet().forEach(slot -> {
            assertEquals(topMostCategoriesSize, result.get(slot).size());
        });
    }

    @Test
    public void testCategorizeByCompanyNamesYearSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Extension calc = new Extension(transactions, userPrefs);
        Map<Pair<YearSlot, String>, BankTransactionCompanyGroup> result = calc.getUnmodifiableSlottedCompanyGroups();
        assertNotNull(result);
        assertEquals(6, result.keySet().size());

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
        assertEquals(6, result.keySet().size());

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
        assertEquals(1, group.getGroupsNumber());
        ArrayList<BankTransactionGroupInterface> subGroups = new ArrayList<>(group.getContainedGroups());
        BankTransactionGroupInterface subGroup = subGroups.get(0);
        assertNotNull(subGroup);
        assertEquals("Employer", subGroup.getCategoryName());
        assertEquals(0, subGroup.getGroupsNumber());
    }

    @Test
    public void testCreateSlottedGroupMonthSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        Extension calc = new Extension(transactions, userPrefs);
        BankTransactionGroupInterface group = calc.createSlottedGroupLocal(new MonthSlot(5, 2018), "Revenues");
        assertNotNull(group);
        assertEquals("Revenues", group.getCategoryName());
        assertEquals(1, group.getGroupsNumber());
        ArrayList<BankTransactionGroupInterface> subGroups = new ArrayList<>(group.getContainedGroups());
        BankTransactionGroupInterface subGroup = subGroups.get(0);
        assertNotNull(subGroup);
        assertEquals("Employer", subGroup.getCategoryName());
        assertEquals(0, subGroup.getGroupsNumber());
    }

    @Test
    public void testCreateSlottedGroupsYearSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Extension calc = new Extension(transactions, userPrefs);
        Map<YearSlot, BankTransactionGroupInterface> groups = calc.createSlottedGroupsLocal("Employer");
        assertNotNull(groups);
        assertEquals(2, groups.keySet().size());
    }

    @Test
    public void testCreateSlottedGroupsMonthSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        Extension calc = new Extension(transactions, userPrefs);
        Map<YearSlot, BankTransactionGroupInterface> groups = calc.createSlottedGroupsLocal("Employer");
        assertNotNull(groups);
        assertEquals(2, groups.keySet().size());
    }

    private class Extension extends TransactionsCategorizedSlotter {

        public Extension(final Collection<BankTransaction> transactions, final UserPreferencesInterface userPrefs) {
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
        public Collection<BankTransaction> getUnmodifiableUnCategorized() {
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
