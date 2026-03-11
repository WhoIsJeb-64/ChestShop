package com.LRFLEW.register.payment.forChestShop.methods;

import com.LRFLEW.register.payment.forChestShop.Method;
import com.projectposeidon.johnymuffin.UUIDManager;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.whoisjeb.aurum.Aurum;
import org.whoisjeb.aurum.data.AurumUser;
import java.util.UUID;

public class AurumEconomy implements Method {
    private Aurum aurum;

    public Object getPlugin() {
        return this.aurum;
    }

    public String getName() {
        return "Aurum";
    }

    public String getVersion() {
        return aurum.getDescription().getVersion();
    }

    public int fractionalDigits() {
        return 2;
    }

    public String format(double amount) {
        return String.valueOf(amount);
    }

    public boolean hasBanks() {
        return false;
    }

    public boolean hasBank(String bank, World world) {
        return false;
    }

    public boolean hasAccount(String name, World world) {
        return Aurum.api().user(UUIDManager.getInstance().getUUIDFromUsername(name)).hasProperty("economy.balance");
    }

    public boolean hasBankAccount(String bank, String name, World world) {
        return false;
    }

    public MethodAccount getAccount(String name, World world) {
        UUID uuid = UUIDManager.getInstance().getUUIDFromUsername(name);
        return new AurumPersonalAccount(uuid);
    }

    public MethodBankAccount getBankAccount(String bank, String name, World world) {
        return null;
    }

    public boolean isCompatible(Plugin plugin) {
        return true;
    }

    public void setPlugin(Plugin plugin) {
        this.aurum = (Aurum) plugin;
    }

    private static class AurumPersonalAccount implements MethodAccount {
        private UUID uuid;
        private AurumUser user;
        private double balance;

        private AurumPersonalAccount(UUID uuid) {
            this.uuid = uuid;
            this.user = Aurum.api().user(uuid);
            this.balance = user.getDouble("economy.balance");
        }

        public double balance(World world) {
            return balance;
        }

        public boolean set(double amount, World world) {
            user.setProperty("economy.balance", amount);
            return true;
        }

        public boolean add(double amount, World world) {
            return user.addBalance(amount);
        }

        public boolean subtract(double amount, World world) {
            return user.subtractBalance(amount, false);
        }

        public boolean multiply(double amount, World world) {
            user.setProperty("economy.balance", (balance * amount));
            return true;
        }

        public boolean divide(double amount, World world) {
            user.setProperty("economy.balance", (balance / amount));
            return true;
        }

        public boolean hasEnough(double amount, World world) {
            return balance >= amount;
        }

        public boolean hasOver(double amount, World world) {
            return balance > amount;
        }

        public boolean hasUnder(double amount, World world) {
            return balance < amount;
        }

        public boolean isNegative(World world) {
            return balance < 0;
        }

        public boolean remove() {
            return false;
        }
    }
}
