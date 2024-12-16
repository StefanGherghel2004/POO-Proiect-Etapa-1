package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public final class InterestCommand extends Command {

    private boolean notSavings;
    private boolean found;

    public InterestCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {
        Account account = bank.findAccount(input.getAccount());
        if (account == null) {
            return;
        }
        User user = bank.findUserHasAccount(input.getAccount());

        if (!account.getType().equals("savings")) {
            notSavings = true;
            return;
        }

        if (input.getCommand().equals("changeInterestRate")) {
            account.changeInterestRate(input.getInterestRate(), input.getTimestamp());
            user.addTransaction(account.getTransactions().getLast());
        } else {
            account.addInterest();
        }

        found = true;
    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
            if (!found) {
                commandOutput.put("command", input.getCommand());
                ObjectNode output = mapper.createObjectNode();
                if (notSavings) {
                    output.put("description", "This is not a savings account");
                    output.put("timestamp", input.getTimestamp());
                }
                commandOutput.put("output", output);
                commandOutput.put("timestamp", input.getTimestamp());
            }
    }

}
