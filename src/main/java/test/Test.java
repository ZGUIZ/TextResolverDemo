package test;

import bean.ExamQuestion;
import bean.QuestionAnswer;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Test {

    @org.junit.Test
    public void testStemResolver(){
        QuestionResolver resolver=new StemResolver();
        ExamQuestion question= (ExamQuestion) resolver.resolveToken("[题干]初次申领的机动车驾驶证的有效期为6年。",new ExamQuestion());
        System.out.println(question.getContent());
    }

    @org.junit.Test
    public void Pattern(){
        String str="A:间歇制动";
        String pattern="^[A-Z]{1}:*\\S{1,}";
        boolean res=Pattern.matches(pattern,str);
        System.out.println(res);

        String pattern1="^选项[A-Z]{1}$";
        res=Pattern.matches(pattern1,"选项C");
        System.out.println(res);

        String pattern2="^空格\\d{1,}答案$";
        res=Pattern.matches(pattern2,"空格1答案");
        System.out.println(res);
        String str1="{BLANK1:口鼻}";

    }

    @org.junit.Test
    public void testAnserResolver(){
        SelectResolver resolver=new SelectResolver();
        ExamQuestion question=new ExamQuestion();
        resolver.resolveToken("[选项]{A:继续加速行驶},{B:稍向右侧行驶，保证横向安全距离},{C:靠道路中心行驶},{D:加速向右侧让路}",question);
        Map<String, QuestionAnswer> answer=question.getAnswers();
        for(String key:answer.keySet()){
            System.out.println(key+"="+answer.get(key).getAnswer());
        }
    }

    @org.junit.Test
    public void testFactory(){
        QuestionResolver resolver=QuestionResolverFactory.getResolver();
        String str="[题干]防抱死制动系统（ABS）在什么情况下可以最大限度发挥制动器效能?\n" +
                "[类型]单选\n" +
                "[选项]{A:间歇制动},{B:持续制动},{C:紧急制动},{D:缓踏制动踏板}\n" +
                "[答案]C\n" +
                "[解析]ABS的目的就是为了防止刹车的时候一脚踩死，导致翻车什么的。 不过刹车刹死就比较慢（速度N快的时候），效果等于踩下刹车，再松下刹车，反复几次！";
        ExamQuestion question=new ExamQuestion();
        Scanner scanner=new Scanner(str);
        while(scanner.hasNextLine()){
            String line=scanner.nextLine();
            System.out.println("test:"+line);
            resolver.resolveToken(line,question);
        }
        System.out.println(question.getContent());
        System.out.println(question.getAnalysis());
        System.out.println(question.getType().getType());
        Map<String,QuestionAnswer> answerMap=question.getAnswers();
        for(String key:answerMap.keySet()) {
            System.out.println(answerMap.get(key).getAnswer()+"\t"+answerMap.get(key).getCorrect());
        }
    }

    private String getTestString(){
//        return "[题干]每年\"中小学生安全教育日\"是在[BLANK1]。\n" +
//                "[类型]填空\n" +
//                "[答案]{BLANK1:3月份最后一周的周一}\n" +
//                "[解析]";
//        return "[题干]初次申领的机动车驾驶证的有效期为6年。\n" +
//                "[类型]判断\n" +
//                "[答案]正确\n" +
//                "[解析]";
//        return "[题干]如何做一名合格的驾驶员？\n" +
//                "[类型]问答\n" +
//                "[答案]首先要有法治观念、要有熟练的驾驶技术并对自己驾驶的汽车了如指掌、要养成良好的驾驶习惯，文明驾驶、要充分照顾交通弱势群体的利益、要有平和的驾驶心态等等\n" +
//                "[解析]";
//        return "[题干]乘车人不得向[BLANK1]抛洒物品，不得有[BLANK2]驾驶人安全驾驶的行为\n" +
//                "[类型]填空\n" +
//                "[答案]{BLANK1:车外},{BLANK2:影响}\n" +
//                "[解析]";
//        return "[题干]使用其他机动车号牌、行驶证的一次记3分。\n" +
//                "[类型]判断\n" +
//                "[答案]错误\n" +
//                "[解析]\n";
        return "[题干]在道路上驾驶自行车、电动自行车应当遵守下列规定：\n" +
                "[类型]多选\n" +
                "[选项]{A:不得牵引、攀扶车辆或者被其他车辆牵引},{B:不得双手离把但可以手中持物},{C:不得扶身并行、互相追逐或者曲折竞驶},{D:不得在道路上骑独轮自行车或者2人以上骑行的自行车}\n" +
                "[答案]ACD\n" +
                "[解析]";
    }

    @org.junit.Test
    public void testFactory2(){
        QuestionResolver resolver=QuestionResolverFactory.getResolver();
        String str=getTestString();
        ExamQuestion question=new ExamQuestion();
        Scanner scanner=new Scanner(str);
        while(scanner.hasNextLine()){
            String line=scanner.nextLine();
            System.out.println("test:"+line);
            resolver.resolveToken(line,question);
        }
        System.out.println(question.getContent());
        System.out.println(question.getAnalysis());
        System.out.println(question.getType().getType());
        Map<String,QuestionAnswer> answerMap=question.getAnswers();
        for(String key:answerMap.keySet()) {
            System.out.println(answerMap.get(key).getAnswer()+"\t"+answerMap.get(key).getCorrect());
        }
    }

    @org.junit.Test
    public void testFileResolver(){
        List<ExamQuestion> questions=FileResolver.resolveTxtFile("F:\\迅雷下载\\ExamTest.txt");
        for(ExamQuestion question:questions){
            System.out.println("context="+question.getContent());
            System.out.println("analysis="+question.getAnalysis());
            System.out.println("type="+question.getType().getType());
            Map<String,QuestionAnswer> answers=question.getAnswers();
            for(String key:answers.keySet()){
                System.out.println(key+"\t"+answers.get(key).getAnswer()+"\t"+answers.get(key).getCorrect());
            }
        }
    }

    @org.junit.Test
    public void testExcelReadFile(){
        InputStream is;
        try{
            is=new FileInputStream("F:\\迅雷下载\\template_questions_excel\\判断题_批量导入模板.xls");
            HSSFWorkbook hssfWorkbook=new HSSFWorkbook(is);
            for(int num=0;num<hssfWorkbook.getNumberOfSheets();num++){
                HSSFSheet xssfSheet=hssfWorkbook.getSheetAt(num);
                if(xssfSheet==null){
                    continue;
                }
                for(int row=0;row<=xssfSheet.getLastRowNum();row++){
                    System.out.println("第"+row+"行");
                    HSSFRow xssfRow=xssfSheet.getRow(row);
                    if(xssfRow!=null){
                        for(int i=0;i<xssfRow.getLastCellNum();i++){
                            System.out.println(xssfRow.getCell(i));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
