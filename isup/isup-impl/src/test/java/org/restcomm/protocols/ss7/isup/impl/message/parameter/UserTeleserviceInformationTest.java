/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Start time:11:36:27 2009-04-27<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class UserTeleserviceInformationTest extends ParameterHarness {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private ByteBuf getData() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 209, (byte) 179 });
    }

    private ByteBuf getData2() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 145, 94, (byte) 181 });
    }

    private ByteBuf getData3() {
        return Unpooled.wrappedBuffer(new byte[] { (byte) 145, 98, (byte) 129 });
    }

    @Test
    public void testDecode() throws Exception {

        UserTeleserviceInformationImpl prim = new UserTeleserviceInformationImpl();
        prim.decode(getData());

        assertEquals(prim.getCodingStandard(), UserTeleserviceInformation._CODING_STANDARD_NATIONAL);
        assertEquals(prim.getInterpretation(), UserTeleserviceInformation._INTERPRETATION_FHGCI);
        assertEquals(prim.getPresentationMethod(), UserTeleserviceInformation._PRESENTATION_METHOD_HLPP);
        assertEquals(prim.getHighLayerCharIdentification(), UserTeleserviceInformation._HLCI_IVTI);
        assertFalse(prim.isEHighLayerCharIdentificationPresent());
        assertFalse(prim.isEVideoTelephonyCharIdentificationPresent());

        prim = new UserTeleserviceInformationImpl();
        prim.decode(getData2());

        assertEquals(prim.getCodingStandard(), UserTeleserviceInformation._CODING_STANDARD_ITU_T);
        assertEquals(prim.getInterpretation(), UserTeleserviceInformation._INTERPRETATION_FHGCI);
        assertEquals(prim.getPresentationMethod(), UserTeleserviceInformation._PRESENTATION_METHOD_HLPP);
        assertEquals(prim.getHighLayerCharIdentification(), UserTeleserviceInformation._HLCI_MAINTAINENCE);
        assertTrue(prim.isEHighLayerCharIdentificationPresent());
        assertFalse(prim.isEVideoTelephonyCharIdentificationPresent());
        assertEquals(prim.getEHighLayerCharIdentification(), 53);

        prim = new UserTeleserviceInformationImpl();
        prim.decode(getData3());

        assertEquals(prim.getCodingStandard(), UserTeleserviceInformation._CODING_STANDARD_ITU_T);
        assertEquals(prim.getInterpretation(), UserTeleserviceInformation._INTERPRETATION_FHGCI);
        assertEquals(prim.getPresentationMethod(), UserTeleserviceInformation._PRESENTATION_METHOD_HLPP);
        assertEquals(prim.getHighLayerCharIdentification(), UserTeleserviceInformation._HLCI_AUDIOGRAPHIC_CONF);
        assertFalse(prim.isEHighLayerCharIdentificationPresent());
        assertTrue(prim.isEVideoTelephonyCharIdentificationPresent());
        assertEquals(prim.getEVideoTelephonyCharIdentification(), UserTeleserviceInformation._EACI_CSIC_H221);
    }

    @Test
    public void testEncode() throws Exception {

        UserTeleserviceInformationImpl prim = new UserTeleserviceInformationImpl(
                UserTeleserviceInformation._CODING_STANDARD_NATIONAL, UserTeleserviceInformation._INTERPRETATION_FHGCI,
                UserTeleserviceInformation._PRESENTATION_METHOD_HLPP, UserTeleserviceInformation._HLCI_IVTI);
        // int codingStandard, int interpretation, int presentationMethod, int highLayerCharIdentification

        ByteBuf data = getData();
        ByteBuf encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));
        
        prim = new UserTeleserviceInformationImpl(UserTeleserviceInformation._CODING_STANDARD_ITU_T,
                UserTeleserviceInformation._INTERPRETATION_FHGCI, UserTeleserviceInformation._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformation._HLCI_MAINTAINENCE, 53);

        data = getData2();
        encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

        prim = new UserTeleserviceInformationImpl(UserTeleserviceInformation._CODING_STANDARD_ITU_T,
                UserTeleserviceInformation._INTERPRETATION_FHGCI, UserTeleserviceInformation._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformation._HLCI_AUDIOGRAPHIC_CONF, UserTeleserviceInformation._EACI_CSIC_H221, true);

        data = getData3();
        encodedData=Unpooled.buffer();
        prim.encode(encodedData);

        assertTrue(ParameterHarness.byteBufEquals(data, encodedData));

    }

    // before is old style tests
    public UserTeleserviceInformationTest() {
        super();
        super.goodBodies.add(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                UserTeleserviceInformationImpl._HLCI_IVTI));
        super.goodBodies.add(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE));
        super.goodBodies
                .add(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                        UserTeleserviceInformationImpl._INTERPRETATION_FHGCI,
                        UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                        UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE,
                        UserTeleserviceInformationImpl._EACI_CSIC_AA_3_1_CALL));
    }

    @Test
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        UserTeleserviceInformationImpl bci = new UserTeleserviceInformationImpl(getBody(
                UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI,
                UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC, UserTeleserviceInformationImpl._HLCI_IVTI));

        String[] methodNames = { "getPresentationMethod", "getInterpretation", "getCodingStandard",
                "getHighLayerCharIdentification" };
        Object[] expectedValues = { UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                UserTeleserviceInformationImpl._HLCI_IVTI };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        UserTeleserviceInformationImpl bci = new UserTeleserviceInformationImpl(
                getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                        UserTeleserviceInformationImpl._INTERPRETATION_FHGCI,
                        UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                        UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE,
                        UserTeleserviceInformationImpl._EACI_CSIC_AA_3_1_CALL));

        String[] methodNames = { "getPresentationMethod", "getInterpretation", "getCodingStandard",
                "getHighLayerCharIdentification", "getEVideoTelephonyCharIdentification" };
        Object[] expectedValues = { UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE, UserTeleserviceInformationImpl._EACI_CSIC_AA_3_1_CALL };
        super.testValues(bci, methodNames, expectedValues);
    }

    private ByteBuf getBody(int _PRESENTATION_METHOD, int _INTERPRETATION, int _CODING_STANDARD, int _HLCI) {
    	ByteBuf bos = Unpooled.buffer();
        bos.writeByte(0x80 | (_CODING_STANDARD << 5) | (_INTERPRETATION << 2) | _PRESENTATION_METHOD);
        bos.writeByte(0x80 | _HLCI);
        return bos;
    }

    private ByteBuf getBody(int _PRESENTATION_METHOD, int _INTERPRETATION, int _CODING_STANDARD, int _HLCI, int _EACI) {
    	ByteBuf bos = Unpooled.buffer();
        bos.writeByte(0x80 | (_CODING_STANDARD << 5) | (_INTERPRETATION << 2) | _PRESENTATION_METHOD);
        bos.writeByte(_HLCI);
        bos.writeByte(0x80 | _EACI);
        return bos;
    }

    public AbstractISUPParameter getTestedComponent() {
        return new UserTeleserviceInformationImpl(1, 1, 1, 1);
    }

}
