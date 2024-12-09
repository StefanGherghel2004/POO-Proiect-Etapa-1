package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.*;
import org.poo.fileio.CommandInput;

import java.util.List;
import java.util.Map;

public final class SendMoneyCommand extends Command {

    public SendMoneyCommand(final Bank bank, final ObjectMapper mapper) {
        super(bank,mapper);
    }

    private static String findRealAccount(final Map<String, List<String>> aliases, final String alias) {
        // Iterate through the map
        for (Map.Entry<String, List<String>> entry : aliases.entrySet()) {
            String realAccount = entry.getKey();
            List<String> aliasList = entry.getValue();

            // Check if the alias exists in the list
            if (aliasList.contains(alias)) {
                return realAccount; // Return the associated account
            }
        }
        return null; // No match found
    }

    public void execute(final CommandInput input) {
        String senderIban = findRealAccount(bank.getAliases(), input.getAccount());
        if (senderIban != null) {
            return;
        }
        String receiverIban = findRealAccount(bank.getAliases(), input.getReceiver());
        System.out.println("");
        Account sender = null;
        Account receiver = null;
        User senderUser = null;
        User receiverUser = null;
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount()) || account.getIban().equals(senderIban)) {
                    sender = account;
                    senderUser = user;
                }
                if (account.getIban().equals(input.getReceiver()) || account.getIban().equals(receiverIban)) {
                    receiver = account;
                    receiverUser = user;
                }
            }
        }
        if (senderUser != null && !input.getEmail().equals(senderUser.getEmail())) {
            //System.out.println("ACOLOOOOOOOOOOOOOOOOOOO");
            return;
        }

        if (sender != null && receiver != null) {
            double rate = bank.getRate(sender.getCurrency(), receiver.getCurrency());
            //System.out.println(rate);
            //System.out.println(sender.getBalance());
            //System.out.println(receiver.getBalance());
            if (sender.getBalance() >= input.getAmount()) {
                senderUser.addTransaction(new TransferTransaction(input.getDescription(), input.getTimestamp(), receiver.getIban(), sender.getIban(), "sent", input.getAmount() + " " +  sender.getCurrency()));
                sender.addTransaction(new TransferTransaction(input.getDescription(), input.getTimestamp(), receiver.getIban(), sender.getIban(), "sent", input.getAmount() + " " +  sender.getCurrency()));;
                sender.setBalance(sender.getBalance() - input.getAmount());
                receiver.setBalance(receiver.getBalance() + rate * input.getAmount());
            } else {
                senderUser.addTransaction(new Transaction("Insufficient funds", input.getTimestamp()));
            }
        }
    }

    public void updateOutput(final CommandInput input, final ObjectMapper mapper) {

    }
}
