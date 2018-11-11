package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames;

import com.mariangolea.fintracker.banks.csvparser.impl.ui.EditDialog;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single.EditCompanyNamePane;
import com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.companynames.single.EditCompanyNameResult;
import java.util.Objects;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class EditCompanyNamesPane extends FlowPane {

    private final UserPreferencesInterface prefs;
    private ListView<String> companyNamesView;

    public EditCompanyNamesPane(final UserPreferencesInterface prefs) {
        super(Orientation.VERTICAL);
        this.prefs = Objects.requireNonNull(Objects.requireNonNull(prefs).deepClone());
        createComponents();
    }

    public UserPreferencesInterface getResult() {
        return prefs;
    }

    protected final void createComponents() {
        companyNamesView = new ListView<>(FXCollections.observableArrayList(prefs.getCompanyDisplayNames()));
        companyNamesView.setEditable(true);
        companyNamesView.setCellFactory(value -> new CompanyNameListCell());

        getChildren().add(companyNamesView);
    }

    public void editCompanyName(final String companyName) {
        EditCompanyNamePane pane = new EditCompanyNamePane(companyName, prefs.getCompanyIdentifierStrings(companyName));
        EditDialog popup = new EditDialog<>("Edit global company names preferences", pane, EditCompanyNamePane::getResult, null);
        Optional<EditCompanyNameResult> result = popup.showAndWait();
        result.ifPresent(userData -> {
            final String newName = userData.companyDisplayName;
            if (!companyName.equals(newName)) {
                prefs.editCompanyName(companyName, newName);
            }
            prefs.resetCompanyIdentifierStrings(newName, userData.identifierStrings);
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
            button.setOnAction(event -> getListView().getItems().remove(getItem()));
            edit.setOnAction(event -> editCompanyName(getItem()));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if (item != null && !empty) {
                if(item.length() > 10){
                    item = item.substring(0,7) + "...";
                }
                label.setText(item);
                setGraphic(hbox);
            }
        }
    }
}
