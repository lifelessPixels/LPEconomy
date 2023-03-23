package pl.lifelesspixels.lpeconomy;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.lifelesspixels.lpdatamanager.LPDataManager;
import pl.lifelesspixels.lpeconomy.commands.BalanceCommand;
import pl.lifelesspixels.lpeconomy.data.Currencies;
import pl.lifelesspixels.lpeconomy.data.PlayerAccounts;

import java.util.Objects;

public class LPEconomy extends JavaPlugin implements Listener {

    private static LPEconomy instance;
    private PlayerAccounts playerAccounts;
    private Currencies currencies;
    private LPDataManager dataManager;

    @Override
    public void onEnable() {
        instance = this;

        // get reference to data manager
        dataManager = (LPDataManager)(getServer().getPluginManager().getPlugin("LPDataManager"));

        // create storage objects
        playerAccounts = new PlayerAccounts();
        currencies = new Currencies();

        // load configuration
        saveDefaultConfig();
        currencies.loadDefaultCurrencySettingsFrom(getConfig());

        // register commands
        Objects.requireNonNull(getCommand("balance")).setExecutor(new BalanceCommand());

        // prefetch cache for online players
        for(Player player : getServer().getOnlinePlayers())
            playerAccounts.fetchAccountStateFromDatabase(player);
    }

    public PlayerAccounts getPlayerAccounts() {
        return playerAccounts;
    }

    public Currencies getCurrencies() {
        return currencies;
    }

    public LPDataManager getDataManager() {
        return dataManager;
    }

    public static LPEconomy getInstance() {
        return instance;
    }

}