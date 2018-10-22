package test.com.mariangolea.fintracker.banks.csvparser.api;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.parsers.impl.BTParser;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class BankTransactionCompanyGroupTest{

    @Test
    public void testLegalAdd() {
        BankTransaction[] legal = createLegalTestTransactions();
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
    public void testIllegalAdd() {
        BankTransaction[] illegal = createIllegalTestTransactions();
        Extension firstGroup = new Extension("description");
        boolean success = firstGroup.addTransaction(illegal[0]);
        assertTrue(!success);

        List<BankTransaction> notAdded = firstGroup.addTransactions(Arrays.asList(illegal[1]));
        assertTrue(notAdded != null && notAdded.size() == 1 && notAdded.get(0) == illegal[1]);
        
        assertTrue(firstGroup.getTransactionsNumber() == 0);
        assertTrue(firstGroup.getGroupsNumber() == 0);
        assertNull(firstGroup.getContainedGroups());
    }

    @Test
    public void testHashEquals() {
        BankTransaction[] legal = createLegalTestTransactions();
        Extension firstGroup = new Extension("description");
        firstGroup.addTransactions(Arrays.asList(legal));

        BankTransaction[] legalCloned = createLegalTestTransactions();
        Extension secondGroup = new Extension("description");
        secondGroup.addTransactions(Arrays.asList(legalCloned));

        assertTrue(firstGroup.equals(secondGroup));
        assertTrue(firstGroup.hashCode() == secondGroup.hashCode());
    }

    protected BankTransaction[] createLegalTestTransactions() {
        BTParser bt = new BTParser();
        Date date = bt.parseCompletedDate("19-08-2018");
        BankTransaction first = new BankTransaction(date, date, BigDecimal.ONE, BigDecimal.ZERO, "description",
                Arrays.asList("one", "two"));
        BankTransaction second = new BankTransaction(date, date, BigDecimal.ONE, BigDecimal.ZERO, "description",
                Arrays.asList("one", "two"));

        return new BankTransaction[]{first, second};
    }

    protected BankTransaction[] createIllegalTestTransactions() {
        BTParser bt = new BTParser();
        Date date = bt.parseCompletedDate("19-08-2018");
        BankTransaction first = new BankTransaction(date, date, BigDecimal.ONE, BigDecimal.ZERO, "uugh",
                Arrays.asList("one", "two"));
        BankTransaction second = new BankTransaction(date, date, BigDecimal.ONE, BigDecimal.ZERO, "",
                Arrays.asList("one", "two"));

        return new BankTransaction[]{first, second};
    }
    
    protected static class Extension extends BankTransactionCompanyGroup{

        public Extension(String companyDesc) {
            super(companyDesc);
        }

        @Override
        protected List<BankTransaction> addTransactions(List<BankTransaction> parsedTransactions) {
            return super.addTransactions(parsedTransactions); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected boolean addTransaction(BankTransaction parsedTransaction) {
            return super.addTransaction(parsedTransaction); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
