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

package org.restcomm.protocols.ss7.map.datacoding;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharset;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetDecoder;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetDecodingData;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetEncoder;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetEncodingData;
import org.restcomm.protocols.ss7.commonapp.datacoding.Gsm7EncodingStyle;
import org.restcomm.protocols.ss7.commonapp.smstpu.AbsoluteTimeStampImpl;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeaderElement;
import org.restcomm.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ConcatenatedShortMessagesIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsDeliverTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataHeaderImpl;
import org.restcomm.protocols.ss7.map.smstpdu.UserDataImpl;
import org.testng.annotations.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class GSMCharsetTest {

    @Test(groups = { "functional.encode", "datacoding" })
    public void testEncode() throws Exception {

        this.doTestEncode("123456\r", new byte[] { 49, -39, -116, 86, -77, 53, 26 }, Gsm7EncodingStyle.bit7_ussd_style, null, 7);
        this.doTestEncode("1\r", new byte[] { -79, 6 }, Gsm7EncodingStyle.bit7_ussd_style, null, 2);
        this.doTestEncode("1", new byte[] { 49 }, Gsm7EncodingStyle.bit7_ussd_style, null, 1);

        // case: adding '\r' instead of '@' - USSD style
        this.doTestEncode("1234567", new byte[] { 49, -39, -116, 86, -77, -35, 26 }, Gsm7EncodingStyle.bit7_ussd_style, null, 7);
        // case: adding double '\r' when '\r' will be removed at the decoder side - USSD style
        this.doTestEncode("1234567\r", new byte[] { 49, -39, -116, 86, -77, -35, 26, 13 }, Gsm7EncodingStyle.bit7_ussd_style, null, 8);
        // case: adding '@' (not '\r') - SMS style
        this.doTestEncode("1234567", new byte[] { 49, -39, -116, 86, -77, -35, 0 }, Gsm7EncodingStyle.bit7_sms_style, null, 7);
        // case: do not adding double '\r' when '\r' will be removed at the decoder side - SMS style
        this.doTestEncode("1234567\r", new byte[] { 49, -39, -116, 86, -77, -35, 26 }, Gsm7EncodingStyle.bit7_sms_style, null, 8);

        // This raw data is from nad1053.pcap, 2nd packet.
        this.doTestEncode("*125*+31628839999#", new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a,
                (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 }, Gsm7EncodingStyle.bit7_ussd_style, null, 18);
        // This raw data is from nad1053.pcap, last packet.
        this.doTestEncode("You don t have enough credit to call this number. ", new byte[] { (byte) 0xd9, (byte) 0x77, 0x1d,
                0x44, (byte) 0x7e, (byte) 0xbb, 0x41, 0x74, 0x10, 0x3a, 0x6c, 0x2f, (byte) 0x83, (byte) 0xca, (byte) 0xee,
                0x77, (byte) 0xfd, (byte) 0x8c, 0x06, (byte) 0x8d, (byte) 0xe5, 0x65, 0x72, (byte) 0x9a, 0x0e, (byte) 0xa2,
                (byte) 0xbf, 0x41, (byte) 0xe3, 0x30, (byte) 0x9b, 0x0d, (byte) 0xa2, (byte) 0xa3, (byte) 0xd3, 0x73,
                (byte) 0x90, (byte) 0xbb, (byte) 0xde, 0x16, (byte) 0x97, (byte) 0xe5, 0x2e, 0x10 }, Gsm7EncodingStyle.bit7_ussd_style, null, 50);
        // The USSD String represented by above raw data
        this.doTestEncode("ndmgapp2ndmgapp2", new byte[] { 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e,
                0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 }, Gsm7EncodingStyle.bit7_ussd_style, null, 16);
        this.doTestEncode("*88#", new byte[] { 0x2a, 0x1c, 0x6e, (byte) 0x4 }, Gsm7EncodingStyle.bit7_ussd_style, null, 4);

        this.doTestEncode("Hell", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 0, -78, -52, 102, 3 }, Gsm7EncodingStyle.bit7_sms_style,
                Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }), 14);

        this.doTestEncode("[88]", new byte[] { 27, 30, 14, -73, -15, 1 }, Gsm7EncodingStyle.bit7_sms_style, null, 6);
        this.doTestEncode("[88]", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, -64, -122, -121, -61, 109, 124 },
                Gsm7EncodingStyle.bit7_sms_style, Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }), 16);

        this.doTestEncode("Hell", new byte[] { 72, 101, 108, 108 }, Gsm7EncodingStyle.bit8_smpp_style, null, 0);
        this.doTestEncode("Hell", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 72, 101, 108, 108 }, Gsm7EncodingStyle.bit8_smpp_style,
        		Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }), 0);
        this.doTestEncode("[88]", new byte[] { 27, 60, 56, 56, 27, 62 }, Gsm7EncodingStyle.bit8_smpp_style, null, 0);
        this.doTestEncode("[88]", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 27, 60, 56, 56, 27, 62 },
                Gsm7EncodingStyle.bit8_smpp_style, Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }), 0);
    }

    @Test(groups = { "functional.decode", "datacoding" })
    public void testDecode() throws Exception {
        int octetOffset = 8;
        int septetOffset = GSMCharset.octetsToSeptets(octetOffset);

        this.doTestDecode("123456\r", new byte[] { 49, -39, -116, 86, -77, 53, 26 }, Gsm7EncodingStyle.bit7_ussd_style, 0, 0);
        this.doTestDecode("1\r", new byte[] { -79, 6 }, Gsm7EncodingStyle.bit7_ussd_style, 0, 0);
        this.doTestDecode("1", new byte[] { 49 }, Gsm7EncodingStyle.bit7_ussd_style, 0, 0);

        this.doTestDecode("1234567", new byte[] { 49, -39, -116, 86, -77, -35, 26 }, Gsm7EncodingStyle.bit7_ussd_style, 0, 0);
        this.doTestDecode("1234567\r\r", new byte[] { 49, -39, -116, 86, -77, -35, 26, 13 }, Gsm7EncodingStyle.bit7_ussd_style,
                0, 0);
        this.doTestDecode("1234567", new byte[] { 49, -39, -116, 86, -77, -35, 0 }, Gsm7EncodingStyle.bit7_sms_style, 7, 0);
        this.doTestDecode("1234567\r", new byte[] { 49, -39, -116, 86, -77, -35, 26 }, Gsm7EncodingStyle.bit7_sms_style, 8, 0);

        // This raw data is from nad1053.pcap, 2nd packet.
        this.doTestDecode("*125*+31628839999#", new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a,
                (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 },
                Gsm7EncodingStyle.bit7_ussd_style, 0, 0);
        // This raw data is from nad1053.pcap, last packet.
        this.doTestDecode("You don t have enough credit to call this number. ", new byte[] { (byte) 0xd9, (byte) 0x77, 0x1d,
                0x44, (byte) 0x7e, (byte) 0xbb, 0x41, 0x74, 0x10, 0x3a, 0x6c, 0x2f, (byte) 0x83, (byte) 0xca, (byte) 0xee,
                0x77, (byte) 0xfd, (byte) 0x8c, 0x06, (byte) 0x8d, (byte) 0xe5, 0x65, 0x72, (byte) 0x9a, 0x0e, (byte) 0xa2,
                (byte) 0xbf, 0x41, (byte) 0xe3, 0x30, (byte) 0x9b, 0x0d, (byte) 0xa2, (byte) 0xa3, (byte) 0xd3, 0x73,
                (byte) 0x90, (byte) 0xbb, (byte) 0xde, 0x16, (byte) 0x97, (byte) 0xe5, 0x2e, 0x10 },
                Gsm7EncodingStyle.bit7_ussd_style, 0, 0);
        // The USSD String represented by above raw data
        this.doTestDecode("ndmgapp2ndmgapp2", new byte[] { 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e,
                0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 }, Gsm7EncodingStyle.bit7_ussd_style, 0, 0);
        this.doTestDecode("*88#", new byte[] { 0x2a, 0x1c, 0x6e, (byte) 0xd4 }, Gsm7EncodingStyle.bit7_ussd_style, 0, 0);

        this.doTestDecode("[88]", new byte[] { 27, 30, 14, -73, -15, 1 }, Gsm7EncodingStyle.bit7_sms_style, 6, 0);
        this.doTestDecode("[88]", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, -64, -122, -121, -61, 109, 124 },
                Gsm7EncodingStyle.bit7_sms_style, 16, septetOffset);

        this.doTestDecode("Hell", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 0, -78, -52, 102, 3 }, Gsm7EncodingStyle.bit7_sms_style, 14, septetOffset);

        this.doTestDecode("Hell", new byte[] { 72, 101, 108, 108 }, Gsm7EncodingStyle.bit8_smpp_style, 0, 0);
        this.doTestDecode("Hell", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 72, 101, 108, 108 }, Gsm7EncodingStyle.bit8_smpp_style, 0, octetOffset);
        this.doTestDecode("[88]", new byte[] { 27, 60, 56, 56, 27, 62 }, Gsm7EncodingStyle.bit8_smpp_style, 0, 0);
        this.doTestDecode("[88]", new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 27, 60, 56, 56, 27, 62 },
                Gsm7EncodingStyle.bit8_smpp_style, 0, octetOffset);

        this.doTestDecode("He l", new byte[] { 72, 101, (byte) 200, 108 }, Gsm7EncodingStyle.bit8_smpp_style, 0, 0);
    }

    private void doTestEncode(String decodedString, byte[] encodedData, Gsm7EncodingStyle gsm7EncodingStyle, ByteBuf bufUDH,
            int totalSeptetCount) throws Exception {
        GSMCharset cs = new GSMCharset();
        GSMCharsetEncoder encoder = (GSMCharsetEncoder) cs.newEncoder();
        encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(gsm7EncodingStyle, bufUDH));
        ByteBuf bb = encoder.encode(decodedString);
        byte[] data = new byte[bb.readableBytes()];
        bb.readBytes(data);
        int len = encoder.getGSMCharsetEncodingData().getTotalSeptetCount();

        assertEquals(encodedData, data);
        assertEquals(totalSeptetCount, len);
    }

    private void doTestDecode(String decodedString, byte[] encodedData, Gsm7EncodingStyle gsm7EncodingStyle, int totalSeptetCount,
            int leadingSeptetSkipCount) throws Exception {

        ByteBuf bb = Unpooled.wrappedBuffer(encodedData);
        GSMCharset cs = new GSMCharset();
        GSMCharsetDecoder decoder = (GSMCharsetDecoder) cs.newDecoder();
        switch (gsm7EncodingStyle) {
            case bit7_ussd_style:
                decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(gsm7EncodingStyle, Integer.MAX_VALUE, 0));
                break;
            case bit7_sms_style:
                decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(gsm7EncodingStyle, totalSeptetCount,
                        leadingSeptetSkipCount));
                break;
            case bit8_smpp_style:
                decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(gsm7EncodingStyle, Integer.MAX_VALUE, leadingSeptetSkipCount));
                break;
        }

        String s1 = decoder.decode(bb);
        
//        int i11 = s1.charAt(0);
//        int i12 = s1.charAt(1);
//        int i13 = s1.charAt(2);
//        int i14 = s1.charAt(3);
//        int j11 = decodedString.charAt(0);
//        int j12 = decodedString.charAt(1);
//        int j13 = decodedString.charAt(2);
//        int j14 = decodedString.charAt(3);
//
//        assertEquals(s1.length(), decodedString.length());
//        assertEquals(s1.charAt(0), decodedString.charAt(0));
//        assertEquals(s1.charAt(1), decodedString.charAt(1));
//        assertEquals(s1.charAt(2), decodedString.charAt(2));
//        assertEquals(s1.charAt(3), decodedString.charAt(3));

        assertEquals(s1, decodedString);
    }

    @Test(groups = { "functional.decode", "datacoding" })
    public void testBufferOverflowTest() throws Exception {
        AbsoluteTimeStampImpl serviceCentreTimeStamp = new AbsoluteTimeStampImpl(15, 5, 12, 2, 20, 11, 0);
        DataCodingSchemeImpl dcs = new DataCodingSchemeImpl(0);
        UserDataHeaderImpl userDataHeader = new UserDataHeaderImpl();
        int messageReferenceNumber = 5;
        int messageSegmentCount = 1;
        int messageSegmentNumber = 3;
        UserDataHeaderElement concatenatedShortMessagesIdentifier = new ConcatenatedShortMessagesIdentifierImpl(
                messageReferenceNumber > 255, messageReferenceNumber, messageSegmentCount, messageSegmentNumber);
        userDataHeader.addInformationElement(concatenatedShortMessagesIdentifier);
        UserDataImpl ud = new UserDataImpl("Hell", dcs, userDataHeader, null);
        SmsDeliverTpduImpl smsDeliverTpduImpl = new SmsDeliverTpduImpl(true, false, false, false, new AddressFieldImpl(
                TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "1111"),
                new ProtocolIdentifierImpl(0), serviceCentreTimeStamp, ud);
        SmsSignalInfoImpl smsSignalInfo = new SmsSignalInfoImpl(smsDeliverTpduImpl, null);

        ByteBuf buffer=smsSignalInfo.getValue();
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        
        assertEquals(data, new byte[] { 64, 4, -111, 17, 17, 0, 0, 81, 80, 33, 32, 2, 17, 0, 11, 5, 0, 3, 5, 1, 3, -112, 101,
                54, 27 });
    }

    @Test(groups = { "datacoding" })
    public void testCedilla() throws Exception {
        Charset ucs2Charset = Charset.forName("UTF-16BE");
        Charset utf8Charset = Charset.forName("UTF-8");

        byte[] textPart0 = new byte[] { 0, (byte) 0xE7 };
        String msg0 = new String(textPart0, ucs2Charset);
        msg0.getBytes(utf8Charset);

        byte[] textPart = new byte[] { 0x74, 0x65, 0x73, 0x74, 0x73, 0x65, 0x6e, 0x64, 0x20, (byte) 0xc3, (byte) 0x87 };
        String msg = new String(textPart, utf8Charset);

        GSMCharset gsm7Charset = new GSMCharset();
        GSMCharsetEncoder encoder = gsm7Charset.newEncoder();
        // encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(buf2));
        ByteBuf bb = null;

        try {
            bb = encoder.encode(msg);
        } catch (Exception e) {
            // This can not occur
        }
        // int encodedUserDataLength = encoder.getGSMCharsetEncodingData().getTotalSeptetCount();
        byte[] encodedData = new byte[bb.readableBytes()];
        bb.readBytes(encodedData);
    }

    @Test(groups = { "datacoding" })
    public void testNationalLanguage() throws Exception {
        //GSMCharset gsm7Charset = new GSMCharset("GSM", new String[] {});
        //Charset cSet = gsm7Charset;
//        Gsm7NationalLanguageIdentifier nationalLanguageLockingShift = null;
//        Gsm7NationalLanguageIdentifier nationalLanguageSingleShift = null;
//        NationalLanguageIdentifier nationalLanguageLockingShiftIdentifier = null;
//        NationalLanguageIdentifier nationalLanguageSingleShiftIdentifier = null;
//        if (this.decodedUserDataHeader != null) {
//            nationalLanguageLockingShift = this.decodedUserDataHeader.getNationalLanguageLockingShift();
//            nationalLanguageSingleShift = this.decodedUserDataHeader.getNationalLanguageSingleShift();
//            if (nationalLanguageLockingShift != null)
//                nationalLanguageLockingShiftIdentifier = nationalLanguageLockingShift
//                        .getNationalLanguageIdentifier();
//            if (nationalLanguageSingleShift != null)
//                nationalLanguageSingleShiftIdentifier = nationalLanguageSingleShift.getNationalLanguageIdentifier();
//        }
//        if (nationalLanguageLockingShift != null || nationalLanguageSingleShift != null) {
//            cSet = new GSMCharset("GSM", new String[] {}, nationalLanguageLockingShiftIdentifier,
//                    nationalLanguageSingleShiftIdentifier);
//        }

        //GSMCharsetEncoder encoder = (GSMCharsetEncoder) cSet.newEncoder();
//        encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(buf2));

        
    }

    @Test(groups = { "datacoding" })
    public void testCheckAllCharsCanBeEncoded() throws Exception {
        GSMCharset gsm7Charset = new GSMCharset();

        assertTrue(gsm7Charset.checkAllCharsCanBeEncoded(""));
        assertTrue(gsm7Charset.checkAllCharsCanBeEncoded("[ **]"));
        assertFalse(gsm7Charset.checkAllCharsCanBeEncoded("w\u044Bw"));
    }

    @Test(groups = { "datacoding" })
    public void testCheckEncodedDataLengthInChars() throws Exception {
        GSMCharset gsm7Charset = new GSMCharset();

        assertEquals(gsm7Charset.checkEncodedDataLengthInChars(""), 0);
        assertEquals(gsm7Charset.checkEncodedDataLengthInChars("123[]\u044B4"), 9);
    }

    @Test(groups = { "datacoding" })
    public void testSliceString() throws Exception {
        GSMCharset gsm7Charset = new GSMCharset();

        String[] ss = gsm7Charset.sliceString("123", 8);
        assertEquals(ss.length, 1);
        assertEquals(ss[0], "123");

        ss = gsm7Charset.sliceString("123", 3);
        assertEquals(ss.length, 1);
        assertEquals(ss[0], "123");

        ss = gsm7Charset.sliceString("123", 2);
        assertEquals(ss.length, 2);
        assertEquals(ss[0], "12");
        assertEquals(ss[1], "3");

        ss = gsm7Charset.sliceString("12[3", 6);
        assertEquals(ss.length, 1);
        assertEquals(ss[0], "12[3");

        ss = gsm7Charset.sliceString("12[3", 5);
        assertEquals(ss.length, 1);
        assertEquals(ss[0], "12[3");

        ss = gsm7Charset.sliceString("12[3", 4);
        assertEquals(ss.length, 2);
        assertEquals(ss[0], "12[");
        assertEquals(ss[1], "3");

        ss = gsm7Charset.sliceString("12[3", 3);
        assertEquals(ss.length, 2);
        assertEquals(ss[0], "12");
        assertEquals(ss[1], "[3");

        ss = gsm7Charset.sliceString("12[3", 2);
        assertEquals(ss.length, 3);
        assertEquals(ss[0], "12");
        assertEquals(ss[1], "[");
        assertEquals(ss[2], "3");

        ss = gsm7Charset.sliceString("12[3]", 2);
        assertEquals(ss.length, 4);
        assertEquals(ss[0], "12");
        assertEquals(ss[1], "[");
        assertEquals(ss[2], "3");
        assertEquals(ss[3], "]");

        ss = gsm7Charset.sliceString("12\u044B3", 3);
        assertEquals(ss.length, 2);
        assertEquals(ss[0], "12\u044B");
        assertEquals(ss[1], "3");

    }

    @Test(groups = { "datacoding" })
    public void testCheckZeroCharacter() throws Exception {
        byte[] encodedDataGsm = new byte[] { 97, 32, 27, 60, 98 };
        String msg = "a\0[b";
        UserDataHeaderImpl udh = new UserDataHeaderImpl();

        int msgLenInChars = UserDataImpl.checkEncodedDataLengthInChars(msg, udh);
        int msgLenInBytes = GSMCharset.septetsToOctets(msgLenInChars);
        assertEquals(msgLenInChars, 5); // "[" is a two septet char
        assertEquals(msgLenInBytes, 5);

        this.doTestEncode(msg, encodedDataGsm, Gsm7EncodingStyle.bit8_smpp_style, null, 0);
    }

}
