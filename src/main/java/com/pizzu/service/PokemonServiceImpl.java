package com.pizzu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzu.exception.PokemonMappingException;
import com.pizzu.model.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class PokemonServiceImpl implements PokemonService{

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.pokemon.url}")
    private String pokemonUrl;

    private static final String IS_LEGENDARY = "is_legendary";
    private static final String FLAVOR_TEXT_ENTRIES = "flavor_text_entries";
    private static final String LANGUAGE = "language";
    private static final String NAME = "name";
    private static final String EN = "en";
    private static final String FLAVOR_TEXT = "flavor_text";
    private static final String HABITAT = "habitat";

    private static final Logger log = LoggerFactory.getLogger(PokemonServiceImpl.class);

    @Override
    public Pokemon getPokemonTranslatedDescriptionByName(String name) {

        try {
            ResponseEntity<String> responsePokemon = restTemplate.getForEntity(pokemonUrl + name, String.class);
            Pokemon pokemon = convertJSONToPokemon(responsePokemon.getBody());
            return pokemon;
        } catch (Exception ex) {
            log.error("Pokèmon non trovato!, Exception:" + ex.getMessage());
            return null;
        }

    }

    private Pokemon convertJSONToPokemon(String responsePokemonBody) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(responsePokemonBody);
            Pokemon pokemonConverted = new Pokemon();


            Optional.ofNullable(rootNode.get(NAME))
                    .map(JsonNode::asText)
                    .filter(name -> !name.isEmpty())
                    .orElseThrow(() -> new PokemonMappingException("Impossibile trovare nome del pokemon"));
            pokemonConverted.setName(rootNode.get(NAME).asText());


            Optional.ofNullable(rootNode.get(IS_LEGENDARY))
                    .filter(JsonNode::isBoolean)
                    .map(JsonNode::asBoolean)
                    .orElseThrow(() -> new PokemonMappingException("Impossibile comprendere se il pokemon è leggendario o no"));
            pokemonConverted.setIsLegendary(rootNode.get(IS_LEGENDARY).asBoolean());

            Optional.ofNullable(rootNode.get(FLAVOR_TEXT_ENTRIES))
                    .filter(JsonNode::isArray)
                    .orElseThrow(() -> new PokemonMappingException("Impossibile trovare descrizione del pokemon"))
                    .forEach(entry -> {
                        if (EN.equals(entry.path(LANGUAGE).path(NAME).asText())) {
                            pokemonConverted.setDescription(entry.path(FLAVOR_TEXT).asText());
                            return;
                        }
                    });

            Optional.ofNullable(rootNode.get(HABITAT))
                    .map(habitatNode -> habitatNode.path(NAME).asText())
                    .filter(habitat -> !habitat.isEmpty())
                    .orElseThrow(() -> new PokemonMappingException("Impossibile trovare habitat del pokemon"));
            pokemonConverted.setHabitat(rootNode.get(HABITAT).path(NAME).asText());

            return pokemonConverted;

        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
