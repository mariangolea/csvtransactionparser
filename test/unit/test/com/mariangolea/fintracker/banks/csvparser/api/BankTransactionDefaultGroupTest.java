package test.com.mariangolea.fintracker.banks.csvparser.api;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionDefaultGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BankTransactionDefaultGroupTest extends BankTransactionCompanyGroupTest {

    @Test
    public void testGroup() {
        BankTransaction[] legal = createTestTransactions();
        Extension defaultGroup = new Extension("description");
        defaultGroup.addTransactions(Arrays.asList(legal));
        Extension firstGroup = new Extension("description");
        firstGroup.addGroup(defaultGroup);

        assertTrue(firstGroup.getContainedGroups() != null && firstGroup.getContainedGroups().size() == 1 && firstGroup.getContainedGroups().get(0) == defaultGroup);
        assertTrue(firstGroup.getTransactionsNumber() == 2);
        assertTrue(firstGroup.getGroupsNumber() == 1);
    }

    @Test
    public void testEqualsHashCode() {
        Extension first = createGroup();
        assertTrue(first.equals(first));

        Extension second = createGroup();
        assertTrue(first.equals(second));
        assertTrue(first.hashCode() == second.hashCode());

        Extension third = null;
        assertFalse(first.equals(third));
    }

    private Extension createGroup() {
        BankTransaction[] legal = createTestTransactions();
        Extension defaultGroup = new Extension("description");
        defaultGroup.addTransactions(Arrays.asList(legal));
        return defaultGroup;
    }

    private class Extension extends BankTransactionDefaultGroup {

        public Extension(String companyDesc) {
            super(companyDesc);
        }

        @Override
        public void addGroup(BankTransactionGroupInterface group) {
            super.addGroup(group);
        }

        @Override
        protected void addTransaction(BankTransaction parsedTransaction) {
            super.addTransaction(parsedTransaction);
        }

        @Override
        protected void addTransactions(Collection<BankTransaction> parsedTransactions) {
            super.addTransactions(parsedTransactions);
        }
    }
}
