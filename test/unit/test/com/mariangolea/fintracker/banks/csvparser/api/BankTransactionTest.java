package test.com.mariangolea.fintracker.banks.csvparser.api;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.BTParser;
import java.math.BigDecimal;

public class BankTransactionTest {

    @Test
    public void testBankTransaction() {
        BTParser bt = new BTParser();
        Date date = bt.parseCompletedDate("19-08-2018");
        BankTransaction first = createTransaction("description", BigDecimal.ONE);
        String toString = first.toString();
        assertTrue(toString != null);
        assertTrue(first.startDate == date);
        assertTrue(first.completedDate == date);
        assertTrue(first.creditAmount == BigDecimal.ZERO);
        assertTrue(first.debitAmount == BigDecimal.ZERO);
        assertTrue(first.description.equals("description"));
        assertTrue(first.getCsvContent().equals(Arrays.asList("one", "two")));
        assertTrue(first.getCSVContentLines()== 2);

        BankTransaction second = createTransaction("description", BigDecimal.ZERO);

        assertTrue(first.equals(second));
        assertTrue(first.hashCode() == second.hashCode());
        second.getCsvContent().add("hello");
        assertTrue(!first.equals(second));
    }

    protected BankTransaction[] createLegalTestTransactions() {
        return new BankTransaction[]{createTransaction("description", BigDecimal.ONE), createTransaction("description", BigDecimal.ONE)};
    }

    protected BankTransaction[] createIllegalTestTransactions() {
        return new BankTransaction[]{createTransaction("uugh", BigDecimal.ONE), createTransaction("", BigDecimal.ONE)};
    }

    protected BankTransaction createTransaction(final String category, final BigDecimal amount) {
        BTParser bt = new BTParser();
        Date date = bt.parseCompletedDate("19-08-2018");
        BankTransaction first = new BankTransaction(date, date, amount, BigDecimal.ZERO, "uugh",
                Arrays.asList("one", "two"));

        return first;
    }
}
