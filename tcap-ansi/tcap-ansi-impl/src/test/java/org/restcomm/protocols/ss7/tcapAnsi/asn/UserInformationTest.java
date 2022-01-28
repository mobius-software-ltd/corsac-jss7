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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
*
*/
public class UserInformationTest {

    private byte[] data = new byte[] { (byte) 253, 20, 40, 18, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 7, 4, 5, 3, 4, 5, 6, 7 };

    private byte[] dataValue = new byte[] { 3, 4, 5, 6, 7 };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(UserInformationImpl.class);
    	
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertTrue(result.getResult() instanceof UserInformationImpl);
        UserInformationImpl userInformation = (UserInformationImpl)result.getResult();
        
        assertEquals(userInformation.getUserInformationElements().size(), 1);
        UserInformationElement userInformationElement = userInformation.getUserInformationElements().get(0);

        assertTrue(userInformationElement.isIDObjectIdentifier());

        assertEquals(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }), userInformationElement.getObjectIdentifier());

        assertFalse(userInformationElement.isIDIndirect());

        assertTrue(userInformationElement.isValueObject());
        UserInformationElementTest.byteBufEquals(Unpooled.wrappedBuffer(dataValue), ((ASNOctetString2)userInformationElement.getChild()).getValue());
    }

    @Test(groups = { "functional.encode" })
    public void testUserInformationEncode() throws ASNException {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(UserInformationImpl.class);
    	
        UserInformationElementImpl userInformationElement = new UserInformationElementImpl();
        userInformationElement.setIdentifier(Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 1L, 1L, 1L }));

        ASNOctetString2 value=new ASNOctetString2(Unpooled.wrappedBuffer(dataValue));
        userInformationElement.setChildAsObject(value);
        
        UserInformationImpl userInformation = new UserInformationImpl();
        userInformation.setUserInformationElements(Arrays.asList(new UserInformationElementImpl[] { userInformationElement }));

        ByteBuf output=parser.encode(userInformation);
        assertTrue(UserInformationElementTest.byteBufEquals(Unpooled.wrappedBuffer(data), output));
    }

}
