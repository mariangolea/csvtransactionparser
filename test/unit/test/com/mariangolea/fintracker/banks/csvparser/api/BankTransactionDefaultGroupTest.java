package test.com.mariangolea.fintracker.banks.csvparser.api;

import org.junit.Test;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionDefaultGroup;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;

public class BankTransactionDefaultGroupTest extends BankTransactionCompanyGroupTest {

    @Test
    public void testGroup() {
        BankTransaction[] legal = createLegalTestTransactions();
        BankTransactionCompanyGroupTest.Extension companyGroup = new BankTransactionCompanyGroupTest.Extension("description");
        companyGroup.addTransactions(Arrays.asList(legal));
        Extension firstGroup = new Extension("description");
        firstGroup.addGroup(companyGroup);

        assertTrue(firstGroup.getContainedGroups() != null && firstGroup.getContainedGroups().size() == 1 && firstGroup.getContainedGroups().get(0) == companyGroup);
        assertTrue(firstGroup.getTransactionsNumber() == 2);
        assertTrue(firstGroup.getGroupsNumber() == 1);
    }

    private class Extension extends BankTransactionDefaultGroup {

        public Extension(String companyDesc) {
            super(companyDesc);
        }

        @Override
        public void addGroup(BankTransactionGroupInterface group) {
            super.addGroup(group); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
