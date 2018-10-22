/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mariangolea.fintracker.banks.csvparser.ui.edit;

import com.mariangolea.fintracker.banks.csvparser.api.transaction.BankTransactionGroupInterface;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferences;
import com.mariangolea.fintracker.banks.csvparser.preferences.UserPreferencesHandler;
import java.util.Optional;
import javafx.util.Pair;

public class BankTransactionGroupEditHandler {

    private final UserPreferences userPrefs = UserPreferencesHandler.INSTANCE.getPreferences();
    private BankTransactionEditDialog editPopup;

    public void editGroup(final BankTransactionGroupInterface group) {
        if (editPopup == null) {
            editPopup = new BankTransactionEditDialog();
        }
        editPopup.setBankTransactionGroup(group);
        Optional<Pair<String, String>> result = editPopup.showAndWait();
        result.ifPresent(userData -> {
            if (userData.getKey() != null && !userData.getKey().isEmpty() && userData.getValue() != null && !userData.getValue().isEmpty()) {
                userPrefs.setCompanyName(userData.getKey(), userData.getValue());
            }
        });

    }
}
