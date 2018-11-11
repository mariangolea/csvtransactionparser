package com.mariangolea.fintracker.banks.csvparser.impl.preferences.companynames;

import com.mariangolea.fintracker.banks.csvparser.api.preferences.CompanyNamesInterface;
import com.mariangolea.fintracker.banks.csvparser.api.preferences.UserPreferencesInterface;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class CompanyNamesPreferences implements CompanyNamesInterface {

    //identifier string, display name.
    private final ObservableMap<String, String> companyIdentifiers;
    //switched keys and values from previous map.
    private final ObservableMap<String, Collection<String>> companyNames;

    public CompanyNamesPreferences() {
        companyIdentifiers = FXCollections.observableMap(new HashMap<>());
        companyNames = FXCollections.observableMap(new HashMap<>());
    }

    protected CompanyNamesPreferences(final CompanyNamesPreferences companyNamesPreferences) {
        Objects.requireNonNull(companyNamesPreferences);
        this.companyIdentifiers = FXCollections.observableHashMap();
        companyIdentifiers.putAll(Objects.requireNonNull(companyNamesPreferences.companyIdentifiers));
        this.companyNames = FXCollections.observableHashMap();
        Objects.requireNonNull(companyNamesPreferences.companyNames).entrySet().forEach(entry ->
            companyNames.put(entry.getKey(), FXCollections.observableArrayList(entry.getValue()))
        );
    }

    @Override
    public Collection<String> getAllCompanyIdentifierStrings() {
        return companyIdentifiers.keySet();
    }

    @Override
    public Collection<String> getCompanyIdentifierStrings(final String companyDisplayName) {
        Collection<String> identifiers = companyNames.get(Objects.requireNonNull(companyDisplayName));
        if (identifiers == null){
            identifiers = FXCollections.emptyObservableList();
        }
        
        return identifiers;
    }

    @Override
    public void editCompanyIdentifier(final String existingIdentifier, final String newIdentifier) {
        final String companyName = companyIdentifiers.get(Objects.requireNonNull(existingIdentifier));
        companyIdentifiers.remove(existingIdentifier);
        companyIdentifiers.put(Objects.requireNonNull(newIdentifier), companyName);
    }

    @Override
    public void resetCompanyIdentifierStrings(final String companyName, final Collection<String> newIdentifiers) {
        //remove existing identifiers
        final Collection<String> existingIdentifiers = FXCollections.observableArrayList(companyNames.get(companyName));
        existingIdentifiers.forEach(former ->
            deleteCompanyIdentifier(former)
        );
        
        //set up the new ones.
        newIdentifiers.forEach(identifier ->
            setCompanyDisplayName(identifier, companyName)
        );
    }
    
    @Override
    public void deleteCompanyIdentifier(final String existingIdentifier) {
        final String companyName = companyIdentifiers.remove(Objects.requireNonNull(existingIdentifier));
        companyNames.get(companyName).remove(existingIdentifier);
    }

    @Override
    public String getCompanyDisplayName(final String descriptionString) {
        return companyIdentifiers.get(Objects.requireNonNull(descriptionString).toLowerCase());
    }

    @Override
    public Collection<String> getCompanyDisplayNames() {
        return FXCollections.observableSet(companyNames.keySet());
    }

    @Override
    public void setCompanyDisplayName(final String descriptionString, final String displayName) {
        companyIdentifiers.put(Objects.requireNonNull(descriptionString).toLowerCase(), Objects.requireNonNull(displayName));
        Collection<String> identifiers = companyNames.get(displayName);
        if (identifiers == null) {
            identifiers = FXCollections.observableArrayList();
            companyNames.put(displayName, identifiers);
        }
        identifiers.add(descriptionString.toLowerCase());
    }

    @Override
    public void deleteCompanyName(final String companyDisplayName) {
        Collection<String> identifiers = companyNames.remove(Objects.requireNonNull(companyDisplayName));
        identifiers.forEach(identifier -> {
            this.companyIdentifiers.remove(identifier);
        });
    }

    @Override
    public void editCompanyName(final String existingName, final String newName) {
        Collection<String> identifiers = companyNames.get(existingName);
        companyNames.remove(existingName);
        companyNames.put(newName, identifiers);
        identifiers.forEach(identifier -> {
            companyIdentifiers.remove(identifier);
            companyIdentifiers.put(identifier, newName);
        });
    }

    @Override
    public void applyChanges(final UserPreferencesInterface userEdited) {
        companyIdentifiers.clear();
        companyNames.clear();
        Objects.requireNonNull(userEdited).getCompanyDisplayNames().forEach(companyName ->{
            final Collection<String> companyLocalIdentifiers = Objects.requireNonNull(userEdited.getCompanyIdentifierStrings(companyName));
            companyNames.put(companyName, FXCollections.observableArrayList(companyLocalIdentifiers));
        });
        userEdited.getAllCompanyIdentifierStrings().forEach(identifier ->
            companyIdentifiers.put(identifier, Objects.requireNonNull(userEdited.getCompanyDisplayName(identifier)))
        );
    }
}
