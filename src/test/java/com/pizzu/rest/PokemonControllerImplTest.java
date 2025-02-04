package com.pizzu.rest;

import com.pizzu.model.Pokemon;
import com.pizzu.service.PokemonService;
import com.pizzu.service.TranslateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class PokemonControllerImplTest {

    @InjectMocks
    private PokemonControllerImpl pokemonController;

    @Mock
    private PokemonService pokemonService;

    @Mock
    private TranslateService translateService;

    @Test
    public void givenName_whenGetPokemonInfo_thenReturnResponseSuccessWithPokemon(){
        Pokemon pokemon = new Pokemon();

        Mockito.when(pokemonService.getPokemonTranslatedDescriptionByName(Mockito.anyString())).thenReturn(pokemon);

        ResponseEntity<Pokemon> result = pokemonController.getPokemonInfo("test");

        Assertions.assertNotNull(result.getBody());

        Assertions.assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void givenName_whenGetPokemonInfo_thenReturnResponseFailureWithNull(){
        Pokemon pokemon = new Pokemon();

        Mockito.when(pokemonService.getPokemonTranslatedDescriptionByName(Mockito.anyString())).thenReturn(null);

        ResponseEntity<Pokemon> result = pokemonController.getPokemonInfo("test");

        Assertions.assertNull(result.getBody());

        Assertions.assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void givenPokemon_whengetPokemonInfoTranslated_thenReturnResponseSuccessWithPokemon(){
        Pokemon pokemon = new Pokemon();
        Mockito.when(pokemonService.getPokemonTranslatedDescriptionByName(Mockito.anyString())).thenReturn(pokemon);
        Mockito.when(translateService.getTranslatePokemon(Mockito.any(Pokemon.class))).thenReturn(pokemon);


        ResponseEntity<Pokemon> result = pokemonController.getPokemonInfoTranslated("test");

        Assertions.assertNotNull(result.getBody());

        Assertions.assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void givenNull_whengetPokemonInfoTranslated_thenReturnResponseFailureWithNull(){
        Mockito.when(pokemonService.getPokemonTranslatedDescriptionByName(Mockito.anyString())).thenReturn(null);
        Mockito.when(translateService.getTranslatePokemon(Mockito.any())).thenReturn(null);

        ResponseEntity<Pokemon> result = pokemonController.getPokemonInfoTranslated("test");

        Assertions.assertNull(result.getBody());

        Assertions.assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
