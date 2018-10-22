package com.mariangolea.fintracker.banks.csvparser.ui.renderer;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import javafx.collections.MapChangeListener;

import javafx.scene.control.TreeCell;

public class TransactionGroupCellRenderer extends TreeCell<BankTransactionGroupInterface> {
    UserPreferencesHandler userPrefsHandler =  UserPreferencesHandler.INSTANCE;
    public TransactionGroupCellRenderer() {
    }

    @Override
    protected void updateItem(BankTransactionGroupInterface value, boolean empty) {
        super.updateItem(value, empty);
        if (empty || value == null) {
            setText(null);
            return;
        }

        String text = "";
        if (value.getUserDefinedCategory() != null) {
            String existingKey = userPrefsHandler.getPreferences().getCompanyNameShortFor(value.getUserDefinedCategory());
            if (existingKey != null) {
                text += userPrefsHandler.getPreferences().getDisplayName(existingKey);
            } else {
                text += value.getUserDefinedCategory();
            }
        }
        text += "\n" + value.getGroupIdentifier() + "\n" + value.getTotalAmount();
        setText(text);

        setStyle("-fx-background-color: lavender; selected: skyblue");
        userPrefsHandler.getPreferences().addCompanyNamesMapListener(new UserPrefsChangeListener(value));
    }

    private class UserPrefsChangeListener implements MapChangeListener<String, String> {

        BankTransactionGroupInterface value;

        UserPrefsChangeListener(BankTransactionGroupInterface value) {
            this.value = value;
        }

        @Override
        public void onChanged(Change<? extends String, ? extends String> change) {
            updateItem(value, false);
        }

    }
}
