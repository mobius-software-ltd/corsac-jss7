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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
public class ForwardingOptionsTest {
    Logger logger = LogManager.getLogger(ForwardingOptionsTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ForwardingOptionsImpl.class);
    	        
        byte[] data1 = new byte[] { 0x4, 0x1, (byte) 0xE4 };
        byte[] data2 = new byte[] { 0x4, 0x1, (byte) 0x00 };
        byte[] data3 = new byte[] { 0x4, 0x1, (byte) 0xA8 };
        byte[] data4 = new byte[] { 0x4, 0x1, (byte) 0xC8 };
        byte[] data5 = new byte[] { 0x4, 0x1, (byte) 0x24 };

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data1));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ForwardingOptionsImpl);
        ForwardingOptionsImpl fo = (ForwardingOptionsImpl)result.getResult();
        
        // logger.info(":::::" + fo.getEncodedDataString());
        assertTrue(fo.isNotificationToForwardingParty());
        assertTrue(fo.isRedirectingPresentation());
        assertTrue(fo.isNotificationToCallingParty());
        assertTrue(fo.getForwardingReason() == ForwardingReason.busy);

        result=parser.decode(Unpooled.wrappedBuffer(data2));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ForwardingOptionsImpl);
        fo = (ForwardingOptionsImpl)result.getResult();

        // logger.info(":::::" + fo.getEncodedDataString());
        assertTrue(!fo.isNotificationToForwardingParty());
        assertTrue(!fo.isRedirectingPresentation());
        assertTrue(!fo.isNotificationToCallingParty());
        assertTrue(fo.getForwardingReason() == ForwardingReason.notReachable);

        result=parser.decode(Unpooled.wrappedBuffer(data3));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ForwardingOptionsImpl);
        fo = (ForwardingOptionsImpl)result.getResult();

        // logger.info(":::::" + fo.getEncodedDataString());
        assertTrue(fo.isNotificationToForwardingParty());
        assertTrue(!fo.isRedirectingPresentation());
        assertTrue(fo.isNotificationToCallingParty());
        assertTrue(fo.getForwardingReason() == ForwardingReason.noReply);

        result=parser.decode(Unpooled.wrappedBuffer(data4));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ForwardingOptionsImpl);
        fo = (ForwardingOptionsImpl)result.getResult();

        // logger.info(":::::" + fo.getEncodedDataString());
        assertTrue(fo.isNotificationToForwardingParty());
        assertTrue(fo.isRedirectingPresentation());
        assertTrue(!fo.isNotificationToCallingParty());
        assertTrue(fo.getForwardingReason() == ForwardingReason.noReply);

        result=parser.decode(Unpooled.wrappedBuffer(data5));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ForwardingOptionsImpl);
        fo = (ForwardingOptionsImpl)result.getResult();

        // logger.info(":::::" + fo.getEncodedDataString());
        assertTrue(!fo.isNotificationToForwardingParty());
        assertTrue(!fo.isRedirectingPresentation());
        assertTrue(fo.isNotificationToCallingParty());
        assertTrue(fo.getForwardingReason() == ForwardingReason.busy);
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ForwardingOptionsImpl.class);
    	        
        byte[] data1 = new byte[] { 0x4, 0x1, (byte) 0xE4 };
        byte[] data2 = new byte[] { 0x4, 0x1, (byte) 0x00 };
        byte[] data3 = new byte[] { 0x4, 0x1, (byte) 0xA8 };
        byte[] data4 = new byte[] { 0x4, 0x1, (byte) 0xC8 };
        byte[] data5 = new byte[] { 0x4, 0x1, (byte) 0x24 };

        ForwardingOptionsImpl fo = new ForwardingOptionsImpl(true, true, true, ForwardingReason.busy);
        ByteBuf buffer=parser.encode(fo);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data1, encodedData));

        fo = new ForwardingOptionsImpl(false, false, false, ForwardingReason.notReachable);
        buffer=parser.encode(fo);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data2, encodedData));

        fo = new ForwardingOptionsImpl(true, false, true, ForwardingReason.noReply);
        buffer=parser.encode(fo);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data3, encodedData));

        fo = new ForwardingOptionsImpl(true, true, false, ForwardingReason.noReply);
        buffer=parser.encode(fo);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data4, encodedData));

        fo = new ForwardingOptionsImpl(false, false, true, ForwardingReason.busy);
        buffer=parser.encode(fo);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data5, encodedData));
    }
}
