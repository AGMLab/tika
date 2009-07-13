/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.Parser;
import org.apache.tika.utils.ParseUtils;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Junit test class for Tika {@link Parser}s.
 */
public class TestParsers extends TestCase {

    private TikaConfig tc;

    public void setUp() throws Exception {
        tc = TikaConfig.getDefaultConfig();
    }

    public void testPDFExtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/testPDF.pdf");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc, "application/pdf");
        String s3 = ParseUtils.getStringContent(file, TikaConfig
                .getDefaultConfig());
        assertEquals(s1, s2);
        assertEquals(s1, s3);
    }

    public void testTXTExtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/testTXT.txt");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc, "text/plain");
        assertEquals(s1, s2);
    }

    public void testRTFExtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/testRTF.rtf");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc, "application/rtf");
        assertEquals(s1, s2);
    }

    public void testXMLExtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/testXML.xml");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc, "application/xml");
        assertEquals(s1, s2);
    }

    public void testPPTExtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/testPPT.ppt");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc,
                "application/vnd.ms-powerpoint");
        assertEquals(s1, s2);
        Parser parser = tc.getParser("application/vnd.ms-powerpoint");
        Metadata metadata = new Metadata();
        InputStream stream = new FileInputStream(file);
        try {
            parser.parse(stream, new DefaultHandler(), metadata);
        } finally {
            stream.close();
        }
        assertEquals("Sample Powerpoint Slide", metadata.get(Metadata.TITLE));
    }

    public void testWORDxtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/testWORD.doc");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc, "application/msword");
        assertEquals(s1, s2);
        Parser parser = tc.getParser("application/msword");
        Metadata metadata = new Metadata();
        InputStream stream = new FileInputStream(file);
        try {
            parser.parse(stream, new DefaultHandler(), metadata);
        } finally {
            stream.close();
        }
        assertEquals("Sample Word Document", metadata.get(Metadata.TITLE));
    }

    public void testEXCELExtraction() throws Exception {
        final String expected = "Numbers and their Squares";
        File file = getResourceAsFile("/test-documents/testEXCEL.xls");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc,
                "application/vnd.ms-excel");
        assertEquals(s1, s2);
        assertTrue("Text does not contain '" + expected + "'", s1
                .contains(expected));
        Parser parser = tc.getParser("application/vnd.ms-excel");
        Metadata metadata = new Metadata();
        InputStream stream = new FileInputStream(file);
        try {
            parser.parse(stream, new DefaultHandler(), metadata);
        } finally {
            stream.close();
        }
        assertEquals("Simple Excel document", metadata.get(Metadata.TITLE));
    }

    public void testOOExtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/testOpenOffice2.odt");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc,
                "application/vnd.oasis.opendocument.text");
        assertEquals(s1, s2);
    }

    public void testOutlookExtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/test-outlook.msg");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc,
                "application/vnd.ms-outlook");
        assertEquals(s1, s2);
    }

    public void testHTMLExtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/testHTML.html");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc, "text/html");
        assertEquals(s1, s2);

        Parser parser = tc.getParser("text/html");
        assertNotNull(parser);
    }

    public void testZipFileExtraction() throws Exception {
        File file = getResourceAsFile("/test-documents/test-documents.zip");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc, "application/zip");
        assertEquals(s1, s2);

        Parser parser = tc.getParser("application/zip");
        assertNotNull(parser);
    }

    public void testMP3Extraction() throws Exception {
        File file = getResourceAsFile("/test-documents/testMP3.mp3");
        String s1 = ParseUtils.getStringContent(file, tc);
        String s2 = ParseUtils.getStringContent(file, tc, "audio/mpeg");
        assertEquals(s1, s2);

        Parser parser = tc.getParser("audio/mpeg");
        assertNotNull(parser);
    }

    /**
     * This method will give you back the filename incl. the absolute path name
     * to the resource. If the resource does not exist it will give you back the
     * resource name incl. the path.
     * 
     * @param name
     *            The named resource to search for.
     * @return an absolute path incl. the name which is in the same directory as
     *         the the class you've called it from.
     */
    public String getFileResource(String name) {
        URL url = this.getClass().getResource(name);
        if (url != null) {
            return url.getFile();
        } else {
            // We have a file which does not exists
            // We got the path
            url = this.getClass().getResource(".");
            return url.getFile() + name;
        }
    }

    public File getResourceAsFile(String filename) {
        return new File(getFileResource(filename));
    }

    public InputStream getResourceAsStream(String name) {
        return this.getClass().getResourceAsStream(name);
    }

}
