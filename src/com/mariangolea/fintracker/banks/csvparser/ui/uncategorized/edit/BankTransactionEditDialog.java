package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.Arrays;
import java.util.Collection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.textfield.TextFields;

public class BankTransactionEditDialog extends Dialog<EditResult> {

    private TextArea companyDescriptionField;
    private TextField companyNameIdentifierField;
    private TextField displayNameField;
    private ComboBox<String> categoryPicker;
    private ComboBox<String> parentCategoryPicker;
    private final UserPreferencesHandler handler = UserPreferencesHandler.INSTANCE;
    private final ObservableList<String> originalCategories = FXCollections.observableArrayList();
    private final static String INVALID_STYLE = "-fx-border-color: red";

    public BankTransactionEditDialog() {
        super();
        setTitle("Edit company name and apply to similar transactions");
        createComponents();

        TitledPane transactionDescriptionGroup = new TitledPane("Uneditable transaction data", companyDescriptionField);
        transactionDescriptionGroup.autosize();
        transactionDescriptionGroup.setCollapsible(false);

        Label first = new Label("Company name substring:", companyNameIdentifierField);
        Label second = new Label("Company display name:", displayNameField);
        FlowPane group = createFlowPane(Orientation.VERTICAL, first, second);
        TitledPane companyNameGroup = new TitledPane("Company name picker", group);
        companyNameGroup.setCollapsible(false);

        first = new Label("Pick a category name", categoryPicker);
        second = new Label("Category's parent category if needed", parentCategoryPicker);
        group = createFlowPane(Orientation.VERTICAL, first, second);
        TitledPane categoryGroup = new TitledPane("Category picker", group);
        categoryGroup.setCollapsible(false);

        ButtonType apply = new ButtonType("Apply", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(apply, ButtonType.CANCEL);

        GridPane main = new GridPane();
        main.add(transactionDescriptionGroup, 0, 0);
        main.add(companyNameGroup, 0, 1);
        main.add(categoryGroup, 0, 2);

        getDialogPane().setContent(main);

        Platform.runLater(() -> companyNameIdentifierField.requestFocus());
        setResultConverter(dialogButton -> {
            if (dialogButton == apply) {
                handler.storePreferences();
                return new EditResult(
                        companyNameIdentifierField.getText(), 
                        displayNameField.getText(), 
                        categoryPicker.getValue(), 
                        parentCategoryPicker.getValue());
            }
            return null;
        });

        final Button applyButton = (Button) getDialogPane().lookupButton(apply);
        applyButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (!isValid()) {
                ae.consume();
            }
        });
    }

    public void setBankTransaction(final BankTransaction transaction) {
        companyDescriptionField.clear();
        categoryPicker.setStyle(null);
        displayNameField.setStyle(null);
        parentCategoryPicker.setStyle(null);

        String categoryName = transaction.description;
        splitLines(categoryName);
        companyNameIdentifierField.setPromptText("Company name substring to apply when looking for ismilar transactions");
        final String substring = handler.getPreferences().getCompanyIdentifierString(categoryName);
        companyNameIdentifierField.setText(substring);
        displayNameField.setPromptText("Short company name for all other similar company descriptions.");
        originalCategories.clear();
        originalCategories.addAll(UserPreferencesHandler.INSTANCE.getPreferences().getUserDefinedCategoryNames());
        Collection<String> companyDisplayNames = UserPreferencesHandler.INSTANCE.getPreferences().getCompanyDisplayNames();
        originalCategories.removeAll(companyDisplayNames);
        categoryPicker.setItems(originalCategories);
        parentCategoryPicker.setItems(originalCategories);
        TextFields.bindAutoCompletion(categoryPicker.getEditor(), categoryPicker.getItems());
        TextFields.bindAutoCompletion(parentCategoryPicker.getEditor(), parentCategoryPicker.getItems());
    }

    private boolean isValid() {
        String companyName = displayNameField.getText();
        String category = categoryPicker.getValue();
        String parentcategory = parentCategoryPicker.getValue();

        if (companyName == null || companyName.isEmpty()) {
            displayNameField.setStyle(INVALID_STYLE);
            return false;
        }
        displayNameField.setStyle(null);

        if (category == null || category.isEmpty()) {
            categoryPicker.setStyle(INVALID_STYLE);
            return false;
        }
        categoryPicker.setStyle(null);
        if (parentcategory == null || parentcategory.isEmpty()) {
            parentCategoryPicker.setStyle(INVALID_STYLE);
            return false;
        }
        parentCategoryPicker.setStyle(null);

        return true;
    }

    protected final void createComponents() {
        companyDescriptionField = new TextArea();
        companyDescriptionField.setEditable(false);
        companyDescriptionField.autosize();

        companyNameIdentifierField = new TextField();
        displayNameField = new TextField();

        categoryPicker = new ComboBox<>();
        categoryPicker.setEditable(true);
        categoryPicker.setTooltip(new Tooltip("Pick a category for this company name"));

        parentCategoryPicker = new ComboBox<>();
        parentCategoryPicker.setEditable(true);
        parentCategoryPicker.setTooltip(new Tooltip("Pick a parent category for selected category, or leave blank"));
    }

    private FlowPane createFlowPane(Orientation orientation, Node... controls) {
        final FlowPane flow = new FlowPane(orientation);
        flow.getChildren().addAll(Arrays.asList(controls));
        return flow;
    }

    private void splitLines(String text) {
        if (text.length() <= 50) {
            companyDescriptionField.appendText(text);
        } else {
            companyDescriptionField.appendText(text.substring(0, 50));
            companyDescriptionField.appendText("\n");
            splitLines(text.substring(50));
        }
    }
}
