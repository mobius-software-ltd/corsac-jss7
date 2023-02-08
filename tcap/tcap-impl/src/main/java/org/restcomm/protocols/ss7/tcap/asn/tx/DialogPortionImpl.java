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

package org.restcomm.protocols.ss7.tcap.asn.tx;

import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.tcap.asn.ASNDialogPortionObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogPortion;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * <p>
 * As per the ITU-T Rec Q.773 Table 30/Q.773 – Dialogue Portion
 * </p>
 * <br/>
 * <p>
 * The DialogPortion consist of Dialogue Portion Tag (0x6B), Dialogue Portion Length; External Tag (0x28), External Length;
 * Structured or unstructured dialogue
 * </p>
 * <br/>
 * <p>
 * As per the Table 33/Q.773 –Structured Dialogue is represented as
 * </p>
 * <br/>
 * <p>
 * Object Identifier Tag (0x06), Object Identifier Length; Dialogue-as-ID value; Single-ASN.1-type Tag (0xa0), Single-ASN.1-type
 * Length; Dialogue PDU
 * </p>
 * <br/>
 * <p>
 * As per the Table 37/Q.773 – Dialogue-As-ID Value is represented OID. Please look {@link DialogPortionImpl._DIALG_STRUCTURED}
 * </p>
 * <br/>
 * <p>
 * As per the Table 34/Q.773 – Unstructured Dialogue is represented as
 * </p>
 * <br/>
 * <p>
 * Object Identifier Tag (0x06), Object Identifier Length; Unidialogue-as-ID value; Single-ASN.1-type Tag (0xa0),
 * Single-ASN.1-type Length; Unidirectional Dialogue PDU
 * </p>
 * <br/>
 * <p>
 * As per the Table 36/Q.773 – Unidialogue-As-ID Value is represented as OID. Please look {@link DialogPortionImpl._DIALG_UNI}
 * </p>
 *
 *
 *
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 * 
 *
 */
@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x0B,constructed=true,lengthIndefinite=false)
public class DialogPortionImpl implements DialogPortion {
	// Encoded OID, dont like this....
    private static final List<Long> _DIALG_UNI = Arrays.asList(new Long[] { 0L, 0L, 17L, 773L, 1L, 2L, 1L });
    private static final List<Long> _DIALG_STRUCTURED = Arrays.asList(new Long[] { 0L, 0L, 17L, 773L, 1L, 1L, 1L });

    private DialogPortionExternalImpl ext = new DialogPortionExternalImpl();

    public DialogPortionImpl() {
        super();        
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogPortion#isUnidirectional()
     */
    public boolean isUnidirectional() {
    	return _DIALG_UNI.equals(ext.getObjectIdentifier());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.asn.DialogPortion#setUnidirectional( boolean)
     */
    public void setUnidirectional(boolean flag) {
        if (flag) {
            ext.setIdentifier(_DIALG_UNI);
            // super.oidValue = _DIALG_UNI;
        } else {
            ext.setIdentifier(_DIALG_STRUCTURED);
            // super.oidValue = _DIALG_STRUCTURED;
        }
    }

    /**
     * @return the dialogAPDU
     */
    public DialogAPDU getDialogAPDU() {
    	if(this.ext.isValueObject()) {
    		ASNDialogPortionObjectImpl output=this.ext.getChild();
    		if(output.getValue() instanceof DialogAPDU)
    			return (DialogAPDU)output.getValue();
    	} 
    	
    	return null;
    }

    /**
     * @param dialogAPDU the dialogAPDU to set
     */
    public void setDialogAPDU(DialogAPDU dialogAPDU) {
    	ASNDialogPortionObjectImpl innerObject=new ASNDialogPortionObjectImpl(dialogAPDU);
    	this.ext.setChildAsObject(innerObject);
    }

    public String toString() {
    	DialogAPDU dialogAPDU=getDialogAPDU();
        return "DialogPortion[dialogAPDU=" + dialogAPDU + ", uniDirectional=" + isUnidirectional() + "]";
    }
}