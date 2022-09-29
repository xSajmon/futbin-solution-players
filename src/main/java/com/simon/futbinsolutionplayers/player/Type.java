package com.simon.futbinsolutionplayers.player;

public enum Type implements Detail {
    GOLD("gold"), SILVER("silver"), BRONZE("bronze");

    String type;


    Type(String type) {
        this.type = type;
    }

    public String getName() {
        return type;
    }

}
