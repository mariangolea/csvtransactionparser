package com.mariangolea.fintracker.banks.csvparser.impl.preferences.categories;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.CategoriesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import com.mariangolea.fintracker.banks.csvparser.impl.preferences.companynames.CompanyNamesPreferences;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import javafx.collections.FXCollections;

public class CategoriesPreferences extends CompanyNamesPreferences implements CategoriesInterface {

    private final CategoriesTree categories;

    public CategoriesPreferences() {
        this(new CategoriesTree());
    }

    public CategoriesPreferences(final CategoriesTree categories) {
        this.categories = Objects.requireNonNull(categories);
    }

    protected CategoriesPreferences(final CategoriesPreferences prefs) {
        super(prefs);
        this.categories = new CategoriesTree(Objects.requireNonNull(prefs.categories));
    }

    @Override
    public Collection<String> getUserDefinedCategoryNames() {
        return categories.getAllSubCategoryNames();
    }

    @Override
    public Collection<String> getTopMostCategories() {
        return categories.getNodeSubCategoryNames();
    }

    @Override
    public Collection<String> getSubCategories(final String categoryName) {
        Objects.requireNonNull(categoryName);
        CategoriesTree tree = categories.getCategory(categoryName);
        if (tree == null) {
            return FXCollections.emptyObservableList();
        }
        return tree.getNodeSubCategoryNames();
    }

    @Override
    public String getMatchingCategory(String companyDescriptionString) {
        for (String companyIdentifier : getAllCompanyIdentifierStrings()) {
            if (companyDescriptionString.toLowerCase().contains(companyIdentifier.toLowerCase())) {
                return getCompanyDisplayName(companyIdentifier);
            }
        }

        return UNCATEGORIZED;
    }

    @Override
    public void appendDefinition(final String categoryName, final Collection<String> subCategories) {
        Objects.requireNonNull(categoryName);
        Objects.requireNonNull(subCategories);

        CategoriesTree tree = categories.getCategory(categoryName);
        if (tree == null) {
            categories.addSubCategories(Arrays.asList(categoryName));
            tree = categories.getCategory(categoryName);
        }
        final Collection<String> newCategories = FXCollections.observableArrayList();
        final Collection<CategoriesTree> existingCategories = FXCollections.observableArrayList();
        subCategories.forEach(subcategory -> {
            CategoriesTree existing = categories.getCategory(subcategory);
            if (existing == null) {
                newCategories.add(subcategory);
            } else {
                existingCategories.add(existing);
            }
        });
        tree.reparent(existingCategories);
        tree.addSubCategories(newCategories);
    }

    @Override
    public String getParent(final String categoryName) {
        CategoriesTree tree = categories.getCategory(categoryName);
        return tree == null ? null : tree.getParentCategory().getCategoryName();
    }

    @Override
    public void applyChanges(final UserPreferencesInterface userEdited) {
        super.applyChanges(userEdited);
        categories.clearSubCategories();
        Collection<String> topMostCategories = userEdited.getTopMostCategories();
        appendDefinition(CategoriesTree.ROOT, topMostCategories);
        topMostCategories.forEach(topMost -> {
            applyCategory(topMost, userEdited, userEdited.getSubCategories(topMost));
        });
    }
    
    private void applyCategory(final String category, final UserPreferencesInterface userEdited, final Collection<String> subCategories){
        appendDefinition(category, subCategories);
        subCategories.forEach(subCategory ->{
            applyCategory(subCategory, userEdited, userEdited.getSubCategories(subCategory));
        });
    }
}
