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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class LSAInformationWithdrawTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 2, 5, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 7, 48, 5, 4, 3, 1, 2, 3 };
    }

    private byte[] getLsaIdData() {
        return new byte[] { 1, 2, 3 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LSAInformationWithdrawImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LSAInformationWithdrawImpl);
        LSAInformationWithdrawImpl asc = (LSAInformationWithdrawImpl)result.getResult();
        
        assertTrue(asc.getAllLSAData());
        assertNull(asc.getLSAIdentityList());


        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LSAInformationWithdrawImpl);
        asc = (LSAInformationWithdrawImpl)result.getResult();
        
        assertFalse(asc.getAllLSAData());
        assertEquals(asc.getLSAIdentityList().size(), 1);
        assertTrue(ByteBufUtil.equals(asc.getLSAIdentityList().get(0).getValue(), Unpooled.wrappedBuffer(getLsaIdData())));
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LSAInformationWithdrawImpl.class);
    	
        LSAInformationWithdrawImpl asc = new LSAInformationWithdrawImpl(true);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        List<LSAIdentity> arr = new ArrayList<LSAIdentity>();
        LSAIdentityImpl lsaId = new LSAIdentityImpl(Unpooled.wrappedBuffer(getLsaIdData()));
        arr.add(lsaId);
        asc = new LSAInformationWithdrawImpl(arr);
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
