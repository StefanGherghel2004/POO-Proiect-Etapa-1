package org.poo.inputHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Bank;
import org.poo.command.*;
import org.poo.fileio.CommandInput;

public class InputHandler {

    static public Command handler(CommandInput command, Bank bank, ObjectMapper mapper) {
        if (command.getCommand().equals("printUsers")) {
            return new PrintUsersCommand(bank, mapper);
        } else if (command.getCommand().equals("addAccount")) {
            return new AddAccountCommand(bank, mapper);
        } else if (command.getCommand().equals("createCard")) {
            return new CreateCardCommand(bank, mapper);
        } else if (command.getCommand().equals("addFunds")) {
            return new AddFundsCommand(bank, mapper);
        } else if (command.getCommand().equals("deleteAccount")) {
            return new DeleteAccountCommand(bank, mapper);
        } else if (command.getCommand().equals("createOneTimeCard")) {
            return new CreateCardCommand(bank, mapper);
        } else if (command.getCommand().equals("deleteCard")) {
            return new DeleteCardCommand(bank, mapper);
        } else if (command.getCommand().equals("setMinimumBalance")) {
            return new SetMinimumBalanceCommand(bank, mapper);
        } else if (command.getCommand().equals("payOnline")) {
            return new PayOnlineCommand(bank, mapper);
        } else if (command.getCommand().equals("sendMoney")) {
            return new SendMoneyCommand(bank, mapper);
        } else if (command.getCommand().equals("setAlias")) {
            return new SetAliasCommand(bank, mapper);
        } else if (command.getCommand().equals("printTransactions")) {
            return new PrintTransactionsCommand(bank, mapper);
        } else  {
            return new PrintUsersCommand(bank, mapper);
        }

    }
}
