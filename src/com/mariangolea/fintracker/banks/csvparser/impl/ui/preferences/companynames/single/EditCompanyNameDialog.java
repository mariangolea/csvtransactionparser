package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single;

import java.util.Objects;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class EditCompanyNameDialog extends Dialog<EditCompanyNameResult> {

    public EditCompanyNameDialog(final EditCompanyNamePane editPane) {
        Objects.requireNonNull(editPane);

        setTitle("Edit global company names preferences");
        getDialogPane().setContent(editPane);
        ButtonType apply = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        setResultConverter(dialogButton -> {
            if (dialogButton == apply) {
                return editPane.getResult();
            }
            return null;
        });

        getDialogPane().getButtonTypes().addAll(apply, ButtonType.CANCEL);
    }

}
