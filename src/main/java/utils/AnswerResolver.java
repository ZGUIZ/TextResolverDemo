package utils;
/**
 * 试题答案解析器
 * creator：zguiz
 */

import bean.ExamQuestion;
import bean.QuestionAnswer;
import bean.Resolveable;
import bean.UnableResolve;

import java.util.HashMap;
import java.util.Map;

public class AnswerResolver extends QuestionResolver{

    public Resolveable resolveToken(String str, Resolveable examQuestion) {
        int start=str.indexOf("[");
        int end=str.indexOf("]");
        //错误的格式
        if(start!=0||end<1){
            return new UnableResolve(str);
        }

        if("答案".equals(str.substring(start+1,end))){
            ExamQuestion question=(ExamQuestion) examQuestion;

            question.setAnswers(adjustAnswer(str.substring(end+1),question));

            return question;
        }

        //如果没有下一个解析器，则返回无法解析
        if(nextResolver==null){
            return new UnableResolve(str);
        }

        return nextResolver.resolveToken(str,examQuestion);
    }

    private Map<String,QuestionAnswer> adjustAnswer(String str, ExamQuestion question){
        Map<String,QuestionAnswer> answers=question.getAnswers();
        if(answers==null){
            answers=new HashMap<String, QuestionAnswer>();
        }
        if ("单选".equals(question.getType().getType())){
            if(answers.size()>0){
                QuestionAnswer answer=answers.get(str.trim());
                answer.setCorrect(1);
            }
            return answers;
        }
        if("填空".equals(question.getType().getType())){   //解析填空题
            int start=str.indexOf("{");
            int end=str.indexOf("}");
            while(!"".equals(str.trim())){
                String content=str.substring(start+1,end);
                QuestionAnswer answer=new QuestionAnswer();
                answer.setAnswer(content);
                answers.put(content.substring(0,content.indexOf(":")),answer);
                str=str.substring(end+1);
                start=str.indexOf("{");
                end=str.indexOf("}");
            }
            return answers;
        }
        if("多选".equals(question.getType().getType())){
            if(answers.size()>0){
                for(int i=0;i<str.length();i++){
                    QuestionAnswer answer=answers.get(String.valueOf(str.charAt(i)));
                    answer.setCorrect(1);
                }
            }
        }
        QuestionAnswer answer = new QuestionAnswer();
        answer.setAnswer(str);
        answer.setCorrect(1);
        answers.put(super.KEY, answer);
        return answers;
    }



}
