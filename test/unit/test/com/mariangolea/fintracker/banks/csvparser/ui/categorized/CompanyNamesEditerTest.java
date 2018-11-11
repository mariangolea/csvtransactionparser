package test.com.mariangolea.fintracker.banks.csvparser.ui.categorized;

import com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.EditCompanyNamesPane;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single.EditCompanyNamePane;
import java.util.Arrays;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import test.com.mariangolea.fintracker.banks.csvparser.UserPreferencesTestFactory;
import test.com.mariangolea.fintracker.banks.csvparser.ui.FXUITest;

public class CompanyNamesEditerTest extends FXUITest{
    
    @Test
    public void testEditPanes(){
        if (!FXUITest.FX_INITIALIZED) {
            assertTrue("Useless in headless mode", true);
            return;
        }
        
        EditCompanyNamePane single = new EditCompanyNamePane("aloha", Arrays.asList("one"));
        assertNotNull(single.getResult());
        EditCompanyNamesPane multi = new EditCompanyNamesPane(new UserPreferencesTestFactory().getUserPreferencesHandler().getPreferences());
        assertNotNull(multi.getResult());
    }
}
