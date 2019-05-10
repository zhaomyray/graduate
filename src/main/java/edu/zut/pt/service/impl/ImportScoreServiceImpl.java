//package edu.zut.pt.service.impl;
//
//import edu.zut.pt.service.ImportScoreService;
//import edu.zut.pt.service.StuScoreService;
//import org.apache.poi.ss.usermodel.BorderStyle;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class ImportScoreServiceImpl implements ImportScoreService {
//
//    public CellStyle getColumnTopStyle(Workbook workbook){
//        CellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setBorderBottom(BorderStyle.THIN);
//        cellStyle.setBorderLeft(BorderStyle.THIN);
//        cellStyle.setBorderRight(BorderStyle.THIN);
//        cellStyle.setBorderTop(BorderStyle.THIN);
//        //设置自动换行
//        cellStyle.setWrapText(false);
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);
//
//    }
//
//    @Override
//    public void importScore(List<StuScoreService> list, HttpServletResponse httpServletResponse) throws IOException {
//
//    }
//}
