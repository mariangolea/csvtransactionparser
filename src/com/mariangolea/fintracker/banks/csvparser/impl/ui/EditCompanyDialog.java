package com.mariangolea.fintracker.banks.csvparser.impl.ui;

import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class EditCompanyDialog<P extends Pane, R> extends Dialog<R> {

    public EditCompanyDialog(final String title, final P pane, final Callback<P, R> callback, final Callback<P, Boolean> validation) {
        Objects.requireNonNull(pane);

        setTitle("Edit global company names preferences");
        getDialogPane().setContent(pane);
        ButtonType apply = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        setResultConverter(dialogButton -> {
            if (dialogButton == apply) {
                return callback.call(pane);
            }
            return null;
        });

        getDialogPane().getButtonTypes().addAll(apply, ButtonType.CANCEL);

        if (validation != null) {
            final Button applyButton = (Button) getDialogPane().lookupButton(apply);
            applyButton.addEventFilter(ActionEvent.ACTION, ae -> {
                if (!validation.call(pane)) {
                    ae.consume();
                }
            });
        }
    }
}
