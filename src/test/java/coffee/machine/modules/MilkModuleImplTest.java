package coffee.machine.modules;

import coffee.machine.components.Pump;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class MilkModuleImplTest {

    @Mock
    private HeatingModule milkHeatingModule;

    @Mock
    private Pump milkToHeaterPump;

    @Mock
    private Pump milkToCupPump;

    private MilkModule milkModule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        milkModule = MilkModuleImpl.of(milkHeatingModule, milkToHeaterPump, milkToCupPump);
    }

    @Test
    void shouldCallHeaterToCheckCapacity() {
        milkModule.checkMilkContainer(200);

        verify(milkHeatingModule, times(1)).checkCapacity(200);
        verifyNoMoreInteractions(milkHeatingModule);
        verifyNoInteractions(milkToHeaterPump, milkToCupPump);
    }

    @Test
    void shouldPrepareMilk() {
        milkModule.prepareMilk(200);

        verify(milkToHeaterPump, times(1)).pump(200);
        verify(milkHeatingModule, times(1)).heat(200);
        verify(milkToCupPump, times(1)).pump(200);
        verifyNoMoreInteractions(milkToHeaterPump, milkHeatingModule, milkToCupPump);
    }

}