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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ChangeOfLocation;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ChangeOfLocationImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaAltImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MidCallControlInfoImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class DpSpecificCriteriaTest {

    public byte[] getData1() {
        return new byte[] { 48, 4, (byte) 129, 2, 3, (byte) 232 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 5, (byte) 162, 3, (byte) 128, 1, 10 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 6, (byte) 163, 4, (byte) 160, 2, (byte) 131, 0 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DpSpecificCriteriaWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DpSpecificCriteriaWrapperImpl);
        
        DpSpecificCriteriaWrapperImpl elem = (DpSpecificCriteriaWrapperImpl)result.getResult();                
        assertEquals((int) elem.getDpSpecificCriteria().getApplicationTimer(), 1000);
        assertNull(elem.getDpSpecificCriteria().getMidCallControlInfo());
        assertNull(elem.getDpSpecificCriteria().getDpSpecificCriteriaAlt());


        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DpSpecificCriteriaWrapperImpl);
        
        elem = (DpSpecificCriteriaWrapperImpl)result.getResult(); 
        assertNull(elem.getDpSpecificCriteria().getApplicationTimer());
        assertEquals((int) elem.getDpSpecificCriteria().getMidCallControlInfo().getMinimumNumberOfDigits(), 10);
        assertNull(elem.getDpSpecificCriteria().getDpSpecificCriteriaAlt());


        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof DpSpecificCriteriaWrapperImpl);
        
        elem = (DpSpecificCriteriaWrapperImpl)result.getResult(); 
        assertNull(elem.getDpSpecificCriteria().getApplicationTimer());
        assertNull(elem.getDpSpecificCriteria().getMidCallControlInfo());
        assertTrue(elem.getDpSpecificCriteria().getDpSpecificCriteriaAlt().getChangeOfPositionControlInfo().get(0).isInterSystemHandOver());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(DpSpecificCriteriaWrapperImpl.class);
    	
        DpSpecificCriteriaImpl elem = new DpSpecificCriteriaImpl(1000);
        DpSpecificCriteriaWrapperImpl wrapper = new DpSpecificCriteriaWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        MidCallControlInfoImpl midCallControlInfo = new MidCallControlInfoImpl(10, null, null, null, null, null);
        elem = new DpSpecificCriteriaImpl(midCallControlInfo);
        wrapper = new DpSpecificCriteriaWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        List<ChangeOfLocation> changeOfPositionControlInfo = new ArrayList<ChangeOfLocation>();
        ChangeOfLocationImpl changeOfLocation = new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interSystemHandOver);
        changeOfPositionControlInfo.add(changeOfLocation);
        DpSpecificCriteriaAltImpl dpSpecificCriteriaAlt = new DpSpecificCriteriaAltImpl(changeOfPositionControlInfo, null);
        elem = new DpSpecificCriteriaImpl(dpSpecificCriteriaAlt);
        wrapper = new DpSpecificCriteriaWrapperImpl(elem);
        rawData = this.getData3();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
