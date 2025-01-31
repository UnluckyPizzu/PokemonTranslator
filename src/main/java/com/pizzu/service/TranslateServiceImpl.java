package com.pizzu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzu.exception.PokemonMappingException;
import com.pizzu.exception.PokemonTranslateException;
import com.pizzu.model.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TranslateServiceImpl implements TranslateService{

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.translate.url.yoda}")
    private String yodaUrl;

    @Value("${api.translate.url.shakespeare}")
    private String shakespeareUrl;

    private static final String CAVE = "cave";
    private static final String CONTENTS = "contents";
    private static final String TRANSLATED = "translated";

    private static Logger logger = LoggerFactory.getLogger(TranslateServiceImpl.class);

    @Override
    public Pokemon getTranslatePokemon(Pokemon pokemon) {

        if (pokemon != null && pokemon.getDescription() != null) {

            String url = (pokemon.getIsLegendary() || pokemon.getHabitat()  == CAVE) ? yodaUrl : shakespeareUrl;

            MultiValueMap<String, Object> parameters  = new LinkedMultiValueMap<String, Object>();
            parameters.add("text", pokemon.getDescription());

            try {

            ResponseEntity<String> responseTranslate = restTemplate.postForEntity(url,parameters, String.class);

            ObjectMapper objectMapper = new ObjectMapper();


                JsonNode rootNode = objectMapper.readTree(responseTranslate.getBody());

                Optional.ofNullable(rootNode.get(CONTENTS).get(TRANSLATED))
                        .map(JsonNode::asText)
                        .filter(name -> !name.isEmpty())
                        .orElseThrow(() -> new PokemonTranslateException("Errore durante traduzione della descrizione"));

                pokemon.setDescription(rootNode.get(CONTENTS).get(TRANSLATED).asText());

                return pokemon;

            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }
        else
            return null;
    }
}
