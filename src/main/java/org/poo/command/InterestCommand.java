package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.transactions.Transaction;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public final class InterestCommand extends Command {

    private boolean notSavings;
    private boolean found;

    public InterestCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(final CommandInput input) {
            for ( User user : bank.getUsers()) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(input.getAccount())) {
                        if (!account.getType().equals("savings")) {
                            notSavings = true;
                            return;
                        }
                        if (input.getCommand().equals("changeInterestRate")) {
                            account.setInterestRate(input.getInterestRate());
                            Transaction transaction = new Transaction("Interest rate of the account changed to " + account.getInterestRate(), input.getTimestamp());
                            account.addTransaction(transaction);
                            user.addTransaction(transaction);
                        } else {
                            account.setBalance(account.getBalance() + account.getInterestRate() * account.getBalance() / 100);
                        }
                        found = true;
                        return;

                    }
                }
            }
    }

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
