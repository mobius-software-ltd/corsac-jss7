/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

/**
*
<code>
FeatureRequestIndicator ::= ENUMERATED {
	hold(0),
	retrieve(1),
	featureActivation(2),
	spare1(3),
	sparen(127)
}
-- Indicates the feature activated (e.g. a switch-hook flash, feature activation). Spare values reserved
-- for future use.
</code>
*
* @author yulian.oifa
*
*/
public enum FeatureRequestIndicator {

	hold(0), retrieve(1), spare1(2), sparen(127);

    private int code;

    private FeatureRequestIndicator(int code) {
        this.code = code;
    }

    public static FeatureRequestIndicator getInstance(int code) {
        switch (code) {
        case 0:
            return FeatureRequestIndicator.hold;
        case 1:
            return FeatureRequestIndicator.retrieve;
        case 2:
            return FeatureRequestIndicator.spare1;
        case 127:
            return FeatureRequestIndicator.sparen;
        }

        return null;
    }

    public int getCode() {
        return code;
    }

}
