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

package org.restcomm.protocols.ss7.cap.gap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.gap.BasicGapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.BasicGapCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CalledAddressAndServiceImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CallingAddressAndServiceImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapOnServiceImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.testng.annotations.Test;

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
public class BasicGapCriteriaTest {

    public static final int SERVICE_KEY = 821;

    // CalledAddressValue
    public byte[] getData() {
        return new byte[] { 48, 7, (byte) 128, 5, 0, 1, 64, 66, (byte) 134};
    }

    // CalledAddressAndService
    public byte[] getData1() {
        return new byte[] { 48, 12, (byte) 189, 10, (byte) 128, 4, 48, 69, 91, 84, (byte) 129, 2, 3, 53};
    }

    public byte[] getDigitsData() {
        return new byte[] {48, 69, 91, 84};
    }

    public byte[] getData2() {
        return new byte[] { 48, 5, (byte) 162, 3, (byte) 128, 1, 18 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 12, (byte) 190, 10, (byte) 128, 4, 48, 69, 91, 84, (byte) 129, 2, 3, 53 };
    }

    @Test(groups = { "functional.decode", "gap" })
    public void testDecode_CalledAddressValue() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BasicGapCriteriaWrapperImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BasicGapCriteriaWrapperImpl);
        
        BasicGapCriteriaWrapperImpl elem = (BasicGapCriteriaWrapperImpl)result.getResult();        
        assertEquals(elem.getBasicGapCriteria().getCalledAddressNumber().getGenericNumber().getAddress(), "2468");
        assertEquals(elem.getBasicGapCriteria().getCalledAddressNumber().getGenericNumber().getNumberingPlanIndicator(), 4);
    }

    @Test(groups = { "functional.encode", "gap" })
    public void testEncode_CalledAddressValue() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BasicGapCriteriaWrapperImpl.class);
    	
        GenericNumber genericNumber = new GenericNumberImpl(1, "2468", 0, 4, 0, false, 0);
        DigitsIsupImpl digits = new DigitsIsupImpl(genericNumber);
        BasicGapCriteriaImpl elem = new BasicGapCriteriaImpl(digits);
        BasicGapCriteriaWrapperImpl prim=new BasicGapCriteriaWrapperImpl(elem);
        
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.decode", "gap" })
    public void testDecode_GapOnService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BasicGapCriteriaWrapperImpl.class);
    	
        byte[] rawData = this.getData2();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BasicGapCriteriaWrapperImpl);
        
        BasicGapCriteriaWrapperImpl elem = (BasicGapCriteriaWrapperImpl)result.getResult();        
        assertEquals(elem.getBasicGapCriteria().getGapOnService().getServiceKey(), 18);
    }

    @Test(groups = { "functional.encode", "gap" })
    public void testEncode_GapOnService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BasicGapCriteriaWrapperImpl.class);
    	
        GapOnServiceImpl gapOnService = new GapOnServiceImpl(18);
        BasicGapCriteriaImpl elem = new BasicGapCriteriaImpl(gapOnService);
        BasicGapCriteriaWrapperImpl prim=new BasicGapCriteriaWrapperImpl(elem);
        
        byte[] rawData = this.getData2();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.decode", "gap" })
    public void testDecode_CalledAddressAndService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BasicGapCriteriaWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BasicGapCriteriaWrapperImpl);
        
        BasicGapCriteriaWrapperImpl elem = (BasicGapCriteriaWrapperImpl)result.getResult();        
        assertEquals(elem.getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), SERVICE_KEY);
        assertTrue(ByteBufUtil.equals(DigitsIsupImpl.translate(elem.getBasicGapCriteria().getCalledAddressAndService().getCalledAddressDigits().getGenericDigits()),Unpooled.wrappedBuffer(getDigitsData())));
    }

    @Test(groups = { "functional.encode", "gap" })
    public void testEncode_CalledAddressAndService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BasicGapCriteriaWrapperImpl.class);
    	
        DigitsIsupImpl digits = new DigitsIsupImpl(new GenericDigitsImpl(Unpooled.wrappedBuffer(getDigitsData())));
        CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteriaImpl elem = new BasicGapCriteriaImpl(calledAddressAndService);
        BasicGapCriteriaWrapperImpl prim=new BasicGapCriteriaWrapperImpl(elem);
        
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.decode", "gap" })
    public void testDecode_CallingAddressAndService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BasicGapCriteriaWrapperImpl.class);
    	
    	byte[] rawData = this.getData3();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof BasicGapCriteriaWrapperImpl);
        
        BasicGapCriteriaWrapperImpl elem = (BasicGapCriteriaWrapperImpl)result.getResult();        
        assertEquals(elem.getBasicGapCriteria().getCallingAddressAndService().getServiceKey(), SERVICE_KEY);
        assertTrue(ByteBufUtil.equals(DigitsIsupImpl.translate(elem.getBasicGapCriteria().getCallingAddressAndService().getCallingAddressDigits().getGenericDigits()),Unpooled.wrappedBuffer(getDigitsData())));
        
    }

    @Test(groups = { "functional.encode", "gap" })
    public void testEncode_CallingAddressAndService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(BasicGapCriteriaWrapperImpl.class);
    	
        DigitsIsupImpl digits = new DigitsIsupImpl(new GenericDigitsImpl(Unpooled.wrappedBuffer(getDigitsData())));
        CallingAddressAndServiceImpl callingAddressAndService = new CallingAddressAndServiceImpl(digits, SERVICE_KEY);
        BasicGapCriteriaImpl elem = new BasicGapCriteriaImpl(callingAddressAndService);
        BasicGapCriteriaWrapperImpl prim=new BasicGapCriteriaWrapperImpl(elem);
        
        byte[] rawData = this.getData3();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
