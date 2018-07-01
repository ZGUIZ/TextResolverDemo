package bean;

/**
 * 无法解析的文字
 */
public class UnableResolve implements Resolveable{
    private String text;

    public UnableResolve() {
    }

    public UnableResolve(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString(){
        return text;
    }
}
