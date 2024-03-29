package com.gunicode.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryparser.classic.QueryParser;

import java.nio.file.*;

public class Example_01
{
    private Analyzer analyzer = new StandardAnalyzer();
    private String indexPath = "";
    private String fieldName = "title";

    private Example_01(String path) {
        indexPath = path;
    }

    private void addDocs(String[] dataArr) throws Exception {
        IndexWriterConfig iwconf = new IndexWriterConfig(analyzer);
        Path path = FileSystems.getDefault().getPath(indexPath);
        Directory store = new SimpleFSDirectory(path);
        IndexWriter iw = new IndexWriter(store, iwconf);
        for (String data : dataArr) {
            Document doc = new Document();
            doc.add(new TextField(fieldName, data, Field.Store.YES));
            iw.addDocument(doc);
        }
        iw.close();
    }

    private void index()
    {
        try {
            addDocs(new String[]{"hi this is doc1",
                    "doc2: this contains information about our lecture information retrieval",
                    "doc3: this is our first example"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchAndDisplay(String searchText) throws Exception{
        System.out.println("Query: " + searchText);
        //query
        QueryParser qParser = new QueryParser(fieldName, analyzer);
        Query q = qParser.parse(searchText);
        //search
        Path path = FileSystems.getDefault().getPath(indexPath);
        DirectoryReader reader = DirectoryReader.open(new SimpleFSDirectory(path));
        IndexSearcher is = new IndexSearcher(reader);
        int topHits = 10;
        TopDocs hits = is.search(q, topHits);
        //display
        for(ScoreDoc hit : hits.scoreDocs){
            Document doc = is.doc(hit.doc);
            System.out.println(doc.get(fieldName));
        }
        //is.close();
    }

    public static void main(String[] args)
    {
        Example_01 hl = new Example_01("example_01_output");
        try {
            hl.index();
            hl.searchAndDisplay("information");
            hl.searchAndDisplay("lecture");
            hl.searchAndDisplay("example");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
