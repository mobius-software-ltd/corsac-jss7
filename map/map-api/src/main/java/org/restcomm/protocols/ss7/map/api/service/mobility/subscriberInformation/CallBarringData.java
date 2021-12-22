/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
CallBarringData ::= SEQUENCE {
  callBarringFeatureList        Ext-CallBarFeatureList,
  password                      Password OPTIONAL,
  wrongPasswordAttemptsCounter  WrongPasswordAttemptsCounter OPTIONAL,
  notificationToCSE             NULL OPTIONAL,
  extensionContainer            ExtensionContainer OPTIONAL,
  ...
}
Ext-CallBarFeatureList ::= SEQUENCE SIZE (1..32) OF Ext-CallBarringFeature
WrongPasswordAttemptsCounter ::= INTEGER (0..4)
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface CallBarringData {

    List<ExtCallBarringFeature> getCallBarringFeatureList();

    Password getPassword();

    Integer getWrongPasswordAttemptsCounter();

    boolean getNotificationToCSE();

    MAPExtensionContainer getExtensionContainer();

}