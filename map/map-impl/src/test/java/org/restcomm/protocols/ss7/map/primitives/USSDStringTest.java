/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSNationalLanguage;
import org.restcomm.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class USSDStringTest {
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

    public static byte[] getDataGSM7() {
        return new byte[] { 0x04, 0x04, 0x2a, 0x1c, 0x6e, (byte) 0x04 };
    }

    public static byte[] getDataUcs2() {
        return new byte[] { 4, 8, 0, 42, 0, 56, 0, 56, 0, 35 };
    }

    public static byte[] getDataUcs2Lang() {
        return new byte[] { 4, 11, -14, -70, 2, 0, 71, 0, 103, 0, 84, 0, 116 };
    }

    public static byte[] getDataGsm7Lang() {
        return new byte[] { 4, 7, -14, -70, -30, 120, -90, -46, 27 };
    }

    public static byte[] getDataGsm7LangArabic() {
        return new byte[] { 4, 8, 16, 80, 68, 50, 1, -123, 55, 65 };
    }

//    Size=160, Pos=160, Tag=16, TagClass=0, pCBit=1
//            [4, 1, 15, 4, 129, 154, 69, 54, 168, 93, 86, 191, 229, 160, 178, 155, 46, 47, 211, 203, 238, 116, 59, 93, 118, 211, 223, 160, 96, 188, 158, 14, 41, 98, 46, 80, 16, 46, 47, 187, 201, 101, 80, 210, 125, 102, 151, 231, 160, 163, 60, 76, 79, 207, 21, 50, 23, 8, 25, 214, 131, 220, 245, 178, 253, 61, 7, 5, 219, 233, 243, 123, 174, 152, 185, 64, 67, 116, 152, 14, 106, 150, 231, 243, 178, 251, 92, 150, 43, 104, 46, 144, 180, 76, 46, 207, 65, 211, 247, 56, 29, 102, 151, 231, 138, 154, 11, 148, 116, 143, 229, 229, 118, 217, 77, 15, 131, 232, 245, 57, 200, 156, 118, 135, 221, 250, 240, 92, 97, 115, 129, 164, 105, 247, 89, 28, 30, 175, 233, 111, 119, 25, 52, 148, 167, 231, 244, 116, 216, 253, 158, 43, 110, 174, 163, 52, 72, 77, 78, 27]
//    
//    public static byte[] getData2() {
//        return new byte[] { 4, (byte) 129, (byte) 154, 69, 54, -88, 93, 86, -65, -27, -96, -78, -101, 46, 47, -45, -53, -18, 116, 59, 93, 118, -45, -33,
//                -96, 96, -68, -98, 14, 41, 98, 46, 80, 16, 46, 47, -69, -55, 101, 80, -46, 125, 102, -105, -25, -96, -93, 60,
//                76, 79, -49, 21, 50, 23, 8, 25, -42, -125, -36, -11, -78, -3, 61, 7, 5, -37, -23, -13, 123, -82, -104, -71, 64,
//                67, 116, -104, 14, 106, -106, -25, -13, -78, -5, 92, -106, 43, 104, 46, -112, -76, 76, 46, -49, 65, -45, -9,
//                56, 29, 102, -105, -25, -118, -102, 11, -108, 116, -113, -27, -27, 118, -39, 77, 15, -125, -24, -11, 57, -56,
//                -100, 118, -121, -35, -6, -16, 92, 97, 115, -127, -92, 105, -9, 89, 28, 30, -81, -23, 111, 119, 25, 52, -108,
//                -89, -25, -12, 116, -40, -3, -98, 43, 110, -82, -93, 52, 72, 77, 78, 27 };
//    }

    public static String getStringGsm7LangArabic() {
        return "\u062B \u062C\u0681\u0684 aA";
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(USSDStringImpl.class);
    	
    	byte[] data = getDataGSM7();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof USSDStringImpl);
        USSDStringImpl ussdStr = (USSDStringImpl)result.getResult();
        ussdStr.setDataCoding(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7,CharacterSet.GSM7, null, null, false));
        assertTrue(ussdStr.getString(null).equals("*88#"));

        data = getDataUcs2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof USSDStringImpl);
        ussdStr = (USSDStringImpl)result.getResult();
        ussdStr.setDataCoding(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralDataCodingIndication,CharacterSet.UCS2, null, null, false));
        assertTrue(ussdStr.getString(null).equals("*88#"));

        data = getDataUcs2Lang();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof USSDStringImpl);
        ussdStr = (USSDStringImpl)result.getResult();
        ussdStr.setDataCoding(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication, CharacterSet.UCS2, null, null, false));
        assertTrue(ussdStr.getString(null).equals("ru\nGgTt"));

        data = getDataGsm7Lang();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof USSDStringImpl);
        ussdStr = (USSDStringImpl)result.getResult();
        ussdStr.setDataCoding(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication, CharacterSet.GSM7, null, null, false));
        assertTrue(ussdStr.getString(null).equals("ru\nGgTt"));

        data = getDataGsm7LangArabic();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof USSDStringImpl);
        ussdStr = (USSDStringImpl)result.getResult();
        ussdStr.setDataCoding(new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, CBSNationalLanguage.Arabic, null, false));
        assertTrue(ussdStr.getString(null).equals(getStringGsm7LangArabic()));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(USSDStringImpl.class);
    	
    	byte[] data = getDataGSM7();

        // first common case - dcs=15 - GSM7
        CBSDataCodingScheme dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, null, null, false);
        USSDStringImpl ussdStr = new USSDStringImpl("*88#", dcs, null);

        ByteBuf buffer=parser.encode(ussdStr);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        // second common case - dcs=72 - USC2
        data = getDataUcs2();
        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralDataCodingIndication, CharacterSet.UCS2, null, null, false);
        ussdStr = new USSDStringImpl("*88#", dcs, null);
        buffer=parser.encode(ussdStr);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataUcs2Lang();
        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication, CharacterSet.UCS2, null, null, false);
        ussdStr = new USSDStringImpl("ru\nGgTt", dcs, null);
        buffer=parser.encode(ussdStr);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataGsm7Lang();
        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralWithLanguageIndication, CharacterSet.GSM7, null, null, false);
        ussdStr = new USSDStringImpl("ru\nGgTt", dcs, null);
        buffer=parser.encode(ussdStr);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataGsm7LangArabic();
        dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, CBSNationalLanguage.Arabic, null, false);
        assertEquals(dcs.getCode(), 34);
        String s1 = getStringGsm7LangArabic();
        ussdStr = new USSDStringImpl(s1, dcs, null);
        buffer=parser.encode(ussdStr);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);        
        assertTrue(Arrays.equals(data, encodedData));
    }
}