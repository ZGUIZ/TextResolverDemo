package fileresolveutils;
/**
 * 试题类型解析器
 * creator: zguiz
 */

import bean.*;
import org.apache.poi.hssf.usermodel.HSSFCell;

import java.util.Map;


public class TypeResolver extends QuestionResolver {
    public Resolveable resolveToken(String str, Resolveable examQuestion) {
        int start=str.indexOf("[");
        int end=str.indexOf("]");
        //错误的格式
        if(start!=0||end<1){
            return new UnableResolve(str);
        }

        if("类型".equals(str.substring(start+1,end))){
            ExamQuestion question=(ExamQuestion) examQuestion;

            //查询数据库并设置ID，需要改动
            QuestionType type=new QuestionType();
            type.setType(str.substring(end+1));

            question.setType(type);
            adjustAnswer(type,question.getAnswers());
            return question;
        }

        //如果没有下一个解析器，则返回无法解析
        if(nextResolver==null){
            return new UnableResolve(str);
        }

        return nextResolver.resolveToken(str,examQuestion);
    }

    //解析xls元素
    public Resolveable resolveCell(HSSFCell title, HSSFCell cell, ExamQuestion question) {
        QuestionType type=new QuestionType();
        type.setType(cell.getStringCellValue());
        question.setType(type);
        return question;
    }

    //调整答案格式
    public void adjustAnswer(QuestionType type, Map<String,QuestionAnswer> answers){
        if((answers!=null||answers.size()>0)&&"判断".equals(type.getType())){
            QuestionAnswer answer=answers.get(super.KEY);
            answer.setCorrect(answer.getAnswer().equals("正确")?1:0);
            answer.setAnswer(null);
            return;
        }
    }
}
