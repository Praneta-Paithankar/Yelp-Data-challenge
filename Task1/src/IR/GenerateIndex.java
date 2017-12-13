package IR;
/*
 * created by Aishwarya Dhage(adhage)and Praneta Paithankar(ppaithan)
 */
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import Data.GetData;

public class GenerateIndex {
	//create restaurant document
	public static Document createDocument(String docid,String text) {

		Document doc = new Document();
		doc.add(new StringField("DOCNO", docid, Field.Store.YES));
		doc.add(new TextField("TEXT", text, Field.Store.YES));
		return doc;

	}
	//Generate index
	public static void main(String args[]) {
		try {
			GetData  data=new GetData();
			ReviewExtractor reviewExtractor=new ReviewExtractor();
			HashMap<String, String> businesses = data.getIRData();
			Directory dir = FSDirectory.open(Paths.get(System.getProperty("user.dir"), "Index"));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			IndexWriter indexwriter = new IndexWriter(dir, iwc);
			
			for (String business_id : businesses.keySet()) {
				String review=businesses.get(business_id);
				review=reviewExtractor.getNouneview(review);
				indexwriter.addDocument(createDocument(business_id,review));
			}
			indexwriter.forceMerge(1);
			indexwriter.commit();
			indexwriter.close();
			System.out.println("Index is created.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
