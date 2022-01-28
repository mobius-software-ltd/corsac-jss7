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

package org.restcomm.protocols.ss7.cap.isup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.RedirectingNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectingNumber;
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
public class RedirectingPartyIDCapTest {

    public byte[] getData() {
        return new byte[] { 4, 6, (byte) 131, 20, 7, 1, 9, 0 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 131, 20, 7, 1, 9, 0 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RedirectingPartyIDIsupImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RedirectingPartyIDIsupImpl);
        
        RedirectingPartyIDIsupImpl elem = (RedirectingPartyIDIsupImpl)result.getResult();                
        RedirectingNumber rn = elem.getRedirectingNumber();
        assertEquals(rn.getNatureOfAddressIndicator(), 3);
        assertTrue(rn.getAddress().equals("7010900"));
        assertEquals(rn.getNumberingPlanIndicator(), 1);
        assertEquals(rn.getAddressRepresentationRestrictedIndicator(), 1);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RedirectingPartyIDIsupImpl.class);
    	
        RedirectingNumber rn = new RedirectingNumberImpl(3, "7010900", 1, 1);
        RedirectingPartyIDIsupImpl elem = new RedirectingPartyIDIsupImpl(rn);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationRestrictedIndicator
    }
}