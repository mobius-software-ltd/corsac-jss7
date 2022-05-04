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

package org.restcomm.protocols.ss7.map.service.mobility.faultRecovery;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
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
public class ResetRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 9, 10, 1, 1, 4, 4, (byte) 145, 33, 67, (byte) 245 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 14, 4, 4, (byte) 145, 33, 67, (byte) 245, 48, 6, 4, 4, 33, 67, 0, (byte) 241 };
    }

    @Test(groups = { "functional.decode", "service.mobility.faultRecovery" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ResetRequestImpl.class);
    	
        byte[] data = getEncodedData();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ResetRequestImpl);
        ResetRequestImpl prim = (ResetRequestImpl)result.getResult();
        
        assertEquals(prim.getNetworkResource(), NetworkResource.hlr);
        assertEquals(prim.getHlrNumber().getAddress(), "12345");
        assertNull(prim.getHlrList());

        data = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ResetRequestImpl);
        prim = (ResetRequestImpl)result.getResult();

        assertNull(prim.getNetworkResource());
        assertEquals(prim.getHlrNumber().getAddress(), "12345");
        assertEquals(prim.getHlrList().size(), 1);
        assertEquals(prim.getHlrList().get(0).getData(), "1234001");
    }

    @Test(groups = { "functional.encode", "service.mobility.faultRecovery" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ResetRequestImpl.class);
    	
        ISDNAddressStringImpl hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "12345");
        ResetRequestImpl prim = new ResetRequestImpl(NetworkResource.hlr, hlrNumber, null);

        byte[] data=this.getEncodedData();
    	ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        List<IMSI> hlrList = new ArrayList<IMSI>();
        IMSIImpl imsi = new IMSIImpl("1234001");
        hlrList.add(imsi);
        prim = new ResetRequestImpl(null, hlrNumber, hlrList);

        data=this.getEncodedData2();
    	buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}