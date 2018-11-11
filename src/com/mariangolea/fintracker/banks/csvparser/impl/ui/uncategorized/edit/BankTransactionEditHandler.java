package com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.EditDialog;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class BankTransactionEditHandler {

    private final UserPreferencesInterface userPrefs;
    private EditDialog editPopup;
    private BankTransactionEditPane editPane;
    private final UncategorizedTransactionApplyListener applyListener;

    public BankTransactionEditHandler(final UncategorizedTransactionApplyListener applyListener, final UserPreferencesInterface userPrefs) {
        this.applyListener = Objects.requireNonNull(applyListener);
        this.userPrefs = Objects.requireNonNull(userPrefs);
    }

    public void editTransaction(final BankTransaction transaction) {
        if (editPopup == null) {
            editPane = new BankTransactionEditPane(userPrefs);
            editPopup = new EditDialog<>("Edit company name and apply to similar transactions", editPane, BankTransactionEditPane::getEditResult, BankTransactionEditPane::isValid);
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
