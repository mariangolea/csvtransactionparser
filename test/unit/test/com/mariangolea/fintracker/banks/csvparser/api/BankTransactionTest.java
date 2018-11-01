package test.com.mariangolea.fintracker.banks.csvparser.api;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.impl.parsers.bancatransilvania.BTParser;
import java.math.BigDecimal;
import test.com.mariangolea.fintracker.banks.csvparser.TestUtilities;

public class BankTransactionTest {

    @Test
    public void testBankTransaction() {
        BTParser bt = new BTParser();
        Date date = bt.parseCompletedDate("19-08-2018");
        BankTransaction first = createTransaction("description", BigDecimal.ONE);
        String toString = first.toString();
        assertTrue(toString != null);
        assertTrue(first.startDate.equals(date));
        assertTrue(first.completedDate.equals(date));
        assertTrue(first.creditAmount == BigDecimal.ONE);
        assertTrue(first.debitAmount == BigDecimal.ZERO);
        assertTrue(first.description.equals("description"));
        assertTrue(first.getCsvContent().equals(Arrays.asList("one", "two")));
        assertTrue(first.getCSVContentLines() == 2);

        BankTransaction second = createTransaction("description", BigDecimal.ONE);

        assertTrue(first.equals(second));
        assertTrue(first.hashCode() == second.hashCode());
    }

    @Test
    public void testSort() {
        BankTransaction first = createTransaction("a", BigDecimal.ONE, TestUtilities.createDate(1, 2018));
        BankTransaction second = createTransaction("a", BigDecimal.ONE, TestUtilities.createDate(2, 2018));
        
        assertTrue(first.compareTo(second) < 0);
        assertTrue(first.compareTo(first) == 0);
        assertTrue(second.compareTo(first) > 0);
        
        second = createTransaction("ab", BigDecimal.ONE, TestUtilities.createDate(1, 2018));
        assertTrue(first.compareTo(second) < 0);
    }

    protected BankTransaction[] createTestTransactions() {
        return new BankTransaction[]{createTransaction("description", BigDecimal.ONE), createTransaction("description", BigDecimal.ONE)};
    }

    protected BankTransaction createTransaction(final String category, final BigDecimal amount) {
        BTParser bt = new BTParser();
        Date date = bt.parseCompletedDate("19-08-2018");
        return createTransaction(category, amount, date);
    }

    protected BankTransaction createTransaction(final String category, final BigDecimal amount, final Date completed) {
        BankTransaction first = new BankTransaction(completed, completed, amount, BigDecimal.ZERO, category,
                Arrays.asList("one", "two"));
        return first;
    }
}
