package cn.plumc.invrollback;

public class PInvRollbackConfig {
    public static String i18n(String key){
        return PInvRollback.instance.getConfig().getString("messages.%s".formatted(key), key);
    }

    public static int maxCount(String type){
        return PInvRollback.instance.getConfig().getInt("maxSaves.%s".formatted(type));
    }
}
