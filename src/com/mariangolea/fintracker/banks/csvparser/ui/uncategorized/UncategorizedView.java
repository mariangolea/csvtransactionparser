package com.mariangolea.fintracker.banks.csvparser.ui.uncategorized;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransaction;
import com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit.BankTransactionContextMenu;
import com.mariangolea.fintracker.banks.csvparser.ui.uncategorized.edit.BankTransactionEditHandler;
import java.util.Collection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;

public class UncategorizedView extends FlowPane {

    private final ObservableList<BankTransaction> model = FXCollections.observableArrayList();

    private BankTransactionEditHandler editHandler;
    private BankTransactionContextMenu contextMenu;
    private Label responseLabel;
    private TextField searchField;
    private ListView<BankTransaction> listView;

    public UncategorizedView() {
        super(Orientation.VERTICAL);
        createComponents();
    }

    protected final void createComponents() {
        if (responseLabel == null) {
            responseLabel = new Label("No transactions loaded yet.");
        }
        FilteredList<BankTransaction> filtered = new FilteredList<>(model, s -> true);
        if (searchField == null) {
            searchField = new TextField();
            searchField.setPromptText("Enter company name to filter on");
            searchField.textProperty().addListener(listener -> {
                String filter = searchField.getText();
                if (filter == null || filter.length() == 0) {
                    filtered.setPredicate(s -> true);
                } else {
                    filtered.setPredicate(s -> s.description.toLowerCase().contains(filter.toLowerCase()));
                }
            });
        }
        if (listView == null) {
            listView = new ListView<>(filtered);
            filtered.addListener(new UncategorizedTransactionListSelectionListener(listView, responseLabel));
        }
        if (editHandler == null) {
            editHandler = new BankTransactionEditHandler();
        }
        if (contextMenu == null) {
            contextMenu = new BankTransactionContextMenu(editHandler);
        }

        getChildren().add(responseLabel);
        getChildren().add(searchField);
        getChildren().add(createTransactionView());
    }

    private ScrollPane createTransactionView() {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setContextMenu(contextMenu);
        listView.setCellFactory((param) -> {
            UncategorizedTransactionCellRenderer renderer = new UncategorizedTransactionCellRenderer();
            renderer.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    renderer.setContextMenu(null);
                } else {
                    renderer.setContextMenu(contextMenu);
                    contextMenu.setBankTransaction(renderer.getItem());
                }
            });
            renderer.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        editHandler.editTransaction(renderer.getItem());
                    }
                }
            });

            return renderer;
        });
        ScrollPane scrollPane = new ScrollPane(listView);
        return scrollPane;
    }

    public void updateModel(Collection<BankTransaction> newModel) {
        model.clear();
        newModel.forEach(transaction -> {
            model.add(transaction);
        });
    }
}
