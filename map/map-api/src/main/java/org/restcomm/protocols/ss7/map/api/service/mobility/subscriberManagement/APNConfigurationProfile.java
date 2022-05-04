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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 APN-ConfigurationProfile ::= SEQUENCE { defaultContext ContextId, completeDataListIncluded NULL OPTIONAL, -- If segmentation
 * is used, completeDataListIncluded may only be present in the -- first segment of APN-ConfigurationProfile. epsDataList [1]
 * EPS-DataList, extensionContainer [2] ExtensionContainer OPTIONAL, ... }
 *
 * ContextId ::= INTEGER (1..50)
 *
 * EPS-DataList ::= SEQUENCE SIZE (1..50) OF APN-Configuration
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface APNConfigurationProfile {

    int getDefaultContext();

    boolean getCompleteDataListIncluded();

    List<APNConfiguration> getEPSDataList();

    MAPExtensionContainer getExtensionContainer();

}