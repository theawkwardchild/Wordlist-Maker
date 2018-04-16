package wordlistscraper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author nnewdorf
 */
public class PageParser {

    private final ArrayList<Rule> ruleList = new ArrayList();

    public void ListWriter() {

    }

    /**
     * Get the source of a webpage
     *
     * @param baseURL The URL of the page minus any identifying
     * @param pageParameter
     * @return s
     */
    public String getSource(String baseURL, String pageParameter) {
        String webPage = "";
        try {
            // Make a URL to the web page
            URL url = new URL(baseURL + pageParameter);

            // Get the input stream through URL Connection
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = null;

            // read each line and write to System.out
            while ((line = br.readLine()) != null) {
                webPage += line;
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(PageParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PageParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return webPage;
    }

    /**
     * Parse internet lists from page source and return as ArrayList to be
     * processed by rules for file writing
     *
     * @param source
     * @return
     */
    public void parsePage(String source) {
        String showTitle;
        int showYear;

        String dataAnchor; // a spot of the source code that appears near data
        dataAnchor = "id=\".24\"";

        int sourceIndex = 0;
        sourceIndex = source.indexOf(dataAnchor);
        sourceIndex = source.indexOf("title=\"", sourceIndex);
        sourceIndex = source.indexOf("title=\"", sourceIndex + 1);
       
       // Path file = Paths.get("show-lists-all.txt");
        int x = 0;
        while (source.indexOf("title=\"", sourceIndex) != -1 && x++ < 3500) {
            ArrayList<String> list = new ArrayList<>();

            ArrayList<Integer> years = new ArrayList<>();
            sourceIndex = source.indexOf("title=\"", sourceIndex);
            sourceIndex = source.indexOf(">", sourceIndex);

            showTitle = source.substring(sourceIndex + 1, source.indexOf("<", sourceIndex + 1)).replaceAll("\\s", "");

            sourceIndex = source.indexOf("(", sourceIndex);

            try {
                showYear = Integer.parseInt(source.substring(sourceIndex + 1, sourceIndex + 5));
            } catch (NumberFormatException e) {
                continue;
            }
            years.add(showYear);
            if (source.charAt(sourceIndex + 5) == '–') { //if multiple years are present
                int endYear;
                if (source.charAt(sourceIndex + 6) == '1' || source.charAt(sourceIndex + 6) == '2') { //through present
                    endYear = 2018;
                } else { //through a year
                    try {
                        endYear = Integer.parseInt(source.substring(sourceIndex + 6, sourceIndex + 10));
                    } catch (NumberFormatException e) {
                        //write out what there is 
                        list.add(showTitle);
                        list.add(showTitle.toLowerCase());
                        list.add(showTitle + showYear);
                        list.add(showTitle.toLowerCase() + showYear);
                        for (Rule rule : ruleList) {
                            list.add(rule.processString(showTitle));
                        }
                        continue;
                    }
                }

                for (int i = showYear + 1; i <= endYear; i++) {
                    years.add(i);
                }
                if(showTitle.contains("&amp;")) {
                    showTitle = showTitle.replaceAll("&amp;", "&");
                }
                list.add(showTitle);
                list.add(showTitle.toLowerCase());
                for (Integer year : years) {
                    list.add(showTitle + year);
                    list.add(showTitle.toLowerCase() + year);
                }
                for (Rule rule : ruleList) {
                    list.add(rule.processString(showTitle));
                }
            }

            try (FileWriter fw = new FileWriter("show-list-all.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                for (String string : list) {
                    out.println(string);
                }
                
            } catch (IOException e) {
                System.out.println(e);
            }
        }

//  return list;
    }

    /**
     * add rules to ruleList to create more password possibilities
     *
     * @param rule
     */
    public void addRule(Rule rule) {
        this.ruleList.add(rule);
    }
}
