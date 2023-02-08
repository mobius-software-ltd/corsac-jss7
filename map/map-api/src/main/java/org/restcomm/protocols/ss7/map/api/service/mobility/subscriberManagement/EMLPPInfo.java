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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 EMLPP-Info ::= SEQUENCE { maximumentitledPriority EMLPP-Priority, defaultPriority EMLPP-Priority, extensionContainer
 * ExtensionContainer OPTIONAL, ...}
 *
 * EMLPP-Priority ::= INTEGER (0..15) -- The mapping from the values A,B,0,1,2,3,4 to the integer-value is -- specified as
 * follows where A is the highest and 4 is the lowest -- priority level -- the integer values 7-15 are spare and shall be mapped
 * to value 4
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface EMLPPInfo {

    int getMaximumentitledPriority();

    int getDefaultPriority();

    MAPExtensionContainer getExtensionContainer();
}