/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.tcapAnsi.api.asn;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 * 
ApplicationContext  CHOICE {
        integerApplicationId    IntegerApplicationContext,
        objectApplicationId ObjectIDApplicationContext
 } OPTIONAL,
IntegerApplicationContext ::= [PRIVATE 27] IMPLICIT INTEGER
ObjectIDApplicationContext ::= [PRIVATE 28] IMPLICIT OBJECT IDENTIFIER
 *
 */
@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x00,constructed=true,lengthIndefinite=false)
public interface ApplicationContext {

	ApplicationContextType getType();

    List<Long> getObj();

    void setObj(List<Long> value);

    Integer getInt();

    void setInt(int val);

}