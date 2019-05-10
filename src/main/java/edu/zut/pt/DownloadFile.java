package edu.zut.pt;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DownloadFile {

    public void download(String filePath, HttpServletResponse httpServletResponse) {

            //设置文件名，根据业务需要替换成要下载的文件名
            String fileProjectName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
            //设置文件路径
            String realProjectPath = filePath;
            //读到流中，文件的存放路径
            InputStream inputStream = null;

            try {
                inputStream = new FileInputStream(realProjectPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //设置输出格式
            httpServletResponse.reset();
            try {
                httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileProjectName, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            //循环去除流中的数据
            byte[] b = new byte[100];
            int len;
            try {

                while ((len = inputStream.read(b)) > 0) {
                    httpServletResponse.getOutputStream().write(b, 0, len);
                }
                httpServletResponse.getOutputStream().close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
