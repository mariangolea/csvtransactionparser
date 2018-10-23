package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.ui.edit.BankTransactionGroupEditHandler;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class BankTransactionGroupContextMenu extends ContextMenu {

    BankTransactionGroupInterface group;
    private final BankTransactionGroupEditHandler editHandler;

    public BankTransactionGroupContextMenu(final BankTransactionGroupEditHandler editHandler) {
        this.editHandler = editHandler;
        getItems().add(constructEditMenu());
    }

    public void setBankTransactionGroup(final BankTransactionGroupInterface group) {
        this.group = group;
    }

    protected final MenuItem constructEditMenu() {
        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(e -> {
            edit();
        });
        return edit;
    }

    protected final void edit() {
        if (group == null) {
            return;
        }

        editHandler.editGroup(group);
    }
}
