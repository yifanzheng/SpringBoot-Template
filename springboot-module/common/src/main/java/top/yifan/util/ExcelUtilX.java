package top.yifan.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.core.io.ClassPathResource;
import sun.security.util.Resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * ExcelUtilX
 *
 * @author sz7v
 */
public class ExcelUtilX {

    private ExcelUtilX() {
    }

    /**
     * 获取Excel数据的二进制形式
     *
     * @param excelFileTemplate Excel 文件模板
     * @param data              数据
     * @return 返回二进制形式的Excel数据
     */
    public static byte[] getExcelDataAsByte(byte[] excelFileTemplate, List<List<String>> data) {
        ByteArrayOutputStream fileByteOut = null;
        // 生成 Excel 设置压缩率为零，不然会在生成文件的时候会报相关异常
        ZipSecureFile.setMinInflateRatio(0);
        ByteArrayInputStream excelFileTemplateInStream = new ByteArrayInputStream(excelFileTemplate);
        try (ExcelWriter writer = ExcelUtil.getReader(excelFileTemplateInStream).getWriter()) {
            writer.setCurrentRow(1);
            writer.write(CollUtil.newArrayList(data));
            // 刷新到字节流中
            fileByteOut = new ByteArrayOutputStream();
            writer.flush(fileByteOut);
            return fileByteOut.toByteArray();
        } finally {
            try {
                IOUtils.close(fileByteOut, excelFileTemplateInStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 以二进制形式获取Excel文件模板
     *
     * @param excelFileName Excel文件名
     * @return Optional<byte[]>
     */
    public static Optional<byte[]> getExcelFileTemplateAsByte(String excelFileName) {
        InputStream fileStream = null;
        ByteArrayOutputStream byteOutStream = null;
        try {
            // 读取文件流
            ClassPathResource resource = new ClassPathResource("excel-template/" + excelFileName + ".xlsx");
            fileStream = resource.getInputStream();
            // 创建字节输出流
            byteOutStream = new ByteArrayOutputStream();
            // 将输入流拷贝到字节输出流中
            IOUtils.copy(fileStream, byteOutStream);
            byte[] fileBytes = byteOutStream.toByteArray();

            return Optional.of(fileBytes);
        } catch (IOException e) {
            return Optional.empty();
        } finally {
            try {
                IOUtils.close(byteOutStream, fileStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
