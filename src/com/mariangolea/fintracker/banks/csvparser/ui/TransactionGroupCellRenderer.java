package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * @author mariangolea@gmail.com
 */
public class TransactionGroupCellRenderer extends ListCell<BankTransactionCompanyGroup> {

    private final ListView<BankTransactionCompanyGroup> param;
    private final BankTransactionGroupContextMenu contextMenu = new BankTransactionGroupContextMenu();
    private final UserPreferences userPrefs;

    public TransactionGroupCellRenderer(ListView<BankTransactionCompanyGroup> param) {
        this.param = param;
        setContextMenu(contextMenu);
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
        if (existingKey != null){
            text += userPrefs.getDisplayName(existingKey);
        } else{
            text += value.getCompanyDesc();
        }
        text += "\n" + value.getGroupIdentifier() + "\n" + value.getTotalAmount();
        setText(text);
        contextMenu.setX(getLayoutX());
        contextMenu.setY(getLayoutY());
        contextMenu.setBankTransactionGroup(value);

        setStyle("-fx-background-color: lavender; selected: skyblue");
    }
}
