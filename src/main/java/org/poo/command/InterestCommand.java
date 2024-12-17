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
     * Executes the interest-related operations on the account.
     *
     * @param input The input containing the command details.
     */
    public void execute(final CommandInput input) {
        Account account = bank.findAccount(input.getAccount());
        if (account == null) {
            return;
        }
        User user = bank.findUserHasAccount(input.getAccount());


        // If the account is not a savings account, set the 'notSavings' flag
        // for output and return early
        if (!account.getType().equals("savings")) {
            notSavings = true;
            return;
        }

        // If the command is "changeInterestRate", change the interest rate
        if (input.getCommand().equals("changeInterestRate")) {
            account.changeInterestRate(input.getInterestRate(), input.getTimestamp());
            // adding the transaction (the method above added it for the account)
            user.addTransaction(account.getTransactions().getLast());
        } else {
            account.addInterest();
        }

        found = true;
    }

    /**
     * Updates the output after executing the interest operation.
     *
     * This method generates the output JSON based on the results of the execute method.
     * If no valid account was found or the account is not a savings account,
     * it adds an error message to the output.
     *
     * @param input The input containing the necessary data for generating the output.
     * @param mapper The ObjectMapper instance used to format and generate the output.
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
