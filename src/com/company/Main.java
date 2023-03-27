package com.company;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < Password.examples.size(); i++) {
            System.out.println("Minimum change for '" + Password.examples.get(i) + "' to be strong is: "
                    + PasswordChecker.minimumChangeRequired(Password.examples.get(i)));
        }
    }
}
