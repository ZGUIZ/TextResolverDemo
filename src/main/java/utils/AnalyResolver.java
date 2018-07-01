package utils;
/**
 * 试题分析解析器
 * creator: zguiz
 */

import bean.ExamQuestion;
import bean.Resolveable;
import bean.UnableResolve;

public class AnalyResolver extends QuestionResolver{
    public Resolveable resolveToken(String str, Resolveable examQuestion) {
        int start=str.indexOf("[");
        int end=str.indexOf("]");
        //错误的格式
        if(start!=0||end<1){
            return new UnableResolve(str);
        }

        if("解析".equals(str.substring(start+1,end))){
            ExamQuestion question=(ExamQuestion) examQuestion;
            question.setAnalysis(str.substring(end+1));
            return question;
        }

        //如果没有下一个解析器，则返回无法解析
        if(nextResolver==null){
            return new UnableResolve(str);
        }

        return nextResolver.resolveToken(str,examQuestion);
    }

    public Resolveable resolveCell(String str,ExamQuestion question){
        question.setAnalysis(str);
        return question;
    }
}
