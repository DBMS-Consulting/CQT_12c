package com.dbms.util;


import com.dbms.csmq.view.hierarchy.GenericTreeNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;


public class POIExportUtil {
    public POIExportUtil() {
        super();
    }

    public static void addFormRow(HSSFSheet worksheet, int rowCount, String label, String value, int lableColSpan,
                                  int valueColSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell labelCell = row.createCell((short)0);
        for (int i = 1; i < lableColSpan; i++) {
            row.createCell((short)i);
        }
        labelCell.setCellValue(label);
        worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, lableColSpan - 1));

        HSSFCell valueCell = row.createCell((short)lableColSpan);
        for (int i = lableColSpan + 1; i < (lableColSpan + valueColSpan); i++) {
            row.createCell((short)i);
        }
        valueCell.setCellValue(value);
        worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, lableColSpan,
                                                       (lableColSpan + valueColSpan)));
    }
    
    public static void addFormRow(HSSFWorkbook workbook, HSSFSheet worksheet, int rowCount, String label, String value, int lableColSpan,
                                  int valueColSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell labelCell = row.createCell((short)0);
        for (int i = 1; i < lableColSpan; i++) {
            row.createCell((short)i);
        }
        labelCell.setCellValue(label);
        worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, lableColSpan - 1));

        HSSFCell valueCell = row.createCell((short)lableColSpan);
        for (int i = lableColSpan + 1; i < (lableColSpan + valueColSpan); i++) {
            row.createCell((short)i);
        }
        valueCell.setCellValue(value);
        CellStyle wrapText=workbook.createCellStyle();
        wrapText.setWrapText(true);
        valueCell.setCellStyle(wrapText);
        row.setHeightInPoints(rowHeight(value));     
        worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, lableColSpan,
                                                       (lableColSpan + valueColSpan)));
    }
    
    private static int rowHeight(String value){
        double upperCount = 0;
        int numberOfLines = 0;
        if(value != null){
        for(int i=0; i<value.length();i++){
            if(Character.isUpperCase(value.charAt(i))){
                upperCount = upperCount + 1;
            }
        }
        
        double lengthOfWord = value.length();
        double smallCount = lengthOfWord - upperCount;
        numberOfLines = numberOfLines + ((int) Math.ceil(smallCount/40));
        numberOfLines = numberOfLines + ((int) Math.ceil(upperCount/30));
        if(numberOfLines == 0){
          return 15;  
        }else{
        return numberOfLines*15 + 10;
        }
        }else{
            return 15;  
        }
    }

    public static void addHeaderTextRow(HSSFSheet worksheet, int rowCount, String label, int colSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell labelCell = row.createCell((short)0);
        labelCell.setCellValue(label);
        for (int i = 1; i < colSpan; i++) {
            row.createCell((short)i);
        }
        worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, colSpan));
        HSSFCellStyle cellStyle = worksheet.getWorkbook().createCellStyle();
        Font font = worksheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short)14);
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(new HSSFColor.BLACK().getIndex());
        cellStyle.setFont(font);
        labelCell.setCellStyle(cellStyle);
    }
    
    public static void addHeaderTextRow1(HSSFSheet worksheet, int rowCount, String label, int colSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell labelCell = row.createCell((short)0);
        labelCell.setCellValue(label);
        for (int i = 1; i < colSpan; i++) {
            row.createCell((short)i);
        }
        worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, colSpan));
        HSSFCellStyle cellStyle = worksheet.getWorkbook().createCellStyle();
        Font font = worksheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short)14);
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(new HSSFColor.BLACK().getIndex());
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        labelCell.setCellStyle(cellStyle);
        row.setHeightInPoints(rowHeight(label)); 
    }

    public static void addDescHeaderTextRow(HSSFSheet worksheet, int rowCount, String label, int colSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell labelCell = row.createCell((short)0);
        labelCell.setCellValue(label);
        for (int i = 1; i < colSpan; i++) {
            row.createCell((short)i);
        }
        worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, colSpan));
        HSSFCellStyle cellStyle = worksheet.getWorkbook().createCellStyle();
        Font font = worksheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short)11);
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(new HSSFColor.BLACK().getIndex());
        cellStyle.setFont(font);
        labelCell.setCellStyle(cellStyle);
    }

    public static void addSimpleDescTextRow(HSSFSheet worksheet, int rowCount, String label, int colSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell labelCell = row.createCell((short)0);
        labelCell.setCellValue(label);
        for (int i = 1; i < colSpan; i++) {
            row.createCell((short)i);
        }

        rowCount++;
        worksheet.createRow((short)rowCount);
        worksheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount, 0, colSpan));

    }
    
    public static void addSimpleDescTextRow1(HSSFSheet worksheet, int rowCount, String label, int colSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell labelCell = row.createCell((short)0);
        labelCell.setCellValue(label);
        for (int i = 1; i < colSpan; i++) {
            row.createCell((short)i);
        }
        if((label != null) && (label.length() >0)){
        row.setHeightInPoints((label.length()/100)*13 + 15); 
        HSSFCellStyle cellStyle = worksheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        labelCell.setCellStyle(cellStyle);
        }
        rowCount++;
        worksheet.createRow((short)rowCount);
        worksheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount, 0, colSpan));

    }
    
    public static void addSimpleDescTextRow2(HSSFSheet worksheet, int rowCount, String label, int colSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell labelCell = row.createCell((short)0);
        labelCell.setCellValue(label);
        for (int i = 1; i < colSpan; i++) {
            row.createCell((short)i);
        }
        if((label != null) && (label.length() >0)){
        row.setHeightInPoints((label.length()/100)*20 + 15); 
        HSSFCellStyle cellStyle = worksheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        labelCell.setCellStyle(cellStyle);
        }
        rowCount++;
        worksheet.createRow((short)rowCount);
        worksheet.addMergedRegion(new CellRangeAddress(rowCount - 1, rowCount, 0, colSpan));

    }

    public static void addSimpleTextRow(HSSFSheet worksheet, int rowCount, String label, int colSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell labelCell = row.createCell((short)0, colSpan);
        labelCell.setCellValue(label);
        for (int i = 1; i < colSpan; i++) {
            row.createCell((short)i);
        }
        worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, colSpan));
    }

    public static void addHierarchyTableHeaderRow(HSSFSheet worksheet, int rowCount, String[] headerNames,
                                                  int[] headerColSpan) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell cell = null;

        CellStyle styleNew = worksheet.getWorkbook().createCellStyle();
        Font font = worksheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(new HSSFColor.BLACK().getIndex());
        styleNew.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleNew.setFillPattern(CellStyle.FINE_DOTS);
        styleNew.setFont(font);

        for (int i = 0; i < headerNames.length; i++) {
            if (i == 0) {
                cell = row.createCell((short)i);
                for (int j = 1; j < 4; j++) {
                    row.createCell((short)j);
                }
                worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            } else {
                cell = row.createCell((short)(4 + i));
            }

            cell.setCellValue(headerNames[i]);
            cell.setCellStyle(styleNew);
        }
    }

    public static void addHierarchyTableValueRow(HSSFSheet worksheet, int rowCount, String[] hierValArr,
                                                 int[] hierValColSpanArr, int depth) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell cell = null;
        String dots = "";
        for (int i = 0; i < hierValArr.length; i++) {
            if (i == 0) {
                cell = row.createCell((short)i);
                for (int j = 0; j < depth; j++) {
                    dots = dots + "....";
                }
                cell.setCellValue(dots + hierValArr[i]);
                for (int j = 1; j < 4; j++) {
                    row.createCell((short)j);
                }
                worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
            } else {
                cell = row.createCell((short)(4 + i));
                cell.setCellValue(hierValArr[i]);
            }
        }
    }

    public static void addImpactedHierarchyTableValueRow(HSSFSheet worksheet, int rowCount, String[] hierValArr,
                                                         int[] hierValColSpanArr, int depth, HSSFCellStyle cellStyle) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        HSSFCell cell = null;
        String dots = "";
        for (int i = 0; i < hierValArr.length; i++) {

            if (i == 0) {
                cell = row.createCell((short)i);
                for (int j = 0; j < depth; j++) {
                    dots = dots + "....";
                }
                cell.setCellValue(dots + hierValArr[i]);

                for (int j = 1; j < 4; j++) {
                    row.createCell((short)j);
                }
                worksheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 4));
                cell.setCellStyle(cellStyle);
            } else {
                cell = row.createCell((short)(4 + i));
                cell.setCellValue(hierValArr[i]);
            }
        }
    }

    public static Map<String, HSSFCellStyle> formVersionImpactCellStyles(HSSFSheet worksheet) {
        Map<String, HSSFCellStyle> versionImpactCellStylesMap = new HashMap<String, HSSFCellStyle>();
        String[] impactStyles =
        { "Impact_NMQ_0", "Impact_NMQ_1010", "Impact_NMQ_1080", "Impact_NMQ_1040", "Impact_NMQ_1070",
          "Impact_NMQ_1110", "Impact_NMQ_1020", "Impact_NMQ_1030", "Impact_NMQ_1050" };
        for (String impactStyle : impactStyles) {
            versionImpactCellStylesMap.put(impactStyle, getVersionImpactCellStyle(worksheet, impactStyle));
        }
        return versionImpactCellStylesMap;
    }

    public static HSSFCellStyle getVersionImpactCellStyle(HSSFSheet worksheet, String impactStyle) {
        HSSFCellStyle cellStyle = worksheet.getWorkbook().createCellStyle();
        Font font = worksheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");

        if (!"Impact_NMQ_0".equals(impactStyle)) {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }

        if ("Impact_NMQ_1010".equals(impactStyle)) {
            font.setColor(new HSSFColor.BLUE().getIndex());
        } else if ("Impact_NMQ_1080".equals(impactStyle)) {
            font.setColor(new HSSFColor.BLACK().getIndex());
            font.setItalic(true);
        } else if ("Impact_NMQ_1040".equals(impactStyle)) {
            font.setColor(new HSSFColor.ORANGE().getIndex());
        } else if ("Impact_NMQ_1070".equals(impactStyle)) {
            font.setColor(new HSSFColor.GREEN().getIndex());
        } else if ("Impact_NMQ_1110".equals(impactStyle)) {
            font.setColor(new HSSFColor.WHITE().getIndex());
            cellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
            cellStyle.setFillPattern(CellStyle.FINE_DOTS);
        } else if ("Impact_NMQ_1020".equals(impactStyle)) {
            font.setColor(new HSSFColor.RED().getIndex());
        } else if ("Impact_NMQ_1030".equals(impactStyle)) {
            font.setColor(new HSSFColor.GREY_25_PERCENT().getIndex());
        } else if ("Impact_NMQ_1050".equals(impactStyle)) {
            font.setColor(new HSSFColor.PINK().getIndex());
        }
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static void formHierarchyTableValueRow(HSSFSheet worksheet, Integer rowCount, int[] hierValColSpanArr,
                                                  GenericTreeNode node, int depth) {
        String scope = node.getTermScope() != null ? node.getTermScope().toString() : "";
        String[] valArr =
        { node.getTerm(), node.getDictContentCode(), node.getLevelName(), node.getTermCategory(), node.getTermWeight(),
          scope };
        addHierarchyTableValueRow(worksheet, rowCount, valArr, hierValColSpanArr, depth);
        if (node.getChildren() != null && node.getChildren().size() > 0) {
            GenericTreeNode cnode = null;
            int nextDepth = depth++;
            for (int i = 0; i < node.getChildren().size(); i++) {
                cnode = (GenericTreeNode)node.getChildren().get(i);
                formHierarchyTableValueRow(worksheet, rowCount++, hierValColSpanArr, cnode, nextDepth);
            }
        }
    }

    public static void addEmptyRow(HSSFSheet worksheet, int rowCount) {
        HSSFRow row = worksheet.createRow((short)rowCount);
    }

    public static void addImageRow(HSSFSheet worksheet, int rowCount) {
        HSSFRow row = worksheet.createRow((short)rowCount);
        for (int i = 0; i < 6; i++) {
            row.createCell(i);
        }
    }

    public static InputStream loadResourceAsStream(final String resourceName) {
        InputStream input = null;
        try {
            input = new FileInputStream(resourceName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return input;
    }

    public static void writeImageTOExcel(Sheet sheet, InputStream imageInputStream) throws IOException {
        byte[] bytes = IOUtils.toByteArray(imageInputStream);
        int pictureIdx = sheet.getWorkbook().addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        imageInputStream.close();
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setRow1(0);
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize(); //DBMS
        //pict.resize(13.0); //NMAT
    }
}
