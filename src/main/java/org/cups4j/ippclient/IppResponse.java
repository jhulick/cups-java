package org.cups4j.ippclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;

public class IppResponse {

    private static final Logger logger = LoggerFactory.getLogger(IppResponse.class);

    private final static String CRLF = "\r\n";

    private static final int BYTEBUFFER_CAPACITY = 8192;
    // Saved response of printer
    private AttributeGroup attributeGroupResult = null;
    private Attribute attributeResult = null;
    private List<AttributeGroup> attributeGroups = null;

    // read IPP response in global buffer
    protected ByteBuffer byteBuffer = null;

    public IppResponse() {
        attributeGroups = new ArrayList<>();
        byteBuffer = ByteBuffer.allocate(BYTEBUFFER_CAPACITY);
    }

    /**
     * @param buffer
     * @return
     * @throws IOException
     */
    public IppResult getResponse(ByteBuffer buffer) throws IOException {

        byteBuffer.clear();

        attributeGroupResult = null;
        attributeResult = null;
        attributeGroups.clear();

        IppResult result = new IppResult();
        result.setBuf(buffer.array());
        boolean ippHeaderResponse = false;

        // be careful: HTTP and IPP could be transmitted in different set of
        // buffers.
        // see RFC2910, http://www.ietf.org/rfc/rfc2910, page 19
        // read IPP header
        if ((!ippHeaderResponse) && (buffer.hasRemaining())) {
            byteBuffer = buffer;
            result.setIppStatusResponse(getIPPHeader());
            ippHeaderResponse = true;
        }

        byteBuffer = buffer;
        // read attribute group list with attributes
        getAttributeGroupList();

        closeAttributeGroup();
        result.setAttributeGroupList(attributeGroups);
        return result;
    }

    /**
     * @return
     */
    private String getIPPHeader() {
        StringBuffer sb = new StringBuffer();
        sb.append("Major Version:" + IppConverter.toHexWithMarker(byteBuffer.get()));
        sb.append(" Minor Version:" + IppConverter.toHexWithMarker(byteBuffer.get()));

        String statusCode = IppConverter.toHexWithMarker(byteBuffer.get()) + IppConverter.toHex(byteBuffer.get());
        String statusMessage = IppLists.statusCodeMap.get(statusCode);
        if (statusMessage == null) {
            statusMessage = "unknown";
        }
        sb.append(" Request Id:" + byteBuffer.getInt() + "\n");
        sb.append("Status Code:" + statusCode + "(" + statusMessage + ")");

        if (sb.length() != 0) {
            return sb.toString();
        }
        return null;
    }

    /**
     * <p>
     * <strong>Note:</strong> Global variables <code>attributeGroupResult</code>,
     * <code>attributeResult</code>, <code>attributeGroups</code> are filled by local
     * 'tag' methods.<br />
     * Decision for this programming solution is based on the structure of IPP tag
     * sequences to clarify the attribute structure with its values.
     * </p>
     *
     * @return list of attributes group
     */
    private List<AttributeGroup> getAttributeGroupList() throws UnsupportedEncodingException {
        while (byteBuffer.hasRemaining()) {

            byte tag = byteBuffer.get();
            switch (tag) {
                case 0x00:
                    setAttributeGroup(tag); // reserved
                    continue;
                case 0x01:
                    setAttributeGroup(tag); // operation-attributes
                    continue;
                case 0x02:
                    setAttributeGroup(tag); // job-attributes
                    continue;
                case 0x03:
                    return attributeGroups; // end-attributes
                case 0x04:
                    setAttributeGroup(tag); // printer-attributes
                    continue;
                case 0x05:
                    setAttributeGroup(tag); // unsupported-attributes
                    continue;
                case 0x06:
                    setAttributeGroup(tag); // subscription-attributes
                    continue;
                case 0x07:
                    setAttributeGroup(tag); // event-notification-attributes
                    continue;
                case 0x13:
                    setNoValueAttribute(tag); // no-value
                    continue;
                case 0x21:
                    setIntegerAttribute(tag); // integer
                    continue;
                case 0x22:
                    setBooleanAttribute(tag); // boolean
                    continue;
                case 0x23:
                    setEnumAttribute(tag); // enumeration
                    continue;
                case 0x30:
                    setTextAttribute(tag); // octetString;
                    continue;
                case 0x31:
                    setDateTimeAttribute(tag);// datetime
                    continue;
                case 0x32:
                    setResolutionAttribute(tag);// resolution
                    continue;
                case 0x33:
                    setRangeOfIntegerAttribute(tag);// rangeOfInteger
                    continue;
                case 0x35:
                    setTextWithLanguageAttribute(tag); // textWithLanguage
                    continue;
                case 0x36:
                    setNameWithLanguageAttribute(tag); // nameWithLanguage
                    continue;
                case 0x41:
                    setTextAttribute(tag); // textWithoutLanguage
                    continue;
                case 0x42:
                    setTextAttribute(tag); // nameWithoutLanguage
                    continue;
                case 0x44:
                    setTextAttribute(tag); // keyword
                    continue;
                case 0x45:
                    setTextAttribute(tag); // uri
                    continue;
                case 0x46:
                    setTextAttribute(tag); // uriScheme
                    continue;
                case 0x47:
                    setTextAttribute(tag); // charset
                    continue;
                case 0x48:
                    setTextAttribute(tag); // naturalLanguage
                    continue;
                case 0x49:
                    setTextAttribute(tag); // mimeMediaType
                    continue;
                default:
                    return attributeGroups; // not defined
            }
        }
        return null;
    }

    /**
     * @param tag
     */
    private void setAttributeGroup(byte tag) {
        if (attributeGroupResult != null) {
            if (attributeResult != null) {
                attributeGroupResult.getAttribute().add(attributeResult);
            }
            attributeGroups.add(attributeGroupResult);
        }
        attributeResult = null;

        attributeGroupResult = new AttributeGroup();
        attributeGroupResult.setTagName(getTagName(IppConverter.toHexWithMarker(tag)));
    }

    /**
     *
     */
    private void closeAttributeGroup() {
        if (attributeGroupResult != null) {
            if (attributeResult != null) {
                attributeGroupResult.getAttribute().add(attributeResult);
            }
            attributeGroups.add(attributeGroupResult);
        }
        attributeResult = null;
        attributeGroupResult = null;
    }

    /**
     * @param tag
     */
    private void setTextAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }

        // set attribute value
        if (!byteBuffer.hasRemaining()) {
            return;
        }
        length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            byte[] dst = new byte[length];
            byteBuffer.get(dst);
            String value = IppConverter.toString(dst);
            String hex = IppConverter.toHexWithMarker(tag);
            AttributeValue attrValue = new AttributeValue();
            attrValue.setTag(hex);
            String tagName = getTagName(hex);
            attrValue.setTagName(tagName);
            attrValue.setValue(value);
            attributeResult.getAttributeValue().add(attrValue);
        }

    }

    /**
     * TODO: natural-language not considered in reporting
     *
     * @param tag
     */
    private void setTextWithLanguageAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }

        // set natural-language and attribute value
        if (!byteBuffer.hasRemaining()) {
            return;
        }

        // set tag, tag name, natural-language
        length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            byte[] dst = new byte[length];
            byteBuffer.get(dst);
            String value = IppConverter.toString(dst);
            String hex = IppConverter.toHexWithMarker(tag);
            AttributeValue attrValue = new AttributeValue();
            attrValue.setTag(hex);
            String tagName = getTagName(hex);
            attrValue.setTagName(tagName);
            attrValue.setValue(value);
            attributeResult.getAttributeValue().add(attrValue);

            // set value
            length = byteBuffer.getShort();
            if ((length != 0) && (byteBuffer.remaining() >= length)) {
                dst = new byte[length];
                byteBuffer.get(dst);
                value = IppConverter.toString(dst);
                attrValue = new AttributeValue();
                attrValue.setValue(value);
                attributeResult.getAttributeValue().add(attrValue);
            }
        }
    }

    /**
     * TODO: natural-language not considered in reporting
     *
     * @param tag
     */
    private void setNameWithLanguageAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }

        // set natural-language and attribute value
        if (!byteBuffer.hasRemaining()) {
            return;
        }

        // set tag, tag name, natural-language
        length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            byte[] dst = new byte[length];
            byteBuffer.get(dst);
            String value = IppConverter.toString(dst);
            String hex = IppConverter.toHexWithMarker(tag);
            AttributeValue attrValue = new AttributeValue();
            attrValue.setTag(hex);
            String tagName = getTagName(hex);
            attrValue.setTagName(tagName);
            attrValue.setValue(value);
            attributeResult.getAttributeValue().add(attrValue);

            // set value
            length = byteBuffer.getShort();
            if ((length != 0) && (byteBuffer.remaining() >= length)) {
                dst = new byte[length];
                byteBuffer.get(dst);
                value = IppConverter.toString(dst);
                attrValue = new AttributeValue();
                attrValue.setValue(value);
                attributeResult.getAttributeValue().add(attrValue);
            }
        }
    }

    /**
     * @param tag
     */
    private void setBooleanAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }

        // set attribute value
        if (!byteBuffer.hasRemaining()) {
            return;
        }
        length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            byte value = byteBuffer.get();
            String hex = IppConverter.toHexWithMarker(tag);
            AttributeValue attrValue = new AttributeValue();
            attrValue.setTag(hex);
            String tagName = getTagName(hex);
            attrValue.setTagName(tagName);
            attrValue.setValue(IppConverter.toBoolean(value));
            attributeResult.getAttributeValue().add(attrValue);
        }
    }

    /**
     * @param tag
     */
    private void setDateTimeAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }

        // set attribute value
        if (!byteBuffer.hasRemaining()) {
            return;
        }
        length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            byte[] dst = new byte[length];
            byteBuffer.get(dst, 0, length);
            String value = IppConverter.toDateTime(dst);
            String hex = IppConverter.toHexWithMarker(tag);
            AttributeValue attrValue = new AttributeValue();
            attrValue.setTag(hex);
            String tagName = getTagName(hex);
            attrValue.setTagName(tagName);
            attrValue.setValue(value);
            attributeResult.getAttributeValue().add(attrValue);
        }
    }

    /**
     * @param tag
     */
    private void setIntegerAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }
        // set attribute value
        if (!byteBuffer.hasRemaining()) {
            return;
        }
        length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            int value = byteBuffer.getInt();
            String hex = IppConverter.toHexWithMarker(tag);
            AttributeValue attrValue = new AttributeValue();
            attrValue.setTag(hex);
            String tagName = getTagName(hex);
            attrValue.setTagName(tagName);
            attrValue.setValue(Integer.toString(value));
            attributeResult.getAttributeValue().add(attrValue);
        }
    }

    /**
     * @param tag
     */
    private void setNoValueAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }
    }

    /**
     * @param tag
     */
    private void setRangeOfIntegerAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }
        // set attribute value
        if (!byteBuffer.hasRemaining()) {
            return;
        }
        length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            int value1 = byteBuffer.getInt();
            int value2 = byteBuffer.getInt();
            String hex = IppConverter.toHexWithMarker(tag);
            AttributeValue attrValue = new AttributeValue();
            attrValue.setTag(hex);
            String tagName = getTagName(hex);
            attrValue.setTagName(tagName);
            attrValue.setValue(Integer.toString(value1) + "," + Integer.toString(value2));
            attributeResult.getAttributeValue().add(attrValue);
        }
    }

    /**
     * @param tag
     */
    private void setResolutionAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }

        // set attribute value
        if (!byteBuffer.hasRemaining()) {
            return;
        }
        length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            int value1 = byteBuffer.getInt();
            int value2 = byteBuffer.getInt();
            byte value3 = byteBuffer.get();
            String hex = IppConverter.toHexWithMarker(tag);
            AttributeValue attrValue = new AttributeValue();
            attrValue.setTag(hex);
            String tagName = getTagName(hex);
            attrValue.setTagName(tagName);
            attrValue.setValue(Integer.toString(value1) + "," + Integer.toString(value2) + "," + Integer.toString(value3));
            attributeResult.getAttributeValue().add(attrValue);
        }
    }

    /**
     * @param tag
     */
    private void setEnumAttribute(byte tag) throws UnsupportedEncodingException {
        short length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            setAttributeName(length);
        }

        // set attribute value
        if (!byteBuffer.hasRemaining()) {
            return;
        }

        length = byteBuffer.getShort();
        if ((length != 0) && (byteBuffer.remaining() >= length)) {
            String hex = IppConverter.toHexWithMarker(tag);
            AttributeValue attrValue = new AttributeValue();
            attrValue.setTag(hex);
            String tagName = getTagName(hex);
            attrValue.setTagName(tagName);

            int value = byteBuffer.getInt();
            if (attributeResult != null) {
                String enumName = getEnumName(value, attributeResult.getName());
                attrValue.setValue(enumName);
            } else {
                attributeResult = new Attribute();
                attributeResult.setName("no attribute name given:");
                attrValue.setValue(Integer.toString(value));
            }

            attributeResult.getAttributeValue().add(attrValue);
        }
    }

    /**
     * @param length
     */
    private void setAttributeName(short length) throws UnsupportedEncodingException {
        if ((length == 0) || (byteBuffer.remaining() < length)) {
            return;
        }
        byte[] dst = new byte[length];
        byteBuffer.get(dst);
        String name = IppConverter.toString(dst);
        if (attributeResult != null) {
            attributeGroupResult.getAttribute().add(attributeResult);
        }
        attributeResult = new Attribute();
        attributeResult.setName(name.toString());
    }

    /**
     * @param tag
     * @return
     */
    private String getTagName(String tag) {
        if (tag == null) {

            logger.error("IppResponse.getTagName(): no tag given");
            return null;
        }
        int l = IppLists.tagList.size();
        for (int i = 0; i < l; i++) {
            if (tag.equals(IppLists.tagList.get(i).getValue())) {
                return IppLists.tagList.get(i).getName();
            }
        }
        return "no name found for tag:" + tag;
    }

    /**
     * @param value
     * @return
     * @nameOfAttribute
     */
    private String getEnumName(int value, String nameOfAttribute) {
        if (nameOfAttribute == null)
            return "Null attribute requested";
        EnumItemMap itemMap = IppLists.enumMap.get(nameOfAttribute);
        if (itemMap == null)
            return "Attribute " + nameOfAttribute + "not found";
        String attrValue = itemMap.get(value).name;
        if (attrValue == null)
            return "Value " + value + " for attribute " + nameOfAttribute + " not found";
        return attrValue;
    }
}
