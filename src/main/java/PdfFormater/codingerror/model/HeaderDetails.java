package PdfFormater.codingerror.model;

import PdfFormater.codingerror.util.ConstantUtil;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceGray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HeaderDetails {
    String invoiceTitle= ConstantUtil.INVOICE_TITLE;
    String invoiceNoText=ConstantUtil.INVOICE_NO_TEXT;
    String invoiceDateText=ConstantUtil.INVOICE_DATE_TEXT;
    String invoiceNo=ConstantUtil.EMPTY;
    String invoiceDate=ConstantUtil.EMPTY;
    private Color borderColor = new DeviceGray(0.75f);

    public HeaderDetails setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
        return this;
    }

    public HeaderDetails setInvoiceNoText(String invoiceNoText) {
        this.invoiceNoText = invoiceNoText;
        return this;
    }

    public HeaderDetails setInvoiceDateText(String invoiceDateText) {
        this.invoiceDateText = invoiceDateText;
        return this;
    }

    public HeaderDetails setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
        return this;
    }

    public HeaderDetails setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public HeaderDetails setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }
    public HeaderDetails build()
    {
        return  this;
    }
}
