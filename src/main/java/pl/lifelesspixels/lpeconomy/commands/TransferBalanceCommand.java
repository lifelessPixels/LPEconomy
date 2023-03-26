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

public class TransferBalanceCommand implements CommandExecutor {

    private static final String TRANSFER_BALANCE_PERMISSION = "lpeconomy.command.transferbalance";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(!sender.hasPermission(TRANSFER_BALANCE_PERMISSION)) {
            CommandUtils.sendNoPermissionsMessage(sender, alias);
            return true;
        }

        if(!(sender instanceof Player)) {
            CommandUtils.sendOnlyAsPlayer(sender, alias);
            return true;
        }
        Player player = (Player)(sender);

        if(args.length == 2) {
            String recipientName = args[0];
            Player recipient = LPEconomy.getInstance().getServer().getPlayer(recipientName);
            if(recipient == null) {
                CommandUtils.sendPlayerNotFound(sender, recipientName);
                return true;
            }

            if(sender == recipient) {
                sender.sendMessage(ChatColor.RED + "You cannot make yourself a target of balance transfer");
                return true;
            }

            long amountToTransfer = 0;
            try { amountToTransfer = Long.parseLong(args[1]); }
            catch (Exception e) {
                CommandUtils.sendCannotParseInteger(sender, args[1]);
                return true;
            }

            if(amountToTransfer < 0) {
                sender.sendMessage(amountToTransfer + "" + ChatColor.RED + " is not a valid amount to transfer");
                return true;
            }

            PlayerAccounts accounts = LPEconomy.getInstance().getPlayerAccounts();
            PlayerAccount playerAccount = accounts.getAccountFor(player);

            if(!playerAccount.canAffordInDefaultCurrency(amountToTransfer)) {
                sender.sendMessage(ChatColor.RED + "You cannot afford this transfer");
                return true;
            }

            PlayerAccount recipientAccount = accounts.getAccountFor(recipient);
            playerAccount.subtractFromDefaultCurrencyBalance(amountToTransfer);
            recipientAccount.addToDefaultCurrencyBalance(amountToTransfer);

            String verb = switch (alias.toLowerCase()) {
                case "donate" -> "donated";
                case "pay" -> "paid";
                default -> "transferred";
            };

            Currency defaultCurrency = LPEconomy.getInstance().getCurrencies().getDefaultCurrency();
            sender.sendMessage(ChatColor.GREEN + "Successfully " + verb + " " + ChatColor.RESET + amountToTransfer +
                    " " + defaultCurrency.getReadableName() + ChatColor.GREEN + " to " + ChatColor.RESET + recipientName);
            recipient.sendMessage(ChatColor.GREEN + "You received " + ChatColor.RESET + amountToTransfer + " " +
                    defaultCurrency.getReadableName() + ChatColor.GREEN + " from " + ChatColor.RESET +  player.getName());
            return true;
        }

        CommandUtils.sendUsage(sender, alias, "<player> <how much>");
        return true;
    }

}
