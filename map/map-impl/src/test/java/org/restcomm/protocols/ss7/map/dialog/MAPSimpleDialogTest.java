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

package org.restcomm.protocols.ss7.map.dialog;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
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
public class MAPSimpleDialogTest {

	private byte[] getDataAcceptInfo() {
        return new byte[] { -95, 41, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getDataCloseInfo() {
        return new byte[] { -94, 41, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "dialog" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPAcceptInfoImpl.class);
    	parser.loadClass(MAPCloseInfoImpl.class);
    	
    	ASNDecodeResult result= parser.decode(Unpooled.wrappedBuffer(getDataAcceptInfo()));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPAcceptInfoImpl);
        MAPAcceptInfoImpl accInfo = (MAPAcceptInfoImpl)result.getResult();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(accInfo.getExtensionContainer()));

        result= parser.decode(Unpooled.wrappedBuffer(getDataCloseInfo()));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MAPCloseInfoImpl);        
        MAPCloseInfoImpl closeInfo = (MAPCloseInfoImpl)result.getResult();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(closeInfo.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "dialog" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.loadClass(MAPAcceptInfoImpl.class);
    	parser.loadClass(MAPCloseInfoImpl.class);
    	
        byte[] b = this.getDataAcceptInfo();
        MAPAcceptInfoImpl accInfo = new MAPAcceptInfoImpl();
        accInfo.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ByteBuf buffer=parser.encode(accInfo);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(b, data));

        b = this.getDataCloseInfo();
        MAPCloseInfoImpl closeInfo = new MAPCloseInfoImpl();
        closeInfo.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        buffer=parser.encode(closeInfo);
        data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(b, data));
    }

}
