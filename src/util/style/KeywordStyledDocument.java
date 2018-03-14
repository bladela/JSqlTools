package util.style;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

public class KeywordStyledDocument extends DefaultStyledDocument  {
	private static final long serialVersionUID = 1L;
	private Style _defaultStyle;
	private Style _cwStyle;
	private Style _objStyle;
	private static List<String> reservedWords=new ArrayList<String>();
	private static List<String> objWords=new ArrayList<String>();


	public KeywordStyledDocument(Style defaultStyle, Style cwStyle, Style objStyle) {
		_defaultStyle =  defaultStyle;
		_cwStyle = cwStyle;
		_objStyle = objStyle;
	}

	public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offset, str, a);
		refreshDocument();
	}

	public void remove (int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		refreshDocument();
	}

	private synchronized void refreshDocument() throws BadLocationException {
		String text = getText(0, getLength());
		final List<HiliteWord> list = processWords(text,"sql");
		final List<HiliteWord> listObj= processWords(text,"obj");

		setCharacterAttributes(0, text.length(), _defaultStyle, true);   
		for(HiliteWord word : list) {
			int p0 = word._position;
			setCharacterAttributes(p0, word._word.length(), _cwStyle, true);
			
		}
		for(HiliteWord word : listObj) {
			int p0 = word._position;
			setCharacterAttributes(p0, word._word.length(), _objStyle, true);
			
		}
		
	}       

	private static List<HiliteWord> processWords(String content,String type) {
		content += " ";
		List<HiliteWord> hiliteWords = new ArrayList<HiliteWord>();
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();

		for(int index=0; index < data.length; index++) {
			char ch = data[index];
			if(!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_')) {
				lastWhitespacePosition = index;
				if(word.length() > 0) {
					if(isReservedWord(word)&&type.equals("sql")) {
						hiliteWords.add(new HiliteWord(word,(lastWhitespacePosition - word.length())));
					}
					if(isObjWord(word)&&type.equals("obj")) {
						hiliteWords.add(new HiliteWord(word,(lastWhitespacePosition - word.length())));
					}
					word="";
				}
			}
			else {
				word += ch;
			}
		}
		return hiliteWords;
	}
	
	private static final Boolean isObjWord(String word) {
		return objWords.contains(word.toLowerCase().trim());
	}

	private static final Boolean isReservedWord(String word) {
		/*return(word.toUpperCase().trim().equals("CROSS") || 
				word.toUpperCase().trim().equals("CURRENT_DATE") ||
				word.toUpperCase().trim().equals("CURRENT_TIME") ||
				word.toUpperCase().trim().equals("CURRENT_TIMESTAMP") ||
				word.toUpperCase().trim().equals("DISTINCT") ||
				word.toUpperCase().trim().equals("EXCEPT") ||
				word.toUpperCase().trim().equals("EXISTS") ||
				word.toUpperCase().trim().equals("FALSE") ||
				word.toUpperCase().trim().equals("FETCH") ||
				word.toUpperCase().trim().equals("FOR") ||
				word.toUpperCase().trim().equals("FROM") ||
				word.toUpperCase().trim().equals("FULL") ||
				word.toUpperCase().trim().equals("GROUP") ||
				word.toUpperCase().trim().equals("HAVING") ||
				word.toUpperCase().trim().equals("INNER") ||
				word.toUpperCase().trim().equals("INTERSECT") ||
				word.toUpperCase().trim().equals("IS") ||
				word.toUpperCase().trim().equals("JOIN") ||
				word.toUpperCase().trim().equals("LIKE") ||
				word.toUpperCase().trim().equals("LIMIT") ||
				word.toUpperCase().trim().equals("MINUS") ||
				word.toUpperCase().trim().equals("NATURAL") ||
				word.toUpperCase().trim().equals("NOT") ||
				word.toUpperCase().trim().equals("NULL") ||
				word.toUpperCase().trim().equals("OFFSET") ||
				word.toUpperCase().trim().equals("ON") ||
				word.toUpperCase().trim().equals("ORDER") ||
				word.toUpperCase().trim().equals("PRIMARY") ||
				word.toUpperCase().trim().equals("ROWNUM") ||
				word.toUpperCase().trim().equals("SELECT") ||
				word.toUpperCase().trim().equals("SYSDATE") ||
				word.toUpperCase().trim().equals("SYSTIME") ||
				word.toUpperCase().trim().equals("SYSTIMESTAMP") ||
				word.toUpperCase().trim().equals("TODAY") ||
				word.toUpperCase().trim().equals("TRUE") ||
				word.toUpperCase().trim().equals("UNION") ||
				word.toUpperCase().trim().equals("UNIQUE") ||
				word.toUpperCase().trim().equals("WHERE"));*/
		
		return reservedWords.contains(word.toLowerCase().trim());
				
	}

	public static void setReservedWords(List<String> words)
	{
		reservedWords=words.stream().collect(Collectors.toList());
	}
	
	public static void addReservedWord(String word)
	{
		reservedWords.add((String)word);
	}
	
	public static void setObjWords(List<String> words)
	{
		objWords=words.stream().collect(Collectors.toList());
	}
	
	public static void addObjWords(String word)
	{
		objWords.add(word);
	}
	
	


}

class HiliteWord {

	int _position;  
	String _word;

	public HiliteWord(String word, int position) {
		_position = position;   
		_word = word;
	}
}
