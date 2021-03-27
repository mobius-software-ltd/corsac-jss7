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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimateImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityType;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VelocityEstimateTest {

    private byte[] getEncodedData_HorizontalVelocity() {
        return new byte[] { 4, 4, 0, 90, 0, 59 };
    }

    private byte[] getEncodedData_HorizontalWithVerticalVelocity() {
        return new byte[] { 4, 5, 17, 44, 39, 16, -56 };
    }

    private byte[] getEncodedData_HorizontalVelocityWithUncertainty() {
        return new byte[] { 4, 5, 33, 45, 39, 17, -57 };
    }

    private byte[] getEncodedData_HorizontalWithVerticalVelocityAndUncertainty() {
        return new byte[] { 4, 7, 49, 46, 39, 18, -54, -58, -59 };
    }

    @Test(groups = { "functional.decode", "lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VelocityEstimateImpl.class);
    	
        byte[] data = getEncodedData_HorizontalVelocity();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VelocityEstimateImpl);
        VelocityEstimateImpl impl = (VelocityEstimateImpl)result.getResult();
        
        assertEquals(impl.getVelocityType(), VelocityType.HorizontalVelocity);
        assertEquals(impl.getHorizontalSpeed(), 59);
        assertEquals(impl.getBearing(), 90);

        data = getEncodedData_HorizontalWithVerticalVelocity();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VelocityEstimateImpl);
        impl = (VelocityEstimateImpl)result.getResult();

        assertEquals(impl.getVelocityType(), VelocityType.HorizontalWithVerticalVelocity);
        assertEquals(impl.getHorizontalSpeed(), 10000);
        assertEquals(impl.getBearing(), 300);
        assertEquals(impl.getVerticalSpeed(), 200);

        data = getEncodedData_HorizontalVelocityWithUncertainty();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VelocityEstimateImpl);
        impl = (VelocityEstimateImpl)result.getResult();

        assertEquals(impl.getVelocityType(), VelocityType.HorizontalVelocityWithUncertainty);
        assertEquals(impl.getHorizontalSpeed(), 10001);
        assertEquals(impl.getBearing(), 301);
        assertEquals(impl.getUncertaintyHorizontalSpeed(), 199);

        data = getEncodedData_HorizontalWithVerticalVelocityAndUncertainty();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof VelocityEstimateImpl);
        impl = (VelocityEstimateImpl)result.getResult();

        assertEquals(impl.getVelocityType(), VelocityType.HorizontalWithVerticalVelocityAndUncertainty);
        assertEquals(impl.getHorizontalSpeed(), 10002);
        assertEquals(impl.getBearing(), 302);
        assertEquals(impl.getVerticalSpeed(), 202);
        assertEquals(impl.getUncertaintyHorizontalSpeed(), 198);
        assertEquals(impl.getUncertaintyVerticalSpeed(), 197);
    }

    @Test(groups = { "functional.encode", "lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(VelocityEstimateImpl.class);
    	
        VelocityEstimateImpl impl = new VelocityEstimateImpl(VelocityType.HorizontalVelocity, 59, 90, 0, 0, 0);
        byte[] data=getEncodedData_HorizontalVelocity();
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
        
        impl = new VelocityEstimateImpl(VelocityType.HorizontalWithVerticalVelocity, 10000, 300, 200, 0, 0);
        data=getEncodedData_HorizontalWithVerticalVelocity();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
        
        impl = new VelocityEstimateImpl(VelocityType.HorizontalVelocityWithUncertainty, 10001, 301, 0, 199, 0);
        data=getEncodedData_HorizontalVelocityWithUncertainty();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
        
        impl = new VelocityEstimateImpl(VelocityType.HorizontalWithVerticalVelocityAndUncertainty, 10002, 302, 202, 198, 197);
        data=getEncodedData_HorizontalWithVerticalVelocityAndUncertainty();
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));   
    }
}