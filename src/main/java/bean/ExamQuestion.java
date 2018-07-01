package bean;

import java.util.List;
import java.util.Map;

public class ExamQuestion implements Resolveable {

    private int id;
    private String content;
    private String source;
    private int status;
    private String analysis;
    private int level;
    private QuestionType type;

    private Map<String,QuestionAnswer> answers;  //答案集合

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Map<String,QuestionAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String,QuestionAnswer> answers) {
        this.answers = answers;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }
}
