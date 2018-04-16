package wordlistscraper;

/**
 * @author nnewdorf
 */
public class WhitespaceToDashRule extends Rule{

    @Override
    public String processString(String s) {
        return s.replaceAll("\\s", "-");
    }
    
}
