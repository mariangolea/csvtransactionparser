package com.mariangolea.fintracker.banks.csvparser.ui.renderer;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import com.mariangolea.fintracker.banks.csvparser.ui.BankTransactionGroupContextMenu;
import com.mariangolea.fintracker.banks.csvparser.ui.edit.BankTransactionGroupEditHandler;
import javafx.collections.MapChangeListener;

import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;

/**
 * @author mariangolea@gmail.com
 */
public class TransactionGroupCellRenderer extends ListCell<BankTransactionCompanyGroup> {
    private final BankTransactionGroupEditHandler editHandler = new BankTransactionGroupEditHandler();
    private final BankTransactionGroupContextMenu contextMenu = new BankTransactionGroupContextMenu(editHandler);
    private final UserPreferences userPrefs;

    public TransactionGroupCellRenderer() {
        setContextMenu(contextMenu);
        setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    editHandler.editGroup(getItem());
                }
            }
        });
        this.userPrefs = UserPreferencesHandler.getInstance().getPreferences();
    }

    @Override
    protected void updateItem(BankTransactionCompanyGroup value, boolean empty) {
        super.updateItem(value, empty);

        if (empty) {
            return;
        }

        String existingKey = userPrefs.getCompanyDescriptionShortFor(value.getCompanyDesc());
        String text = "";
        if (existingKey != null) {
            text += userPrefs.getDisplayName(existingKey);
        } else {
            text += value.getCompanyDesc();
        }
        text += "\n" + value.getGroupIdentifier() + "\n" + value.getTotalAmount();
        setText(text);
        contextMenu.setX(getLayoutX());
        contextMenu.setY(getLayoutY());
        contextMenu.setBankTransactionGroup(value);

        setStyle("-fx-background-color: lavender; selected: skyblue");
        userPrefs.addTransactionDisplayNamesMapListener(new UserPrefsChangeListener(value));
    }

    private class UserPrefsChangeListener implements MapChangeListener<String, String> {

        BankTransactionCompanyGroup value;

        UserPrefsChangeListener(BankTransactionCompanyGroup value) {
            this.value = value;
        }

        @Override
        public void onChanged(Change<? extends String, ? extends String> change) {
            updateItem(value, false);
        }

    }
}
