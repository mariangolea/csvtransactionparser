package test.com.mariangolea.fintracker.banks.csvparser.ui.categorized;

import com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.EditCompanyNamesPane;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single.EditCompanyNamePane;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;
import test.com.mariangolea.fintracker.banks.csvparser.ui.FXUITest;

public class CompanyNamesEditerTest extends FXUITest {

    @Test
    public void testEditPanes() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        EditCompanyNamePane single = new EditCompanyNamePane("aloha", Arrays.asList("one"));
        assertNotNull(single.getResult());
        EditCompanyNamesPane multi = new EditCompanyNamesPane(new UserPreferencesTestFactory().getUserPreferencesHandler().getPreferences());
        assertNotNull(multi.getResult());
    }

    @Test
    public void testEventHandlers() {
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }

        Extension single = new Extension("aloha", Arrays.asList("one"));
        assertNotNull(single.getResult());
        single.searchFieldTextChanged("o");
        assertTrue(single.getAddBtnEnabledState());
        
        single.searchFieldTextChanged("b");
        single.addButtonClicked();
        assertNull(single.getSearchFieldText());
    }

    private class Extension extends EditCompanyNamePane {

        Extension(final String companyName, final Collection<String> companyIdentifiers) {
            super(companyName, companyIdentifiers);
        }

        @Override
        protected void addButtonClicked() {
            super.addButtonClicked();
        }

        @Override
        protected void searchFieldTextChanged(String filter) {
            super.searchFieldTextChanged(filter);
        }

        private boolean getAddBtnEnabledState() {
            return add.disableProperty().get();
        }

        private String getSearchFieldText() {
            return searchField.getText();
        }

    }
}
