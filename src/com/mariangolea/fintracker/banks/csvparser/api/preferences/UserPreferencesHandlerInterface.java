package com.mariangolea.fintracker.banks.csvparser.api.preferences;

public interface UserPreferencesHandlerInterface {

    public UserPreferencesInterface getPreferences();

    public boolean storePreferences();

}
