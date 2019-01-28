package org.restcomm.protocols.ss7.sccp;

public class MaxConnectionCountReached extends Exception {
	private static final long serialVersionUID = 1L;

	public MaxConnectionCountReached() {
    }

    public MaxConnectionCountReached(String message) {
        super(message);
    }
}
