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
import org.restcomm.protocols.ss7.commonapp.gap.CompoundCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapOnServiceImpl;
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
 * @author yulianoifa
 *
 */
public class GapCriteriaTest {

    public static final int SERVICE_KEY = 821;

    // choice BasicGapCriteria -> CalledAddressValue
    public byte[] getData() {
        return new byte[] { 48, 6, (byte) 128, 4, 48, 69, 91, 84 };
    }

    // choice BasicGapCriteria -> GapOnService
    public byte[] getData1() {
        return new byte[] { 48, 6, (byte) 162, 4, (byte) 128, 2, 3, 53 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 16, 48, 14, (byte) 160, 6, (byte) 162, 4, (byte) 128, 2, 3, 53, (byte) 129, 4, 12, 32, 23, 56 };
    }

    // calledAddressValue
    public byte[] getDigitsData() {
        return new byte[] {48, 69, 91, 84};
    }

    public byte[] getScfIDData() {
        return new byte[] { 12, 32, 23, 56 };
    }

    @Test(groups = { "functional.decode", "gap" })
    public void testDecode_CalledAddressValue() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GapCriteriaWrapperImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GapCriteriaWrapperImpl);
        
        GapCriteriaWrapperImpl elem = (GapCriteriaWrapperImpl)result.getResult();        
        assertTrue(ByteBufUtil.equals(DigitsIsupImpl.translate(elem.getGapCriteria().getBasicGapCriteria().getCalledAddressDigits().getGenericDigits()),Unpooled.wrappedBuffer(getDigitsData())));
    }

    @Test(groups = { "functional.encode", "gap" })
    public void testEncode_CalledAddressValue() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GapCriteriaWrapperImpl.class);
    	
        DigitsIsupImpl calledAddressValue = new DigitsIsupImpl(new GenericDigitsImpl(Unpooled.wrappedBuffer(getDigitsData())));
        BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(calledAddressValue);
        GapCriteriaImpl elem = new GapCriteriaImpl(basicGapCriteria);
        GapCriteriaWrapperImpl param = new GapCriteriaWrapperImpl(elem);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(param);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.decode", "gap" })
    public void testDecode_GapOnService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GapCriteriaWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GapCriteriaWrapperImpl);
        
        GapCriteriaWrapperImpl elem = (GapCriteriaWrapperImpl)result.getResult();        
        assertEquals(elem.getGapCriteria().getBasicGapCriteria().getGapOnService().getServiceKey(), SERVICE_KEY);
    }

    @Test(groups = { "functional.encode", "gap" })
    public void testEncode_GapOnService() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GapCriteriaWrapperImpl.class);
    	
        GapOnServiceImpl gapOnService = new GapOnServiceImpl(SERVICE_KEY);
        BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(gapOnService);
        GapCriteriaImpl elem = new GapCriteriaImpl(basicGapCriteria);
        GapCriteriaWrapperImpl param = new GapCriteriaWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(param);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.decode", "gap" })
    public void testDecode_CompoundCriteria() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GapCriteriaWrapperImpl.class);
    	
    	byte[] rawData = this.getData2();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GapCriteriaWrapperImpl);
        
        GapCriteriaWrapperImpl elem = (GapCriteriaWrapperImpl)result.getResult();        
        assertEquals(elem.getGapCriteria().getCompoundGapCriteria().getBasicGapCriteria().getGapOnService().getServiceKey(), SERVICE_KEY);
        assertTrue(ByteBufUtil.equals(elem.getGapCriteria().getCompoundGapCriteria().getScfID().getValue(), Unpooled.wrappedBuffer(getScfIDData())));
    }

    @Test(groups = { "functional.encode", "gap" })
    public void testEncode_CompoundCriteria() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(GapCriteriaWrapperImpl.class);
    	
        GapOnServiceImpl gapOnService = new GapOnServiceImpl(SERVICE_KEY);
        BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(gapOnService);
        // BasicGapCriteria basicGapCriteria, ScfID scfId
        ScfIDImpl scfId = new ScfIDImpl(Unpooled.wrappedBuffer(getScfIDData()));
        CompoundCriteriaImpl compoundCriteria = new CompoundCriteriaImpl(basicGapCriteria, scfId);
        GapCriteriaImpl elem = new GapCriteriaImpl(compoundCriteria);
        GapCriteriaWrapperImpl param = new GapCriteriaWrapperImpl(elem);
        byte[] rawData = this.getData2();
        ByteBuf buffer=parser.encode(param);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
