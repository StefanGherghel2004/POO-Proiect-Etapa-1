package org.poo.inputHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.command.*;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

import static org.poo.utils.Utils.resetRandom;

public final class InputHandler {
    public static final double ROUNDING_UTIL = 10000.0;

    private InputHandler() {

    }

    /**
     * Handles a specific command input and returns the corresponding command object.
     *
     * @param command The command input object that contains the details of the command.
     * @param bank The bank instance which will be passed to the command.
     * @param mapper The object mapper instance for JSON processing.
     * @return The command object that corresponds to the given command input.
     */
    public static Command handler(final CommandInput command, final Bank bank,
                                  final ObjectMapper mapper) {
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

    /**
     * Processes the bank input and handles the execution of all commands sequentially.
     * This method takes in the input data, initializes a bank instance, processes each
     * command, and accumulates the output for each executed command.
     *
     * @param input The object input containing user data, exchange rates, and commands to execute.
     * @param output The output array node where the results of executed commands will be added.
     */
    public static void bankHandler(final ObjectInput input, final ArrayNode output) {
        resetRandom();
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

    }
}
