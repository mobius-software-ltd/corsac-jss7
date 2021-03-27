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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Start time:20:07:45 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public class RedirectionInformationTest extends ParameterHarness {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private ByteBuf getData() {
        return Unpooled.wrappedBuffer(new byte[] { 35, 20 });
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        RedirectionInformationImpl prim = new RedirectionInformationImpl();
        prim.decode(getData());

        assertEquals(prim.getRedirectingIndicator(), RedirectionInformation._RI_CALL_D);
        assertEquals(prim.getOriginalRedirectionReason(), RedirectionInformation._ORR_NO_REPLY);
        assertEquals(prim.getRedirectionCounter(), 4);
        assertEquals(prim.getRedirectionReason(), RedirectionInformation._RI_CALL_REROUTED);
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        RedirectionInformationImpl prim = new RedirectionInformationImpl(RedirectionInformation._RI_CALL_D,
                RedirectionInformation._ORR_NO_REPLY, 4, RedirectionInformation._RI_CALL_REROUTED);
        // int redirectingIndicator, int originalRedirectionReason, int redirectionCounter, int redirectionReason

        ByteBuf data = getData();
        ByteBuf encodedData = Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        RedirectionInformationImpl original = new RedirectionInformationImpl(RedirectionInformation._RI_CALL_D,
                RedirectionInformation._ORR_NO_REPLY, 4, RedirectionInformation._RI_CALL_REROUTED);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "redirectionInformation", RedirectionInformationImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        RedirectionInformationImpl copy = reader.read("redirectionInformation", RedirectionInformationImpl.class);

        assertEquals(copy.getRedirectingIndicator(), original.getRedirectingIndicator());
        assertEquals(copy.getOriginalRedirectionReason(), original.getOriginalRedirectionReason());
        assertEquals(copy.getRedirectionCounter(), original.getRedirectionCounter());
        assertEquals(copy.getRedirectionReason(), original.getRedirectionReason());
    }*/

    public RedirectionInformationTest() {
        super();
        super.goodBodies.add(Unpooled.wrappedBuffer(new byte[] { (byte) 0xC5, (byte) 0x03 }));

        super.badBodies.add(Unpooled.wrappedBuffer(new byte[] { (byte) 0xC5, (byte) 0x0F }));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[3]));
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        RedirectionInformationImpl bci = new RedirectionInformationImpl(getBody(RedirectionInformationImpl._RI_CALL_D_RNPR,
                RedirectionInformationImpl._ORR_UNA, 1, RedirectionInformationImpl._RR_DEFLECTION_IE));

        String[] methodNames = { "getRedirectingIndicator", "getOriginalRedirectionReason", "getRedirectionCounter",
                "getRedirectionReason" };
        Object[] expectedValues = { RedirectionInformationImpl._RI_CALL_D_RNPR, RedirectionInformationImpl._ORR_UNA, 1,
                RedirectionInformationImpl._RR_DEFLECTION_IE };
        super.testValues(bci, methodNames, expectedValues);
    }

    private ByteBuf getBody(int riCallDRnpr, int orrUna, int counter, int rrDeflectionIe) {
    	ByteBuf b = Unpooled.buffer(2);

        byte b0 = (byte) riCallDRnpr;
        b0 |= orrUna << 4;

        byte b1 = (byte) counter;
        b1 |= rrDeflectionIe << 4;
        
        b.writeByte(b0);
        b.writeByte(b1);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent()
     */

    public AbstractISUPParameter getTestedComponent() throws IllegalArgumentException, ParameterException {
        return new RedirectionInformationImpl(Unpooled.wrappedBuffer(new byte[] { 0, 1 }));
    }

}
