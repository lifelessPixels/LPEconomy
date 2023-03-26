package pl.lifelesspixels.lpeconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lifelesspixels.lpeconomy.LPEconomy;
import pl.lifelesspixels.lpeconomy.data.Currency;
import pl.lifelesspixels.lpeconomy.data.PlayerAccount;
import pl.lifelesspixels.lputilities.commands.CommandUtils;

public class AddBalanceCommand implements CommandExecutor {

    private static final String ADD_BALANCE_PERMISSION = "lpeconomy.command.addbalance";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(!sender.hasPermission(ADD_BALANCE_PERMISSION)) {
            CommandUtils.sendNoPermissionsMessage(sender, alias);
            return true;
        }

        if(args.length == 2) {
            String playerName = args[0];
            Player player = LPEconomy.getInstance().getServer().getPlayer(playerName);
            if(player == null) {
                CommandUtils.sendPlayerNotFound(sender, playerName);
                return true;
            }

            long balance = 0;
            try { balance = Long.parseLong(args[1]); }
            catch (Exception e) {
                CommandUtils.sendCannotParseInteger(sender, args[1]);
                return true;
            }

            if(balance <= 0) {
                sender.sendMessage(balance + "" + ChatColor.RED + " is not a valid amount to add");
                return true;
            }

            PlayerAccount account = LPEconomy.getInstance().getPlayerAccounts().getAccountFor(player);
            account.addToDefaultCurrencyBalance(balance);

            String nickname = playerName.endsWith("s") ? playerName + "'" : playerName + "'s";
            Currency defaultCurrency = LPEconomy.getInstance().getCurrencies().getDefaultCurrency();
            sender.sendMessage(ChatColor.GREEN + "Successfully added " + ChatColor.RESET + balance + " " +
                    defaultCurrency.getReadableName() + ChatColor.GREEN + " to " + ChatColor.RESET + nickname +
                    ChatColor.GREEN + " account");

            if(sender != player) {
                player.sendMessage(balance + " " + defaultCurrency.getReadableName() + ChatColor.GREEN +
                        " has been added to your account");
            }
            return true;
        }

        CommandUtils.sendUsage(sender, alias, "<player> <amount to add>");
        return true;
    }
}
