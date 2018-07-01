package utils;
/**
 * 解析器父类
 * creator: zguiz
 */

import bean.Resolveable;

public abstract class QuestionResolver {
     static final String KEY="result";
     protected QuestionResolver nextResolver;  //下一个解析器
     abstract public Resolveable resolveToken(String str,Resolveable examQuestion);

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
