package cn.plumc.invrollback.ui;

import cn.plumc.invrollback.Config;
import cn.plumc.invrollback.PInvRollback;
import cn.plumc.invrollback.RollbackManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RollbackUI extends ChestUI{
    public static HashMap<UUID, RollbackUI> opened = new HashMap<>();

    private static final int PLAYER_HEAD = 4;
    private static final int FILTER = 49;
    private static final int PAGE_PREV = 45;
    private static final int PAGE_NEXT = 53;

    private static final int VIEW_PRE_PAGE = 36;

    private int page = 0;

    private final UUID target;

    public RollbackUI(Player player, UUID target) {
        super(player, 54, Component.text(Config.i18n("ui.rollback.title")));
        opened.put(player.getUniqueId(), this);
        this.target = target;
    }

    @Override
    public void update() {
        List<RollbackManager.ProfileView> views = PInvRollback.rollbackManager.getSortedViews(player.getUniqueId());
        int size = views.size();

        int maxPages;
        if (size % VIEW_PRE_PAGE == 0) {
            maxPages = size / VIEW_PRE_PAGE;
        } else {
            maxPages = size / VIEW_PRE_PAGE + 1;
        }
        maxPages-=1;

        int viewStart = page * VIEW_PRE_PAGE;
        int viewEnd = Math.min((page + 1) * VIEW_PRE_PAGE, size - 1);

        for (int i = 9; i < 45; i++) inventory.clear(i);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        for (int i = viewStart; i < viewEnd; i++) {
            RollbackManager.ProfileView view = views.get(i);
            int invIndex = i - viewStart + 9;
            ItemStack viewItem = new ItemStack(Material.CHEST);
            ItemMeta meta = viewItem.getItemMeta();
            meta.displayName(Component.text(Config.i18n("ui.rollback.view.type").formatted(view.type())));
            meta.lore(List.of(
                    Component.text(Config.i18n("ui.rollback.view.id").formatted(view.id())),
                    Component.text(Config.i18n("ui.rollback.view.date").formatted(format.format(view.date()))),
                    Component.text(Config.i18n("ui.rollback.view.message").formatted("".equals(view.message()) ? Config.i18n("view.message.null") : view.message())),
                    Component.text(""),
                    Component.text(Config.i18n("ui.rollback.view.tip").formatted(view.message()))
                    )
            );
            viewItem.setItemMeta(meta);
            inventory.setItem(invIndex, viewItem);
        }

        ItemStack frame = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setHideTooltip(true);
        frameMeta.displayName(Component.text(""));
        frame.setItemMeta(frameMeta);
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, frame);
        }
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, frame);
        }

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMetal = (SkullMeta) playerHead.getItemMeta();
        headMetal.setOwningPlayer(player);
        headMetal.displayName(Component.text(Config.i18n("ui.rollback.player").formatted(player.getName())));
        headMetal.lore(List.of(
                Component.text(Config.i18n("ui.rollback.player.count").formatted(size)),
                Component.text(Config.i18n("ui.rollback.player.new_date").formatted(views.getFirst()==null?Config.i18n("view.message.null"):format.format(views.getFirst().date())))
        ));
        playerHead.setItemMeta(headMetal);
        inventory.setItem(PLAYER_HEAD, playerHead);

        ItemStack filter = new ItemStack(Material.HOPPER);
        ItemMeta filterMeta = filter.getItemMeta();
        filterMeta.displayName(Component.text(Config.i18n("ui.rollback.filter")));
        filter.setItemMeta(filterMeta);
        inventory.setItem(FILTER, filter);

        ItemStack pagePrev = new ItemStack(Material.ARROW);
        ItemMeta pagePrevMeta = pagePrev.getItemMeta();
        if (page==0) pagePrevMeta.displayName(Component.text(Config.i18n("ui.rollback.previous.disabled").formatted(page+1, maxPages+1)));
        else pagePrevMeta.displayName(Component.text(Config.i18n("ui.rollback.previous").formatted(page+1, maxPages+1)));
        pagePrev.setItemMeta(pagePrevMeta);
        inventory.setItem(PAGE_PREV, pagePrev);

        ItemStack pageNext = new ItemStack(Material.ARROW);
        ItemMeta pageNextMeta = pageNext.getItemMeta();
        if (page==maxPages) pageNextMeta.displayName(Component.text(Config.i18n("ui.rollback.next.disabled").formatted(page+1, maxPages+1)));
        else pageNextMeta.displayName(Component.text(Config.i18n("ui.rollback.next").formatted(page+1, maxPages+1)));
        pageNext.setItemMeta(pageNextMeta);
        inventory.setItem(PAGE_NEXT, pageNext);
    }

    @Override
    public void onClick(ClickType clickType, InventoryAction action, int slot) {
        List<RollbackManager.ProfileView> views = PInvRollback.rollbackManager.getSortedViews(player.getUniqueId());
        int size = views.size();

        int maxPages;
        if (size % VIEW_PRE_PAGE == 0) {
            maxPages = size / VIEW_PRE_PAGE;
        } else {
            maxPages = size / VIEW_PRE_PAGE + 1;
        }

        if (slot==PAGE_PREV){
            if (page>0) page--;
            update();
            return;
        }
        if (slot==PAGE_NEXT){
            if (page<maxPages-1) page++;
            update();
            return;
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        opened.remove(player.getUniqueId());
    }

    public static void open(Player player, UUID target){
        new RollbackUI(player, target).open();
    }

    public static RollbackUI get(Player player, Inventory inventory){
        if (isPlayerOpen(player.getUniqueId(), inventory)) {
            return opened.get(player.getUniqueId());
        }
        return null;
    }
}
