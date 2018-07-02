package utils;
/**
 * 试题题干解析器
 * creator: zguiz
 */

import bean.ExamQuestion;
import bean.Resolveable;
import bean.UnableResolve;
import org.apache.poi.hssf.usermodel.HSSFCell;

public class StemResolver extends QuestionResolver{

    public Resolveable resolveToken(String str, Resolveable examQuestion) {
        int start=str.indexOf("[");
        int end=str.indexOf("]");

        //错误的格式
        if((start>0&&!str.substring(0,start).trim().equals(""))||end<1){
            return new UnableResolve(str);
        }

        if("题干".equals(str.substring(start+1,end))){
            ExamQuestion question=(ExamQuestion) examQuestion;
            question.setContent(str.substring(end+1));
            return question;
        }

        //如果没有下一个解析器，则返回无法解析
        if(nextResolver==null){
            return new UnableResolve(str);
        }

        return nextResolver.resolveToken(str,examQuestion);
    }

    public Resolveable resolveCell(HSSFCell title, HSSFCell cell, ExamQuestion question) {
        question.setContent(cell.getStringCellValue());
        return question;
    }

}
