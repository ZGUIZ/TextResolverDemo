package utils;
/**
 * 试题类型解析器
 * creator: zguiz
 */

import bean.*;


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

            return question;
        }

        //如果没有下一个解析器，则返回无法解析
        if(nextResolver==null){
            return new UnableResolve(str);
        }

        return nextResolver.resolveToken(str,examQuestion);
    }
}
