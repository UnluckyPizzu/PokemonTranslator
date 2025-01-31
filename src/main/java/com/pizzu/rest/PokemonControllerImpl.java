package com.pizzu.rest;

import com.pizzu.model.Pokemon;
import com.pizzu.service.PokemonService;
import com.pizzu.service.TranslateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class PokemonControllerImpl implements PokemonController {

    @Autowired
    private PokemonService pokemonService;

    @Autowired
    private TranslateService translateService;

    private Logger logger = LoggerFactory.getLogger(PokemonControllerImpl.class);

    @GetMapping(value = "/pokemon/{pokemonName}")
    public ResponseEntity<Pokemon> getPokemonInfo(@PathVariable(value = "pokemonName") String name) {
        Pokemon pokemon = pokemonService.getPokemonTranslatedDescriptionByName(name);

        if (pokemon == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity<>(pokemon, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/pokemon/translated/{pokemonName}")
    public ResponseEntity<Pokemon> getPokemonInfoTranslated(@PathVariable(value = "pokemonName") String name) {
        Pokemon pokemon = translateService.getTranslatePokemon(pokemonService.getPokemonTranslatedDescriptionByName(name));

        if (pokemon == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity<>(pokemon, HttpStatus.OK);
        }

    }

}
