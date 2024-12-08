package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.fileio.CommandInput;

public class SendMoneyCommand extends Command {

    public SendMoneyCommand(Bank bank, ObjectMapper mapper) {
        super(bank,mapper);
    }

    public void execute(CommandInput input) {
        Account sender = null;
        Account receiver = null;
        User receiverUser = null;
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(input.getAccount())) {
                    sender = account;
                    receiverUser = user;
                }
                if (account.getIban().equals(input.getReceiver())) {
                    receiver = account;
                }
            }
        }
        if (receiverUser != null && !input.getEmail().equals(receiverUser.getEmail())) {
            return;
        }

        if (sender != null && receiver != null) {
            double rate = bank.getRate(sender.getCurrency(), receiver.getCurrency());
            if (sender.getBalance() >= input.getAmount()) {
                sender.setBalance(sender.getBalance() - input.getAmount());
                receiver.setBalance(receiver.getBalance() + rate * input.getAmount());
            }

            System.out.println(rate);
            System.out.println(sender.getBalance());
            System.out.println(receiver.getBalance());
        }
    }

    public void updateOutput(CommandInput input, ObjectMapper mapper) {

    }
}
