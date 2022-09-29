package com.simon.futbinsolutionplayers.player;

public enum Rarity implements Detail {
    RARE("rare"), NONRARE("non-rare");

    String rarity;

    Rarity(String rarity) {
        this.rarity = rarity;
    }

    public String getName() {
        return rarity;
    }

}
