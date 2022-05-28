package net.ccbluex.liquidbounce.launch.data.legacyui.clickgui.style.styles.ClickGui1.setting;

import net.ccbluex.liquidbounce.features.module.Module;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public final class Manager {

    private static final List<Setting> settingList = new CopyOnWriteArrayList<>();

    public static void put(Setting setting) {
        settingList.add(setting);
    }

    public static List<Setting> getSettingsByMod(Module module) {
        return settingList.stream().filter(setting -> setting.getParentModule().equals(module))
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    public static List<Setting> getSettingList() {
        return settingList;
    }

}
