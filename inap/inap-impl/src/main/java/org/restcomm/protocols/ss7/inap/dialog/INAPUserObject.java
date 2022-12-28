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
package org.restcomm.protocols.ss7.inap.dialog;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPDialogState;
/**
*
* @author yulian.oifa
*/
public class INAPUserObject implements Externalizable {
	private INAPDialogState state;
	private boolean returnMessageOnError;
	private INAPApplicationContext applicationContext;
	private Externalizable realObject;
	
	public INAPUserObject() {
		
	}
	
	public INAPUserObject(INAPDialogState state, boolean returnMessageOnError, INAPApplicationContext applicationContext, Externalizable realObject) {
		this.state = state;
		this.returnMessageOnError = returnMessageOnError;
		this.applicationContext = applicationContext;
		this.realObject = realObject;
	}
	
	public INAPDialogState getState() {
		return state;
	}


	public boolean isReturnMessageOnError() {
		return returnMessageOnError;
	}


	public INAPApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public Externalizable getRealObject() {
		return realObject;
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		state=INAPDialogState.getInstance(in.readInt());
		applicationContext=INAPApplicationContext.getInstance(in.readInt());
		returnMessageOnError=in.readBoolean();
		if(in.readByte()==1)
			realObject=(Externalizable) in.readObject();
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(state.getState());
		out.writeInt(applicationContext.getCode());
		out.writeBoolean(returnMessageOnError);
		if(realObject!=null)
		{
			out.writeByte(1);
			out.writeObject(realObject);
		}
		else
			out.writeByte(0);		
	}
}