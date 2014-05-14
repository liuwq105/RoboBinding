package org.robobinding.viewattribute;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Cheng Wei
 */
public class ViewUpdatePropagationLatchTest {
    private ViewUpdatePropagationLatch latch = new ViewUpdatePropagationLatch();

    @Test
    public void givenLatchIsOn_whenTryToPass_thenFailed() {
	latch.turnOn();

	assertFalse(latch.tryToPass());
    }

    @Test
    public void givenLatchIsOn_whenTryToPassAfterFirstAttempt_thenAllAreSuccessful() {
	latch.turnOn();

	latch.tryToPass();
	assertAllTryToPassAttemptsAreSuccessful(anyNumAttempts());
    }

    private void assertAllTryToPassAttemptsAreSuccessful(int times) {
	assertTrue(latch.tryToPass());
    }

    private int anyNumAttempts() {
	return RandomValues.nextInt(10);
    }

    @Test
    public void givenLatchIsOff_whenTryToPass_thenAllAreSuccessful() {
	latch.turnOff();

	assertAllTryToPassAttemptsAreSuccessful(anyNumAttempts());
    }
}