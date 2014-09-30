package cutepics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CutePicPanel extends JPanel {

	private static final long serialVersionUID = -2185169295668499671L;

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		String imageUrl = getImageUrl();
		if (imageUrl == null){
			return;
		}
		try {
			URL url = new URL(imageUrl);
			BufferedImage image = ImageIO.read(url.openStream());
			g.drawImage(image, 0, 0, null);
			g.drawString(imageUrl, 2, 190);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getImageUrl(){
		try {
			String imgurString = getHtmlFromImgur();

			ArrayList<String> matches = new ArrayList<String>();

			String[] splits = imgurString.split("<");

			for(String item : splits){

				Pattern r = Pattern.compile("//i.(imgur.com/[\\w\\d]+.(gif|jpg|png)?)\"");

				Matcher m = r.matcher(item); 
				if (m.find()){
					matches.add("http://i." + m.toMatchResult().group(1));
				}
			}

			int random = (int)(Math.random() * matches.size());
			return matches.get(random);
		} catch (IOException e) {
		}
		return null;
	}

	private String getHtmlFromImgur() throws IOException{
		URL url = new URL("http://imgur.com/r/aww/top");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		String html = "";
		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null)
		{
			str.append(line);
		}
		in.close();
		html = str.toString();
		return html;
	}
}
