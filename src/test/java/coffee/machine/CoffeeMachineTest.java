package coffee.machine;

import coffee.machine.coffee.*;
import coffee.machine.ingredients.CoffeeGrain;
import coffee.machine.ingredients.Milk;
import coffee.machine.ingredients.Water;
import coffee.machine.modules.CoffeeModule;
import coffee.machine.modules.MilkModule;
import coffee.machine.modules.WastesModule;
import coffee.machine.modules.WaterModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoffeeMachineTest {

    @Mock
    private WaterModule waterModule;

    @Mock
    private CoffeeModule coffeeModule;

    @Mock
    private WastesModule wastesModule;

    @Mock
    private MilkModule milkModule;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private CoffeeMachine coffeeMachine;

    private Water testWater = Water.of(200, 23, false);
    private Milk testMilk = Milk.of(100, 23, false);
    private CoffeeGrain testCoffeeGrain = CoffeeGrain.of(20, false);
    private CoffeeEssence testCoffeeEssence = ImmutableCoffeeEssence.of(200, 20);

    @BeforeEach
    void setUp() {
        coffeeMachine = CoffeeMachine.of(waterModule, coffeeModule, wastesModule, milkModule, executorService);

        given(waterModule.prepareSteam(anyInt())).willReturn(testWater);
        given(coffeeModule.ground(anyInt())).willReturn(testCoffeeGrain);
        given(coffeeModule.pushSteamThroughGroundedCoffee(any(), any())).willReturn(testCoffeeEssence);
    }

    @Test
    void shouldUseProperModulesToMakeBlackCoffee() {
        int waterNeeded = CoffeeKind.AMERICANO.getWaterNeeded();
        int coffeeNeeded = CoffeeKind.AMERICANO.getCoffeeNeeded();
        int milkNeeded = CoffeeKind.AMERICANO.getMilkNeeded();

        Coffee expected = ImmutableCoffee.builder()
                .water(testCoffeeEssence.getAmount())
                .coffeeExtract(testCoffeeEssence.getCoffeeExtract())
                .milk(0)
                .withFoam(false)
                .build();

        Coffee coffee = coffeeMachine.makeCoffee(CoffeeKind.AMERICANO);

        assertThat(coffee).isEqualTo(expected);

        checkModules(waterNeeded, coffeeNeeded, milkNeeded);
        verify(coffeeModule, times(1)).ground(coffeeNeeded);
        verify(coffeeModule, times(1))
                .pushSteamThroughGroundedCoffee(testWater, testCoffeeGrain);
        verify(waterModule, times(1)).prepareSteam(waterNeeded);
        verifyNoMoreInteractions(waterModule, coffeeModule, wastesModule, milkModule);
    }


    @Test
    void shouldUseProperModulesToMakeWhiteCoffee() {
        int waterNeeded = CoffeeKind.LATTE.getWaterNeeded();
        int coffeeNeeded = CoffeeKind.LATTE.getCoffeeNeeded();
        int milkNeeded = CoffeeKind.LATTE.getMilkNeeded();
        boolean withFoam = CoffeeKind.LATTE.isWithFoam();

        given(milkModule.prepareMilk(anyInt(), anyBoolean())).willReturn(testMilk);

        Coffee expected = ImmutableCoffee.builder()
                .water(testCoffeeEssence.getAmount())
                .coffeeExtract(testCoffeeEssence.getCoffeeExtract())
                .milk(testMilk.getAmount())
                .withFoam(testMilk.isFoamed())
                .build();

        Coffee coffee = coffeeMachine.makeCoffee(CoffeeKind.LATTE);

        assertThat(coffee).isEqualTo(expected);

        checkModules(waterNeeded, coffeeNeeded, milkNeeded);
        verify(coffeeModule, times(1)).ground(coffeeNeeded);
        verify(coffeeModule, times(1))
                .pushSteamThroughGroundedCoffee(testWater, testCoffeeGrain);
        verify(waterModule, times(1)).prepareSteam(waterNeeded);
        verify(milkModule, times(1)).prepareMilk(milkNeeded, withFoam);
        verifyNoMoreInteractions(waterModule, coffeeModule, wastesModule, milkModule);
    }

    private void checkModules(int waterNeeded, int coffeeNeeded, int milkNeeded) {
        verify(milkModule, times(1)).checkMilkTank(milkNeeded);
        verify(waterModule, times(1)).checkWaterTank(waterNeeded);
        verify(coffeeModule, times(1)).checkCapacity(coffeeNeeded);
        verify(wastesModule, times(1)).checkOverflow();
    }

}