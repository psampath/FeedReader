package com.solstice.feedreader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

/**
 * A SAX parser to parse the feeds and populate the
 * feedStructure objects for each entry
 * 
 * @author sampathpasupunuri
 */
public class XMLHandler extends DefaultHandler {

	private FeedStructure feedStr = new FeedStructure();
	private List<FeedStructure> rssList = new ArrayList<FeedStructure>();

	StringBuilder chars = new StringBuilder();

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		super.startElement(uri, localName, qName, atts);
		if (localName.equalsIgnoreCase("thumbnail")) {
			Log.e("attr value: ", atts.getValue("url"));
			feedStr.setImageLink(atts.getValue("url"));
		}
		chars = new StringBuilder();
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);

		if (localName.equalsIgnoreCase("title")) {
			feedStr.setTitle(chars.toString());
		} else if (localName.equalsIgnoreCase("published")) {
			feedStr.setPubDate(chars.toString());
		} else if (localName.equalsIgnoreCase("content")) {
			feedStr.setContent(chars.toString());
		}
		// TODO:Set the author name
		if (localName.equalsIgnoreCase("entry")) {
			rssList.add(feedStr);
			feedStr = new FeedStructure();
		}
	}

	public void characters(char ch[], int start, int length) {
		chars.append(new String(ch, start, length));
	}

	public List<FeedStructure> getLatestArticles(String feedUrl) {
		URL url = null;
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			url = new URL(feedUrl);
			xr.setContentHandler(this);
			xr.parse(new InputSource(url.openStream()));
		} catch (IOException e) {
			Log.d("XMLHANDLER", "IOException");
			e.printStackTrace();
		} catch (SAXException e) {
			Log.d("XMLHANDLER", "SAXException");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
		}
		return rssList;
	}
}
