package com.learning.google_search_api;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class App {
    
    
    static String google = "http://www.google.com/search?q=";
    static String charset = "UTF-8";
    static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    private static final String EXCEL_INPUT_FILE_LOCATION= "65000companieslist.xls";
    private static final String EXCEL_OUTPUT_FILE_LOCATION= "output-file-lat.xls";
    
    
    public static void main( String[] args ) throws Exception {
       
        ArrayList<String> list = getCompanyName();
        System.out.println(getCompanyName().size());
        HashMap<String, String> details=new HashMap<String ,String>();
        for (String obj: list) {
            if(obj != "") {
                details.put(obj, getContactNumber(obj));
                System.out.println(obj + " : " + getContactNumber(obj));
                
                
                int millis = ThreadLocalRandom.current().nextInt(500, 15000 + 1);
                Thread.sleep(millis);
            }
            else {
                break;
            }
        }
        System.out.println("Uploaded");
        writeExcel(details);
    }
    public static String getContactNumber (String searchQuery) throws Exception {
        Document document = Jsoup.connect(google + URLEncoder.encode(searchQuery, charset)).userAgent(userAgent).get();
        Elements rightColumn = document.select("div#rhscol");
        Elements no = rightColumn.select("div._eFb").select("span._Xbe._ZWk.kno-fv");
        //String website = rightColumn.select("._ldf>a").attr("href");
        return no.text();
    }
    public static ArrayList<String> getCompanyName() throws Exception {
        ArrayList<String> companyNames = new ArrayList<String>();
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new java.io.File(EXCEL_INPUT_FILE_LOCATION));
            Sheet sheet = workbook.getSheet(0);
            for (int i = 0; i < 110; i++) {
                Cell cell1 = sheet.getCell(1, i);
                System.out.println("Comp name "+cell1.getContents());
                if(cell1.getContents() == "" || cell1.getContents() == null) {
                    break;
                }
                companyNames.add(cell1.getContents());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
        return companyNames;
    }
    public static void writeExcel(HashMap<String,String> companyDetails) {
        WritableWorkbook writeworkbook = null;
        try {
            writeworkbook = Workbook.createWorkbook(new java.io.File(EXCEL_OUTPUT_FILE_LOCATION));
            // create an Excel sheet
            WritableSheet excelSheet = writeworkbook.createSheet("Sheet 1", 0);
            // add something into the Excel sheet
            int row = 0;
            for(Map.Entry m:companyDetails.entrySet()) {
                int column = 0;
                System.out.println("Col : " + column + " | row : " + row + " | " + m.getKey() );
                Label companyName = new Label(column, row, m.getKey().toString());
                excelSheet.addCell(companyName);
                column++;
                System.out.println("Col : " + column + " | row : " + row + " | " + m.getValue());
                Label contactNo = new Label(column, row, m.getValue().toString());
                excelSheet.addCell(contactNo);
                row++;
            }
            writeworkbook.write();
            System.out.println("Completed..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writeworkbook != null) {
                try {
                    writeworkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
