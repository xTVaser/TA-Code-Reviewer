import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**

 */
public class PDFTester {

    public static void main(String[] args) {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream("Anchor2.pdf"));

            document.open();


            Anchor anchor1 = new Anchor("This is an internal link");
            anchor1.setName("link1");
            document.add(anchor1);
            document.newPage();
            Anchor anchor2 = new Anchor("Click here to jump to the internal link");
            anchor2.setReference("#link1");
            document.add(anchor2);


            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}