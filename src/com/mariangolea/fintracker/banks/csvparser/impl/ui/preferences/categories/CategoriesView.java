package com.mariangolea.fintracker.banks.csvparser.impl.ui.preferences.categories;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories.CategoriesTree;
import java.util.Collection;
import java.util.Objects;
import javafx.geometry.Orientation;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.FlowPane;
import javafx.util.Pair;

public class CategoriesView extends FlowPane {
    private final UserPreferencesInterface prefs;
    private final CategoriesTreeView treeView;
    
    public CategoriesView(final UserPreferencesInterface prefs) {
        super(Orientation.VERTICAL);
        this.prefs = Objects.requireNonNull(Objects.requireNonNull(prefs).deepClone());
        treeView = new CategoriesTreeView(createTreeItem(), this.prefs);
        createComponents();
    }
    
    public UserPreferencesInterface getResult(){
        return prefs;
    }
    
    protected final void createComponents(){
        getChildren().add(treeView);
    }
    
    protected final TreeItem<Pair<String, Boolean>> createTreeItem(){
        TreeItem<Pair<String, Boolean>> root = new TreeItem<>(new Pair<>(CategoriesTree.ROOT, false));
        prefs.getTopMostCategories().forEach(category ->{
            root.getChildren().add(createItem(category));
        });
        
        return root;
    }
    
    protected TreeItem<Pair<String, Boolean>> createItem(final String category){
        Collection<String> subCategories = prefs.getSubCategories(Objects.requireNonNull(category));
        if (Objects.requireNonNull(subCategories).isEmpty()){
            Boolean isCompanyName = prefs.hasCompanyDisplayName(category);
            return new TreeItem<>(new Pair<>(category, isCompanyName));
        }
        
        TreeItem<Pair<String, Boolean>> item = new TreeItem<>(new Pair<>(category, Boolean.FALSE));
        Objects.requireNonNull(subCategories).forEach(subCategory ->{
            item.getChildren().add(createItem(subCategory));
        });
        return item;
    }
}
