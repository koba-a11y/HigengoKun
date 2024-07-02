package com.websarva.wings.android.higengokun.enums;

import com.websarva.wings.android.higengokun.ResultActivity;

//CategoryActivityへ遷移する可能性があるもの
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
