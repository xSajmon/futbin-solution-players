package com.simon.futbinsolutionplayers.player;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Type {
    GOLD("gold"), SILVER("silver"), BRONZE("bronze");

    String type;


    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
