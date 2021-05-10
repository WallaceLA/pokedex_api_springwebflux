package com.aguiarlwallace.pokedex.controller;

import com.aguiarlwallace.pokedex.model.Pokemon;
import com.aguiarlwallace.pokedex.model.PokemonEvent;
import com.aguiarlwallace.pokedex.repository.PokemonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private final PokemonRepository pokemonRepository;
    public PokemonController (PokemonRepository pokemonRepository) {this.pokemonRepository = pokemonRepository;}

    @GetMapping
    public Flux<Pokemon> getAllPokemon() { return pokemonRepository.findAll(); }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Pokemon>> getPokemon(@PathVariable String id) {
        return pokemonRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound()
                .build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pokemon> savePokemon(@RequestBody Pokemon pokemon){
        return pokemonRepository.save(pokemon);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Pokemon>> updatePokemon(@PathVariable String id, @RequestBody Pokemon pokemon){
        return pokemonRepository.findById(id)
                .flatMap(existingPokemon -> {
                    existingPokemon.setName(pokemon.getName());
                    existingPokemon.setType(pokemon.getType());
                    existingPokemon.setAbilities(pokemon.getAbilities());
                    existingPokemon.setHeight(pokemon.getHeight());
                    existingPokemon.setWeight(pokemon.getWeight());
                    return pokemonRepository.save(existingPokemon);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePokemon(@PathVariable String id){
        return pokemonRepository.findById(id)
                .flatMap(existingPokemon ->
                            pokemonRepository.delete(existingPokemon)
                                    .then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteAllPokemons() {
        return pokemonRepository.deleteAll();
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PokemonEvent> getPokemonEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(val -> new PokemonEvent(val, "Pokemonsssss"));
    }

}
