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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class OfferedCamel4CSIsImpl extends ASNBitString implements OfferedCamel4CSIs {
	private static final int _ID_o_csi = 0;
    private static final int _ID_d_csi = 1;
    private static final int _ID_vt_csi = 2;
    private static final int _ID_t_csi = 3;
    private static final int _ID_mt_sms_csi = 4;
    private static final int _ID_mg_csi = 5;
    private static final int _ID_psi_enhancements = 6;

    public OfferedCamel4CSIsImpl() {    
    	super("OfferedCamel4CSIs",6,15,false);
    }

    public OfferedCamel4CSIsImpl(boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi, boolean mgCsi,
            boolean psiEnhancements) {
    	super("OfferedCamel4CSIs",6,15,false);
        if (oCsi)
            this.setBit(_ID_o_csi);
        if (dCsi)
            this.setBit(_ID_d_csi);
        if (vtCsi)
            this.setBit(_ID_vt_csi);
        if (tCsi)
            this.setBit(_ID_t_csi);
        if (mtSMSCsi)
            this.setBit(_ID_mt_sms_csi);
        if (mgCsi)
            this.setBit(_ID_mg_csi);
        if (psiEnhancements)
            this.setBit(_ID_psi_enhancements);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getOCsi()
     */
    public boolean getOCsi() {
        return this.isBitSet(_ID_o_csi);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getDCsi()
     */
    public boolean getDCsi() {
        return this.isBitSet(_ID_d_csi);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getVtCsi()
     */
    public boolean getVtCsi() {
        return this.isBitSet(_ID_vt_csi);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getTCsi()
     */
    public boolean getTCsi() {
        return this.isBitSet(_ID_t_csi);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getMtSmsCsi()
     */
    public boolean getMtSmsCsi() {
        return this.isBitSet(_ID_mt_sms_csi);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getMgCsi()
     */
    public boolean getMgCsi() {
        return this.isBitSet(_ID_mg_csi);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs#getPsiEnhancements()
     */
    public boolean getPsiEnhancements() {
        return this.isBitSet(_ID_psi_enhancements);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OfferedCamel4CSIs [");

        if (getOCsi())
            sb.append("o_csi, ");
        if (getDCsi())
            sb.append("d_csi, ");
        if (getVtCsi())
            sb.append("vt_csi, ");
        if (getTCsi())
            sb.append("t_csi, ");
        if (getMtSmsCsi())
            sb.append("mt_sms_csi, ");
        if (getMgCsi())
            sb.append("mg_csi, ");
        if (getPsiEnhancements())
            sb.append("psi_enhancements, ");

        sb.append("]");

        return sb.toString();
    }
}
