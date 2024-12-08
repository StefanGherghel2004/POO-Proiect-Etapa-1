package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public class PayOnlineCommand extends Command {

    public boolean cardNotFound;
    public PayOnlineCommand(Bank bank, ObjectMapper mapper) {
        super(bank, mapper);
    }

    public void execute(CommandInput input) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                String currency = account.getCurrency();
                for (int i = 0; i < account.getCards().size(); i++) {
                    Card card = account.getCards().get(i);
                    if (card.getCardNumber().equals(input.getCardNumber())) {
                        double rate = bank.getRate(currency, input.getCurrency());

                        System.out.println(currency + input.getCurrency() + rate);
                        if (account.getBalance() >= (1 / rate) * input.getAmount()) {
                            account.setBalance(account.getBalance() - (1 / rate) * input.getAmount());
                        }
                        return;
                    }
                }
            }
        }
        cardNotFound = true;
    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {
        if (cardNotFound) {
            commandOutput.put("command", "payOnline");
            ObjectNode output =  mapper.createObjectNode();
            output.put("timestamp", input.getTimestamp());
            output.put("description", "Card not found");
            commandOutput.put("output", output);
            commandOutput.put("timestamp", input.getTimestamp());
        }
    }
}
