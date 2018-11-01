package test.com.mariangolea.fintracker.banks.csvparser.ui.categorized;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.categorized.table.TableViewData;
import com.mariangolea.fintracker.banks.csvparser.transaction.BankTransactionDefaultGroup;
import java.math.BigDecimal;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class TableViewDataTest {
    
    @Test
    public void testConstructor(){
        TableViewData data = new TableViewData(new YearSlot(2018), Arrays.asList(new BankTransactionDefaultGroup("aloha")));
        assertNotNull(data);
        assertEquals("2018", data.getTimeSlotString());
        assertEquals(BigDecimal.ZERO.toString(), data.getAmountString(0));
        assertEquals(1, data.getAmountStrings().get().size());
    }
}
