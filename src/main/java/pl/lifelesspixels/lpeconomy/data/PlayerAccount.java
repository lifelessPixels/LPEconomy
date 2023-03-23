package pl.lifelesspixels.lpeconomy.data;

import org.bukkit.entity.Player;
import pl.lifelesspixels.lpeconomy.LPEconomy;

import java.util.HashMap;

public class PlayerAccount {

    private final PlayerAccountDatabaseEntry databaseEntry;
    private final PlayerAccounts parent;

    public PlayerAccount(PlayerAccountDatabaseEntry databaseEntry, PlayerAccounts parent) {
        this.databaseEntry = databaseEntry;
        this.parent = parent;
    }

    public PlayerAccountDatabaseEntry getDatabaseEntry() {
        return databaseEntry;
    }

    public void setCurrencyBalance(String currencyIdentifier, long balance) {
        setCurrencyBalance(LPEconomy.getInstance().getCurrencies().getCurrency(currencyIdentifier), balance);
    }

    public void setCurrencyBalance(Currency currency, long balance) {
        if(balance < 0)
            throw new IllegalArgumentException("balance cannot be negative");

        HashMap<String, Long> balances = databaseEntry.getBalances();
        String currencyIdentifier = currency.getIdentifier();

        if(balances.containsKey(currencyIdentifier) && balances.get(currencyIdentifier) == balance)
            return;

        balances.put(currencyIdentifier, balance);
        notifyParentAboutChanges();
    }

    public void setDefaultCurrencyBalance(long balance) {
        setCurrencyBalance(LPEconomy.getInstance().getCurrencies().getDefaultCurrency(), balance);
    }

    public long getCurrencyBalance(String currencyIdentifier) {
        return getCurrencyBalance(LPEconomy.getInstance().getCurrencies().getCurrency(currencyIdentifier));
    }

    public long getCurrencyBalance(Currency currency) {
        HashMap<String, Long> balances = databaseEntry.getBalances();
        String currencyIdentifier = currency.getIdentifier();

        if(balances.containsKey(currencyIdentifier))
            return balances.get(currencyIdentifier);

        long currencyDefaultBalance = currency.getDefaultBalance();
        setCurrencyBalance(currency, currencyDefaultBalance);
        return currencyDefaultBalance;
    }

    public long getDefaultCurrencyBalance() {
        return getCurrencyBalance(LPEconomy.getInstance().getCurrencies().getDefaultCurrency());
    }

    public boolean canAffordInCurrency(String currencyIdentifier, long expense) {
        return canAffordInCurrency(LPEconomy.getInstance().getCurrencies().getCurrency(currencyIdentifier), expense);
    }

    public boolean canAffordInCurrency(Currency currency, long expense) {
        if(expense < 0)
            throw new IllegalArgumentException("expense amount cannot be negative");

        return getCurrencyBalance(currency) >= expense;
    }

    public boolean canAffordInDefaultCurrency(long expense) {
        return canAffordInCurrency(LPEconomy.getInstance().getCurrencies().getDefaultCurrency(), expense);
    }

    public void addToCurrencyBalance(String currencyIdentifier, long howMuch) {
        addToCurrencyBalance(LPEconomy.getInstance().getCurrencies().getCurrency(currencyIdentifier), howMuch);
    }

    public void addToCurrencyBalance(Currency currency, long howMuch) {
        if(howMuch < 0)
            throw new IllegalArgumentException("amount to add cannot be negative");

        long currentBalance = getCurrencyBalance(currency);
        long resultingBalance;

        try { resultingBalance = Math.addExact(currentBalance, howMuch); }
        catch (ArithmeticException e) { resultingBalance = Long.MAX_VALUE; }
        setCurrencyBalance(currency, resultingBalance);
    }

    public void addToDefaultCurrencyBalance(long howMuch) {
        addToCurrencyBalance(LPEconomy.getInstance().getCurrencies().getDefaultCurrency(), howMuch);
    }

    public void subtractFromCurrencyBalance(String currencyIdentifier, long howMuch) {
        subtractFromCurrencyBalance(LPEconomy.getInstance().getCurrencies().getCurrency(currencyIdentifier), howMuch);
    }

    public void subtractFromCurrencyBalance(Currency currency, long howMuch) {
        if(howMuch < 0)
            throw new IllegalArgumentException("amount to subtract cannot be negative");

        long currentBalance = getCurrencyBalance(currency);
        long resultingBalance;

        try { resultingBalance = Math.subtractExact(currentBalance, howMuch); }
        catch (ArithmeticException e) { resultingBalance = 0; }
        setCurrencyBalance(currency, resultingBalance);
    }

    public void subtractFromDefaultCurrencyBalance(long howMuch) {
        subtractFromCurrencyBalance(LPEconomy.getInstance().getCurrencies().getDefaultCurrency(), howMuch);
    }

    public boolean subtractFromCurrencyIfCanAfford(String currencyIdentifier, long expense) {
        return subtractFromCurrencyIfCanAfford(LPEconomy.getInstance().getCurrencies().getCurrency(currencyIdentifier), expense);
    }

    public boolean subtractFromCurrencyIfCanAfford(Currency currency, long expense) {
        if(!canAffordInCurrency(currency, expense))
            return false;

        subtractFromCurrencyBalance(currency, expense);
        return true;
    }

    public boolean subtractFromDefaultCurrencyIfCanAfford(long expense) {
        return subtractFromCurrencyIfCanAfford(LPEconomy.getInstance().getCurrencies().getDefaultCurrency(), expense);
    }

    public void notifyParentAboutChanges() {
        parent.accountDidChange(databaseEntry);
    }

}
