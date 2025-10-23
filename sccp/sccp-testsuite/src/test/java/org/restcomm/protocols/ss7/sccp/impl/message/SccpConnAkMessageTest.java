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

package org.restcomm.protocols.ss7.sccp.impl.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReceiveSequenceNumberImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequenceNumberImpl;

import com.mobius.software.common.dal.timers.WorkerPool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnAkMessageTest {

    private Logger logger;
    private WorkerPool workerPool;
    private SccpStackImpl stack;
    private MessageFactoryImpl messageFactory;

    @Before
    public void setUp() {
	workerPool = new WorkerPool("SCCP");
	workerPool.start(4);
	stack = new SccpStackImpl("SccpConnAkMessageTestStack", workerPool);

        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = LogManager.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @After
    public void tearDown() {
	this.workerPool.stop();
    }

    public ByteBuf getDataAk() {
        return Unpooled.wrappedBuffer(new byte[] { 0x08, 0x00, 0x00, 0x01, 0x00, 0x64 });
    }

    @Test
    public void testDecode() throws Exception {
        ByteBuf buf = this.getDataAk();
        int type = buf.readByte();
        SccpConnAkMessageImpl testObjectDecoded = (SccpConnAkMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 1);
        assertEquals(testObjectDecoded.getReceiveSequenceNumber().getNumber().getValue(), 0);
        assertEquals(testObjectDecoded.getCredit().getValue(), 100);
    }

    @Test
    public void testEncode() throws Exception {
        SccpConnAkMessageImpl original = new SccpConnAkMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(1));
        original.setReceiveSequenceNumber(new ReceiveSequenceNumberImpl(new SequenceNumberImpl((byte)0)));
        original.setCredit(new CreditImpl(100));


        EncodingResultData encoded = original.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        MessageSegmentationTest.assertByteBufs(encoded.getSolidData(), this.getDataAk());
    }
}
