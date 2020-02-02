package coffee.machine;

import coffee.machine.modules.CoffeeModule;
import coffee.machine.modules.MilkModule;
import coffee.machine.modules.WastesModule;
import coffee.machine.modules.WaterModule;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static coffee.machine.CoffeeKind.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class CoffeeMachine {

    @NonNull
    private WaterModule waterModule;

    @NonNull
    private CoffeeModule coffeeModule;

    @NonNull
    private WastesModule wastesModule;

    @NonNull
    private MilkModule milkModule;

    private Map<CoffeeKind, Consumer<CoffeeKind>> programs = new EnumMap<>(CoffeeKind.class);

    public static CoffeeMachine of(WaterModule waterModule, CoffeeModule coffeeModule,
                                   WastesModule wastesModule, MilkModule milkModule
    ) {
        CoffeeMachine coffeeMachine = new CoffeeMachine(
                Objects.requireNonNull(waterModule),
                Objects.requireNonNull(coffeeModule),
                Objects.requireNonNull(wastesModule),
                Objects.requireNonNull(milkModule)
        );
        coffeeMachine.programs.put(ESPRESSO, coffeeMachine::makeBlackCoffee);
        coffeeMachine.programs.put(AMERICANO, coffeeMachine::makeBlackCoffee);
        coffeeMachine.programs.put(LATTE, coffeeMachine::makeLatte);
        coffeeMachine.programs.put(CAPPUCCINO, coffeeMachine::makeCappuccino);
        return coffeeMachine;
    }

    public void makeCoffee(CoffeeKind coffeeKind) {
        checkContainers(coffeeKind);
        log.info("Making coffee {}", coffeeKind.toString());
        programs.get(coffeeKind).accept(coffeeKind);
    }

    private void makeBlackCoffee(CoffeeKind coffeeKind) {
        coffeeModule.ground(coffeeKind.getCoffeeNeeded());
        waterModule.prepareWater(coffeeKind.getWaterNeeded());
        coffeeModule.flipUsedCoffee();
    }

    private void makeLatte(CoffeeKind coffeeKind) {
        coffeeModule.ground(coffeeKind.getCoffeeNeeded());
        waterModule.prepareWater(coffeeKind.getWaterNeeded());
        milkModule.prepareMilk(coffeeKind.getMilkNeeded());
        coffeeModule.flipUsedCoffee();
    }

    private void makeCappuccino(CoffeeKind coffeeKind) {
        coffeeModule.ground(coffeeKind.getCoffeeNeeded());
        waterModule.prepareWater(coffeeKind.getWaterNeeded());
        milkModule.prepareFoamedMilk(coffeeKind.getMilkNeeded());
        coffeeModule.flipUsedCoffee();
    }

    private void checkContainers(CoffeeKind coffeeKind) {
        wastesModule.checkOverflow();
        waterModule.checkWaterTank(coffeeKind.getWaterNeeded());
        coffeeModule.checkCapacity(coffeeKind.getCoffeeNeeded());
        milkModule.checkMilkContainer(coffeeKind.getMilkNeeded());
    }

}
