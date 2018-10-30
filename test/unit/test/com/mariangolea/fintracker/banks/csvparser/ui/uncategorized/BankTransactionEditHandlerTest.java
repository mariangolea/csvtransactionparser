package test.com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit.BankTransactionEditHandler;
import com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit.EditResult;
import com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit.UncategorizedTransactionApplyListener;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BankTransactionEditHandlerTest {

    @Test
    public void testApplyEditResult() {
        UncategorizedTransactionApplyListener listener = () -> {
        };
        Extension handler = new Extension(listener);
        EditResult result = new EditResult("a", "b", "c", "d");
        handler.applyResult(result);

        UserPreferences prefs = UserPreferencesHandler.INSTANCE.getPreferences();
        assertEquals("b", prefs.getCompanyDisplayName("a"));
        assertEquals("c", prefs.getParent("b"));
        assertEquals("d", prefs.getParent("c"));
        UserPreferencesHandler.INSTANCE.deletePreferences();
    }

    private class Extension extends BankTransactionEditHandler {

        public Extension(UncategorizedTransactionApplyListener applyListener) {
            super(applyListener);
        }

        @Override
        protected void applyResult(EditResult result) {
            super.applyResult(result);
        }
    }
}
