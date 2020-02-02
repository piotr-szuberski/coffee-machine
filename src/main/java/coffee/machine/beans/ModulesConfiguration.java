package coffee.machine.beans;

import coffee.machine.components.containers.Container;
import coffee.machine.components.containers.Tank;
import coffee.machine.components.foamers.Foamer;
import coffee.machine.components.grounders.Grounder;
import coffee.machine.components.heaters.Heater;
import coffee.machine.components.pots.Pot;
import coffee.machine.components.pumps.Pump;
import coffee.machine.modules.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModulesConfiguration {

    @Bean
    CoffeeModule coffeeModule(Tank coffeeTank, Pot coffeePot, Grounder coffeeGrounder) {
        return CoffeeModuleImpl.of(coffeeTank, coffeePot, coffeeGrounder);
    }

    @Bean
    WastesModule wastesModule(Tank wastesTank) {
        return WastesModuleImpl.of(wastesTank);
    }

    @Bean
    HeatingModule waterHeatingModule(Container waterHeaterContainer, Heater waterHeater) {
        return HeatingModuleImpl.of(waterHeaterContainer, waterHeater);
    }

    @Bean
    WaterModule waterModule(Tank waterTank, Pump waterPump, HeatingModule waterHeatingModule) {
        return WaterModuleImpl.of(waterTank, waterPump, waterHeatingModule);
    }

    @Bean
    HeatingModule milkHeatingModule(Container milkHeaterContainer, Heater milkHeater) {
        return HeatingModuleImpl.of(milkHeaterContainer, milkHeater);
    }

    @Bean
    MilkModule milkModule(HeatingModule milkHeatingModule, Pump milkToHeaterPump,
                          Pump milkToCupPump, Foamer milkFoamer) {
        return MilkModuleImpl.of(milkHeatingModule, milkToHeaterPump, milkToCupPump, milkFoamer);
    }

}