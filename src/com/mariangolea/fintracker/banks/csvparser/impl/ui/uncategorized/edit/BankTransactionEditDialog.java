package com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit;

import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class BankTransactionEditDialog extends Dialog<EditResult> {

    public BankTransactionEditDialog(final BankTransactionEditPane editPane) {
        super();
        Objects.requireNonNull(editPane);
        
        setTitle("Edit company name and apply to similar transactions");
        getDialogPane().setContent(editPane);
        ButtonType apply = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        setResultConverter(dialogButton -> {
            if (dialogButton == apply) {
                return editPane.getEditResult();
            }
            return null;
        });

        getDialogPane().getButtonTypes().addAll(apply, ButtonType.CANCEL);

        final Button applyButton = (Button) getDialogPane().lookupButton(apply);
        applyButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (!editPane.isValid()) {
                ae.consume();
            }
        });
    }

}
