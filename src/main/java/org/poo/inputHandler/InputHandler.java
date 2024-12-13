package org.poo.inputHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.command.*;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

import static org.poo.utils.Utils.resetRandom;

public class InputHandler {

    public static Command handler(final CommandInput command, final Bank bank, final ObjectMapper mapper) {
        switch (command.getCommand()) {
            case "printUsers" -> {
                return new PrintUsersCommand(bank, mapper);
            }
            case "addAccount" -> {
                return new AddAccountCommand(bank, mapper);
            }
            case "createCard", "createOneTimeCard" -> {
                return new CreateCardCommand(bank, mapper);
            }
            case "addFunds" -> {
                return new AddFundsCommand(bank, mapper);
            }
            case "deleteAccount" -> {
                return new DeleteAccountCommand(bank, mapper);
            }
            case "deleteCard" -> {
                return new DeleteCardCommand(bank, mapper);
            }
            case "setMinimumBalance" -> {
                return new SetMinimumBalanceCommand(bank, mapper);
            }
            case "payOnline" -> {
                return new PayOnlineCommand(bank, mapper);
            }
            case "sendMoney" -> {
                return new SendMoneyCommand(bank, mapper);
            }
            case "setAlias" -> {
                return new SetAliasCommand(bank, mapper);
            }
            case "printTransactions" -> {
                return new PrintTransactionsCommand(bank, mapper);
            }
            case "checkCardStatus" -> {
                return new CardStatusCommand(bank, mapper);
            }
            case "changeInterestRate", "addInterest" -> {
                return new InterestCommand(bank, mapper);
            }
            case "splitPayment" -> {
                return new SplitPaymentCommand(bank, mapper);
            }
            case "report" -> {
                return new ReportCommand(bank, mapper);
            }
            case "spendingsReport" -> {
                return new SpendingsReportCommand(bank, mapper);
            }
            default -> {
                return null;
            }
        }

    }

    public static void bankHandler(final ObjectInput input, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        Bank bank = new Bank()
                .addUsers(input.getUsers())
                .addExchangeRates(input.getExchangeRates())
                .addSecondaryExchangeRates();

        for (CommandInput command : input.getCommands()) {
            Command myCommand = handler(command, bank, mapper);
            if (myCommand != null) {

                myCommand.execute(command);
                myCommand.updateOutput(command, mapper);

                if (!myCommand.getCommandOutput().isEmpty()) {
                    output.add(myCommand.getCommandOutput());
                }
            }
        }

        resetRandom();
    }
}
