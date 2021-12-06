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
 *
 */
public class ReportingPLMNListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 26, -128, 0, -95, 22, 48, 10, -128, 3, 11, 12, 13, -127, 1, 0, -126, 0, 48, 8, -128, 3, 21, 22,
                33, -127, 1, 1 };
    }

    private byte[] getDataPlmnId1() {
        return new byte[] { 11, 12, 13 };
    }

    private byte[] getDataPlmnId2() {
        return new byte[] { 21, 22, 33 };
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
        assertTrue(Arrays.equals(p1.getPlmnId().getData(), getDataPlmnId1()));
        assertTrue(Arrays.equals(p2.getPlmnId().getData(), getDataPlmnId2()));
        assertEquals(p1.getRanTechnology(), RANTechnology.gsm);
        assertEquals(p2.getRanTechnology(), RANTechnology.umts);
        assertTrue(p1.getRanPeriodicLocationSupport());
        assertFalse(p2.getRanPeriodicLocationSupport());
    }

    @Test(groups = { "functional.encode", "service.lms" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ReportingPLMNListImpl.class);
    	
        PlmnIdImpl plmnId = new PlmnIdImpl(getDataPlmnId1());
        ReportingPLMNImpl rp1 = new ReportingPLMNImpl(plmnId, RANTechnology.gsm, true);
        plmnId = new PlmnIdImpl(getDataPlmnId2());
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