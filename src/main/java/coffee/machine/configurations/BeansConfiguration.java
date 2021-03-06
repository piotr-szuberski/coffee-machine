package coffee.machine.configurations;

import coffee.machine.components.*;
import coffee.machine.ingredients.CoffeeGrain;
import coffee.machine.ingredients.Milk;
import coffee.machine.ingredients.Water;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

    @Bean
    Tank<Water> waterTank() {
        return new Tank<>(Water.create(), 1500);
    }

    @Bean
    Tank<Milk> milkTank() {
        return new Tank<>(Milk.create(), 1500);
    }

    @Bean
    Tank<CoffeeGrain> coffeeTank() {
        return new Tank<>(CoffeeGrain.create(), 1500);
    }

    @Bean
    Tank<CoffeeGrain> wastesTank() {
        return new Tank<>(CoffeeGrain.create(), 1500);
    }

    @Bean
    Heater milkHeater() {
        return Heater.of(Milk.PERFECT_TEMPERATURE);
    }

}
