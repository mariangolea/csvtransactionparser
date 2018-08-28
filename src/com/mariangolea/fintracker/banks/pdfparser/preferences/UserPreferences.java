package com.mariangolea.fintracker.banks.pdfparser.preferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container of user preferences.
 * @author mariangolea@gmail.com
 */
public class UserPreferences {
    
    private final Map<String, List<String>> transactionCategories = new HashMap<>();
    private String pdfInputFolder;

    /**
     * Adds or updates (if category name already exists) a category name and its
     * associated transaction names.
     *
     * @param categoryName category name
     * @param transactionNames associated transaction names
     */
    public void addUpdateCategory(final String categoryName, final List<String> transactionNames) {
        List<String> existingAssociations = transactionCategories.get(categoryName);
        if (existingAssociations == null){
            transactionCategories.put(categoryName, transactionNames);
        } else{
            transactionNames.forEach(transactionName -> {
                if (!existingAssociations.contains(transactionName)){
                    existingAssociations.add(transactionName);
                }
            });
        }
    }
    
    public void removeCategory(final String categoryName){
        transactionCategories.remove(categoryName);
    }
    
    public List<String> getCategory(final String categoryName){
        return transactionCategories.get(categoryName);
    }
    
    public Map<String, List<String>> getAllCategories(){
        return transactionCategories;
    }
    
    public String getPDFInputFolder(){
        return pdfInputFolder;
    }
    
    public void setPDFInputFolder(final String pdfInputFolder){
        this.pdfInputFolder = pdfInputFolder;
    }
}
