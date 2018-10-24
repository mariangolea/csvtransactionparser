package test.com.mariangolea.fintracker.banks.csvparser.parsers;

import com.mariangolea.fintracker.banks.csvparser.api.Bank;
import com.mariangolea.fintracker.banks.csvparser.parsers.AbstractBankParser;
import com.mariangolea.fintracker.banks.csvparser.parsers.BankCSVParserFactory;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class BankCSVParserFactoryTest {
    
    @Test
    public void testFactory(){
        AbstractBankParser res = BankCSVParserFactory.getInstance(Bank.ING);
        assertNotNull(res);
        
        res = BankCSVParserFactory.getInstance(Bank.BT);
        assertNotNull(res);
        
        res = BankCSVParserFactory.getInstance(null);
        assertNull(res);
    }
}
