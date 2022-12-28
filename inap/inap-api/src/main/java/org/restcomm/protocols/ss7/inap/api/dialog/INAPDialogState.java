/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.restcomm.protocols.ss7.inap.api.dialog;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yulian.oifa
 *
 */
public enum INAPDialogState {

    IDLE(0),

    INITIAL_RECEIVED(1), INITIAL_SENT(2),

    ACTIVE(3),
    // additional state to mark removal
    EXPUNGED(4);

    private final Integer state;
    private static final Map<Integer, INAPDialogState> intToTypeMap = new HashMap<Integer, INAPDialogState>();
	static
	{
	    for (INAPDialogState state : INAPDialogState.values()) 
	    {
	        intToTypeMap.put(state.state, state);
	    }
	}
	
    private INAPDialogState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return this.state;
    }

    public static INAPDialogState getInstance(Integer state) {
    	return intToTypeMap.get(Integer.valueOf(state));
    }
}