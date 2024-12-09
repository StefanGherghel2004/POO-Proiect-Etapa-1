package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public class SetAliasCommand extends Command {

    public SetAliasCommand(Bank bank, ObjectMapper mapper) {
        super(bank,mapper);
    }

    public void execute(CommandInput input) {
        bank.addAlias(input.getAccount(), input.getAlias());
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount())) {
                    account.addAlias(input.getAlias());
                }
                //System.out.println(account.getAliases());
            }
        }

    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {

    }
}
