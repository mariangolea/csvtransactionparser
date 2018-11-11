package test.com.mariangolea.fintracker.banks.csvparser.ui.categorized;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.transaction.BankTransactionDefaultGroup;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.categorized.table.TableViewData;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javafx.collections.FXCollections;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;

public class TableViewDataTest {
    
    @Test
    public void testConstructor(){
        Map<YearSlot, Collection<BankTransactionGroupInterface>> map = FXCollections.observableHashMap();
        Collection<BankTransactionGroupInterface> groups = Arrays.asList(new BankTransactionDefaultGroup("aloha"));
        map.put(new YearSlot(2018), groups);
        UserPreferencesInterface prefs = new UserPreferencesTestFactory().getUserPreferencesHandler().getPreferences();
        TableViewData data = new TableViewData("aloha", true, map, prefs);
        assertNotNull(data);
        assertEquals("aloha", data.getTopMostCategoryString());
        assertEquals(BigDecimal.ZERO.toString(), data.getAmountString(0));
        assertEquals(1, data.getAmountStrings().get().size());
        
        data = new TableViewData("aloha", false, map, prefs);
        assertEquals("aloha", data.getSubCategoryString());
    }
}
