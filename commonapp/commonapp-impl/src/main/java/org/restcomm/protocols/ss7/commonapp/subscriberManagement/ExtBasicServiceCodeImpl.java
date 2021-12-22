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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtTeleserviceCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtBasicServiceCodeImpl implements ExtBasicServiceCode {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = ExtBearerServiceCodeImpl.class)
    private ExtBearerServiceCode extBearerService;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = ExtTeleserviceCodeImpl.class)
    private ExtTeleserviceCode extTeleservice;

    public ExtBasicServiceCodeImpl() {
    }

    public ExtBasicServiceCodeImpl(ExtBearerServiceCode extBearerService) {
        this.extBearerService = extBearerService;
    }

    public ExtBasicServiceCodeImpl(ExtTeleserviceCode extTeleservice) {
        this.extTeleservice = extTeleservice;
    }

    public ExtBearerServiceCode getExtBearerService() {
        return extBearerService;
    }

    public ExtTeleserviceCode getExtTeleservice() {
        return extTeleservice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtBasicServiceCode [");

        if (this.extBearerService != null) {
            sb.append(this.extBearerService.toString());
        }
        if (this.extTeleservice != null) {
            sb.append(this.extTeleservice.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
