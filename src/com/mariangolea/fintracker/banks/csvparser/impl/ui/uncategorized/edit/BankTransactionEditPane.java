package com.mariangolea.fintracker.banks.csvparser.impl.ui.uncategorized.edit;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories.CategoriesTree;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.textfield.TextFields;

public class BankTransactionEditPane extends GridPane {

    private TextArea companyDescriptionField;
    private TextField companyNameIdentifierField;
    protected ComboBox<String> companyDisplayNameField;
    private ComboBox<String> categoryPicker;
    protected ComboBox<String> parentCategoryPicker;
    private final UserPreferencesInterface userPrefs;
    private static final String INVALID_STYLE = "-fx-border-color: red";

    public BankTransactionEditPane(final UserPreferencesInterface userPrefs) {
        this.userPrefs = Objects.requireNonNull(userPrefs);
        createComponents();
        layoutComponents();
    }

    public EditResult getEditResult() {
        return new EditResult(
                companyNameIdentifierField.getText(),
                companyDisplayNameField.getValue(),
                categoryPicker.getValue(),
                parentCategoryPicker.getValue());
    }

    public void setBankTransaction(final BankTransaction transaction) {
        clearFields();
        final ObservableList<String> originalCategories = FXCollections.observableArrayList();
        originalCategories.addAll(userPrefs.getUserDefinedCategoryNames());
        originalCategories.removeAll(userPrefs.getCompanyDisplayNames());

        String transactionDescription = transaction.description;
        splitLines(transactionDescription);
        final Collection<String> substrings = userPrefs.getCompanyIdentifierStrings(transactionDescription);
        String substring = null;
        for (String sub : substrings) {
            if (transactionDescription.contains(sub)) {
                substring = sub;
            }
        }
        companyNameIdentifierField.setText(substring);
        companyDisplayNameField.setItems(FXCollections.observableArrayList(userPrefs.getCompanyDisplayNames()));
        companyDisplayNameField.setValue(substring == null ? null : userPrefs.getCompanyDisplayName(substring));
        categoryPicker.setItems(originalCategories);
        parentCategoryPicker.setItems(originalCategories);
        TextFields.bindAutoCompletion(categoryPicker.getEditor(), categoryPicker.getItems());
        TextFields.bindAutoCompletion(parentCategoryPicker.getEditor(), parentCategoryPicker.getItems());
    }

    protected void clearFields() {
        companyDescriptionField.setText(null);
        companyDisplayNameField.setValue(null);
        companyDisplayNameField.getItems().clear();
        categoryPicker.setValue(null);
        categoryPicker.getItems().clear();
        parentCategoryPicker.getItems().clear();
    }

    protected boolean isValid() {
        boolean valid = true;

        boolean tempValid = validateControl(companyNameIdentifierField, companyNameIdentifierField.getText());
        valid &= tempValid;

        tempValid = validateControl(companyDisplayNameField, companyDisplayNameField.getValue());
        valid &= tempValid;

        tempValid = validateControl(categoryPicker, categoryPicker.getValue());
        valid &= tempValid;

        return valid;
    }

    protected boolean validateControl(final Control control, final String value) {
        boolean valid = value != null && !value.isEmpty();
        control.setStyle(valid ? null : INVALID_STYLE);
        return valid;
    }

    private void createComponents() {
        companyDescriptionField = new TextArea();
        companyDescriptionField.setEditable(false);
        companyDescriptionField.autosize();

        companyNameIdentifierField = new TextField();
        companyNameIdentifierField.setPromptText("Company name substring to apply when looking for similar transactions");
        companyDisplayNameField = new ComboBox<>();
        companyDisplayNameField.setEditable(true);
        companyDisplayNameField.setTooltip(new Tooltip("Select a substring that will be used to identify similar transactions"));
        companyDisplayNameField.valueProperty().addListener((observable, oldValue, newValue) -> companyDisplayNameFieldChanged(newValue));

        categoryPicker = new ComboBox<>();
        categoryPicker.setEditable(true);
        categoryPicker.setTooltip(new Tooltip("Pick a category for this company name"));
        categoryPicker.valueProperty().addListener((observable, oldValue, newValue) -> categoryPickerChanged(newValue));

        parentCategoryPicker = new ComboBox<>();
        parentCategoryPicker.setEditable(true);
        parentCategoryPicker.setTooltip(new Tooltip("Pick a parent category for selected category, or leave blank"));
    }

    protected void companyDisplayNameFieldChanged(final String newValue) {
        if (newValue == null) {
            categoryPicker.setValue(null);
        } else {
            String category = userPrefs.getParent(newValue);
            if (Objects.equals(CategoriesTree.ROOT, category) || Objects.equals(UserPreferencesInterface.UNCATEGORIZED, category)) {
                categoryPicker.setValue(null);
            } else {
                categoryPicker.setValue(category);
            }
        }
    }

    protected void categoryPickerChanged(final String newValue) {
        if (newValue == null) {
            parentCategoryPicker.setValue(null);
        } else {
            String parentCategory = userPrefs.getParent(newValue);
            if (!Objects.equals(CategoriesTree.ROOT, parentCategory)) {
                parentCategoryPicker.setValue(parentCategory);
            }
        }
    }

    private void layoutComponents() {
        TitledPane transactionDescriptionGroup = new TitledPane("Uneditable transaction data", companyDescriptionField);
        transactionDescriptionGroup.setCollapsible(false);

        Label first = new Label("Company name substring:", companyNameIdentifierField);
        Label second = new Label("Company display name:", companyDisplayNameField);
        Label third = new Label("Pick a category name", categoryPicker);
        Label fourth = new Label("Category's parent category if needed", parentCategoryPicker);
        FlowPane group = createFlowPane(Orientation.VERTICAL, first, second, third, fourth);
        TitledPane companyNameGroup = new TitledPane("Edit Fields", group);
        companyNameGroup.setCollapsible(false);

        add(transactionDescriptionGroup, 0, 0);
        add(companyNameGroup, 0, 1);

        Platform.runLater(() -> companyNameIdentifierField.requestFocus());
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
