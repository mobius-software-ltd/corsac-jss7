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

package org.restcomm.protocols.ss7.commonapp.datacoding;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class GSMCharsetDecodingData {

    protected int totalSeptetCount;
    protected int leadingSeptetSkipCount;
    protected Gsm7EncodingStyle encodingStyle;

    /**
     * constructor
     *
     * @param totalSeptetCount Length of a decoded message in characters (for SMS case)
     * @param leadingSeptetSkipCount Count of leading septets to skip
     */
    public GSMCharsetDecodingData(Gsm7EncodingStyle encodingStyle, int totalSeptetCount, int leadingSeptetSkipCount) {
        this.totalSeptetCount = totalSeptetCount;
        this.leadingSeptetSkipCount = leadingSeptetSkipCount;
        this.encodingStyle = encodingStyle;
    }
}
