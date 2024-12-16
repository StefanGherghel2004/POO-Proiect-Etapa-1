package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.transactions.Transaction;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;


public final class DeleteAccountCommand extends Command {
    private boolean couldnotDelete = true;
    public DeleteAccountCommand(final Bank bank, final  ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
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
            Transaction transaction = new Transaction("Account couldn't be deleted - there are funds remaining", input.getTimestamp());
            user.addTransaction(transaction);
        }

    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        commandOutput.put("command", "deleteAccount");
        ObjectNode output = mapper.createObjectNode();
        if (couldnotDelete) {
            output.put("error", "Account couldn't be deleted - "
                    + "see org.poo.transactions for details");
        } else {
            output.put("success", "Account deleted");
        }
            output.put("timestamp", input.getTimestamp());
        commandOutput.put("output", output);
        commandOutput.put("timestamp", input.getTimestamp());
    }
}
