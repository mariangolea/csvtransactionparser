package test.com.mariangolea.fintracker.banks.csvparser.ui.categorized;

import com.mariangolea.fintracker.banks.csvparser.api.filters.YearSlot;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.categorized.table.TransactionTableView;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.categorized.table.TransactionTableView.AmountStringPropertyValueFactory;
import com.mariangolea.fintracker.banks.csvparser.transaction.TransactionsCategorizedSlotter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleObjectProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;
import test.com.mariangolea.fintracker.banks.csvparser.Utilities;
import test.com.mariangolea.fintracker.banks.csvparser.ui.FXUITest;

public class TransactionTableViewTest extends FXUITest{

    @Test
    public void testColumns() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        BankTransaction transaction = Utilities.createTransaction(Utilities.createDate(1, 2017), BigDecimal.ONE, BigDecimal.ZERO, "company1 identifier");
        UserPreferencesInterface prefs = factory.getUserPreferencesHandler().getPreferences();
        prefs.setTransactionGroupingTimeframe(UserPreferencesInterface.Timeframe.MONTH);
        prefs.setCompanyDisplayName("company1", "Company");
        prefs.appendDefinition("CompanyGroup", Arrays.asList("Company"));

        TransactionTableView view = new TransactionTableView(prefs);
        TransactionsCategorizedSlotter slotter = new TransactionsCategorizedSlotter(Arrays.asList(transaction), prefs);
        Map<YearSlot, Collection<BankTransactionGroupInterface>> model = slotter.getUnmodifiableSlottedCategorized();
        view.resetView(model);

        assertEquals(2, view.getColumns().size());
        
        Extension cellFactory = new Extension(1);
        SimpleObjectProperty<List<String>> props = new SimpleObjectProperty<>(Arrays.asList("","CompanyGroup"));
        cellFactory.updateItem(props, true);
        assertNull(cellFactory.getText());
        
        cellFactory.updateItem(props, false);
        assertEquals("CompanyGroup", cellFactory.getText());
    }
    
    private class Extension extends AmountStringPropertyValueFactory{
        
        public Extension(int column) {
            super(column);
        }

        @Override
        protected void updateItem(SimpleObjectProperty<List<String>> item, boolean empty) {
            super.updateItem(item, empty); 
        }
    }
}
