package com.github.dekaulitz.mockyup.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

@NoArgsConstructor
/**
 * @see  https://swagger.io/docs/specification/data-models/representing-xml/
 */
public class OpenApiXExampleXmlHandler {
    private final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    @Getter
    @Setter
    private String stringNode;
    @Getter
    @Setter
    private String rootNode;

    public String getDomXml() throws ParserConfigurationException, JsonProcessingException, TransformerException {
        Assert.notNull(stringNode, "node cannot be null");
        Assert.notNull(rootNode, "rootNode cannot be null");
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
        Element root = document.createElement(this.rootNode);
        this.transformNode(document, root);
        document.appendChild(root);
        DOMSource source = new DOMSource(document);

        StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.transform(source, new StreamResult(sw));
        return sw.toString();
    }


    private void transformNode(Document document, Element root) throws JsonProcessingException {
        //@FIXME if you have another way for handling xml
        JsonNode node = JsonMapper.mapper().readTree(this.stringNode);
        if (node instanceof ArrayNode) {
            this.iterateArrayNode("", node, document, root);
        } else {
            this.iterateFromObjectNode(node, document, root);
        }
    }

    private void iterateArrayNode(String elementName, Object arrayNode, Document document, Element root) {
        ArrayNode node = (ArrayNode) arrayNode;
        Element element;
        //check if root node is initialized or not if not then create as new node
        if (!elementName.isEmpty()) {
            element = document.createElement(elementName);
        } else {
            // root  was initialized then continue the node
            element = root;
        }
        Element finalElement = element;
        node.forEach(jsonNode -> {
            if (jsonNode instanceof ObjectNode) {
                Element element1 = document.createElement("value");
                this.iterateFromObjectNode(jsonNode, document, element1);
                finalElement.appendChild(element1);
            } else {
                Element element1 = document.createElement("value");
                element1.appendChild(document.createTextNode(jsonNode.asText()));
                finalElement.appendChild(element1);
            }
        });
        if (!elementName.isEmpty()) {
            root.appendChild(finalElement);
        }
    }

    private void iterateFromObjectNode(Object jsonNode, Document document, Element element) {
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.fields().forEachRemaining(objectNodeEntry -> {
            if (objectNodeEntry.getValue() instanceof ArrayNode)
                this.iterateArrayNode(objectNodeEntry.getKey(), objectNodeEntry.getValue(), document, element);
            else {
                Element elem = document.createElement(objectNodeEntry.getKey());
                elem.appendChild(document.createTextNode(objectNodeEntry.getValue().asText()));
                element.appendChild(elem);
            }
        });
    }
}
