package com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;

/**
 * Leafs do not have any sub categories and represent a company name.
 * <br>Intermediate nodes represent categories, and will have both a list of sub
 * categories and a list of companies.
 */
public class CategoriesTree {

    public static final String ROOT = "Root";
    private String categoryName;
    private CategoriesTree parent;
    private final List<CategoriesTree> subCategoriesTrees = FXCollections.observableArrayList();

    public CategoriesTree() {
        this(ROOT);
    }
    
    public CategoriesTree(final CategoriesTree copiedTree) {
        this(copiedTree.categoryName, copiedTree.parent);
        copiedTree.subCategoriesTrees.forEach(subTree -> {
            subCategoriesTrees.add(new CategoriesTree(subTree));
        });
    }
    
    public CategoriesTree(final String categoryName) {
        this(categoryName, null);
    }
    
    private CategoriesTree(final String categoryName, final CategoriesTree parent){
        this.categoryName = categoryName;
        this.parent = parent;
    }

    public String getCategoryName() {
        return categoryName;
    }
    
    public void clearSubCategories(){
        subCategoriesTrees.clear();
    }
    
    public void setCategoryName(final String categoryName){
        this.categoryName = Objects.requireNonNull(categoryName);
    }
    
    public CategoriesTree getCategory(final String category) {
        Objects.requireNonNull(category);
        if (Objects.equals(categoryName, category)) {
            return this;
        } else if (subCategoriesTrees.isEmpty()) {
            return null;
        } else {
            for (CategoriesTree subCategory : subCategoriesTrees) {
                CategoriesTree tree = subCategory.getCategory(category);
                if (tree != null) {
                    return tree;
                }
            }
            return null;
        }
    }

    public Collection<String> getNodeSubCategoryNames() {
        Collection<String> categories = FXCollections.observableArrayList();
        subCategoriesTrees.forEach(subCategory ->{
            categories.add(subCategory.categoryName);
        });
        
        return categories;
    }

    public Collection<String> getAllSubCategoryNames() {
        final Collection<String> categories = FXCollections.observableArrayList();
        subCategoriesTrees.forEach(subcategory -> {
            categories.add(subcategory.categoryName);
            categories.addAll(subcategory.getNodeSubCategoryNames());
        });

        return categories;
    }

    public CategoriesTree getParentCategory() {
        return parent;
    }

    public void reparent(final Collection<CategoriesTree> existingNodes){
        Objects.requireNonNull(existingNodes);
        for (CategoriesTree node : existingNodes){
            CategoriesTree formerParent = node.getParentCategory();
            if (formerParent != null){
                formerParent.subCategoriesTrees.remove(node);
            }
            subCategoriesTrees.add(node);
            node.parent = this;
        }
    }
    
    public void addSubCategories(final Collection<String> subCategories) {
        Objects.requireNonNull(subCategories);
        subCategories.forEach(subCategoryName -> {
            CategoriesTree subcategory = new CategoriesTree(subCategoryName, this);
            subCategoriesTrees.add(subcategory);
        });
    }
    
    public void removeSubCategories(final Collection<CategoriesTree> toRemove){
        subCategoriesTrees.removeAll(Objects.requireNonNull(toRemove));
    }
}
