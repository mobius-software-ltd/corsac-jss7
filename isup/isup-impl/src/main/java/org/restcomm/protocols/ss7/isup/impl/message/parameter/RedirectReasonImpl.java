/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.message.parameter.RedirectReason;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class RedirectReasonImpl implements RedirectReason {
	private byte redirectReason;
    //we need to know if that one was set.
    private Byte redirectPossibleAtPerformingExchange;
    public RedirectReasonImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public byte getRedirectReason() {
        return this.redirectReason;
    }

    @Override
    public void setRedirectReason(byte b) {
        this.redirectReason = (byte) (b & 0x7F);
    }

    @Override
    public byte getRedirectPossibleAtPerformingExchange() {
        if(this.redirectPossibleAtPerformingExchange == null)
            return 0;
        return this.redirectPossibleAtPerformingExchange;
    }

    @Override
    public void setRedirectPossibleAtPerformingExchange(byte b) {
        this.redirectPossibleAtPerformingExchange = (byte) (b & 0x07);
    }

    /**
     * Remove redirectPossbileAtPerformingExchange.
     */
    void trim(){
        this.redirectPossibleAtPerformingExchange = null;
    }

}
