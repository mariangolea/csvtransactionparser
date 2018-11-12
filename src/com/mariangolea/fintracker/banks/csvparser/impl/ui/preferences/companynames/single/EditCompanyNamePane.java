package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class EditCompanyNamePane extends FlowPane {

    private final ObservableList<String> identifierStrings = FXCollections.observableArrayList();
    protected final FilteredList<String> filtered = new FilteredList<>(identifierStrings, s -> true);
    private final String companyName;
    private final TextField companyDisplayName = new TextField();
    protected Button add;
    protected final TextField searchField = new TextField();
    private static final String INVALID_STYLE = "-fx-border-color: red";
    private ListView<String> listView;

    public EditCompanyNamePane(final String companyName, final Collection<String> companyIdentifiers) {
        super(Orientation.VERTICAL);
        this.companyName = Objects.requireNonNull(companyName);
        identifierStrings.addAll(companyIdentifiers);
        Collections.sort(identifierStrings);
        createCompanyNamePane();
    }

    public EditCompanyNameResult getResult() {
        return new EditCompanyNameResult(companyDisplayName.getText(), identifierStrings);
    }

    protected final void createCompanyNamePane() {
        add = new Button("Add");
        add.disableProperty().set(true);
        searchField.setPromptText("Search a identifier");
        searchField.textProperty().addListener(listener -> {
            searchFieldTextChanged(searchField.getText());
        });
        add.setOnAction(action -> {
            addButtonClicked();
        });

        VBox box = new VBox(searchField, add);
        listView = new ListView<>(filtered);
        listView.setTooltip(new Tooltip("All string identifiers used to correlate transactions to mentioned company display name."));
        listView.setCellFactory(value -> new CompanyIdentifierListCell(identifierStrings));
        listView.setEditable(true);
        companyDisplayName.setText(companyName);
        companyDisplayName.setTooltip(new Tooltip("Company Display Name"));

        getChildren().addAll(Arrays.asList(companyDisplayName, box, listView));
    }

    protected void addButtonClicked() {
        String identifier = searchField.getText();
        if (identifier != null && !identifier.isEmpty() && !identifierStrings.contains(identifier)) {
            identifierStrings.add(searchField.getText());
            Collections.sort(identifierStrings);
            searchField.setText(null);
        }
    }

    public boolean isValid() {
        boolean valid = true;

        boolean tempValid = validateControl(companyDisplayName, companyDisplayName.getText());
        valid &= tempValid;

        tempValid = true;
        listView.setStyle(null);
        if (identifierStrings == null || identifierStrings.isEmpty()){
            listView.setStyle(INVALID_STYLE);
            tempValid = false;
        } else{
            for (String identifier : identifierStrings){
                if (identifier == null || identifier.isEmpty()){
                    listView.setStyle(INVALID_STYLE);
                    tempValid = false;
                }
            }
        }
        valid &= tempValid;

        return valid;
    }

    protected boolean validateControl(final Control control, final String value) {
        boolean valid = value != null && !value.isEmpty();
        control.setStyle(valid ? null : INVALID_STYLE);
        return valid;
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

    protected class CompanyIdentifierListCell extends ListCell<String> {

        HBox hbox = new HBox();
        protected TextField label = new TextField();
        Pane pane = new Pane();
        Button button = new Button("Delete");

        public CompanyIdentifierListCell(final ObservableList<String> identifierStrings) {
            super();

            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(event -> identifierStrings.remove(getItem()));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            boolean valid = item != null && !empty;
            label.setStyle(valid ? null : INVALID_STYLE);
            if (valid) {
                label.setText(item);
                setGraphic(hbox);
            } 
        }
    }
}
