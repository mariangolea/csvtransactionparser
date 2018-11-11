package test.com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.UncategorizedTransactionCellRenderer;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit.BankTransactionContextMenu;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit.BankTransactionEditHandler;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit.UncategorizedTransactionApplyListener;
import java.math.BigDecimal;
import java.util.Date;
import javafx.event.EventTarget;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;
import test.com.mariangolea.fintracker.banks.csvparser.Utilities;
import test.com.mariangolea.fintracker.banks.csvparser.ui.FXUITest;

public class UncategorizedTransactionCellRendererTest extends FXUITest {

    @Test
    public void testConstructor() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UncategorizedTransactionApplyListener listener = () -> {
        };
        BankTransactionEditHandler handler= new BankTransactionEditHandler(listener, factory.getUserPreferencesHandler().getPreferences());
        Extension rend = new Extension(new BankTransactionContextMenu(handler), handler);

        rend.updateItem(null, true);
        assertNull(rend.getText());

        BankTransaction transaction = Utilities.createTransaction(new Date(), BigDecimal.ZERO, BigDecimal.ZERO, "aloha");
        rend.updateItem(transaction, false);
        assertEquals(rend.getText(), transaction.toString());
    }
    
    @Test
    public void testEvents(){
        UserPreferencesTestFactory factory = new UserPreferencesTestFactory();
        UncategorizedTransactionApplyListener listener = () -> {
        };
        BankTransactionEditHandler handler= new BankTransactionEditHandler(listener, factory.getUserPreferencesHandler().getPreferences());
        BankTransactionContextMenu contextMenu = new BankTransactionContextMenu(handler);
        Extension rend = new Extension(contextMenu, handler);
        
        //if it were 2, popup will trigger and fail the test since app is not on a FX thread in unit test mode!
        rend.mouseClicked(MouseButton.PRIMARY, 1);
        
        rend.emptyStateChanged(true);
        assertNull(rend.getContextMenu());
        
        rend.emptyStateChanged(false);
        assertEquals(contextMenu, rend.getContextMenu());
    }

    private class Extension extends UncategorizedTransactionCellRenderer {

        Extension(final BankTransactionContextMenu contextMenu, final BankTransactionEditHandler editHandler){
            super(contextMenu, editHandler);
        }
        @Override
        protected void updateItem(BankTransaction item, boolean empty) {
            super.updateItem(item, empty);
        }

        @Override
        protected void mouseClicked(MouseButton button, int clickCount) {
            super.mouseClicked(button, clickCount); 
        }

        @Override
        protected void emptyStateChanged(boolean isNowEmpty) {
            super.emptyStateChanged(isNowEmpty); 
        }
    }
}
