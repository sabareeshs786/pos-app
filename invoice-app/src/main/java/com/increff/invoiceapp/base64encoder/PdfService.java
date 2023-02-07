package com.increff.invoiceapp.base64encoder;

import com.increff.invoiceapp.models.InvoiceItem;
import com.increff.invoiceapp.models.InvoiceList;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.util.Base64Utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;


public class PdfService {

//    private static final Logger logger = Logger.getLogger(PdfService.class);
//    private static final String RESOURCES_DIR = System.getProperty("user.dir") + "/src/main/resources/com/increff/posapp";
    public static String getBase64String(List<Integer> orderItemsIds, List<String> productNames, List<Integer> quantities, List<String> sellingPrices, List<String> mrps) throws IOException, TransformerException, JAXBException, FOPException {


//             Create the XML file
            writeInvoiceToXml(orderItemsIds, productNames, quantities, sellingPrices, mrps, "invoice.xml");

//             Setup FOP
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

            //Set up a buffer to obtain the content length
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(new File("invoice.xsl")));

            //Make sure the XSL transformation's result is piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            //Setup input
            Source src = new StreamSource(new File("invoice.xml"));

            //Start the transformation and rendering process
            transformer.transform(src, res);

            byte[] bytes = out.toByteArray();

            out.flush();
            out.close();

        return Base64Utils.encodeToString(bytes);
    }

    public static void writeInvoiceToXml(List<Integer> orderItemsIds, List<String> productNames, List<Integer> quantities, List<String> sellingPrices, List<String> mrp, String fileName) throws JAXBException, IOException, TransformerException {
        JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceList.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        // Writing the data
        InvoiceList invoiceList = new InvoiceList();
        Integer size = orderItemsIds.size();
        for (int i = 0; i < size; i++) {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setId(orderItemsIds.get(i));
            invoiceItem.setProductName(productNames.get(i));
            invoiceItem.setQuantity(quantities.get(i));
            invoiceItem.setMrp(mrp.get(i));
            invoiceItem.setSellingPrice(sellingPrices.get(i));
            invoiceList.getItems().add(invoiceItem);
        }
        invoiceList.setTotal(getTotal(sellingPrices));

        StringWriter sw = new StringWriter();

        jaxbMarshaller.marshal(invoiceList, sw);
        String xmlContent = sw.toString();

//        logger.info("XML Content >>> "+ xmlContent);
        System.out.println("XML Content: >>" + xmlContent);

        File path = new File("invoice.xml");

        //passing file instance in filewriter
        FileWriter wr = new FileWriter(path);

        //calling writer.write() method with the string
        wr.write(xmlContent);

        //flushing the writer
        wr.flush();

        //closing the writer
        wr.close();

//        sw.flush();
//
//        sw.close();
    }

    private static Double getTotal(List<String> sellingPrices){
        Double total = 0.0;
        for(String sellingPrice: sellingPrices){
            total += Double.parseDouble(sellingPrice);
        }
        return total;
    }

}
