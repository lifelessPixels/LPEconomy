package pl.lifelesspixels.lpeconomy.data;

import org.bukkit.entity.Player;
import pl.lifelesspixels.lpdatamanager.LPDataManager;
import pl.lifelesspixels.lpeconomy.LPEconomy;

import java.util.HashMap;

public class PlayerAccounts {

    private static final String ACCOUNTS_RESOURCE_IDENTIFIER = "lpeconomy.accounts";

    private final LPDataManager dataManager = LPEconomy.getInstance().getDataManager();

    private HashMap<Player, PlayerAccount> playerAccounts = new HashMap<>();

    public PlayerAccount getAccountFor(Player player) {
        if(playerAccounts.containsKey(player))
            return playerAccounts.get(player);

        // if for some reason, map does not contain player's account, retrieve it from database and cache
        return fetchAccountStateFromDatabase(player);
    }

    public PlayerAccount fetchAccountStateFromDatabase(Player player) {
        PlayerAccount account = getPlayerAccountFromDatabase(player);
        playerAccounts.put(player, account);
        return account;
    }

    public void removeAccountCacheFor(Player player) {
        playerAccounts.remove(player);
    }

    public boolean hasDatabaseAccountFor(Player player) {
        PlayerAccountDatabaseEntry entry = dataManager.getPlayerData(player, ACCOUNTS_RESOURCE_IDENTIFIER, PlayerAccountDatabaseEntry.class);
        return entry != null;
    }

    public void accountDidChange(PlayerAccountDatabaseEntry changedEntry) {
        setPlayerAccountInDatabase(changedEntry);
    }

    private PlayerAccount getPlayerAccountFromDatabase(Player player) {
        PlayerAccountDatabaseEntry accountEntry = dataManager.getPlayerData(player, ACCOUNTS_RESOURCE_IDENTIFIER, PlayerAccountDatabaseEntry.class);
        if(accountEntry == null) {
            accountEntry = new PlayerAccountDatabaseEntry(player.getUniqueId().toString());
            setPlayerAccountInDatabase(accountEntry);
        }

        return new PlayerAccount(accountEntry, this);
    }

    private void setPlayerAccountInDatabase(PlayerAccountDatabaseEntry account) {
        dataManager.setPlayerData(ACCOUNTS_RESOURCE_IDENTIFIER, account, PlayerAccountDatabaseEntry.class);
    }

}
