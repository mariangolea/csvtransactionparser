package com.mariangolea.fintracker.banks.csvparser.impl.preferences;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesAbstractFactory;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesHandlerInterface;

public class UserPreferencesHandlerFactory implements UserPreferencesAbstractFactory {

    private final UserPreferencesHandler handler;

    public UserPreferencesHandlerFactory() {
        handler = new UserPreferencesHandler();
    }

    @Override
    public UserPreferencesHandlerInterface getUserPreferencesHandler() {
        return handler;
    }

}
