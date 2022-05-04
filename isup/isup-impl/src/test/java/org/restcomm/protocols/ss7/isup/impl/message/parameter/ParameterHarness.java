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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Start time:09:16:42 2009-04-22<br>
 * Project: restcomm-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public abstract class ParameterHarness {

    // 21 10000011 Address....................... 83
    // 22 01100000 Address....................... 60
    // 23 00111000 Address....................... 38
    // NOTE: now see how nice digits swap can come out with conversion, lol
    private static final byte[] sixDigits = new byte[] { (byte) 0x83, 0x60, 0x38 };
    private static final byte[] fiveDigits = new byte[] { (byte) 0x83, 0x60, 0x08 };
    private static final byte[] sevenDigits = new byte[] { (byte) 0x83, 0x60, 0x33, 0x08 };
    private static final byte[] eightDigits = new byte[] { (byte) 0x83, 0x60, 0x33, 0x48 };
    private static final byte[] threeDigits = new byte[] { (byte) 0x83, 0x0 };;
    private static final String sixDigitsString = "380683";
    private static final String fiveDigitsString = "38068";
    private static final String sevenDigitsString = "3806338";
    private static final String eightDigitsString = "38063384";
    private static final String threeDigitsString = "380";

    // FIXME: add code to check values :)

    protected List<ByteBuf> goodBodies = new ArrayList<ByteBuf>();

    protected List<ByteBuf> badBodies = new ArrayList<ByteBuf>();

    protected String dumpData(ByteBuf b) {
    	ByteBuf copy=Unpooled.wrappedBuffer(b);
    	byte[] innerArray=new byte[copy.readableBytes()];
    	copy.readBytes(innerArray);
        String s = "\n";
        for (byte bb : innerArray) {
            s += Integer.toHexString(bb & 0xFF)+"\n";
        }

        return s;
    }

    public static Boolean byteBufEquals(ByteBuf value1,ByteBuf value2) {
    	ByteBuf value1Wrapper=Unpooled.wrappedBuffer(value1);
    	ByteBuf value2Wrapper=Unpooled.wrappedBuffer(value2);
    	byte[] value1Arr=new byte[value1Wrapper.readableBytes()];
    	byte[] value2Arr=new byte[value2Wrapper.readableBytes()];
    	value1Wrapper.readBytes(value1Arr);
    	value2Wrapper.readBytes(value2Arr);
    	return Arrays.equals(value1Arr, value2Arr);
    }
    
    protected String makeCompare(ByteBuf hardcodedBodyValue, ByteBuf elementEncodedValue) {
    	ByteBuf hardcodedBody=Unpooled.wrappedBuffer(hardcodedBodyValue);
    	ByteBuf elementEncoded=Unpooled.wrappedBuffer(elementEncodedValue);
    	
        int totalLength = 0;
        if (hardcodedBody == null || elementEncoded == null) {
            return "One arg is null";
        }
        if (hardcodedBody.readableBytes() >= elementEncoded.readableBytes()) {
            totalLength = hardcodedBody.readableBytes();
        } else {
            totalLength = elementEncoded.readableBytes();
        }

        String out = "";

        for (int index = 0; index < totalLength; index++) {
            if (hardcodedBody.readableBytes() > 0) {
                out += "hardcodedBody[" + Integer.toHexString(hardcodedBody.readByte()) + "]";
            } else {
                out += "hardcodedBody[NOP]";
            }

            if (elementEncoded.readableBytes() > 0) {
                out += "elementEncoded[" + Integer.toHexString(elementEncoded.readByte()) + "]";
            } else {
                out += "elementEncoded[NOP]";
            }
            out += "\n";
        }

        return out;
    }    

    protected String makeCompare(Object hardcodedBody, Object elementEncoded) {
        int totalLength = 0;
        if (hardcodedBody == null || elementEncoded == null) {
            return "One arg is null";
        }

        if (Array.getLength(hardcodedBody) >= Array.getLength(elementEncoded)) {
            totalLength = Array.getLength(hardcodedBody);
        } else {
            totalLength = Array.getLength(elementEncoded);
        }

        String out = "";

        for (int index = 0; index < totalLength; index++) {
            if (Array.getLength(hardcodedBody) > index) {
                out += "hardcodedBody[" + Array.get(hardcodedBody, index) + "]";
            } else {
                out += "hardcodedBody[NOP]";
            }
            
            if (Array.getLength(elementEncoded) > index) {
                out += "elementEncoded[" + Array.get(elementEncoded, index) + "]";
            } else {
                out += "elementEncoded[NOP]";
            }
            out += "\n";
        }

        return out;
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testDecodeEncode() throws ParameterException {

        for (int index = 0; index < this.goodBodies.size(); index++) {
        	ByteBuf goodBody = this.goodBodies.get(index);
        	AbstractISUPParameter component = this.getTestedComponent();
            doTestDecode(Unpooled.wrappedBuffer(goodBody), true, component, index);
            ByteBuf output=Unpooled.buffer(255);
            component.encode(output);
            boolean equal = byteBufEquals(goodBody, output);
            assertTrue(equal, "Body index: " + index + "\n" + makeCompare(goodBody, output));

        }
        
        for (int index = 0; index < this.badBodies.size(); index++) {

            ByteBuf badBody = this.badBodies.get(index);
            AbstractISUPParameter component = this.getTestedComponent();
            doTestDecode(badBody, false, component, index);
            // TODO: make some tests here?
        }
    }

    public abstract AbstractISUPParameter getTestedComponent() throws ParameterException;

    protected void doTestDecode(ByteBuf presumableBody, boolean shouldPass, AbstractISUPParameter component, int index)
            throws ParameterException {
        try {
            component.decode(presumableBody);
            if (!shouldPass) {
                fail("Decoded[" + index + "] parameter[" + component.getClass() + "], should not pass. Passed data: "
                        + dumpData(presumableBody));
            }

        } catch (IllegalArgumentException iae) {
            if (shouldPass) {
                fail("Failed to decode[" + index + "] parameter[" + component.getClass() + "], should not happen. " + iae
                        + ".Passed data: " + dumpData(presumableBody));
                iae.printStackTrace();
            }
        } catch (ParameterException iae) {
            if (shouldPass) {
                fail("Failed to decode[" + index + "] parameter[" + component.getClass() + "], should not happen. " + iae
                        + ".Passed data: " + dumpData(presumableBody));
                throw iae;
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to decode[" + index + "] parameter[" + component.getClass() + "]." + e + ". Passed data: "
                    + dumpData(presumableBody));
        }
    }

    public void testValues(final Object component, final String getMethodName, final String[] getterMethodNames,
            final Object[][] expectedValues) {
        try {
            Method m = component.getClass().getMethod(getMethodName);
            Object result=m.invoke(component, (Object[])null);
            if(result instanceof List) {
            	@SuppressWarnings("rawtypes")
				List parts = (List)result;
	            for (int index = 0; index < parts.size(); index++) {
	                testValues(parts.get(index), getterMethodNames, expectedValues[index]);
	            }
            } else {
	            Object[] parts = (Object[])result;
	            for (int index = 0; index < parts.length; index++) {
	                testValues(parts[index], getterMethodNames, expectedValues[index]);
	            }
            }
        } catch (Exception e) {
            fail("Failed to check values on component: " + component.getClass().getName() + ", due to: "
                    + e.getMessage());
        }
    }

    public void testValues(final Object component, final String[] getterMethodNames, final Object[] expectedValues) {
        try {
            Class<?> cClass = component.getClass();
            for (int index = 0; index < getterMethodNames.length; index++) {
                Method m = cClass.getMethod(getterMethodNames[index], (Class[])null);
                // Should not be null by now
                Object v = m.invoke(component, (Object[])null);
                if (v == null && expectedValues[index] != null) {
                    fail("Failed to validate values in component: " + component.getClass().getName() + ". Value of: "
                            + getterMethodNames[index] + " is null, but test values is not.");
                }
                if (expectedValues[index] != null && expectedValues[index].getClass().isArray()) {
                    assertTrue(
                            Arrays.deepEquals(new Object[] { expectedValues[index] }, new Object[] { v }),
                            "Failed to validate values in component: " + component.getClass().getName() + ". Value of: "
                                    + getterMethodNames[index] + ":\n"
                                    + makeCompare( expectedValues[index],v));
                }
                else if(expectedValues[index] != null && expectedValues[index] instanceof ByteBuf) {
                	assertTrue(byteBufEquals((ByteBuf)v,(ByteBuf)expectedValues[index]));
                }
                else {
                    assertEquals(v, expectedValues[index], "Failed to validate values in component: "
                            + component.getClass().getName() + ". Value of: " + getterMethodNames[index]);
                }

            }

        } catch (Exception e) {
            fail("Failed to check values on component: " + component.getClass().getName() + ", due to: "
                    + e.getMessage());
        }
    }

    public static byte[] getSixDigits() {
        return sixDigits;
    }

    public static byte[] getFiveDigits() {
        return fiveDigits;
    }

    public static byte[] getThreeDigits() {
        return threeDigits;
    }

    public static byte[] getSevenDigits() {
        return sevenDigits;
    }

    public static byte[] getEightDigits() {
        return eightDigits;
    }

    public static String getSixDigitsString() {
        return sixDigitsString;
    }

    public static String getFiveDigitsString() {
        return fiveDigitsString;
    }

    public static String getThreeDigitsString() {
        return threeDigitsString;
    }

    public static String getSevenDigitsString() {
        return sevenDigitsString;
    }

    public static String getEightDigitsString() {
        return eightDigitsString;
    }

}
