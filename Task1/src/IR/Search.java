package IR;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

import Data.GetData;
/*
 * created by Aishwarya Dhage(adhage) and Praneta Paithankar(ppaithan)
 */
public class Search {
	static float total_doc ;

	public static void main(String[] args) {
		try {
			GetData  data=new GetData();
			ReviewExtractor reviewExtractor=new ReviewExtractor();
			IndexReader reader= DirectoryReader.open(FSDirectory.open(Paths.get(System.getProperty("user.dir"), "index")));
			IndexSearcher searcher = new IndexSearcher(reader);
			HashMap<String, List<String>> users = data.getIRUserData();
			searcher.setSimilarity(new BM25Similarity()); 
			Analyzer analyzer = new StandardAnalyzer();
			QueryParser parser = new QueryParser("TEXT", analyzer); 
			PrintWriter pw =new PrintWriter(new File("IR.csv"));
			String s;
			int rank;
			Query query ;
			TopDocs topDocs;
			ScoreDoc[] docs ;
			for (String user_id :users.keySet()) {
				String nounReview=reviewExtractor.getNouneview(users.get(user_id));
				rank=1;
				if(nounReview != null && !nounReview.isEmpty()) {
					query = parser.parse(QueryParser.escape(nounReview));
					topDocs = searcher.search(query, 50);
					docs = topDocs.scoreDocs;
					for (int i = 0; i < docs.length; i++) {		
							Document doc = searcher.doc(docs[i].doc);
							s=user_id+","+doc.get("DOCNO")+","+rank+","+
									docs[i].score;
							rank++;
							pw.write(s+"\n");
						}
					}
			}
			pw.close();
			reader.close();
			System.out.println("Done");

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
