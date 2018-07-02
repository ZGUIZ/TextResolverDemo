package utils;
/**
 * 试题选项解析器
 * creator: zguiz
 */

import bean.ExamQuestion;
import bean.QuestionAnswer;
import bean.Resolveable;
import bean.UnableResolve;
import org.apache.poi.hssf.usermodel.HSSFCell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SelectResolver extends QuestionResolver{
    public Resolveable resolveToken(String str, Resolveable examQuestion) {
        int start=str.indexOf("[");
        int end=str.indexOf("]");
        //错误的格式
        if(start!=0||end<1){
            return new UnableResolve(str);
        }
        try {
            if("选项".equals(str.substring(start+1,end))){
                ExamQuestion question=(ExamQuestion) examQuestion;
                question.setAnswers(adjustAnswer(str.substring(end+1),question));
                return question;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果没有下一个解析器，则返回无法解析
        if(nextResolver==null){
            return new UnableResolve(str);
        }

        return nextResolver.resolveToken(str,examQuestion);
    }

    //解析表内数据
    public Resolveable resolveCell(HSSFCell title, HSSFCell cell, ExamQuestion question) {
        String value=cell.getStringCellValue();
        if(value==null||"".equals(value.trim())){  //如果没有内容，则不做解析
            return question;
        }
        Map<String,QuestionAnswer> answerMap=question.getAnswers();
        if(answerMap==null){
            answerMap=new HashMap<String, QuestionAnswer>();
            question.setAnswers(answerMap);
        }
        String t=title.getStringCellValue();
        String pattern="^选项[A-Z]{1}$";
        if(Pattern.matches(pattern,t)){  //匹配选项
            String option=String.valueOf(t.charAt(t.length()-1));  //获取到选项需要，如A
            QuestionAnswer answer=new QuestionAnswer();
            answer.setAnswer(value);
            answerMap.put(option,answer);
        }
        return question;
    }

    //解析各个选项
    private Map<String,QuestionAnswer> adjustAnswer(String str, ExamQuestion question) throws Exception{
        Map<String,QuestionAnswer> answers=question.getAnswers();
        if(answers==null){
            answers=new HashMap<String, QuestionAnswer>();
        }

        int start=str.indexOf("{");
        int end=str.indexOf("}");

        String pattern="^[A-Z]{1}:*\\S{1,}";
        while(!str.trim().equals("")){
            String option=str.substring(start+1,end);
            boolean isMatch=Pattern.matches(pattern,option);
            if(!isMatch){
                throw new Exception("格式错误!");
            }
            int potinIndex=option.indexOf(":");
            String key=option.substring(0,potinIndex);
            String content=option.substring(potinIndex+1);
            QuestionAnswer answer=new QuestionAnswer();
            answer.setAnswer(content);
            answers.put(key,answer);

            str=str.substring(end+1);
            start=str.indexOf("{");
            end=str.indexOf("}");
        }
        if(answers.containsKey(super.KEY)){  //如果答案已经输入
            QuestionAnswer answer=answers.get(super.KEY);
            answers.get(answer.getAnswer()).setCorrect(1);  //设置为正确答案
        }
        return answers;
    }
}
