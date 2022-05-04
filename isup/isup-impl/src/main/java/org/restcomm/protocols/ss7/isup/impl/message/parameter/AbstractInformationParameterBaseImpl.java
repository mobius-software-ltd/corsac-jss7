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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.Information;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
abstract class AbstractInformationParameterBaseImpl extends AbstractISUPParameter{
	private List<Information> infos = new ArrayList<Information>();

    public void decode(ByteBuf b) throws ParameterException {
        if (b.readableBytes() < 1) {
            throw new ParameterException();
        }

        for (;;) {
            byte tag = b.readByte();
            byte len = b.readByte();
            Information info = initializeInformation(tag,b.slice(b.readerIndex(), len));
            b.skipBytes(len);
            this.infos.add(info);
            if (b.readableBytes()==0) {
                break;
            }
        }
    }

    public void encode(ByteBuf b) throws ParameterException {
        if (this.infos.size() == 0) {
            throw new ParameterException();
        }
        
        for (Information info : this.infos) {
            b.writeByte(info.getTag());
            b.writeByte(((AbstractInformationImpl)info).getLength());
            ((AbstractInformationImpl)info).encode(b);            
        }
    }

    protected void setInformation(Information... infos) {

        if (infos == null || infos.length == 0) {
            return;
        }
        Class<?> cellClass = infos.getClass().getComponentType();
        for(int index = 0;index<this.infos.size();index++){
            if(cellClass.isAssignableFrom(this.infos.get(index).getClass())){
                this.infos.remove(index);
                index--;
            }
        }
        for (Information i : infos) {
            if (i == null) {
                continue;
            }
            this.infos.add(i);
        }
    }
    protected Information[] getInformation(Class<? extends Information> targetClass){
        List<Information> target = new ArrayList<Information>();
        for(Information i:this.infos){
            if(targetClass.isAssignableFrom(i.getClass()))
                target.add(i);
        }
        return target.toArray((Information[])Array.newInstance(targetClass,target.size()));
    }
    protected abstract Map<Integer, Class<? extends AbstractInformationImpl>> getTagMapping();

    protected AbstractInformationImpl initializeInformation(final int tag, final ByteBuf data) throws ParameterException{
        final int tagStripped = tag & 0xFF;
        final Map<Integer, Class<? extends AbstractInformationImpl>> tagMapping = getTagMapping();
        Class<? extends AbstractInformationImpl> clazz = tagMapping.get(tagStripped);
        if(clazz == null){
            throw new ParameterException("No registered information for tag: "+tagStripped);
        }
        try {
            AbstractInformationImpl info = clazz.newInstance();
            info.setTag(tagStripped);
            info.decode(data);
            return info;
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }
}
