package Assessment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiScrape {

	public static void main(String[] args) throws IOException {
		System.out.print("Enter Link: ");

		Scanner scan = new Scanner(System.in);
		String linkURL = scan.nextLine();
		int cycle = 0;
		List<String> linkListAll = new ArrayList<String>();

		if (isValidWikiURL(linkURL)) {
			Boolean isValidCycle;
			
			List<String> linkList = new ArrayList<String>();
			

			do {
				System.out.print("Enter number of cycles (1-3): ");
				cycle = scan.nextInt();
				if (cycle > 0 && cycle < 4) {
					isValidCycle = true;
				} else {
					isValidCycle = false;
				}
			} while (!isValidCycle);

			for(int i = 0;i<cycle;i++) {
				linkList = getLinksonPage(linkURL);
				if(linkList.isEmpty()) {
					i=cycle;
				}else {
					linkURL = linkList.get(i);
				}
				linkListAll.addAll(linkList);
			}
						
		} else {
			System.out.println("Link is a not valid Wiki link.");
		}
		scan.close();
		System.out.println("\nNumber of Cycles: " + cycle);
		System.out.println("Total Links: " + linkListAll.size());
		
		System.out.println("\nLinks:");
		for(String link : linkListAll) {
			System.out.println(link);
		}
	}
	
	private static List<String> getLinksonPage(String linkURL) throws IOException {
		URL pageURL = new URL(linkURL);
		InputStream is = null;
		BufferedReader br;
		Scanner inPage = new Scanner(pageURL.openStream());
		List<String> links = new ArrayList<String>();
		int counter = 1;
		
		is = pageURL.openStream();
		br = new BufferedReader(new InputStreamReader(is));
		String line;
		
		while ((line = br.readLine()) != null && counter <= 10) {
			// line = inPage.next();
			if (line.contains("href=\"/wiki/")) {
				int from = line.indexOf("href=\"");
				line = line.substring(from + 6);

				int to = line.indexOf("\"");

				String curLink = line.substring(0, to);
				
				if (links.contains("https://en.wikipedia.org" + curLink) == false
						&& !(curLink.contains("Main_Page") || curLink.contains("Help:Contents")
								|| curLink.contains("Help:Contents") || curLink.contains("Special:Search")
								|| curLink.contains("Help:Introduction") || curLink.contains("Special:MyContributions"))
						&& (linkURL.compareTo("https://en.wikipedia.org" + curLink) != 0)) {
					links.add("https://en.wikipedia.org" + curLink);
					counter += 1;
				}
			}

			
		}
		inPage.close();
		return links;
	}

	private static boolean isValidWikiURL(String linkURL) {
		String urlRegex = "^(http|https)://en.wikipedia.org[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
		Pattern pattern = Pattern.compile(urlRegex);
		Matcher m = pattern.matcher(linkURL);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

}
