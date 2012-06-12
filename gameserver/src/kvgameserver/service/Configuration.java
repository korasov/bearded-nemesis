package kvgameserver.service;

import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Configuration {

	private static final String CONFIGFILE = "config.xml";
	private static Configuration instance = null;
	private static HashMap<String, String> configdata = null;

	private Configuration() {
		configdata = new HashMap<String, String>();
		//read configuration here
		readConfiguration();
	}

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	private void readConfiguration() {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			sp.parse(CONFIGFILE, new SaxHandler());
		} catch (Exception e) { e.printStackTrace(); }
	}

	private class SaxHandler extends DefaultHandler {
		String key = null;
		String value = null;
		public void startElement(String uri, String loaclName, String qName,
				Attributes attributes) throws SAXException {
			key = qName;
		}
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			value = new String(ch, start, length);
		}
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (qName == key) {
				configdata.put(key, value);
			}
		}
	}

	public String get(String key) {
		return configdata.get(key);
	}
}
