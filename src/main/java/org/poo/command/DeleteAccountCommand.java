package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.transactions.Transaction;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;


public final class DeleteAccountCommand extends Command {
    private boolean couldnotDetlete = true;
    public DeleteAccountCommand(final Bank bank, final  ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {
        for (User user : bank.getUsers()) {
            for (int i = 0; i < user.getAccounts().size(); i++) {
                Account account = user.getAccounts().get(i);
                if (account.getIban().equals(input.getAccount())) {
                    if (account.getBalance() == 0) {
                        user.getAccounts().remove(i);
                        couldnotDetlete = false;
                    } else {
                        Transaction transaction = new Transaction("Account couldn't be deleted - there are funds remaining", input.getTimestamp());
                        user.addTransaction(transaction);
                    }
                    return;
                }
            }
        }
    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        commandOutput.put("command", "deleteAccount");
        ObjectNode output = mapper.createObjectNode();
        if (couldnotDetlete) {
            output.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
        } else {
            output.put("success", "Account deleted");
        }
            output.put("timestamp", input.getTimestamp());
        commandOutput.put("output", output);
        commandOutput.put("timestamp", input.getTimestamp());
    }
}
