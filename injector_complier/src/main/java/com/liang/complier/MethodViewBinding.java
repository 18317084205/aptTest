package com.liang.complier;

public class MethodViewBinding {
    private final String name;
    private final int[] resIds;

    public MethodViewBinding(String name, int[] resIds) {
        this.name = name;
        this.resIds = resIds;
    }

    public String getName() {
        return name;
    }

    public int[] getIds() {
        return resIds;
    }
}
