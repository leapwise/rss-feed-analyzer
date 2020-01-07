package hr.peterlic.rss.feed.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

/**
 * Class that retrieves nouns (both, proper nouns and nouns) from titles from
 * RSS feed article. <br>
 *
 * @author Ana Peterlic
 */
@Slf4j
@Component
public class WordClassifier
{

	/**
	 * Use en-pos-maxent.bin file to detect part of speech (POS).
	 * 
	 * @param title
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public List<String> retrievePartOfSpeech(String title) throws IOException, ClassNotFoundException
	{
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
		String[] tokens = tokenizer.tokenize(title.replaceAll("[^a-zA-Z0-9\\s]", ""));
		InputStream inputStreamPOSTagger = new FileInputStream("models" + File.separator + "en-pos-maxent.bin");
		POSModel posModel = new POSModel(inputStreamPOSTagger);
		POSTaggerME posTagger = new POSTaggerME(posModel);
		String[] tags = posTagger.tag(tokens);

		List<String> nouns = filterNounsOnly(tokens, tags);

		removeSpecificNouns(nouns);

		return nouns;
	}

	/**
	 * Filters nouns from titles. <br>
	 * NNP - proper noun NN - noun
	 *
	 * @param tokens
	 * @param tags
	 * @return
	 */
	private List<String> filterNounsOnly(String[] tokens, String[] tags)
	{
		List<String> nouns = new ArrayList<>();
		for (int i = 0; i < tags.length; i++)
		{
			if (ConstantsUtils.PROPER_NOUN_TAG_VALUE.equals(tags[i]) || ConstantsUtils.NOUN_TAG_VALUE.equals(tags[i]))
			{
				nouns.add(tokens[i].toLowerCase());
			}
		}
		return nouns;
	}

	/**
	 * Remove "news" and * "new" from the list of nouns - there is no need to
	 * analyse occurrence of this * words since it will be returned in title like
	 * "Bolton says he will testify in * Senate impeachment trial if subpoenaed -
	 * Fox News".
	 * 
	 * @param nouns
	 */
	private void removeSpecificNouns(List<String> nouns)
	{
		List<String> nounsToRemove = new ArrayList<>();
		nounsToRemove.add(ConstantsUtils.NOUN_WITH_VALUE_NEW);
		nounsToRemove.add(ConstantsUtils.NOUN_WITH_VALUE_NEWS);
		nouns.removeAll(nounsToRemove);
	}

}
