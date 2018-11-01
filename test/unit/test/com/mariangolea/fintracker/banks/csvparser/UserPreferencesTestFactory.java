package test.com.mariangolea.fintracker.banks.csvparser;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesAbstractFactory;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferencesHandler;

public class UserPreferencesTestFactory implements UserPreferencesAbstractFactory {
    private final String folderPath;
    private UserPreferencesHandler handler;
    
    public UserPreferencesTestFactory(final String folderPath){
        this.folderPath = folderPath;
    }
    
    @Override
    public UserPreferencesHandlerInterface getUserPreferencesHandler() {
        if (handler == null){
            handler = new UserPreferencesHandler(folderPath);
        }
        return handler;
    }
}
