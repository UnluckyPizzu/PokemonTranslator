package com.pizzu.service;

import com.pizzu.model.Pokemon;
import org.springframework.stereotype.Service;

public interface PokemonService {
    public Pokemon getPokemonTranslatedDescriptionByName(String name);
}
