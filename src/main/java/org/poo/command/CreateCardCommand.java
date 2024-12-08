package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public class CreateCardCommand extends Command {
    public CreateCardCommand(Bank bank, ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(CommandInput input) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount()))
                    account.addCard(new Card());
            }
        }
    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {

    }

}