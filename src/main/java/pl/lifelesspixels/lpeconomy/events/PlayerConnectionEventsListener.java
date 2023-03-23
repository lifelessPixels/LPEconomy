package pl.lifelesspixels.lpeconomy.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.lifelesspixels.lpeconomy.LPEconomy;

public class PlayerConnectionEventsListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        LPEconomy.getInstance().getPlayerAccounts().fetchAccountStateFromDatabase(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        LPEconomy.getInstance().getPlayerAccounts().removeAccountCacheFor(event.getPlayer());
    }

}
