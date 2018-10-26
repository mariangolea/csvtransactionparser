package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class UncategorizedTransactionGroupCellRenderer extends ListCell<BankTransactionGroupInterface> {

    private final ListView<BankTransactionGroupInterface> param;

    public UncategorizedTransactionGroupCellRenderer(ListView<BankTransactionGroupInterface> param) {
        this.param = param;
    }

    @Override
    protected void updateItem(BankTransactionGroupInterface value, boolean empty) {
        super.updateItem(value, empty);
        if (empty) {
            return;
        }
        String[] split = value.toString().split("\n");
        String text = "";
        for (String split1 : split) {
            text += split1;
        }
        setText(text);

        setStyle("-fx-background-color: lavender; selected: skyblue");
    }
}