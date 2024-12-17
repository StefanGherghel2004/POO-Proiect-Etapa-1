package org.poo.command;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

import static org.poo.outputConstants.OutputConstants.NOT_SAVINGS;


public class SpendingsReportCommand extends Command {

    public SpendingsReportCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     * The report consists of just updating the output and does not modify
     * the bank instance so this method is empty
     * @param input
     */
    public void execute(final CommandInput input) {

    }

    /**
     * Updates the output of the spendings report command. It generates a report by:
     * 1. Verifying if the account type is "savings."
     * 2. If it is a savings account, it outputs an error message.
     * 3. If it is not a savings account, it delegates the report generation to the
     * `ReportCommand` and sets the spendings report flag to true.
     *
     * @param input  The input data containing account and timestamp information.
     * @param mapper The object mapper used to create JSON nodes.
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        Account account = bank.findAccount(input.getAccount());
        if (account != null && account.getType().equals("savings")) {
            ObjectNode output = mapper.createObjectNode();
            output.put("error", NOT_SAVINGS);
            commandOutput.set("output", output);
            commandOutput.put("command", "spendingsReport");
            commandOutput.put("timestamp", input.getTimestamp());
            return;
        }

        ReportCommand command = new ReportCommand(bank, mapper);
        command.setSpendingsReport(true);
        command.updateOutput(input, mapper);

        commandOutput = command.getCommandOutput();
    }


}
