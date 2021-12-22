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

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TypeOfShape;
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
public class AddGeographicalInformationTest {

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyCircle() {
        return new byte[] { 4, 8, 16, 92, 113, -57, -106, 11, 97, 7 };
    }

    @Test(groups = { "functional.decode", "lsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AddGeographicalInformationImpl.class);
    	
        byte[] data = getEncodedData_EllipsoidPointWithUncertaintyCircle();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AddGeographicalInformationImpl);
        AddGeographicalInformationImpl impl = (AddGeographicalInformationImpl)result.getResult();
        
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - (-149)) < 0.01); // -31
        assertTrue(Math.abs(impl.getUncertainty() - 9.48) < 0.01);
    }

    @Test(groups = { "functional.encode", "lsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(AddGeographicalInformationImpl.class);
    	
        AddGeographicalInformationImpl impl = new AddGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, 65, -149, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        byte[] data=this.getEncodedData_EllipsoidPointWithUncertaintyCircle();
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}