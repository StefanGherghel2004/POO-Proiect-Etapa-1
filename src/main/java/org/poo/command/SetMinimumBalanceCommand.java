package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public final class SetMinimumBalanceCommand extends Command {

    public SetMinimumBalanceCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {
        for (User user: bank.getUsers()) {
            for (Account account : user.getAccounts() ) {
                if (account.getIban().equals(input.getAccount())) {
                    account.setMinBalance(input.getAmount());
                    account.setSetMinBalance(true);
                    if (account.getBalance() <= account.getMinBalance()) {
                        for (Card card: account.getCards()) {
                            card.setStatus("frozen");
                        }
                    }
                }
            }
        }
    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
