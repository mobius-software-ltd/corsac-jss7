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
package org.restcomm.protocols.ss7.tcap.tc.dialog.events;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.DialogIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;

import io.netty.buffer.ByteBuf;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public abstract class DialogIndicationImpl implements DialogIndication {
	private List<ComponentImpl> components;
    private Dialog dialog;
    private Byte qos;
    private EventType type;
    private ByteBuf originalBuffer;
    
    protected DialogIndicationImpl(EventType type,ByteBuf originalBuffer) {
        super();
        this.type = type;
    }

    /**
     * @return the components
     */
    public List<BaseComponent> getComponents() {
    	if(components==null)
    		return null;
    	
    	List<BaseComponent> result=new ArrayList<BaseComponent>();
    	for(ComponentImpl curr:components)
    		result.add(curr.getExistingComponent());
    	
    	return result;
    }

    /**
     * @param components the components to set
     */
    public void setComponents(List<BaseComponent> components) {
    	if(components==null) {
    		this.components=null;
    		return;
    	}
    	
    	this.components=new ArrayList<ComponentImpl>();
    	for(BaseComponent curr:components) {
    		ComponentImpl newComponent=new ComponentImpl();
    		if(curr instanceof Invoke)
    			newComponent.setInvoke((Invoke)curr);
    		else if(curr instanceof Reject)
    			newComponent.setReject((Reject)curr);
    		else if(curr instanceof ReturnError)
    			newComponent.setReturnError((ReturnError)curr);
    		else if(curr instanceof ReturnResult)
    			newComponent.setReturnResult((ReturnResult)curr);
    		else if(curr instanceof ReturnResultLast)
    			newComponent.setReturnResultLast((ReturnResultLast)curr);
    		
    		this.components.add(newComponent);
    	}
    }

    /**
     * @return the dialog
     */
    public Dialog getDialog() {
        return dialog;
    }

    /**
     * @param dialog the dialog to set
     */
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    /**
     * @return the type
     */
    public EventType getType() {
        return type;
    }

    /**
     * @return the qos
     */
    public Byte getQos() {
        return qos;
    }

    /**
     * @param qos the qos to set
     */
    public void setQos(Byte qos) {
        this.qos = qos;
    }
    
    public ByteBuf getOriginalBuffer() {
    	return originalBuffer;
    }
}