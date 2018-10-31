package com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit.UncategorizedTransactionApplyListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class BankTransactionEditHandler {

    private final UserPreferencesInterface userPrefs;
    private BankTransactionEditDialog editPopup;
    private BankTransactionEditPane editPane;
    private final UncategorizedTransactionApplyListener applyListener;

    public BankTransactionEditHandler(UncategorizedTransactionApplyListener applyListener, final UserPreferencesInterface userPrefs) {
        this.applyListener = Objects.requireNonNull(applyListener);
        this.userPrefs = Objects.requireNonNull(userPrefs);
    }

    public void editTransaction(final BankTransaction transaction) {
        if (editPopup == null) {
            editPane = new BankTransactionEditPane(userPrefs);
            editPane.setBankTransaction(transaction);
            editPopup = new BankTransactionEditDialog(editPane);
        }
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
