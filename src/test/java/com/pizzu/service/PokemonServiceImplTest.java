package com.pizzu.service;

import com.pizzu.model.Pokemon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceImplTest {
    @InjectMocks
    private PokemonServiceImpl pokemonService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void givenCorrectJson_whenConvertJSONToPokemon_thenReturnPokemon() {
        String jsonResponse = """
            {
              "name": "pikachu",
              "is_legendary": false,
              "flavor_text_entries": [
                {
                  "language": {"name": "en"},
                  "flavor_text": "Pikachu is an Electric-type Pokémon."
                }
              ],
              "habitat": {"name": "forest"}
            }
        """;

        ResponseEntity<String>  responseEntity = new ResponseEntity<>(jsonResponse,HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(),Mockito.any(Class.class))).thenReturn(responseEntity);

        Pokemon result = pokemonService.getPokemonTranslatedDescriptionByName("test");

        assertNotNull(result);
        assertEquals("pikachu", result.getName());
        assertFalse(result.getIsLegendary());
        assertEquals("Pikachu is an Electric-type Pokémon.", result.getDescription());
        assertEquals("forest", result.getHabitat());
    }

    @Test
    public void givenExceptionDuringGetForEntity_whenConvertJSONToPokemon_thenReturnNull() {

        Mockito.when(restTemplate.getForEntity(Mockito.anyString(),Mockito.any(Class.class))).thenThrow(HttpClientErrorException.class);

        Pokemon result = pokemonService.getPokemonTranslatedDescriptionByName("test");

        assertNull(result);
    }


    @Test
    public void givenMissingNameJson_whenConvertJSONToPokemon_thenReturnNull() {
        String jsonResponse = """
            {
              "is_legendary": false,
              "flavor_text_entries": [
                {
                  "language": {"name": "en"},
                  "flavor_text": "Pikachu is an Electric-type Pokémon."
                }
              ],
              "habitat": {"name": "forest"}
            }
        """;

        ResponseEntity<String>  responseEntity = new ResponseEntity<>(jsonResponse,HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(),Mockito.any(Class.class))).thenReturn(responseEntity);

        Pokemon result = pokemonService.getPokemonTranslatedDescriptionByName("test");

        assertNull(result);
    }

    @Test
    public void givenMissingIsLegendaryJson_whenConvertJSONToPokemon_thenReturnNull() {
        String jsonResponse = """
            {
              "name": "pikachu",
              "flavor_text_entries": [
                {
                  "language": {"name": "en"},
                  "flavor_text": "Pikachu is an Electric-type Pokémon."
                }
              ],
              "habitat": {"name": "forest"}
            }
        """;

        ResponseEntity<String>  responseEntity = new ResponseEntity<>(jsonResponse,HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(),Mockito.any(Class.class))).thenReturn(responseEntity);

        Pokemon result = pokemonService.getPokemonTranslatedDescriptionByName("test");

        assertNull(result);
    }

    @Test
    public void givenMissingFlavorTextEntriesJson_whenConvertJSONToPokemon_thenReturnNull() {
        String jsonResponse = """
            {
              "name": "pikachu",
              "is_legendary": false,
              "habitat": {"name": "forest"}
            }
        """;

        ResponseEntity<String>  responseEntity = new ResponseEntity<>(jsonResponse,HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(),Mockito.any(Class.class))).thenReturn(responseEntity);

        Pokemon result = pokemonService.getPokemonTranslatedDescriptionByName("test");

        assertNull(result);
    }

    @Test
    public void givenMissingHabitatJson_whenConvertJSONToPokemon_thenReturnNull() {
        String jsonResponse = """
            {
              "name": "pikachu",
              "is_legendary": false,
              "flavor_text_entries": [
                {
                  "language": {"name": "en"},
                  "flavor_text": "Pikachu is an Electric-type Pokémon."
                }
              ]
            }
        """;

        ResponseEntity<String>  responseEntity = new ResponseEntity<>(jsonResponse,HttpStatus.OK);
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(),Mockito.any(Class.class))).thenReturn(responseEntity);

        Pokemon result = pokemonService.getPokemonTranslatedDescriptionByName("test");

        assertNull(result);
    }
}
