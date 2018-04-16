package wordlistscraper;

/**
 * @author nnewdorf
 */
public class UpperCaseRule extends Rule{

    @Override
    public String processString(String s) {
        return s.toUpperCase();
    }
    
}
