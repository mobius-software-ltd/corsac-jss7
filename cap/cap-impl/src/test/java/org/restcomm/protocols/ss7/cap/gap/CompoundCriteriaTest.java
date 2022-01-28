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

package org.restcomm.protocols.ss7.cap.gap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.gap.BasicGapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CalledAddressAndServiceImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CompoundCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 *
 */
public class CompoundCriteriaTest {

    public static final int SERVICE_KEY = 821;

    public byte[] getData() {
        return new byte[] {48, 20, (byte) 160, 12, (byte) 189, 10, (byte) 128, 4, 48, 69, 91, 11, (byte) 129, 2, 3, 53, (byte) 129, 4, 12, 32, 23, 56};
    }

    public byte[] getDigitsData() {
        return new byte[] {48, 69, 91, 11};
    }

    public byte[] getDigitsData1() {
        return new byte[] {12, 32, 23, 56};
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode_CalledAddressAndService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CompoundCriteriaImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CompoundCriteriaImpl);
        
        CompoundCriteriaImpl elem = (CompoundCriteriaImpl)result.getResult();        
        assertEquals(elem.getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertTrue(ByteBufUtil.equals(DigitsIsupImpl.translate(elem.getBasicGapCriteria().getCalledAddressAndService().getCalledAddressDigits().getGenericDigits()),Unpooled.wrappedBuffer(getDigitsData())));
        assertTrue(ByteBufUtil.equals(elem.getScfID().getValue(), Unpooled.wrappedBuffer(getDigitsData1())));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode_CalledAddressAndService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CompoundCriteriaImpl.class);
    	
    	DigitsIsupImpl digits = new DigitsIsupImpl(new GenericDigitsImpl(Unpooled.wrappedBuffer(getDigitsData())));
        CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);

        ScfIDImpl scfId = new ScfIDImpl(Unpooled.wrappedBuffer(getDigitsData1()));

        CompoundCriteriaImpl elem = new CompoundCriteriaImpl(basicGapCriteria, scfId);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
