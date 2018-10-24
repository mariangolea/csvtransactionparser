package test.com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.api.filters.MonthSlot;
import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.TransactionCategorizer;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

public class TransactionCategorizerTest {

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
    public void testTimeSlotsComputer() throws IOException {
        TransactionCategorizer calc = new TransactionCategorizer(transactions, userPrefs);
        Map<YearSlot, Collection<BankTransactionGroupInterface>> result = calc.categorize();
        assertTrue(result.keySet().size() == 4);

        Collection<String> topMostCategories = userPrefs.getTopMostCategories();
        final int topMostCategoriesSize = topMostCategories.size();

        result.keySet().forEach(slot -> {
            assertEquals(result.get(slot).size(), topMostCategoriesSize);
        });
    }

    @Test
    public void testCreateTimeSlot() throws IOException {
        Extension calc = new Extension(transactions, userPrefs);
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        YearSlot slot = calc.createSlot(TestUtilities.createDate(1, 2018));
        assertNotNull(slot);
        assertTrue(slot.year == 2018);

        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        slot = calc.createSlot(TestUtilities.createDate(1, 2018));
        assertNotNull(slot);
        assertTrue(slot instanceof MonthSlot);
        assertTrue(slot.year == 2018);
        MonthSlot month = (MonthSlot) slot;
        assertTrue(month.month == 1);
    }

    @Test
    public void testComputeTimeSlots() throws IOException {
        //transactions started from (6,2016), eneded in (3,2019)
        Extension calc = new Extension(transactions, userPrefs);
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Collection<YearSlot> slots = calc.computeTimeSlots();
        assertNotNull(slots);
        assertTrue(slots.size() == 4);

        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        slots = calc.computeTimeSlots();
        assertNotNull(slots);
        assertTrue(slots.size() == 4);
    }

    @Test
    public void testGetMatchingCategory() {
        Extension calc = new Extension(transactions, userPrefs);
        String category = calc.getMatchingCategory("Irrelevant....Limited Petrom SA...irelevant");
        assertNotNull(category);
        assertEquals(category, "Petrom");

        category = calc.getMatchingCategory("Limited PetroOOMm SA");
        assertNotNull(category);
        assertEquals(category, BankTransactionGroupInterface.UNCATEGORIZED);
    }

    @Test
    public void testCategorizeByCompanyNamesYearSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Extension calc = new Extension(transactions, userPrefs);
        Map<YearSlot, Collection<BankTransactionGroupInterface>> result = calc.categorizeByCompanyNames();
        assertNotNull(result);
        assertEquals(result.keySet().size(), 4);

        //2016 one transaction: Carrefour
        Collection<BankTransactionGroupInterface> groups = result.get(new YearSlot(2016));
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        Optional<BankTransactionGroupInterface> existentialOptional = groups.stream().findFirst();
        BankTransactionGroupInterface food = testGroupBasic(existentialOptional, "Carrefour");
        assertEquals(food.getTransactionsNumber(), 1);

        //2017 one transaction: Auchan
        groups = result.get(new YearSlot(2017));
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        existentialOptional = groups.stream().findFirst();
        food = testGroupBasic(existentialOptional, "Auchan");
        assertEquals(food.getTransactionsNumber(), 1);

        //2018 two transactions: Petrom, Employer, Auchan
        groups = result.get(new YearSlot(2018));
        assertNotNull(groups);
        //simple transactions are sorted based on actual description not company name, so not reliably.
        assertEquals(groups.size(), 3);

        //2019 one transaction: Employer
        groups = result.get(new YearSlot(2019));
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        existentialOptional = groups.stream().findFirst();
        food = testGroupBasic(existentialOptional, "Employer");
        assertEquals(food.getTransactionsNumber(), 1);
    }

    @Test
    public void testCategorizeByCompanyNamesMonthSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        Extension calc = new Extension(transactions, userPrefs);
        Map<YearSlot, Collection<BankTransactionGroupInterface>> result = calc.categorizeByCompanyNames();
        assertNotNull(result);
        assertEquals(result.keySet().size(), 4);

        //2016 one transaction: Carrefour
        Collection<BankTransactionGroupInterface> groups = result.get(new MonthSlot(6, 2016));
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        Optional<BankTransactionGroupInterface> existentialOptional = groups.stream().findFirst();
        BankTransactionGroupInterface food = testGroupBasic(existentialOptional, "Carrefour");
        assertEquals(food.getTransactionsNumber(), 1);

        //2017 one transaction: Auchan
        groups = result.get(new MonthSlot(1, 2017));
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        existentialOptional = groups.stream().findFirst();
        food = testGroupBasic(existentialOptional, "Auchan");
        assertEquals(food.getTransactionsNumber(), 1);

        //2018 two transactions: Petrom, Employer
        groups = result.get(new MonthSlot(5, 2018));
        assertNotNull(groups);
        //simple transactions are sorted based on actual description not company name, so not reliably.
        assertEquals(groups.size(), 3);

        //2019 one transaction: Employer
        groups = result.get(new MonthSlot(2, 2019));
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        existentialOptional = groups.stream().findFirst();
        food = testGroupBasic(existentialOptional, "Employer");
        assertEquals(food.getTransactionsNumber(), 1);
    }

    @Test
    public void testFindCompanyGroupYearSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Extension calc = new Extension(transactions, userPrefs);
        BankTransactionGroupInterface group = calc.findCompanyGroup(new YearSlot(2018), "Employer");
        assertNull(group);

        calc.categorizeByCompanyNames();
        group = calc.findCompanyGroup(new YearSlot(2018), "Employer");
        assertNotNull(group);

        group = calc.findCompanyGroup(new YearSlot(2018), "Fake");
        assertNull(group);
    }

    @Test
    public void testFindCompanyGroupMonthSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        Extension calc = new Extension(transactions, userPrefs);
        BankTransactionGroupInterface group = calc.findCompanyGroup(new MonthSlot(5, 2018), "Employer");
        assertNull(group);

        calc.categorizeByCompanyNames();
        group = calc.findCompanyGroup(new MonthSlot(5, 2018), "Employer");
        assertNotNull(group);

        group = calc.findCompanyGroup(new MonthSlot(5, 2018), "Fake");
        assertNull(group);
    }

    @Test
    public void testCreateSlottedGroupYearSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Extension calc = new Extension(transactions, userPrefs);
        BankTransactionGroupInterface group = calc.createSlottedGroup(new YearSlot(2018), "Employer");
        assertNull(group);

        calc.categorizeByCompanyNames();
        group = calc.createSlottedGroup(new YearSlot(2018), "Employer");
        assertNotNull(group);
        assertEquals(group, calc.findCompanyGroup(new YearSlot(2018), "Employer"));

        group = calc.createSlottedGroup(new YearSlot(2018), "Revenues");
        assertNotNull(group);
        testFirstSubGroup("Employer", group);
    }

    @Test
    public void testCreateSlottedGroupMonthSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        Extension calc = new Extension(transactions, userPrefs);
        BankTransactionGroupInterface group = calc.createSlottedGroup(new MonthSlot(5, 2018), "Employer");
        assertNull(group);

        calc.categorizeByCompanyNames();
        group = calc.createSlottedGroup(new MonthSlot(5, 2018), "Employer");
        assertNotNull(group);
        assertEquals(group, calc.findCompanyGroup(new MonthSlot(5, 2018), "Employer"));

        group = calc.createSlottedGroup(new MonthSlot(5, 2018), "Revenues");
        assertNotNull(group);
        testFirstSubGroup("Employer", group);
    }

    @Test
    public void testCreateSlottedGroupsYearSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.YEAR);
        Extension calc = new Extension(transactions, userPrefs);
        Map<YearSlot, BankTransactionGroupInterface> groups = calc.createSlottedGroups("Employer");
        assertNotNull(groups);
        assertTrue(groups.keySet().isEmpty());

        calc.categorizeByCompanyNames();
        groups = calc.createSlottedGroups("Employer");
        assertNotNull(groups);
        assertEquals(groups.keySet().size(), 2);
    }

    @Test
    public void testCreateSlottedGroupsMonthSlot() {
        userPrefs.setTransactionGroupingTimeframe(UserPreferences.Timeframe.MONTH);
        Extension calc = new Extension(transactions, userPrefs);
        Map<YearSlot, BankTransactionGroupInterface> groups = calc.createSlottedGroups("Employer");
        assertNotNull(groups);
        assertTrue(groups.keySet().isEmpty());

        calc.categorizeByCompanyNames();
        groups = calc.createSlottedGroups("Employer");
        assertNotNull(groups);
        assertEquals(groups.keySet().size(), 2);
    }

    private BankTransactionGroupInterface testFirstSubGroup(final String categoryName, BankTransactionGroupInterface group) {
        Collection<BankTransactionGroupInterface> subGroups = group.getContainedGroups();
        Optional<BankTransactionGroupInterface> first = subGroups.stream().findFirst();
        return testGroupBasic(first, categoryName);
    }

    private BankTransactionGroupInterface testGroupBasic(Optional<BankTransactionGroupInterface> groupOptional, final String categoryName) {
        assertNotNull(groupOptional);
        BankTransactionGroupInterface actual = groupOptional.get();
        assertNotNull(actual);
        assertNotNull(actual.getCategoryName());
        assertEquals(actual.getCategoryName(), categoryName);
        return actual;
    }

    private class Extension extends TransactionCategorizer {

        public Extension(final Collection<BankTransaction> transactions, final UserPreferences userPrefs) {
            super(transactions, userPrefs);
        }

        @Override
        protected Collection<YearSlot> computeTimeSlots() {
            return super.computeTimeSlots();
        }

        @Override
        protected String getMatchingCategory(String transactionDescription) {
            return super.getMatchingCategory(transactionDescription);
        }

        @Override
        protected BankTransactionGroupInterface findCompanyGroup(YearSlot timeSlot, String category) {
            return super.findCompanyGroup(timeSlot, category);
        }

        @Override
        protected YearSlot createSlot(Date date) {
            return super.createSlot(date);
        }

        @Override
        protected BankTransactionGroupInterface createSlottedGroup(YearSlot timeSlot, String category) {
            return super.createSlottedGroup(timeSlot, category);
        }

        @Override
        protected Map<YearSlot, BankTransactionGroupInterface> createSlottedGroups(String category) {
            return super.createSlottedGroups(category);
        }

        @Override
        protected Map<YearSlot, Collection<BankTransactionGroupInterface>> categorizeByCompanyNames() {
            return super.categorizeByCompanyNames();
        }
    }
}
