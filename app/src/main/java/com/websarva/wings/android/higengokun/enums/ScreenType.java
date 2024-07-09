package com.websarva.wings.android.higengokun.enums;


public enum ScreenType {
    MainActivity(-1),
    WeaknessActivity(-2),
    ResultActivity(-3),
    CategoryActivity(-4),
    ;

    private final int id;

    ScreenType(final int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }
}
