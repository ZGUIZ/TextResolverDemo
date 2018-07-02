package fileresolveutils;
/**
 * 文件解析
 * creator: zguiz
 */

import bean.ExamQuestion;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileResolver {
    /**
     * 解析txt文档
     * @param path
     * @return
     */
    public static List<ExamQuestion> resolveTxtFile(String path){
        List<ExamQuestion> questions=new ArrayList<ExamQuestion>();
        FileInputStream inputStream=null;
        InputStreamReader reader=null;
        Scanner scanner=null;
        try {
            //读取文件
            inputStream=new FileInputStream(path);
            reader=new InputStreamReader(inputStream,"UTF-8");
            scanner=new Scanner(reader);

            ExamQuestion question=null;
            String line=null;
            QuestionResolver resolver=QuestionResolverFactory.getResolver();  //获得解析器
            while(scanner.hasNextLine()){
                line=scanner.nextLine().trim();
                line=compatWindows(line);
                //如果是首次创建，则先压入List
                if("".equals(line.trim())||question==null){
                    if(question!=null){
                        questions.add(question);
                    }
                    question=new ExamQuestion();
                }

                //解析问题
                resolver.resolveToken(line,question);
            }
            //如果最后没有空行
            if(!"".equals(line)){
                questions.add(question);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if(scanner!=null){
                scanner.close();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        return questions;
    }

    //去除windows txt以utf-8编码文档中开头第一个BOM
    private static String compatWindows(String line){
        if (!"".equals(line)&&line.charAt(0) == 65279) {
            return line.substring(1);
        }
        return line;
    }

    public static List<ExamQuestion> resolveXlsFile(String path){
        List<ExamQuestion> questions=new ArrayList<ExamQuestion>();
        InputStream is;
        try{
            is=new FileInputStream(path);
            HSSFWorkbook hssfWorkbook=new HSSFWorkbook(is);

            //获取文件所有的表所有表
            for(int page=0;page<hssfWorkbook.getNumberOfSheets();page++) {
                HSSFSheet xssfSheet = hssfWorkbook.getSheetAt(page);
                //没有数据
                if(xssfSheet.getLastRowNum()<=1){
                    continue;
                }

                HSSFRow titles=xssfSheet.getRow(0);     //获得标题
                List<QuestionResolver> resolvers=QuestionResolverFactory.getResolver(titles);  //获得解析器
                //获取行
                for (int row = 1; row <= xssfSheet.getLastRowNum(); row++) {
                    HSSFRow xssfRow = xssfSheet.getRow(row);
                    if (xssfRow != null) {
                        ExamQuestion question=new ExamQuestion();
                        for (int i = 0; i < xssfRow.getLastCellNum(); i++) {
                            resolvers.get(i).resolveCell(titles.getCell(i),xssfRow.getCell(i),question);
                        }
                        questions.add(question);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
