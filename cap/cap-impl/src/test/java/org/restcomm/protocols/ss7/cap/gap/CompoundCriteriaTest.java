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

package org.restcomm.protocols.ss7.cap.gap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.gap.BasicGapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CalledAddressAndServiceImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CompoundCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 * @author yulianoifa
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

    @Test
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

    @Test
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
