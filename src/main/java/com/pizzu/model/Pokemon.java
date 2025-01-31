package com.pizzu.model;

public class Pokemon {
    private String name;
    private String description;
    private String habitat;
    private Boolean isLegendary;

    public Pokemon() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public Boolean getIsLegendary() {
        return isLegendary;
    }

    public void setIsLegendary(Boolean legendary) {
        isLegendary = legendary;
    }
}
