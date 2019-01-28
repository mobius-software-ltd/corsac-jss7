package org.restcomm.protocols.ss7.cap;

public class XmlPayloadTest {

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {
        File file = new File( "E:\\01\\aaa.txt" );

        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String line = null;
        while ((line = br.readLine()) != null) {
            // variable line does NOT have new-line-character at the end
            sb.append(line);
            sb.append("\n");
        }
        br.close();

        byte[] rawData = sb.toString().getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ConnectRequestImpl copy;
        try {
            copy = reader.read("connect_Request", ConnectRequestImpl.class);
            int g = 0;
        } catch (Exception e) {
            int g1 = 0;
        }

    }*/
}
