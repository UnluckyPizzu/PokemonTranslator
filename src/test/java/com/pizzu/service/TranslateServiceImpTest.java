package com.pizzu.service;

import com.pizzu.model.Pokemon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;



import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TranslateServiceImpTest {

    @InjectMocks
    private TranslateServiceImpl translateService;

    @Mock
    private RestTemplate restTemplate;

    Pokemon pokemon;

    @BeforeEach
    void setUp() {
        pokemon = new Pokemon();
        pokemon.setDescription("Descrizione inizio test");
        pokemon.setIsLegendary(false);
        pokemon.setHabitat("forest");

        ReflectionTestUtils.setField(translateService,"shakespeareUrl","shakespeareUrl");
        ReflectionTestUtils.setField(translateService,"yodaUrl","yodaUrl");
    }

    @Test
    public void givenPokemonWithNoDescription_whenGetTranslatePokemon_thenReturnSamePokemon() {
        Pokemon pokemon = new Pokemon();

        Pokemon result = translateService.getTranslatePokemon(pokemon);

        Assertions.assertEquals(result, pokemon);
    }


    @Test
    public void givenNull_whenGetTranslatePokemon_thenReturnNull() {
        Pokemon pokemon = null;

        Pokemon result = translateService.getTranslatePokemon(pokemon);

        Assertions.assertNull(result);
    }


    @Test
    void givenNotLegendaryAndNotCavePokemon_whenGetTranslatePokemon_thenReturnPokemonTranslatedShakespeare() {
        String translatedText = "Descrizione tradotta Shakespeare";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{ \"contents\": { \"translated\": \"" + translatedText + "\" }}",HttpStatus.OK);
        when(restTemplate.postForEntity(any(),any(),any(Class.class)))
                .thenReturn(responseEntity);

        Pokemon result = translateService.getTranslatePokemon(pokemon);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(translatedText, result.getDescription());
    }

    @Test
    void givenLegendaryPokemon_whenGetTranslatePokemon_thenReturnPokemonTranslatedYoda() {
        pokemon.setIsLegendary(true);
        String translatedText = "Descrizione tradotta Yoda";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{ \"contents\": { \"translated\": \"" + translatedText + "\" }}",HttpStatus.OK);
        when(restTemplate.postForEntity(any(),any(),any(Class.class)))
                .thenReturn(responseEntity);

        Pokemon result = translateService.getTranslatePokemon(pokemon);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(translatedText, result.getDescription());
    }

    @Test
    void givenCavePokemon_whenGetTranslatePokemon_thenReturnPokemonTranslatedYoda() {
        pokemon.setHabitat("cave");
        String translatedText = "Descrizione tradotta Yoda";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{ \"contents\": { \"translated\": \"" + translatedText + "\" }}",HttpStatus.OK);
        when(restTemplate.postForEntity(any(),any(),any(Class.class)))
                .thenReturn(responseEntity);

        Pokemon result = translateService.getTranslatePokemon(pokemon);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(translatedText, result.getDescription());
    }


    @Test
    void givenPokemon_whenGetTranslatePokemonHasErrorDuringPostForEntity_thenReturnPokemon() {
         when(restTemplate.postForEntity(any(),any(),any(Class.class)))
                .thenThrow(HttpClientErrorException.class);

        Pokemon result = translateService.getTranslatePokemon(pokemon);

        Assertions.assertEquals(result,pokemon);
    }

    @Test
    void givenpostForEntityReturnNoBody_whenGetTranslatePokemon_thenReturnPokemon() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("{ \"contents\": { \"translated\": \"\" }}",HttpStatus.OK);
        when(restTemplate.postForEntity(any(),any(),any(Class.class)))
                .thenReturn(responseEntity);

        Pokemon result = translateService.getTranslatePokemon(pokemon);

        Assertions.assertEquals(result,pokemon);
    }

}
