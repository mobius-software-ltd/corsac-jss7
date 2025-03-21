package com.mobius.software.telco.protocols.ss7.asn;

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

/**
*
* @author yulian oifa
*
*/

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import com.google.common.truth.ExpectFailure;
import com.google.testing.compile.JavaFileObjects;

public class ASNAnnotationsTest 
{
	@Rule public final ExpectFailure expectFailure = new ExpectFailure();

	 @Test
	 public void MissingAllAnnotationsTest() throws MalformedURLException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 final ASNTagAnnotationProcessor processor = new ASNTagAnnotationProcessor();
		 
		 URL badPrimiteURL=classLoader.getResource("com/mobius/software/telco/protocols/ss7/asn/primitives/AllAnnotationsMissing.java");
		 expectFailure.whenTesting().about(javaSource()).that(JavaFileObjects.forResource(badPrimiteURL)).processedWith(processor).compilesWithoutError();
		 
		 AssertionError expected = expectFailure.getFailure();
		 assertThat(expected.getMessage()).contains("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.AllAnnotationsMissing' is annotated as @ASNTag, however no method found with ASNLength annotation");
		 assertThat(expected.getMessage()).contains("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.AllAnnotationsMissing' is annotated as @ASNTag, however no method found with ASNEncode annotation");	
		 assertThat(expected.getMessage()).contains("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.AllAnnotationsMissing' is annotated as @ASNTag, however no method found with ASNDecode annotation");		 
	 }
	 
	 
	 @Test
	 public void MissingDecodeTest() throws MalformedURLException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 final ASNTagAnnotationProcessor processor = new ASNTagAnnotationProcessor();
		 
		 URL badPrimiteURL=classLoader.getResource("com/mobius/software/telco/protocols/ss7/asn/primitives/DecodeMissing.java");
		 expectFailure.whenTesting().about(javaSource()).that(JavaFileObjects.forResource(badPrimiteURL)).processedWith(processor).compilesWithoutError();
		 
		 AssertionError expected = expectFailure.getFailure();
		 assertThat(expected.getMessage()).doesNotContain("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.DecodeMissing' is annotated as @ASNTag, however no method found with ASNLength annotation");
		 assertThat(expected.getMessage()).doesNotContain("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.DecodeMissing' is annotated as @ASNTag, however no method found with ASNEncode annotation");	
		 assertThat(expected.getMessage()).contains("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.DecodeMissing' is annotated as @ASNTag, however no method found with ASNDecode annotation");		 
	 }
	 
	 @Test
	 public void MissingEncodeTest() throws MalformedURLException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 final ASNTagAnnotationProcessor processor = new ASNTagAnnotationProcessor();
		 	
		 URL badPrimiteURL=classLoader.getResource("com/mobius/software/telco/protocols/ss7/asn/primitives/EncodeMissing.java");
		 expectFailure.whenTesting().about(javaSource()).that(JavaFileObjects.forResource(badPrimiteURL)).processedWith(processor).compilesWithoutError();

		 AssertionError expected = expectFailure.getFailure();
		 assertThat(expected.getMessage()).doesNotContain("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.EncodeMissing' is annotated as @ASNTag, however no method found with ASNLength annotation");
		 assertThat(expected.getMessage()).contains("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.EncodeMissing' is annotated as @ASNTag, however no method found with ASNEncode annotation");	
		 assertThat(expected.getMessage()).doesNotContain("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.EncodeMissing' is annotated as @ASNTag, however no method found with ASNDecode annotation");
	}
	 
	 @Test
	 public void MissingLengthTest() throws MalformedURLException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 final ASNTagAnnotationProcessor processor = new ASNTagAnnotationProcessor();
		 
		 URL badPrimiteURL=classLoader.getResource("com/mobius/software/telco/protocols/ss7/asn/primitives/LengthMissing.java");
		 expectFailure.whenTesting().about(javaSource()).that(JavaFileObjects.forResource(badPrimiteURL)).processedWith(processor).compilesWithoutError();
		 
		 AssertionError expected = expectFailure.getFailure();
		 assertThat(expected.getMessage()).contains("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.LengthMissing' is annotated as @ASNTag, however no method found with ASNLength annotation");
		 assertThat(expected.getMessage()).doesNotContain("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.LengthMissing' is annotated as @ASNTag, however no method found with ASNEncode annotation");	
		 assertThat(expected.getMessage()).doesNotContain("error: Class 'com.mobius.software.telco.protocols.ss7.asn.primitives.LengthMissing' is annotated as @ASNTag, however no method found with ASNDecode annotation");
	 }
	 
	 @Test
	 public void InvalidSignatureTest() throws MalformedURLException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 final ASNTagAnnotationProcessor processor = new ASNTagAnnotationProcessor();
		 
		 URL badPrimiteURL=classLoader.getResource("com/mobius/software/telco/protocols/ss7/asn/primitives/InvalidSignaturePrimitive.java");
		 expectFailure.whenTesting().about(javaSource()).that(JavaFileObjects.forResource(badPrimiteURL)).processedWith(processor).compilesWithoutError();
		 
		 AssertionError expected = expectFailure.getFailure();
		 assertThat(expected.getMessage()).contains("error: Element 'com.mobius.software.telco.protocols.ss7.asn.primitives.InvalidSignaturePrimitive' is annotated as @ASNTag, however its method getLength annoted with @ASNLength should have only one parameter");
		 assertThat(expected.getMessage()).contains("error: Element 'com.mobius.software.telco.protocols.ss7.asn.primitives.InvalidSignaturePrimitive' is annotated as @ASNTag, however its method decode annoted with @ASNDecode should have six parameters");	
		 assertThat(expected.getMessage()).contains("error: Element 'com.mobius.software.telco.protocols.ss7.asn.primitives.InvalidSignaturePrimitive' is annotated as @ASNTag, however its method encode annoted with @ASNEncode should have two parameters");
	 }
	 
	 @Test
	 public void GoodPrimitiveTest() throws MalformedURLException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 final ASNTagAnnotationProcessor processor = new ASNTagAnnotationProcessor();
		 
		 URL badPrimiteURL=classLoader.getResource("com/mobius/software/telco/protocols/ss7/asn/primitives/GoodPrimitive.java");
		 assertAbout(javaSource()).that(JavaFileObjects.forResource(badPrimiteURL)).processedWith(processor).compilesWithoutError();				 		 
	 }
	 
	 @Test
	 public void GoodCompoundPrimitiveTest() throws MalformedURLException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 final ASNTagAnnotationProcessor processor = new ASNTagAnnotationProcessor();
		 
		 URL badPrimiteURL=classLoader.getResource("com/mobius/software/telco/protocols/ss7/asn/primitives/GoodCompoundPrimitive.java");
		 assertAbout(javaSource()).that(JavaFileObjects.forResource(badPrimiteURL)).processedWith(processor).compilesWithoutError();				 		 
	 }
	 
	 @Test
	 public void GoodCompoundPrimitive2Test() throws MalformedURLException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 final ASNTagAnnotationProcessor processor = new ASNTagAnnotationProcessor();
		 
		 URL badPrimiteURL=classLoader.getResource("com/mobius/software/telco/protocols/ss7/asn/primitives/GoodCompoundPrimitive2.java");
		 assertAbout(javaSource()).that(JavaFileObjects.forResource(badPrimiteURL)).processedWith(processor).compilesWithoutError();				 		 
	 }
	 
	 @Test
	 public void GoodCompoundPrimitiveWithListTest() throws MalformedURLException {
		 ClassLoader classLoader = getClass().getClassLoader();
		 final ASNTagAnnotationProcessor processor = new ASNTagAnnotationProcessor();
		 
		 URL badPrimiteURL=classLoader.getResource("com/mobius/software/telco/protocols/ss7/asn/primitives/GoodCompoundPrimitiveWithList.java");
		 assertAbout(javaSource()).that(JavaFileObjects.forResource(badPrimiteURL)).processedWith(processor).compilesWithoutError();				 		 
	 }
}