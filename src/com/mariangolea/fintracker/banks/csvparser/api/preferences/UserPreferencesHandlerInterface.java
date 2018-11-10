package com.mariangolea.fintracker.banks.csvparser.api.preferences;

public interface UserPreferencesHandlerInterface {

    public UserPreferencesInterface getPreferences();
    
    public UserPreferencesInterface deepCopyPreferences(UserPreferencesInterface original);

    public boolean storePreferences();

}
