package com.github.dekaulitz.mockyup.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiXExampleXmlHandlerTest {
    @Test
    void coba() throws TransformerException, ParserConfigurationException, JsonProcessingException {

        System.out.println(xml4());
    }


    String xml() throws ParserConfigurationException, JsonProcessingException, TransformerException {
        String testNode = "{\n" +
                "          \"id\": 2,\n" +
                "          \"title\": \"BOOK_2\",\n" +
                "          \"author\": \"string\",\n" +
                "          \"category\": [\n" +
                "            \"cat1\",\n" +
                "            \"cat2\"\n" +
                "          ],\n" +
                "          \"images\": [\n" +
                "            {\n" +
                "              \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "              \"order\": 1\n" +
                "            },\n" +
                "            {\n" +
                "              \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "              \"order\": 2\n" +
                "            }\n" +
                "          ],\n" +
                "          \"publisher\": \"string\",\n" +
                "          \"publishedDate\": \"2020-06-13T16:05:04.126Z\"\n" +
                "        }";

        OpenApiXExampleXmlHandler openApiXExampleXmlHandler =new OpenApiXExampleXmlHandler();
        openApiXExampleXmlHandler.setStringNode(testNode);
        openApiXExampleXmlHandler.setRootNode("test");
        return openApiXExampleXmlHandler.getDomXml();
    }

    String xml2() throws ParserConfigurationException, JsonProcessingException, TransformerException {
        String testNode = " [\n" +
                "          {\n" +
                "            \"id\": 1,\n" +
                "            \"title\": \"BOOK_1\",\n" +
                "            \"author\": \"string\",\n" +
                "            \"category\": [\"1\",\"2\"],\n" +
                "            \"images\": [\n" +
                "              {\n" +
                "                \"url\": \"string\",\n" +
                "                \"order\": 0\n" +
                "              }\n" +
                "            ],\n" +
                "            \"publisher\": \"string\",\n" +
                "            \"publishedDate\": \"2020-06-13T16:05:04.126Z\"\n" +
                "          }\n" +
                "        ]";

        OpenApiXExampleXmlHandler openApiXExampleXmlHandler =new OpenApiXExampleXmlHandler();
        openApiXExampleXmlHandler.setStringNode(testNode);
        openApiXExampleXmlHandler.setRootNode("test");
        return openApiXExampleXmlHandler.getDomXml();
    }

    String xml3() throws ParserConfigurationException, JsonProcessingException, TransformerException {
        String testNode = "{\n" +
                "          \"id\": 1,\n" +
                "          \"title\": \"BOOK_2_WITH_PATH_REFF\",\n" +
                "          \"author\": \"string\",\n" +
                "          \"category\": [\n" +
                "            \"cat1\",\n" +
                "            \"cat2\"\n" +
                "          ],\n" +
                "          \"images\": [\n" +
                "            {\n" +
                "              \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "              \"order\": 1,\n" +
                "              \"category\": [\n" +
                "                \"cat1\",\n" +
                "                \"cat2\"\n" +
                "              ],\n" +
                "              \"images\": [\n" +
                "                {\n" +
                "                  \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                  \"order\": 1,\n" +
                "                  \"category\": [\n" +
                "                    \"cat1\",\n" +
                "                    \"cat2\"\n" +
                "                  ]\n" +
                "                },\n" +
                "                {\n" +
                "                  \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                  \"order\": 2,\n" +
                "                  \"category\": [\n" +
                "                    \"cat1\",\n" +
                "                    \"cat2\"\n" +
                "                  ]\n" +
                "                }\n" +
                "              ]\n" +
                "            },\n" +
                "            {\n" +
                "              \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "              \"order\": 2,\n" +
                "              \"category\": [\n" +
                "                \"cat1\",\n" +
                "                \"cat2\"\n" +
                "              ],\n" +
                "              \"images\": [\n" +
                "                {\n" +
                "                  \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                  \"order\": 1,\n" +
                "                  \"category\": [\n" +
                "                    \"cat1\",\n" +
                "                    \"cat2\"\n" +
                "                  ]\n" +
                "                },\n" +
                "                {\n" +
                "                  \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                  \"order\": 2,\n" +
                "                  \"category\": [\n" +
                "                    \"cat1\",\n" +
                "                    \"cat2\"\n" +
                "                  ]\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ],\n" +
                "          \"publisher\": \"string\",\n" +
                "          \"publishedDate\": \"2020-06-13T16:05:04.126Z\"\n" +
                "        }";

        OpenApiXExampleXmlHandler openApiXExampleXmlHandler =new OpenApiXExampleXmlHandler();
        openApiXExampleXmlHandler.setStringNode(testNode);
        openApiXExampleXmlHandler.setRootNode("test");
        return openApiXExampleXmlHandler.getDomXml();
    }
    String xml4() throws ParserConfigurationException, JsonProcessingException, TransformerException {
        String testNode = "[\n" +
                "          {\n" +
                "            \"id\": 1,\n" +
                "            \"title\": \"BOOK_2_WITH_PATH_REFF\",\n" +
                "            \"author\": \"string\",\n" +
                "            \"category\": [\n" +
                "              \"cat1\",\n" +
                "              \"cat2\"\n" +
                "            ],\n" +
                "            \"images\": [\n" +
                "              {\n" +
                "                \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                \"order\": 1,\n" +
                "                \"category\": [\n" +
                "                  \"cat1\",\n" +
                "                  \"cat2\"\n" +
                "                ],\n" +
                "                \"images\": [\n" +
                "                  {\n" +
                "                    \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                    \"order\": 1,\n" +
                "                    \"category\": [\n" +
                "                      \"cat1\",\n" +
                "                      \"cat2\"\n" +
                "                    ]\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                    \"order\": 2,\n" +
                "                    \"category\": [\n" +
                "                      \"cat1\",\n" +
                "                      \"cat2\"\n" +
                "                    ]\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                \"order\": 2,\n" +
                "                \"category\": [\n" +
                "                  \"cat1\",\n" +
                "                  \"cat2\"\n" +
                "                ],\n" +
                "                \"images\": [\n" +
                "                  {\n" +
                "                    \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                    \"order\": 1,\n" +
                "                    \"category\": [\n" +
                "                      \"cat1\",\n" +
                "                      \"cat2\"\n" +
                "                    ]\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                    \"order\": 2,\n" +
                "                    \"category\": [\n" +
                "                      \"cat1\",\n" +
                "                      \"cat2\"\n" +
                "                    ]\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            ],\n" +
                "            \"publisher\": \"string\",\n" +
                "            \"publishedDate\": \"2020-06-13T16:05:04.126Z\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1,\n" +
                "            \"title\": \"BOOK_2_WITH_PATH_REFF\",\n" +
                "            \"author\": \"string\",\n" +
                "            \"category\": [\n" +
                "              \"cat1\",\n" +
                "              \"cat2\"\n" +
                "            ],\n" +
                "            \"images\": [\n" +
                "              {\n" +
                "                \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                \"order\": 1,\n" +
                "                \"category\": [\n" +
                "                  \"cat1\",\n" +
                "                  \"cat2\"\n" +
                "                ],\n" +
                "                \"images\": [\n" +
                "                  {\n" +
                "                    \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                    \"order\": 1,\n" +
                "                    \"category\": [\n" +
                "                      \"cat1\",\n" +
                "                      \"cat2\"\n" +
                "                    ]\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                    \"order\": 2,\n" +
                "                    \"category\": [\n" +
                "                      \"cat1\",\n" +
                "                      \"cat2\"\n" +
                "                    ]\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                \"order\": 2,\n" +
                "                \"category\": [\n" +
                "                  \"cat1\",\n" +
                "                  \"cat2\"\n" +
                "                ],\n" +
                "                \"images\": [\n" +
                "                  {\n" +
                "                    \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                    \"order\": 1,\n" +
                "                    \"category\": [\n" +
                "                      \"cat1\",\n" +
                "                      \"cat2\"\n" +
                "                    ]\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"url\": \"http://any.domain.com/any_image.jpg\",\n" +
                "                    \"order\": 2,\n" +
                "                    \"category\": [\n" +
                "                      \"cat1\",\n" +
                "                      \"cat2\"\n" +
                "                    ]\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            ],\n" +
                "            \"publisher\": \"string\",\n" +
                "            \"publishedDate\": \"2020-06-13T16:05:04.126Z\"\n" +
                "          }\n" +
                "        ]";

        OpenApiXExampleXmlHandler openApiXExampleXmlHandler =new OpenApiXExampleXmlHandler();
        openApiXExampleXmlHandler.setStringNode(testNode);
        openApiXExampleXmlHandler.setRootNode("test");
        return openApiXExampleXmlHandler.getDomXml();
    }
}
