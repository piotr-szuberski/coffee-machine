package coffee.machine.modules;

import coffee.machine.components.Tank;
import coffee.machine.components.Heater;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor(staticName = "of")
public class HeatingModule {
    private final Tank heaterTank;
    private final Heater heater;

    public void heat(int amount) {
        heater.heat(amount);
    }

    public void checkCapacity(int amount) {
        checkContainerCapacity(amount);
    }

    private void checkContainerCapacity(int amountNeeded) {
        if (heaterTank.getCapacity() < amountNeeded) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                    "The heating module container is too small. Consider disabling " +
                            "this coffee program or replace with a bigger tank");
        }
    }
}
