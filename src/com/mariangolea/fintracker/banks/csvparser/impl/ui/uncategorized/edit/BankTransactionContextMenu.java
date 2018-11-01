package com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class BankTransactionContextMenu extends ContextMenu {

    BankTransaction transaction;
    private final BankTransactionEditHandler editHandler;

    public BankTransactionContextMenu(final BankTransactionEditHandler editHandler) {
        this.editHandler = editHandler;
        getItems().add(constructEditMenu());
    }

    public void setBankTransaction(final BankTransaction group) {
        this.transaction = group;
    }

    protected final MenuItem constructEditMenu() {
        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(e -> {
            edit();
        });
        return edit;
    }

    protected void edit() {
        if (transaction == null) {
            return;
        }

        editHandler.editTransaction(transaction);
    }
}
