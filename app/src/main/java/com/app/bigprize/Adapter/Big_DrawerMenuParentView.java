package com.app.bigprize.Adapter;

import com.app.bigprize.Async.Models.Big_Menu_ListItem;
import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class Big_DrawerMenuParentView implements Parent<Big_DrawerMenuChildView> {

    // a recipe contains several ingredients
    private Big_Menu_ListItem menuListItem;
    private List<Big_DrawerMenuChildView> answer;

    public Big_DrawerMenuParentView(Big_Menu_ListItem question, List<Big_DrawerMenuChildView> answer) {
        this.menuListItem = question;
        this.answer = answer;
    }

    @Override
    public List<Big_DrawerMenuChildView> getChildList() {
        return answer;
    }

    public Big_Menu_ListItem getMenu() {
        return menuListItem;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
