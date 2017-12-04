package IR;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class ReviewExtractor {
	public String getNouneview(List<String> originalReview) {
		InputStream tokenModelIn = null;
		InputStream posModelIn = null;
		String nounReview="";
		try {

			tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			POSModel posModel = new POSModel(posModelIn);
			POSTaggerME posTagger = new POSTaggerME(posModel);
			for (String review:originalReview) {
				String tokens[] = tokenizer.tokenize(review);
				String tags[] = posTagger.tag(tokens);
				for(int i=0;i<tokens.length;i++)   {
					String res = tags[i];
					if(res.equals("NN")||res.equals("NNP")||res.equals("JJ")) {
						String adj=tokens[i];
						nounReview+=adj+" ";
					}
				}        
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (tokenModelIn != null) {
				try {
					tokenModelIn.close();
				}
				catch (IOException e) {
				}
			}
			if (posModelIn != null) {
				try {
					posModelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		return nounReview;
	}

	public String getNouneview(String review) {
		InputStream tokenModelIn = null;
		InputStream posModelIn = null;
		String nounReview="";
		try {

			tokenModelIn = new FileInputStream("en-token.bin");
			TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
			Tokenizer tokenizer = new TokenizerME(tokenModel);
			posModelIn = new FileInputStream("en-pos-maxent.bin");
			POSModel posModel = new POSModel(posModelIn);
			POSTaggerME posTagger = new POSTaggerME(posModel);
			String tokens[] = tokenizer.tokenize(review);
			String tags[] = posTagger.tag(tokens);
			for(int i=0;i<tokens.length;i++)   {
				String res = tags[i];
				if(res.equals("NN")||res.equals("NNP")||res.equals("JJ")) {
					String adj=tokens[i];
					nounReview+=adj+" ";
				}
			}        
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (tokenModelIn != null) {
				try {
					tokenModelIn.close();
				}
				catch (IOException e) {
				}
			}
			if (posModelIn != null) {
				try {
					posModelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		return nounReview;
	}
	

}
