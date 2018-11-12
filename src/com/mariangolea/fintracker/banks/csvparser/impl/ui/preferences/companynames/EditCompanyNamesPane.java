package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.EditDialog;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single.EditCompanyNamePane;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single.EditCompanyNameResult;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class EditCompanyNamesPane extends FlowPane {

    private final UserPreferencesInterface prefs;
    private ListView<String> companyNamesView;
    private final ObservableList<String> identifierStrings = FXCollections.observableArrayList();
    protected final FilteredList<String> filtered = new FilteredList<>(identifierStrings, s -> true);
    protected final TextField searchField = new TextField();
    protected Button add;

    public EditCompanyNamesPane(final UserPreferencesInterface prefs) {
        super(Orientation.VERTICAL);
        this.prefs = Objects.requireNonNull(Objects.requireNonNull(prefs).deepClone());
        loadIdentifierStrings();
        createComponents();
    }

    public UserPreferencesInterface getResult() {
        return prefs;
    }

    protected final void createComponents() {
        add = new Button("Add");
        add.disableProperty().set(true);
        add.setOnAction(action -> {
            addButtonClicked();
        });
        companyNamesView = new ListView<>(filtered);
        companyNamesView.setEditable(true);
        companyNamesView.setCellFactory(value -> new CompanyNameListCell());
        searchField.setPromptText("Search a identifier");
        searchField.textProperty().addListener(listener -> {
            searchFieldTextChanged(searchField.getText());
        });
        getChildren().addAll(searchField, add, companyNamesView);
    }

    protected void addButtonClicked() {
        String identifier = searchField.getText();
        if (!identifierStrings.contains(identifier)) {
            editCompanyName(identifier);
        }
    }

    protected void deleteButtonClicked(final String toDelete){
        prefs.deleteCompanyName(toDelete);
        loadIdentifierStrings();
        searchFieldTextChanged(searchField.getText());
    }
    
    protected final void loadIdentifierStrings() {
        identifierStrings.clear();
        identifierStrings.addAll(prefs.getCompanyDisplayNames());
        Collections.sort(identifierStrings);
    }

    protected void searchFieldTextChanged(final String filter) {
        if (filter == null || filter.length() == 0) {
            filtered.setPredicate(s -> true);
            add.disableProperty().set(true);
        } else {
            filtered.setPredicate(s -> s.toLowerCase().contains(filter.toLowerCase()));
            add.disableProperty().set(!filtered.isEmpty());
        }
    }

    public void editCompanyName(final String companyName) {
        Collection<String> identifiers = prefs.getCompanyIdentifierStrings(companyName);
        EditCompanyNamePane pane = new EditCompanyNamePane(companyName, identifiers);
        EditDialog popup = new EditDialog<>("Edit global company names preferences", pane, EditCompanyNamePane::getResult, EditCompanyNamePane::isValid);
        Optional<EditCompanyNameResult> result = popup.showAndWait();
        result.ifPresent(userData -> {
            final String newName = userData.companyDisplayName;
            if (!companyName.equals(newName)) {
                prefs.editCompanyName(companyName, newName);
            }
            prefs.resetCompanyIdentifierStrings(userData.companyDisplayName, userData.identifierStrings);
            loadIdentifierStrings();
        });
    }

    private class CompanyNameListCell extends ListCell<String> {

        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        Button button = new Button("Delete");
        Button edit = new Button("Edit");

        public CompanyNameListCell() {
            super();

            hbox.getChildren().addAll(label, pane, button, edit);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(event -> deleteButtonClicked(getItem()));
            edit.setOnAction(event -> editCompanyName(getItem()));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if (item != null && !empty) {
                if (item.length() > 10) {
                    item = item.substring(0, 7) + "...";
                }
                label.setText(item);
                setGraphic(hbox);
            }
        }
    }
}
