package com.pizzu.rest;

import com.pizzu.model.Pokemon;
import com.pizzu.service.PokemonService;
import com.pizzu.service.TranslateService;
import com.pizzu.utils.GeneralResponse;
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
    public GeneralResponse<Pokemon> getPokemonInfo(@PathVariable(value = "pokemonName") String name) {
        Pokemon pokemon = pokemonService.getPokemonTranslatedDescriptionByName(name);

        if (pokemon == null) {
            return new GeneralResponse<Pokemon>(HttpStatus.INTERNAL_SERVER_ERROR,"Error during getPokemonInfo",null);
        }
        else
        {
            return new GeneralResponse<Pokemon>(HttpStatus.OK,"Pokemon found",pokemon);

        }
    }

    @GetMapping(value = "/pokemon/translated/{pokemonName}")
    public GeneralResponse<Pokemon> getPokemonInfoTranslated(@PathVariable(value = "pokemonName") String name) {
        Pokemon pokemon = translateService.getTranslatePokemon(pokemonService.getPokemonTranslatedDescriptionByName(name));

        if (pokemon == null) {
            return new GeneralResponse<Pokemon>(HttpStatus.INTERNAL_SERVER_ERROR,"Error during getPokemonInfoTranslated",null);
        }
        else
        {
            return new GeneralResponse<Pokemon>(HttpStatus.OK,"Pokemon found and translated",pokemon);
        }

    }

}
