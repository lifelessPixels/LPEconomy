package pl.lifelesspixels.lpeconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lifelesspixels.lpeconomy.LPEconomy;
import pl.lifelesspixels.lpeconomy.data.Currency;
import pl.lifelesspixels.lpeconomy.data.PlayerAccount;
import pl.lifelesspixels.lpeconomy.data.PlayerAccounts;
import pl.lifelesspixels.lputilities.commands.CommandUtils;

public class BalanceCommand implements CommandExecutor {

    private static final String OWN_BALANCE_PERMISSION = "lpeconomy.command.balance.own";
    private static final String OTHERS_BALANCE_PERMISSION = "lpeconomy.command.balance.others";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        Currency defaultCurrency = LPEconomy.getInstance().getCurrencies().getDefaultCurrency();
        PlayerAccounts accounts = LPEconomy.getInstance().getPlayerAccounts();

        if(args.length == 0) {
            // if no arguments were provided just show own balance
            if(!(sender instanceof Player)) {
                sender.sendMessage("/" + alias + " command without arguments must be used as a player");
                return true;
            }
            Player player = (Player)(sender);

            // check permissions
            if(!player.hasPermission(OWN_BALANCE_PERMISSION)) {
                CommandUtils.sendNoPermissionsMessage(player, alias);
                return true;
            }

            // show balance
            long balance = accounts.getAccountFor(player).getCurrencyBalance(defaultCurrency);
            player.sendMessage(ChatColor.GREEN + "Your balance: " + ChatColor.RESET + balance + " " + defaultCurrency.getReadableName());
            return true;

        } else if (args.length == 1) {
            // if one argument was provided, treat it as a player name
            String playerName = args[0];

            // get player in question
            Player player = LPEconomy.getInstance().getServer().getPlayer(playerName);

            // check permissions
            if((player == sender && !sender.hasPermission(OWN_BALANCE_PERMISSION)) ||
                    (player != sender && !sender.hasPermission(OTHERS_BALANCE_PERMISSION))) {
                CommandUtils.sendNoPermissionsMessage(sender, alias);
                return true;
            }

            // check if player exists
            if(player == null) {
                CommandUtils.sendPlayerNotFound(sender, playerName);
                return true;
            }

            // fetch balance
            long balance;
            if(player.isOnline()) {
                balance = accounts.getAccountFor(player).getCurrencyBalance(defaultCurrency);
            } else {
                if(!accounts.hasDatabaseAccountFor(player)) {
                    CommandUtils.sendPlayerNotFound(sender, playerName);
                    return true;
                }

                PlayerAccount account = accounts.fetchAccountStateFromDatabase(player);
                balance = account.getCurrencyBalance(defaultCurrency);
                accounts.removeAccountCacheFor(player);
            }

            // show balance
            String nickname = playerName.endsWith("s") ? playerName + "'" : playerName + "'s";
            player.sendMessage(ChatColor.GREEN + nickname + " balance: " + ChatColor.RESET + balance + " " + defaultCurrency.getReadableName());
            return true;
        }

        CommandUtils.sendUsage(sender, alias, "[player]");
        return true;
    }

}
