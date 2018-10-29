package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class BankTransactionEditDialog extends Dialog<EditResult> {

    private TextField companyNameField;
    private TextField companyNameSubstringField;
    private TextField displayNameField;
    private ComboBox<String> existingCategories;
    private TextField newCategory;
    private final UserPreferencesHandler handler = UserPreferencesHandler.INSTANCE;

    public BankTransactionEditDialog() {
        super();
        setTitle("Edit company name and apply to similar transactions");

        //current transaction unedited data.
        GridPane currentGroup = new GridPane();
        Label companyNameFieldLabel = new Label("Company name text:");
        companyNameField = new TextField();
        companyNameField.setEditable(false);
        companyNameField.setPrefWidth(Control.USE_COMPUTED_SIZE);
        currentGroup.add(companyNameFieldLabel, 0, 0);
        currentGroup.add(companyNameField, 1, 0);
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
        existingCategories = new ComboBox<>();
        newCategory = new TextField();
        newAssociation.add(existingCategories, 0, 2);
        newAssociation.add(newCategory, 0, 3);

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
                Pair<String, String> company = new Pair<>(companyNameSubstringField.getText(), displayNameField.getText());
                Pair<String, String> category = new Pair<>(getCategoryName(), displayNameField.getText());
                return new EditResult(company, category);
            }
            return null;
        });
    }

    public void setBankTransaction(final BankTransaction transaction) {
        String categoryName = transaction.description;
        companyNameField.setText(categoryName);
        companyNameField.setPrefColumnCount(categoryName.length());
        companyNameSubstringField.setPromptText("Company name substring to apply when looking for ismilar transactions");
        final String substring = handler.getPreferences().getCompanyIdentifierString(categoryName);
        companyNameSubstringField.setText(substring);
        displayNameField.setPromptText("Short company name for all other similar company descriptions.");
        ObservableList<String> categories = FXCollections.observableArrayList(UserPreferencesHandler.INSTANCE.getPreferences().getUserDefinedCategoryNames());
        existingCategories.setItems(categories);
        newCategory.setPromptText("Type a category name if no existing one is of interest.");
    }

    private String getCategoryName() {
        if (newCategory.getText().isEmpty()) {
            return existingCategories.getValue();
        }
        return newCategory.getText();
    }
}
