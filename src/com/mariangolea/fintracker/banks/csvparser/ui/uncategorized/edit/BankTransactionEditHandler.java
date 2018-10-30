package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class BankTransactionEditHandler {

    private final UserPreferences userPrefs = UserPreferencesHandler.INSTANCE.getPreferences();
    private BankTransactionEditDialog editPopup;
    private final UncategorizedTransactionApplyListener applyListener;

    public BankTransactionEditHandler(UncategorizedTransactionApplyListener applyListener) {
        this.applyListener = Objects.requireNonNull(applyListener);
    }

    public void editTransaction(final BankTransaction group) {
        if (editPopup == null) {
            editPopup = new BankTransactionEditDialog();
        }
        editPopup.setBankTransaction(group);
        Optional<EditResult> result = editPopup.showAndWait();
        result.ifPresent(userData -> {
            userPrefs.setCompanyDisplayName(userData.companyIdentifierString, userData.companyDisplayName);
            userPrefs.appendDefinition(userData.categoryName, Arrays.asList(userData.companyDisplayName));
            if (userData.parentCategory != null && !userData.parentCategory.isEmpty()) {
                userPrefs.appendDefinition(userData.parentCategory, Arrays.asList(userData.categoryName));
            }
            applyListener.transactionEditApplyed();
        });
    }
}
