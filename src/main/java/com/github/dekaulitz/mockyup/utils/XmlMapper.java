package com.github.dekaulitz.mockyup.utils;

public class XmlMapper {
    private static com.fasterxml.jackson.dataformat.xml.XmlMapper xmlMapper;

    public static com.fasterxml.jackson.dataformat.xml.XmlMapper mapper() {
        if (xmlMapper == null) {
            xmlMapper = new com.fasterxml.jackson.dataformat.xml.XmlMapper();
        }
        return xmlMapper;
    }
}
