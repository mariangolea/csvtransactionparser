package test.com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.transaction.BankTransactionCompanyGroup;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class BankTransactionCompanyGroupTest extends BankTransactionTest {

    @Test
    public void testAdd() {
        BankTransaction[] legal = createTestTransactions();
        Extension firstGroup = new Extension("description");
        firstGroup.addTransaction(legal[0]);
        firstGroup.addTransactions(Arrays.asList(legal[1]));

        assertTrue(firstGroup.getTotalAmount().intValue() == 2);
        assertTrue(firstGroup.getTransactionsNumber() == 2);
        assertTrue(firstGroup.getGroupsNumber() == 0);
        assertTrue(firstGroup.getCategoryName().equals("description"));
        assertTrue(firstGroup.getContainedTransactions().equals(Arrays.asList(legal)));
        assertNull(firstGroup.getContainedGroups());
        String toString = firstGroup.toString();
        assertTrue(toString != null
                && !toString.isEmpty()
                && toString.contains("description")
                && toString.contains(firstGroup.getTotalAmount().floatValue() + ""));
    }

    @Test
    public void testHashEquals() {
        BankTransaction[] legal = createTestTransactions();
        Extension firstGroup = new Extension("description");
        firstGroup.addTransactions(Arrays.asList(legal));

        BankTransaction[] legalCloned = createTestTransactions();
        Extension secondGroup = new Extension("description");
        secondGroup.addTransactions(Arrays.asList(legalCloned));

        assertTrue(firstGroup.equals(secondGroup));
        assertTrue(firstGroup.hashCode() == secondGroup.hashCode());
    }

    protected static class Extension extends BankTransactionCompanyGroup {

        public Extension(String companyDesc) {
            super(companyDesc);
        }

        @Override
        protected void addTransactions(Collection<BankTransaction> parsedTransactions) {
            super.addTransactions(parsedTransactions);
        }

        @Override
        protected void addTransaction(BankTransaction parsedTransaction) {
            super.addTransaction(parsedTransaction);
        }
    }
}
