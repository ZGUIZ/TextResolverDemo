package fileresolveutils;
/**
 * 解析器父类
 * creator: zguiz
 */

import bean.ExamQuestion;
import bean.Resolveable;
import org.apache.poi.hssf.usermodel.HSSFCell;

public abstract class QuestionResolver {
     static final String KEY="result";
     protected QuestionResolver nextResolver;  //下一个解析器

     /**
      * 解析txt文档接口
      * @param str
      * @param examQuestion
      * @return
      */
     abstract public Resolveable resolveToken(String str,Resolveable examQuestion);

     abstract public Resolveable resolveCell(HSSFCell title,HSSFCell cell, ExamQuestion question);

     /**
      * 设置下一个解析器
      * @param nextResolver
      */
     void setNextResolver(QuestionResolver nextResolver) {
          this.nextResolver = nextResolver;
     }

     /**
      * 添加解析器
      * @param resolver
      */
     public void addResolver(QuestionResolver resolver){
          resolver.setNextResolver(this.nextResolver);
          this.nextResolver=resolver;
     }
}
