package com.simon.futbinsolutionplayers.player;

public enum Rarity {
    RARE("rare"), NONRARE("non-rare");

    String rarity;

    Rarity(String rarity) {
        this.rarity = rarity;
    }

    public String getRarity() {
        return rarity;
    }

}
