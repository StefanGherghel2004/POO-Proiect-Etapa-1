package org.poo.command;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;


public class SpendingsReportCommand extends Command {

    public SpendingsReportCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank, mapper);
    }

    /**
     *
     * @param input
     */
    public void execute(final CommandInput input) {

    }

    /**
     *
     * @param input
     * @param mapper
     */
    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount()) && account.getType().equals("savings")) {
                    ObjectNode output = mapper.createObjectNode();
                    output.put("error",
                            "This kind of report is not supported for a saving account");
                    commandOutput.set("output", output);
                    commandOutput.put("command", "spendingsReport");
                    commandOutput.put("timestamp", input.getTimestamp());
                    return;
                }
            }
        }
        ReportCommand command = new ReportCommand(bank, mapper);
        command.setSpendingsReport(true);
        command.updateOutput(input, mapper);
        command.getCommandOutput().remove("command");
        command.getCommandOutput().put("command", "spendingsReport");

        commandOutput = command.getCommandOutput();
    }


}
