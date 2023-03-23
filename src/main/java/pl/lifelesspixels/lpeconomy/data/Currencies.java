package pl.lifelesspixels.lpeconomy.data;

import org.bukkit.configuration.file.FileConfiguration;
import pl.lifelesspixels.lpeconomy.LPEconomy;

import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

public class Currencies {

    private Currency defaultCurrency;
    private HashMap<String, Currency> currencies = new HashMap<>();

    public void loadDefaultCurrencySettingsFrom(FileConfiguration configuration) {
        Logger logger = LPEconomy.getInstance().getLogger();

        if(!configuration.contains("default-currency-identifier") || !configuration.isString("default-currency-identifier")) {
            logger.severe("missing or invalid default currency identifier in plugin.yml");
            loadFallbackDefaultCurrency();
            return;
        }
        String currencyIdentifier = Objects.requireNonNull(configuration.getString("default-currency-identifier"));

        if(!configuration.contains("default-currency-readable-name") || !configuration.isString("default-currency-readable-name")) {
            logger.severe("missing or invalid default currency readable name in plugin.yml");
            loadFallbackDefaultCurrency();
            return;
        }
        String readableName = Objects.requireNonNull(configuration.getString("default-currency-readable-name"));

        if(!configuration.contains("default-currency-default-balance") || !configuration.isLong("default-currency-default-balance")) {
            logger.severe("missing or invalid default currency default balance in plugin.yml");
            loadFallbackDefaultCurrency();
            return;
        }

        long defaultBalance = configuration.getLong("default-currency-default-balance");
        if(defaultBalance < 0) {
            logger.severe("default currency default balance cannot be negative");
            loadFallbackDefaultCurrency();
            return;
        }

        defaultCurrency = new Currency(currencyIdentifier, readableName, defaultBalance);
        currencies.put(defaultCurrency.getIdentifier(), defaultCurrency);
    }

    private void loadFallbackDefaultCurrency() {
        defaultCurrency = new Currency("coins", "coins", 100);
        currencies.put(defaultCurrency.getIdentifier(), defaultCurrency);
    }

    public Currency getCurrency(String identifier) {
        return currencies.get(identifier);
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void registerCurrency(Currency currency) {

    }

}
