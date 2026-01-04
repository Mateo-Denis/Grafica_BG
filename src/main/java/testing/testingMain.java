package testing;


import PdfFormater.JobBudgetPDFConverter;

public class testingMain {
    private final JobBudgetPDFConverter jobBudgetPDFConverter = new JobBudgetPDFConverter();

    public static void main(String[] args) {
        try {
            JobBudgetPDFConverter.generarFactura();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

