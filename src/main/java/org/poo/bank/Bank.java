package org.poo.bank;

import org.poo.fileio.CommandInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<User> users = new ArrayList<User>();

    public Bank() {

    }
    public Bank(Bank bank) {
        this.users = bank.getUsers();
    }

    public void addUser(UserInput user) {
        User u = new User(user);
        users.add(u);
    }

    public List<User> getUsers() {
        return  users;
    }

    public void addAccount(CommandInput input) {
        for (User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                user.addAccount(input);
            }
        }
    }
}

