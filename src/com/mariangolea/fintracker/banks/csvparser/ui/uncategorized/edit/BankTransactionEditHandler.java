package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.Arrays;
import java.util.Optional;

public class BankTransactionEditHandler {

    private final UserPreferences userPrefs = UserPreferencesHandler.INSTANCE.getPreferences();
    private BankTransactionEditDialog editPopup;

    public void editTransaction(final BankTransaction group) {
        if (editPopup == null) {
            editPopup = new BankTransactionEditDialog();
        }
        editPopup.setBankTransaction(group);
        Optional<EditResult> result = editPopup.showAndWait();
        result.ifPresent(userData -> {
            if (userData.companyNameDefinition != null) {
                userPrefs.setCompanyDisplayName(userData.companyNameDefinition.getKey(), userData.companyNameDefinition.getValue());
            }
            if (userData.categoryNameDefinition != null) {
                userPrefs.appendDefinition(userData.categoryNameDefinition.getKey(), Arrays.asList(userData.categoryNameDefinition.getValue()));
            }
        });
    }
}
