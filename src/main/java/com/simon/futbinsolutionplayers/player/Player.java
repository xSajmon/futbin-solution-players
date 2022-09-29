package com.simon.futbinsolutionplayers.player;

public class Player {

    private String name;
    private Type type;
    private Rarity rarity;

    public Player(String name, Type type, Rarity rarity) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", name, type.getType(), rarity.getRarity());
    }
}
