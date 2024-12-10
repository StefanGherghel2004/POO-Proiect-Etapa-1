package org.poo.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

public class SpendingsReportCommand extends Command{

    public SpendingsReportCommand(Bank bank, ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(CommandInput input) {

    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount()) && account.getType().equals("savings")) {
                    ObjectNode output = mapper.createObjectNode();
                    output.put("error", "This kind of report is not supported for a saving account");
                    commandOutput.set("output", output);
                    commandOutput.put("command", "spendingsReport");
                    commandOutput.put("timestamp", input.getTimestamp());
                    return;
                }
            }
        }
        //Bank bank1 = new Bank(bank);
        ReportCommand command = new ReportCommand(bank, mapper);
        command.setSpendingsReport(true);
        command.updateOutput(input, mapper);
        command.getCommandOutput().remove("command");
        command.getCommandOutput().put("command", "spendingsReport");

        commandOutput = command.getCommandOutput();
    }


}
