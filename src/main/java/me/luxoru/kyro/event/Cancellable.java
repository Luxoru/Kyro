package me.luxoru.kyro.event;

/**
 * Represents an event which can be cancelled
 *
 * @author Luxoru
 */
public interface Cancellable {

    /**
     * Returns whether event has been cancelled or not
     * @return boolean of whether event cancelled
     */

    boolean isCancelled();

}
