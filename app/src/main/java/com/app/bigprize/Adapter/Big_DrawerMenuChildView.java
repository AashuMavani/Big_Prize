package com.app.bigprize.Adapter;


import com.app.bigprize.Async.Models.Big_Sub_MenuList_Item;

public class Big_DrawerMenuChildView {

    // a recipe contains several ingredients
    private Big_Sub_MenuList_Item subMenu;

    public Big_DrawerMenuChildView(Big_Sub_MenuList_Item name) {

        subMenu = name;
    }

    public Big_Sub_MenuList_Item getMenu() {
        return subMenu;
    }
}
