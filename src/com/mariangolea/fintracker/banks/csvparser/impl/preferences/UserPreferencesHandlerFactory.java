package com.mariangolea.fintracker.banks.csvparser.impl.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesAbstractFactory;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;

public class UserPreferencesHandlerFactory implements UserPreferencesAbstractFactory{
    
    @Override
    public UserPreferencesHandlerInterface getUserPreferencesHandler() {
        return new UserPreferencesHandler();
    }
    
}
