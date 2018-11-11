package com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit.BankTransactionContextMenu;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit.BankTransactionEditHandler;
import java.util.Objects;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;

public class UncategorizedTransactionCellRenderer extends ListCell<BankTransaction> {

    private final BankTransactionContextMenu contextMenu;
    private final BankTransactionEditHandler editHandler;

    public UncategorizedTransactionCellRenderer(final BankTransactionContextMenu contextMenu, final BankTransactionEditHandler editHandler) {
        this.contextMenu = Objects.requireNonNull(contextMenu);
        this.editHandler = Objects.requireNonNull(editHandler);
        emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
            emptyStateChanged(isNowEmpty);
        });
        setOnMouseClicked(mouseEvent -> {
            mouseClicked(mouseEvent.getButton(), mouseEvent.getClickCount());
        });
    }

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

    protected void emptyStateChanged(boolean isNowEmpty) {
        if (isNowEmpty) {
            setContextMenu(null);
        } else {
            setContextMenu(contextMenu);
            contextMenu.setBankTransaction(getItem());
        }
    }

    protected void mouseClicked(MouseButton button, int clickCount) {
        if (MouseButton.PRIMARY == button && clickCount == 2) {
            editHandler.editTransaction(getItem());
        }
    }
}
