package com.pizzu.rest;

import com.pizzu.model.Pokemon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface PokemonController {
    public ResponseEntity<Pokemon> getPokemonInfo(String pokemonName);

    public ResponseEntity<Pokemon> getPokemonInfoTranslated(String pokemonName);


}
