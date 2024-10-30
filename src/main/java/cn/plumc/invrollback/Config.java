package cn.plumc.invrollback;

public class Config {
    public static String i18n(String key){
        String text = PInvRollback.instance.getConfig().getString("messages.%s".formatted(key.replaceAll("\\.", "_")), key).replaceAll("&", "§");
        if (key.startsWith("command")) return getPrefix()+" "+text;
        return text;
    }

    public static int maxCount(String type){
        return PInvRollback.instance.getConfig().getInt("maxSaves.%s".formatted(type));
    }

    public static int pageLines(){
        return PInvRollback.instance.getConfig().getInt("command.page_lines");
    }

    public static String getPrefix(){
        return PInvRollback.instance.getConfig().getString("messages.command.prefix", "").replaceAll("&", "§");
    }
}
