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

package org.restcomm.protocols.ss7.commonapp.datacoding;

import java.nio.charset.Charset;

import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class EncodingData {

    @Test(groups = { "functional.encode", "datacoding" })
    public void testEncode() throws Exception {
        Charset ucs2Charset = Charset.forName("UTF-16BE");
        Charset utf8Charset = Charset.forName("UTF-8");

        String s1 = "ABab$[]ß";

        byte[] b1 = s1.getBytes(ucs2Charset);
        byte[] b2 = s1.getBytes(utf8Charset);

        this.toByteArray(b1);
        this.toByteArray(b2);
    }

    private String toByteArray(byte[] b1) {
        StringBuilder sb = new StringBuilder();
        int i1 = 0;
        for (byte b : b1) {
            if (i1 == 0)
                i1 = 1;
            else
                sb.append(", ");

            int i2 = (b & 0xFF);
            String s1 = Integer.toHexString(i2);
            sb.append("0x");
            if (s1.length() == 1)
                sb.append("0");
            sb.append(s1);
        }
        return sb.toString();
    }
}
