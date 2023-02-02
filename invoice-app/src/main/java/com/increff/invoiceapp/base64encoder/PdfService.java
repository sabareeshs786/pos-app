package com.increff.invoiceapp.base64encoder;

import com.increff.invoiceapp.models.InvoiceItem;
import com.increff.invoiceapp.models.InvoiceList;
import lombok.Setter;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class PdfService {

    public static String getBase64String(List<Integer> orderItemsIds, List<String> productNames, List<Integer> quantities, List<String> sellingPrices, List<String> mrps) {
        try {

            // Create the XML file
            writeInvoiceToXml(orderItemsIds,productNames, quantities, sellingPrices, mrps, "invoice.xml");
//            convertToUtf8("invoice.xsl", "invoiceutf-8.xsl");
//            convertToUtf8("invoice.xml", "invoiceutf-8.xml");
            // Setup FOP
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File("invoice.xsl"));

            Transformer transformer = factory.newTransformer(xslt);
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // Setup input for XSLT transformation
            Source xml = new StreamSource(new File("invoice.xml"));

            // Resulting SAX events
            Result result = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            transformer.transform(xml, result);

            byte[] bytes = out.toByteArray();

            out.close();
            out.flush();

            // Return the result as a Base64-encoded string
            return Base64Utils.encodeToString(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeInvoiceToXml(List<Integer> orderItemsIds, List<String> productNames, List<Integer> quantities, List<String> sellingPrices, List<String> mrp, String fileName) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceList.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        InvoiceList invoiceList = new InvoiceList();
        Integer size = orderItemsIds.size();
        for(int i = 0; i < size; i++){
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setId(orderItemsIds.get(i));
            invoiceItem.setProductName(productNames.get(i));
            invoiceItem.setQuantity(quantities.get(i));
            invoiceItem.setMrp(mrp.get(i));
            invoiceItem.setSellingPrice(sellingPrices.get(i));
            invoiceList.getItems().add(invoiceItem);
        }
        invoiceList.setTotal(getTotal(sellingPrices));

        jaxbMarshaller.marshal(invoiceList, new File(fileName));
    }
    private static Double getTotal(List<String> sellingPrices){
        Double total = 0.0;
        for(String sellingPrice: sellingPrices){
            total += Double.parseDouble(sellingPrice);
        }
        return total;
    }

    private static void convertToUtf8(String infile, String outfile) throws IOException {
        File inputFile = new File(infile);
        File outputFile = new File(outfile);

        // Read the input file using US-ASCII encoding
        InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.US_ASCII);

        // Write the output file using UTF-8 encoding
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8);

        // Copy the contents from the input to the output file
        int c;
        while ((c = reader.read()) != -1) {
            writer.write(c);
        }

        reader.close();
        writer.close();
    }
}
