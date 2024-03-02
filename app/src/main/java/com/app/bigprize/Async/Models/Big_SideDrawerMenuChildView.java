package com.app.bigprize.Async.Models;

public class Big_SideDrawerMenuChildView {

    // a recipe contains several ingredients
    private Big_Sub_MenuList_Item subMenu;

    public Big_SideDrawerMenuChildView(Big_Sub_MenuList_Item name) {
        subMenu = name;
    }

    public Big_Sub_MenuList_Item getMenu() {
        return subMenu;
    }
}
