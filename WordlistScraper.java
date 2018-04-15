package wordlistscraper;

/**
 * @author nnewdorf
 */
public class WordlistScraper {
    public static void main(String[] args) {
        PageParser p = new PageParser();
        p.parsePage(p.getSource("https://en.wikipedia.org/wiki/List_of_American_television_programs", ""));
    }
    
}
