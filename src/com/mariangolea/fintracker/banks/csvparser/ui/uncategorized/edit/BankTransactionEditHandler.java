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
    private BankTransactionEditPane editPane;
    private final UncategorizedTransactionApplyListener applyListener;

    public BankTransactionEditHandler(UncategorizedTransactionApplyListener applyListener) {
        this.applyListener = Objects.requireNonNull(applyListener);
    }

    public void editTransaction(final BankTransaction transaction) {
        if (editPopup == null) {
            editPane = new BankTransactionEditPane();
            editPopup = new BankTransactionEditDialog(editPane);
        }
        editPane.setBankTransaction(transaction);
        Optional<EditResult> result = editPopup.showAndWait();
        result.ifPresent(userData -> {
            applyResult(userData);
        });
    }

    protected void applyResult(final EditResult result) {
        userPrefs.setCompanyDisplayName(result.companyIdentifierString, result.companyDisplayName);
        userPrefs.appendDefinition(result.categoryName, Arrays.asList(result.companyDisplayName));
        if (result.parentCategory != null && !result.parentCategory.isEmpty()) {
            userPrefs.appendDefinition(result.parentCategory, Arrays.asList(result.categoryName));
        }
        applyListener.transactionEditApplied();
    }
}
