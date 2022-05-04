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
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.ServiceActivation;

/**
 * Start time:17:25:24 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class ServiceActivationImpl extends AbstractISUPParameter implements ServiceActivation {
	// FIXME: this is again simple container
    /**
     * See Q.763 3.49
     */
    public static final byte _FEATURE_CODE_CALL_TRANSFER = 1;

    private ByteBuf featureCodes;

    public ServiceActivationImpl() {
        super();

    }

    public ServiceActivationImpl(ByteBuf featureCodes) {
        super();
        this.featureCodes = featureCodes;
    }

    public void decode(ByteBuf b) throws ParameterException {
        this.featureCodes = b;        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	buffer.writeBytes(getFeatureCodes());
    }

    public ByteBuf getFeatureCodes() {
    	if(featureCodes==null)
        	return null;
        
        return Unpooled.wrappedBuffer(featureCodes);
    }

    public void setFeatureCodes(ByteBuf featureCodes) {
        this.featureCodes = featureCodes;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
