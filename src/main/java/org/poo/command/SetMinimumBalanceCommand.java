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
     * Executes the set minimum balance operation for an account.
     * This method finds the account specified by the input, sets the minimum balance
     * to the provided amount
     *
     * @param input The input containing the account and amount to set as the minimum balance.
     */
    public void execute(final CommandInput input) {

        Account account = bank.findAccount(input.getAccount());

        account.setMinBalance(input.getAmount());
        account.setSetMinBalance(true);

        // If the account's balance is less than or equal to
        // the minimum balance, freeze the cards
        if (account.getBalance() <= account.getMinBalance()) {
            account.frozeCards();
        }

    }

    /**
     * This command does not update the output at the moment
     *
     * @param input The input containing the relevant details for the operation.
     * @param mapper The ObjectMapper used for formatting the output.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
