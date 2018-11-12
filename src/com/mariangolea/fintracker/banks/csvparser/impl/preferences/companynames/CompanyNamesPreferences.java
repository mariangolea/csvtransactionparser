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
        Objects.requireNonNull(companyNamesPreferences.companyNames).entrySet().forEach(entry
                -> companyNames.put(entry.getKey(), FXCollections.observableArrayList(entry.getValue()))
        );
    }

    @Override
    public Collection<String> getAllCompanyIdentifierStrings() {
        return companyIdentifiers.keySet();
    }

    @Override
    public Collection<String> getCompanyIdentifierStrings(final String companyDisplayName) {
        Collection<String> identifiers = companyNames.get(Objects.requireNonNull(companyDisplayName));
        return identifiers == null ? FXCollections.observableArrayList() : identifiers;
    }

    @Override
    public Collection<String> getMatchingIdentifierStrings(String transactionDescription) {
        final Collection<String> existing = FXCollections.observableArrayList();
        companyIdentifiers.keySet().forEach(existingIdentifier -> {
            if (transactionDescription.toLowerCase().contains(existingIdentifier.toLowerCase())) {
                existing.add(existingIdentifier);
            }
        });

        return existing;
    }

    @Override
    public void editCompanyIdentifier(final String existingIdentifier, final String newIdentifier) {
        final String companyName = companyIdentifiers.get(Objects.requireNonNull(existingIdentifier));
        companyIdentifiers.remove(existingIdentifier);
        companyIdentifiers.put(Objects.requireNonNull(newIdentifier), companyName);
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
    public void resetCompanyIdentifierStrings(final String companyName, final Collection<String> newIdentifiers) {
        //remove existing identifiers
        Collection<String> existing = getCompanyIdentifierStrings(Objects.requireNonNull(companyName));
        if (!hasCompanyDisplayName(companyName)){
            companyNames.put(companyName, existing);
        }

        //compute orphanes and remove them.
        Collection<String> orphaned = FXCollections.observableArrayList(existing);
        orphaned.removeAll(Objects.requireNonNull(newIdentifiers));
        deleteCompanyIdentifiers(orphaned);

        //set the new ones.
        newIdentifiers.forEach(identifier -> {
            companyIdentifiers.put(Objects.requireNonNull(identifier).toLowerCase(), companyName);
            companyNames.get(companyName).add(identifier.toLowerCase());
        });
    }

    public void deleteCompanyIdentifiers(final Collection<String> existingIdentifiers) {
        Objects.requireNonNull(existingIdentifiers).forEach(identifier -> {
            final String companyName = companyIdentifiers.remove(Objects.requireNonNull(identifier));
            companyNames.get(companyName).remove(identifier);
        });
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
        Collection<String> identifiers = companyNames.get(Objects.requireNonNull(existingName));
        companyNames.remove(existingName);
        companyNames.put(newName, identifiers);
        identifiers.forEach(identifier -> {
            companyIdentifiers.put(identifier, newName);
        });
    }

    @Override
    public void applyChanges(final UserPreferencesInterface userEdited) {
        companyIdentifiers.clear();
        companyNames.clear();
        Objects.requireNonNull(userEdited).getCompanyDisplayNames().forEach(companyName -> {
            final Collection<String> companyLocalIdentifiers = userEdited.getCompanyIdentifierStrings(companyName);
            companyNames.put(companyName, FXCollections.observableArrayList(companyLocalIdentifiers));
        });
        userEdited.getAllCompanyIdentifierStrings().forEach(identifier
                -> companyIdentifiers.put(identifier, Objects.requireNonNull(userEdited.getCompanyDisplayName(identifier)))
        );
    }

    @Override
    public boolean hasCompanyDisplayName(String companyDisplayName) {
        return companyNames.containsKey(Objects.requireNonNull(companyDisplayName));
    }

}
