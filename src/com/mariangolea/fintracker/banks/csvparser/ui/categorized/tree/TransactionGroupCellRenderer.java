package com.mariangolea.fintracker.banks.csvparser.ui.categorized.tree;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.Collection;
import javafx.collections.MapChangeListener;

import javafx.scene.control.TreeCell;

public class TransactionGroupCellRenderer extends TreeCell<BankTransactionGroupInterface> {

    UserPreferencesHandler userPrefsHandler = UserPreferencesHandler.INSTANCE;

    @Override
    protected void updateItem(BankTransactionGroupInterface value, boolean empty) {
        super.updateItem(value, empty);
        if (empty || value == null) {
            setText(null);
            return;
        }

        setText(value.toString());
        setStyle("-fx-background-color: lavender; selected: skyblue");
    }
}
