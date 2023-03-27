package com.company;

public class PasswordChecker {
    public static int minimumChangeRequired(String password) {
        int insertionChanges = checkInsertion(password);
        int deletionChanges = checkDeletion(password);
        int diversityChanges = checkDiversity(password);
        int repeatingChanges = checkRepeating(password);
        // minimum raw changes (if we assume every change satisfies only one condition)
        int minimumChanges = insertionChanges + deletionChanges + diversityChanges + repeatingChanges;

        // check if we can insert and diversify with the same changes
        if (insertionChanges > 0 && diversityChanges > 0) {
            if (insertionChanges >= diversityChanges) { // diversity condition can be resolved with insertion changes
                minimumChanges -= diversityChanges;
                diversityChanges = 0; // update for next condition
            } else {
                minimumChanges -= insertionChanges;
                diversityChanges -= insertionChanges; // update for next condition
            }
        }

        // check if we can delete and change repeating characters with the same change
        if (deletionChanges > 0 && repeatingChanges > 0) { //repeating condition can be resolved with deletion changes
            if (deletionChanges >= repeatingChanges) {
                minimumChanges -= repeatingChanges;
                repeatingChanges = 0; // update for next condition
            } else {
                minimumChanges -= deletionChanges;
                repeatingChanges -= deletionChanges; // update for next condition
            }
        }

        // check if we can diversify and change repeating characters with the same change
        if (diversityChanges > 0 && repeatingChanges > 0) {
            minimumChanges -= Math.min(diversityChanges, repeatingChanges);
        }
        return minimumChanges;
    }

    private static int checkInsertion(String password) { // returns minimum insertion for the length condition
        if (password.length() < 6) {
            return 6 - password.length();
        }
        return 0;
    }

    private static int checkDeletion(String password) { // returns minimum deletion for the length condition
        if (password.length() > 20) {
            return password.length() - 20;
        }
        return 0;
    }

    private static int checkDiversity(String password) { // check for lowercase letter, uppercase letter, and digit
        boolean containsLowercase = false;               // returns minimum change for this condition only
        boolean containsUppercase = false;
        boolean containsDigit = false;
        int changes = 0;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLowerCase(c)) {
                containsLowercase = true;
            } else if (Character.isUpperCase(c)) {
                containsUppercase = true;
            } else if (Character.isDigit(c)) {
                containsDigit = true;
            }
        }

        if (!containsDigit) {
            changes++;
        }
        if (!containsUppercase) {
            changes++;
        }
        if (!containsLowercase) {
            changes++;
        }
        return changes;
    }

    private static int checkRepeating(String password) { // checks for three repeating characters in a row
        int changes = 0;                                 // returns minimum change for this condition only
        int repeatingChars = 0;

        /* 3-5 repeating characters requires only one change to satisfy the condition
           example: aaaaa -> aaXaa */
        for (int i = 0; i < password.length() - 2; i++) { // compare the first character to the next four
            if (i + 4 < password.length()) { // verify end of string
                for (int j = i; j <= i + 4; j++) {
                    if (password.charAt(i) == password.charAt(j)) {
                        repeatingChars++;
                    } else {
                        break;
                    }
                }
            } else {
                for (int j = i; j < password.length(); j++) {
                    if (password.charAt(i) == password.charAt(j)) {
                        repeatingChars++;
                    } else {
                        break;
                    }
                }
            }

            if (repeatingChars >= 3) { /* if we find 3-5 repeating characters in a row we need one change
                                        (repeatingChars can't be greater than 5) */
                changes++;
                i += 2;/* we skip the next two characters and continue searching (next iteration of i will check for 6, 7
                        and 8 repeating characters that will require one extra change: aaaaaaaa -> aaXaaXaa) */
            }
            repeatingChars = 0;
        }
        return changes;
    }
}
