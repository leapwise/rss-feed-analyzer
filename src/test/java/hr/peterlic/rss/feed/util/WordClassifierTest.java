package hr.peterlic.rss.feed.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

public class WordClassifierTest
{

	@Test
	public void givenEnglishDictionary_whenLemmatize_thenLemmasAreDetected() throws Exception
	{

		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
		String[] tokens = tokenizer.tokenize("John has a sister named Penny.");

		InputStream inputStreamPOSTagger = new FileInputStream("models" + File.separator + "en-pos-maxent.bin");
		POSModel posModel = new POSModel(inputStreamPOSTagger);
		POSTaggerME posTagger = new POSTaggerME(posModel);
		String tags[] = posTagger.tag(tokens);

		assertThat(tags).contains("NNP", "VBZ", "DT", "NN", "VBN", "NNP", ".");
	}

}
