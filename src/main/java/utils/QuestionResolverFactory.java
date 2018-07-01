package utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class QuestionResolverFactory {

    public static QuestionResolver getResolver(){
        QuestionResolver resolver=new StemResolver();
        QuestionResolver resolver1=new TypeResolver();
        QuestionResolver resolver2=new SelectResolver();
        QuestionResolver resolver3=new AnswerResolver();
        QuestionResolver resolver4=new AnalyResolver();
        resolver.setNextResolver(resolver1);
        resolver1.setNextResolver(resolver2);
        resolver2.setNextResolver(resolver3);
        resolver3.setNextResolver(resolver4);
        return resolver;
    }

    public List<QuestionResolver> getResolver(HSSFRow titles){
        List<QuestionResolver> resolvers=new ArrayList<QuestionResolver>();
        String pattern="^选项[A-Z]{1}$";
        String blankPattern="^空格\\d{1,}答案$";
        SelectResolver selectResolver=null;
        AnswerResolver answerResolver=new AnswerResolver();
        for(int i=0;i<titles.getLastCellNum();i++){
            String value=titles.getCell(i).getStringCellValue();
            if("类型".equals(value)){
                resolvers.add(new TypeResolver());
                continue;
            }
            if("题干".equals(value)){
                resolvers.add(new StemResolver());
                continue;
            }
            if("答案".equals(value)){
                resolvers.add(answerResolver);
                continue;
            }
            if("试题解析".equals(value)|"解析".equals(value)){
                resolvers.add(new AnalyResolver());
                continue;
            }
            //选项匹配
            if(Pattern.matches(pattern,value)){
                if(selectResolver==null){
                    selectResolver=new SelectResolver();
                }
                resolvers.add(selectResolver);
                continue;
            }
            //填空答案匹配
            if(Pattern.matches(blankPattern,value)){
                resolvers.add(answerResolver);
            }
        }
        return resolvers;
    }

}
