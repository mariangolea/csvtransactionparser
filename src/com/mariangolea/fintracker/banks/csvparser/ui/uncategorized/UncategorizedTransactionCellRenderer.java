package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;

public class UncategorizedTransactionCellRenderer extends ListCell<BankTransaction>{

    @Override
    protected void updateItem(BankTransaction item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            return;
        }

        setText(item.toString());
        setTooltip(new Tooltip(item.description));
    }
    
}
