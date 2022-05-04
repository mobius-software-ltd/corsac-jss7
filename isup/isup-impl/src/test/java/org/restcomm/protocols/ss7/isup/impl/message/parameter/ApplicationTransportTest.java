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
import io.netty.buffer.Unpooled;

import java.lang.reflect.InvocationTargetException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: restcomm-isup-stack<br>
 * Class to test BCI
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class ApplicationTransportTest extends ParameterHarness {

    public ApplicationTransportTest() {
        super();

        super.badBodies.add(Unpooled.wrappedBuffer(new byte[1]));
        super.badBodies.add(Unpooled.wrappedBuffer(new byte[3]));

        super.goodBodies.add(Unpooled.wrappedBuffer(getBody1()));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody2()));
        super.goodBodies.add(Unpooled.wrappedBuffer(getBody3()));
    }

    private ByteBuf getBody1() {

       byte[] body = new byte[]{(byte)0x81};
       return Unpooled.wrappedBuffer(body);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        ApplicationTransportImpl at = new ApplicationTransportImpl(getBody1());

        String[] methodNames = { "getApplicationContextIdentifier","isSendNotificationIndicator","isReleaseCallIndicator","isSegmentationIndicator",
                "getAPMSegmentationIndicator","getSegmentationLocalReference", "getEncapsulatedApplicationInformation" };
        Object[] expectedValues = { new Byte((byte)1),null,null,null,null,null,null };

        super.testValues(at, methodNames, expectedValues);
    }

    private ByteBuf getBody2() {
        byte[] body = new byte[]{
          //ACI
          5,
          //SNI,RCI (1 0)
          0x02,
          //SI,APM indicator (1 00 01 01)
          0x45,
          //Segmentation reference
          (byte)(0x80 | 6)
        };
        return Unpooled.wrappedBuffer(body);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        ApplicationTransportImpl at = new ApplicationTransportImpl(getBody2());

        String[] methodNames = { "getApplicationContextIdentifier","isSendNotificationIndicator","isReleaseCallIndicator","isSegmentationIndicator",
                "getAPMSegmentationIndicator","getSegmentationLocalReference", "getEncapsulatedApplicationInformation" };
        Object[] expectedValues = { new Byte((byte)5),Boolean.TRUE,Boolean.FALSE,Boolean.TRUE,new Byte((byte)5),new Byte((byte)6),null };

        super.testValues(at, methodNames, expectedValues);
    }

    private ByteBuf getBody3() {
        byte[] body = new byte[]{
          //ACI
          2,
          //SNI,RCI (0 1)
          0x01,
          //SI,APM indicator (0 00 01 01)
          0x05,
          //Segmentation reference
          (byte)7,
          // information, we dont parse it now
          (byte)1,
          (byte)2,
          (byte)3,
          (byte)4,
          (byte)8,
          (byte)9,
        };
        return Unpooled.wrappedBuffer(body);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody3EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, ParameterException {
        ApplicationTransportImpl at = new ApplicationTransportImpl(getBody3());

        String[] methodNames = { "getApplicationContextIdentifier","isSendNotificationIndicator","isReleaseCallIndicator","isSegmentationIndicator",
                "getAPMSegmentationIndicator","getSegmentationLocalReference", "getEncapsulatedApplicationInformation" };
        Object[] expectedValues = { new Byte((byte)2),Boolean.FALSE,Boolean.TRUE,Boolean.FALSE,new Byte((byte)5),new Byte((byte)7),Unpooled.wrappedBuffer(new byte[]{(byte)1,
            (byte)2,
            (byte)3,
            (byte)4,
            (byte)8,
            (byte)9}) };

        super.testValues(at, methodNames, expectedValues);
    }
    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.messages.parameters.ParameterHarness#getTestedComponent ()
     */

    public AbstractISUPParameter getTestedComponent() throws ParameterException {
        return new org.restcomm.protocols.ss7.isup.impl.message.parameter.ApplicationTransportImpl();
    }

}
