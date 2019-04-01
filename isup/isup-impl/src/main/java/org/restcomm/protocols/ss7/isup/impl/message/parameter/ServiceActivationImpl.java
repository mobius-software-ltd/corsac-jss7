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

/**
 * Start time:17:25:24 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.ServiceActivation;

/**
 * Start time:17:25:24 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
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
