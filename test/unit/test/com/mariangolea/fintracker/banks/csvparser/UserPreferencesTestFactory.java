package test.com.mariangolea.fintracker.banks.csvparser;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesAbstractFactory;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.UserPreferencesHandler;

public class UserPreferencesTestFactory implements UserPreferencesAbstractFactory {

    public static final String TEST_FOLDER_NAME = "unittests";

    @Override
    public UserPreferencesHandlerInterface getUserPreferencesHandler() {
        return new UserPreferencesHandler(TEST_FOLDER_NAME);
    }
}
