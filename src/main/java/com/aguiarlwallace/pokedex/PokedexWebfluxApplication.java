package com.aguiarlwallace.pokedex;

import com.aguiarlwallace.pokedex.model.Pokemon;
import com.aguiarlwallace.pokedex.repository.PokemonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class PokedexWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokedexWebfluxApplication.class, args);
        System.out.println("Gotta catch'em all!");
    }

    @Bean
    CommandLineRunner init(ReactiveMongoOperations operations, PokemonRepository repository){
        return  args -> {
            Flux<Pokemon> pokemonFlux = Flux.just(
                new Pokemon(null, "Bulbasaur", new String[]{"Grass", "Poison"}, new String[]{"Overgrow", "Chlorophyll"},6.9,0.7 ),
                new Pokemon(null, "Squirtle", new String[]{"Water"}, new String[]{"Torrent", "Rain Dish"},9,0.5),
                new Pokemon(null, "Charmander", new String[]{"Fire"}, new String[]{"Blaze", "Solar Power"},8.5,0.6 ))
                    .flatMap(repository::save);

            pokemonFlux
                    .thenMany(repository.findAll())
                    .subscribe(System.out::println);
        };
    }

}
