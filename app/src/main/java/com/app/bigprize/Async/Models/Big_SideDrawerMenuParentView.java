package com.app.bigprize.Async.Models;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class Big_SideDrawerMenuParentView implements Parent<Big_SideDrawerMenuChildView> {

    // a recipe contains several ingredients
    private Big_Menu_ListItem menuListItem;
    private List<Big_SideDrawerMenuChildView> answer;

    public Big_SideDrawerMenuParentView(Big_Menu_ListItem question, List<Big_SideDrawerMenuChildView> answer) {
        this.menuListItem = question;
        this.answer = answer;
    }

    @Override
    public List<Big_SideDrawerMenuChildView> getChildList() {
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
