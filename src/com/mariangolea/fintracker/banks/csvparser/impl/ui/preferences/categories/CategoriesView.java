package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.categories;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.CategoriesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories.CategoriesTree;
import java.util.Objects;
import javafx.geometry.Orientation;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.FlowPane;

public class CategoriesView extends FlowPane {
    private final UserPreferencesInterface prefs;
    private final CategoriesTreeView treeView;
    
    public CategoriesView(final UserPreferencesInterface prefs) {
        super(Orientation.VERTICAL);
        this.prefs = Objects.requireNonNull(Objects.requireNonNull(prefs).deepClone());
        treeView = new CategoriesTreeView(createTreeItem());
        createComponents();
        layoutComponents();
    }
    
    public UserPreferencesInterface getResult(){
        return prefs;
    }
    
    protected void createComponents(){
        getChildren().add(treeView);
    }
    
    protected void layoutComponents(){
    }
    
    protected TreeItem<String> createTreeItem(){
        TreeItem<String> root = new TreeItem<>(CategoriesTree.ROOT);
        prefs.getTopMostCategories().forEach(category ->{
            root.getChildren().add(createItem(category));
        });
        
        return root;
    }
    
    protected TreeItem<String> createItem(final String category){
        TreeItem<String> item = new TreeItem<>(category);
        prefs.getSubCategories(Objects.requireNonNull(category)).forEach(subCategory ->{
            item.getChildren().add(createItem(subCategory));
        });
        return item;
    }
}
