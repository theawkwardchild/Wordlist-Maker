package wordlistscraper;

/**
 * @author nnewdorf
 */
public class LowerCaseRule extends Rule{

    @Override
    public String processString(String s) {
        return s.toLowerCase();
    }
    
}
