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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;
import org.testng.annotations.Test;

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
public class ReportingPLMNListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 26, -128, 0, -95, 22, 48, 10, -128, 3, 17, 33, 17, -127, 1, 0, -126, 0, 48, 8, -128, 3, 21, 22,
                33, -127, 1, 1 };
    }

    @Test(groups = { "functional.decode", "service.lms" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ReportingPLMNListImpl.class);
    	
        byte[] data = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ReportingPLMNListImpl);
        ReportingPLMNListImpl imp = (ReportingPLMNListImpl)result.getResult();
        
        assertTrue(imp.getPlmnListPrioritized());

        List<ReportingPLMN> al = imp.getPlmnList();
        assertEquals(al.size(), 2);
        ReportingPLMN p1 = al.get(0);
        ReportingPLMN p2 = al.get(1);
        assertEquals(p1.getPlmnId().getMcc(), 111);
        assertEquals(p1.getPlmnId().getMnc(), 112);
        assertEquals(p2.getPlmnId().getMcc(), 516);
        assertEquals(p2.getPlmnId().getMnc(), 121);
        assertEquals(p1.getRanTechnology(), RANTechnology.gsm);
        assertEquals(p2.getRanTechnology(), RANTechnology.umts);
        assertTrue(p1.getRanPeriodicLocationSupport());
        assertFalse(p2.getRanPeriodicLocationSupport());
    }

    @Test(groups = { "functional.encode", "service.lms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ReportingPLMNListImpl.class);
    	
        PlmnIdImpl plmnId = new PlmnIdImpl(111,112);
        ReportingPLMNImpl rp1 = new ReportingPLMNImpl(plmnId, RANTechnology.gsm, true);
        plmnId = new PlmnIdImpl(516,121);
        ReportingPLMNImpl rp2 = new ReportingPLMNImpl(plmnId, RANTechnology.umts, false);

        List<ReportingPLMN> plmnList = new ArrayList<ReportingPLMN>();
        plmnList.add(rp1);
        plmnList.add(rp2);

        ReportingPLMNListImpl imp = new ReportingPLMNListImpl(true, plmnList);
        byte[] data = getEncodedData();
        ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}