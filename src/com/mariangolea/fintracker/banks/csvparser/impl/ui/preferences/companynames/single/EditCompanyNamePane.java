package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
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
    private final String companyName;
    private final TextField companyDisplayName = new TextField();
    
    public EditCompanyNamePane(final String companyName, final Collection<String> companyIdentifiers) {
        super(Orientation.VERTICAL);
        this.companyName = Objects.requireNonNull(companyName);
        identifierStrings.addAll(companyIdentifiers);
        createCompanyNamePane();
    }
    
    public EditCompanyNameResult getResult(){
        return new EditCompanyNameResult(companyDisplayName.getText(), identifierStrings);
    }
    
    protected final void createCompanyNamePane() {
        FilteredList<String> filtered = new FilteredList<>(identifierStrings, s -> true);
        
        final Button add = new Button("Add");
        add.disableProperty().set(true);
        final TextField searchField = new TextField();
        searchField.setPromptText("Search a identifier");
        searchField.textProperty().addListener(listener -> {
                String filter = searchField.getText();
                if (filter == null || filter.length() == 0) {
                    filtered.setPredicate(s -> true);
                    add.disableProperty().set(true);
                } else {
                    filtered.setPredicate(s -> s.toLowerCase().contains(filter.toLowerCase()));
                    add.disableProperty().set(!filtered.isEmpty());
                }
            });
        add.setOnAction(action ->{
            String identifier = searchField.getText();
            if (!identifierStrings.contains(identifier)){
                identifierStrings.add(searchField.getText());
                searchField.setText(null);
            }
        });
        
        VBox box = new VBox(searchField, add);
        ListView<String> view = new ListView<>(filtered);
        view.setTooltip(new Tooltip("All string identifiers used to correlate transactions to mentioned company display name."));
        view.setCellFactory(value -> new CompanyIdentifierListCell());
        view.setEditable(true);
        companyDisplayName.setText(companyName);
        companyDisplayName.setTooltip(new Tooltip("Company Display Name"));
        
        getChildren().addAll(Arrays.asList(companyDisplayName, box, view));
    }

    private class CompanyIdentifierListCell extends ListCell<String> {

        HBox hbox = new HBox();
        TextField label = new TextField();
        Pane pane = new Pane();
        Button button = new Button("Delete");

        public CompanyIdentifierListCell() {
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

            if (item != null && !empty) {
                label.setText(item);
                setGraphic(hbox);
            }
        }
    }
}
