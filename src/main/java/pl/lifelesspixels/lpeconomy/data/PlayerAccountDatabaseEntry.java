package pl.lifelesspixels.lpeconomy.data;

import pl.lifelesspixels.lpdatamanager.data.DatabaseEntry;

import java.util.HashMap;

public class PlayerAccountDatabaseEntry extends DatabaseEntry {

    private HashMap<String, Long> balances = new HashMap<>();

    public PlayerAccountDatabaseEntry() { }

    public PlayerAccountDatabaseEntry(String playerID) {
        setPlayerID(playerID);
    }

    public HashMap<String, Long> getBalances() {
        return balances;
    }

    public void setBalances(HashMap<String, Long> balances) {
        this.balances = balances;
    }

}
