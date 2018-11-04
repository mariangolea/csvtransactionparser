package test.com.mariangolea.fintracker.banks.csvparser;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesAbstractFactory;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferencesHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class UserPreferencesTestFactory implements UserPreferencesAbstractFactory {
    private UserPreferencesHandler handler;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    public UserPreferencesTestFactory(){
        try {
            folder.create();
            handler = new UserPreferencesHandler(TestUtilities.createFolder(folder, "prefsTest"));
        } catch (IOException ex) {
            Logger.getLogger(UserPreferencesTestFactory.class.getName()).log(Level.SEVERE, null, ex);
            handler = new UserPreferencesHandler("prefsTest");
        }
    }
    
    @Override
    public UserPreferencesHandlerInterface getUserPreferencesHandler() {
        return handler;
    }
}
