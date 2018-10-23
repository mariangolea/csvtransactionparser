package com.mariangolea.fintracker.banks.csvparser.ui.edit;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import javafx.application.Platform;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class BankTransactionEditDialog extends Dialog<Pair<String,String>> {

    private BankTransactionGroupInterface group;
    private TextField companyNameField;
    private TextField displayCompanyNameField;
    private TextField companyNameSubstringField;
    private TextField displayNameField;
    private final UserPreferencesHandler handler = UserPreferencesHandler.INSTANCE;

    public BankTransactionEditDialog() {
        super();
        setTitle("Edit company name and apply to similar transactions");

        //current transaction unedited data.
        GridPane currentGroup = new GridPane();
        Label companyNameFieldLabel = new Label("Company name text:");
        companyNameField = new TextField();
        Label displayCompanyNameLabel = new Label("Company name display:");
        displayCompanyNameField = new TextField();
        currentGroup.add(companyNameFieldLabel, 0, 0);
        currentGroup.add(companyNameField, 1, 0);
        currentGroup.add(displayCompanyNameLabel, 0, 1);
        currentGroup.add(displayCompanyNameField, 1, 1);
        currentGroup.setStyle("-fx-border-color: lavender");

        //user edit fields.
        GridPane newAssociation = new GridPane();
        newAssociation.setStyle("-fx-border-color: lavender");
        Label companyNameSubstringLabel = new Label("Company name substring for similar transactions:");
        companyNameSubstringField = new TextField();
        Label displayNameLabel = new Label("Company display name:");
        displayNameField = new TextField();
        newAssociation.add(companyNameSubstringLabel, 0, 0);
        newAssociation.add(companyNameSubstringField, 1, 0);
        newAssociation.add(displayNameLabel, 0, 1);
        newAssociation.add(displayNameField, 1, 1);

        //main Content
        FlowPane main = new FlowPane();
        main.getChildren().addAll(currentGroup, newAssociation);

        ButtonType apply = new ButtonType("Apply", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(apply, ButtonType.CANCEL);
        getDialogPane().setContent(main);
        Platform.runLater(() -> companyNameSubstringField.requestFocus());
        setResultConverter(dialogButton -> {
            if (dialogButton == apply) {
                handler.storePreferences();
                return new Pair<>(companyNameSubstringField.getText(), displayNameField.getText());
            }
            return null;
        });
    }

    public void setBankTransactionGroup(final BankTransactionGroupInterface group) {
        this.group = group;
        String categoryName = group.getCategoryName();
        companyNameField.setText(categoryName);
        displayCompanyNameField.setText(categoryName);
        companyNameSubstringField.setPromptText("Company name substring to apply when looking for ismilar transactions");
        companyNameSubstringField.setText(handler.getPreferences().getCompanyIdentifierString(categoryName));
        displayNameField.setPromptText("Short company name for all other similar company descriptions.");
    }
}
