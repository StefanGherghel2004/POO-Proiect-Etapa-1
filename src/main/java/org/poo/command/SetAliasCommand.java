package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public final class SetAliasCommand extends Command {

    public SetAliasCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * Executes the SetAliasCommand. This method sets an alias for the specified account.
     * It first adds the alias to the bank's records,
     * then to the specific account's alias list.
     *
     * @param input The input data containing the account iban and alias to be set.
     */
    public void execute(final CommandInput input) {
        bank.addAlias(input.getAccount(), input.getAlias());
        Account account = bank.findAccount(input.getAccount());
        if (account != null) {
            account.addAlias(input.getAlias());
        }
    }

    /**
     * At the moment this execution of this command does not update the output
     * so this method is empty
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        // maybe deal with accountNotFound case
    }
}
