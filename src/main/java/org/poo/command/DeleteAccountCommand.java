package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.transactions.Transaction;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

import static org.poo.outputConstants.OutputConstants.ACCOUNT_ERROR;
import static org.poo.outputConstants.OutputConstants.ACCOUNT_FUNDS;


public final class DeleteAccountCommand extends Command {

    // indicates whether the account could not be deleted (positive balance)
    private boolean couldnotDelete = true;

    public DeleteAccountCommand(final Bank bank, final  ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * Executes the command to delete an account.
     * If the account is not found it returns without setting a flag
     * If the account has a positive balance it adds a corresponding transaction
     * to its user (that the account has funds remaining).
     *
     * @param input The input containing the account information.
     */
    public void execute(final CommandInput input) {
        User user = bank.findUserHasAccount(input.getAccount());
        if (user == null) {
            return;
        }
        Account account = user.getAccount(input.getAccount());

        if (account.getBalance() == 0) {
            user.getAccounts().remove(account);
            couldnotDelete = false;
        } else {
            Transaction transaction = new Transaction(ACCOUNT_FUNDS, input.getTimestamp());
            user.addTransaction(transaction);
        }

    }

    /**
     * Updates the output with the result of the delete account operation.
     * If the account could not be deleted (because it had a non-zero balance or the account
     * was not found), an error message is included; otherwise, a success message is returned.
     *
     * @param input The input containing the necessary data to format the output.
     * @param mapper The ObjectMapper used to generate the JSON output.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        commandOutput.put("command", "deleteAccount");
        ObjectNode output = mapper.createObjectNode();
        if (couldnotDelete) {
            output.put("error", ACCOUNT_ERROR);
        } else {
            output.put("success", "Account deleted");
        }
            output.put("timestamp", input.getTimestamp());
        commandOutput.put("output", output);
        commandOutput.put("timestamp", input.getTimestamp());
    }
}
