/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mariangolea.fintracker.banks.csvparser.ui;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionCompanyGroup;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Popup;

/**
 *
 * @author Marian
 */
public class BankTransactionEditPopup extends Popup {

    private BankTransactionCompanyGroup group;
    private TextField companyNameField;
    private TextField displayCompanyNameField;
    private TextField companyNameSubstringField;
    private TextField displayNameField;
    private final UserPreferences userPrefs = UserPreferencesHandler.getInstance().getPreferences();

    public BankTransactionEditPopup() {
        FlowPane currentGroup = new FlowPane();
        companyNameField = new TextField();
        displayCompanyNameField = new TextField();
        currentGroup.setOrientation(Orientation.VERTICAL);
        currentGroup.getChildren().add(companyNameField);
        currentGroup.getChildren().add(displayCompanyNameField);
        currentGroup.setStyle("-fx-border-color: lavender");

        FlowPane newAssociation = new FlowPane();
        newAssociation.setStyle("-fx-border-color: lavender");
        newAssociation.setOrientation(Orientation.VERTICAL);
        companyNameSubstringField = new TextField();
        displayNameField = new TextField();
        newAssociation.getChildren().add(companyNameSubstringField);
        newAssociation.getChildren().add(displayNameField);

        FlowPane main = new FlowPane();
        main.getChildren().addAll(currentGroup, newAssociation);

        FlowPane buttonPane = new FlowPane(constructButtons());
        main.getChildren().add(buttonPane);
        getContent().addAll(main, buttonPane);
    }

    public void setBankTransactionGroup(final BankTransactionCompanyGroup group) {
        this.group = group;
        companyNameField.setText(group.getCompanyDesc());
        String shortCompanyDesc = userPrefs.getCompanyDescriptionShortFor(group.getCompanyDesc());
        String display = shortCompanyDesc == null ? "" : userPrefs.getDisplayName(shortCompanyDesc);
        displayCompanyNameField.setText(display);

        companyNameSubstringField.setText(display.isEmpty() ? group.getCompanyDesc() : display);
        displayNameField.setPromptText("Feel free to write a short company name to store for all other similar company descriptions.");
    }

    protected final Button[] constructButtons() {
        Button ok = new Button("Apply");
        ok.setOnAction(e -> {
            if (displayNameField.getText() != null && !displayNameField.getText().isEmpty()) {
                userPrefs.setTransactionDisplayName(companyNameSubstringField.getText(), displayNameField.getText());
            }
            hide();
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
            hide();
        });

        return new Button[]{ok, cancel};
    }
}
