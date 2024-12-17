package org.poo.outputConstants;

public final class OutputConstants {
    private OutputConstants() {

    }

    public static final String FUNDS_ERROR = "Insufficient funds";
    public static final String CARD_FROZEN = "The card is frozen";
    public static final String CARD_DESTROYED = "The card has been destroyed";
    public static final String CARD_CREATED = "New card created";
    public static final String ACCOUNT_FUNDS =
            "Account couldn't be deleted - there are funds remaining";
    public static final String ACCOUNT_ERROR =
            "Account couldn't be deleted - see org.poo.transactions for details";
    public static final String NOT_SAVINGS =
            "This kind of report is not supported for a saving account";
    public static final String FUNDS_WARNING =
            "You have reached the minimum amount of funds, the card will be frozen";
    public static final String INTEREST_CHANGE =
            "Interest rate of the account changed to ";
}
