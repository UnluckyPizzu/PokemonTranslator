package com.pizzu.rest;

import com.pizzu.model.Pokemon;
import com.pizzu.utils.GeneralResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface PokemonController {
    public GeneralResponse<Pokemon> getPokemonInfo(String pokemonName);

    public GeneralResponse<Pokemon> getPokemonInfoTranslated(String pokemonName);


}
