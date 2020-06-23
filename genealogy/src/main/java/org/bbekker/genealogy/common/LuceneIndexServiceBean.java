package org.bbekker.genealogy.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.EntityManagerFactory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

public class LuceneIndexServiceBean {

	private FullTextEntityManager fullTextEntityManager;

	public LuceneIndexServiceBean(EntityManagerFactory entityManagerFactory) {
		fullTextEntityManager = Search.getFullTextEntityManager(entityManagerFactory.createEntityManager());
	}

	public void triggerIndexing() {
		try {
			fullTextEntityManager.createIndexer().startAndWait();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void resetIndex(String luceneIndexPath) {

		try {
			Path path = Paths.get(luceneIndexPath);
			File file = path.toFile();
			if (!file.exists()) {
				// If the folder does not exist, create
				file.mkdirs();
			}
			Directory directory = FSDirectory.open(path);

			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE);
			IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

			// Clear index
			indexWriter.deleteAll();
			indexWriter.commit();

			indexWriter.close();
			directory.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
