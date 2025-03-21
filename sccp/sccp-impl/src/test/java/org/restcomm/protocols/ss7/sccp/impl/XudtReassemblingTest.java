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

package org.restcomm.protocols.ss7.sccp.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.restcomm.protocols.ss7.mtp.RoutingLabelFormat;
import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SignallingPointStatus;
import org.restcomm.protocols.ss7.sccp.impl.router.RouterImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
/**
 * 
 * @author yulianoifa
 *
 */
public class XudtReassemblingTest {

    private ByteBuf data1 = Unpooled.wrappedBuffer(new byte[] { 0x11, (byte) 0x81, 0x0f, 0x04, 0x0d, 0x16, (byte) 0xff, 0x09, 0x0a, 0x06, 0x01, 0x09,
            0x00, 0x08, 0x00, 0x01, 0x20, 0x09, 0x0a, 0x07, 0x01, 0x09, 0x00, 0x08, 0x00, 0x02, 0x10, (byte) 0xe9, 0x62,
            (byte) 0x81, (byte) 0xff, 0x48, 0x04, 0x04, 0x00, 0x00, 0x29, 0x6b, 0x1a, 0x28, 0x18, 0x06, 0x07, 0x00, 0x11,
            (byte) 0x86, 0x05, 0x01, 0x01, 0x01, (byte) 0xa0, 0x0d, 0x60, 0x0b, (byte) 0xa1, 0x09, 0x06, 0x07, 0x04, 0x00,
            0x00, 0x01, 0x00, 0x01, 0x03, 0x6c, (byte) 0x81, (byte) 0xda, (byte) 0xa1, (byte) 0x81, (byte) 0xd7, 0x02, 0x01,
            0x01, 0x02, 0x01, 0x02, 0x30, (byte) 0x81, (byte) 0xce, 0x04, 0x08, 0x04, 0x04, 0x11, 0x01, 0x00, 0x09, 0x00,
            (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x09, 0x00, 0x08, 0x00, 0x02, 0x10, 0x04, 0x07, (byte) 0x91, 0x09,
            0x00, 0x08, 0x00, 0x02, 0x10, 0x30, 0x3a, (byte) 0xa0, 0x38, 0x30, 0x36, 0x06, 0x02, 0x29, 0x01, (byte) 0x82, 0x30,
            (byte) 0x91, 0x19, (byte) 0x81, 0x01, 0x0e, (byte) 0x82, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xa6, 0x62, (byte) 0x80,
            0x02, 0x05, (byte) 0xe0, 0x30, 0x3a, (byte) 0xa0, 0x38, 0x30, 0x36, 0x06, 0x02, 0x29, 0x01, (byte) 0x82, 0x30,
            (byte) 0x91, 0x19, (byte) 0x81, 0x01, 0x0e, (byte) 0x82, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x82, 0x00, (byte) 0x81,
            0x01, 0x00, (byte) 0xa3, 0x07, (byte) 0x81, 0x05, 0x01, 0x01, 0x01, 0x01, 0x01, (byte) 0x84, 0x00, (byte) 0x85,
            0x03, 0x00, 0x01, 0x01, (byte) 0x86, 0x03, 0x00, 0x01, 0x01, (byte) 0x87, 0x10, 0x04, (byte) 0x81, 0x04, 0x00,
            0x04, 0x00 });

    private ByteBuf data2 = Unpooled.wrappedBuffer(new byte[] { 0x11, (byte) 0x81, 0x0f, 0x04, 0x0d, 0x16, 0x2f, 0x09, 0x0a, 0x06, 0x01, 0x09, 0x00,
            0x08, 0x00, 0x01, 0x20, 0x09, 0x0a, 0x07, 0x01, 0x09, 0x00, 0x08, 0x00, 0x02, 0x10, 0x19, 0x02, 0x00, 0x01,
            (byte) 0x88, 0x00, (byte) 0x89, 0x00, (byte) 0x8b, 0x00, (byte) 0x8c, 0x00, (byte) 0xad, 0x0a, (byte) 0x80, 0x08,
            0x53, (byte) 0x83, 0x61, 0x50, 0x01, 0x47, 0x33, (byte) 0xf0, (byte) 0x90, 0x00, 0x10, 0x04, 0x00, 0x04, 0x00,
            0x04, 0x00 });

    @Test
    public void testA() throws Exception {
        SccpStackImpl sccpStack = new SccpStackImpl("TestUudt");
        sccpStack.start();

        SccpListenerProxy listenerProxy = new SccpListenerProxy();
        sccpStack.getSccpProvider().registerSccpListener(8, listenerProxy);
        sccpStack.setMtp3UserPart(1, new Mtp3UserPartImpl(null));

        sccpStack.removeAllResourses();
        RouterImpl router = (RouterImpl) sccpStack.getRouter();
        router.addMtp3ServiceAccessPoint(1, 1, 1002, 2, 0, null);

        Mtp3TransferPrimitiveFactory mtp3TransferPrimitiveFactory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ITU);
        Mtp3TransferPrimitive mtp3Msg = mtp3TransferPrimitiveFactory.createMtp3TransferPrimitive(3, 2, 0, 1001, 1002, 5, data1,new AtomicBoolean(false));
        sccpStack.onMtp3TransferMessage(mtp3Msg);

        mtp3TransferPrimitiveFactory = new Mtp3TransferPrimitiveFactory(RoutingLabelFormat.ITU);
        mtp3Msg = mtp3TransferPrimitiveFactory.createMtp3TransferPrimitive(3, 2, 0, 1001, 1002, 5, data2,new AtomicBoolean(false));
        sccpStack.onMtp3TransferMessage(mtp3Msg);
    }

    private class SccpListenerProxy extends BaseSccpListener implements SccpListener {
		private static final long serialVersionUID = 1L;

		@Override
        public void onMessage(SccpDataMessage message) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onNotice(SccpNoticeMessage message) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onCoordResponse(int ssn, int multiplicityIndicator) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel,
                RemoteSccpStatus remoteSccpStatus) {
            // TODO Auto-generated method stub
        }
    }

}
