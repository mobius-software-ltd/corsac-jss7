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

package org.restcomm.protocols.ss7.inap.functional.listeners;

/**
 * Steps of INAP functional test
 *
 * @author yulian.oifa
 *
 */
public enum FunctionalTestScenario {
    /**
     * TC-BEGIN + InitialDp
     */
    Action_InitilDp(0);

    private int code;

    private FunctionalTestScenario(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
