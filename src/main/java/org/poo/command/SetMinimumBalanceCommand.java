package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public final class SetMinimumBalanceCommand extends Command {

    public SetMinimumBalanceCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {

        Account account = bank.findAccount(input.getAccount());

        account.setMinBalance(input.getAmount());
        account.setSetMinBalance(true);
        if (account.getBalance() <= account.getMinBalance()) {
            account.frozeCards();
        }

    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
