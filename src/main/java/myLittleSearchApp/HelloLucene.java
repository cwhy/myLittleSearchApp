package myLittleSearchApp;

import java.io.IOException;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import org.json.JSONArray;
import org.json.JSONObject;

public class HelloLucene {
	static StandardAnalyzer analyzer = new StandardAnalyzer();
	static Directory index = new RAMDirectory();

    public static String search(String querystr) throws ParseException, IOException{
    	return search(querystr, 10);
    }

    public static String search(String querystr, Integer N_hits) throws ParseException, IOException{
        JSONObject out = new JSONObject();
        out.put("query", querystr);
        out.put("N_hits_queried", N_hits);
        // 2. query
        Query q = new QueryParser("title", analyzer).parse(querystr);

        // 3. search
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, N_hits);
        ScoreDoc[] hits = docs.scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
		out.put("N_hits_found", hits.length);

        JSONArray hitsList = new JSONArray();
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
			JSONObject doc = new JSONObject();
            Document d = searcher.doc(docId);
            doc.put("rank", new Integer(i + 1));
            doc.put("isbn", d.get("isbn"));
            doc.put("title", d.get("title"));
			hitsList.put(doc);
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
        }
        out.put("hits", hitsList);
		reader.close();
		return(out.toString());
    }
    public static void index(){
//    public static void main(String[] args) throws IOException, ParseException {
        // 0. Specify the analyzer for tokenizing text.
        //    The same analyzer should be used for indexing and searching

        // 1. create the index

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter w;
		try {
			w = new IndexWriter(index, config);
			addDoc(w, "Lucene in Action", "193398817");
			addDoc(w, "Lucene for Dummies", "55320055Z");
			addDoc(w, "Managing Gigabytes", "55063554A");
			addDoc(w, "The Art of Computer Science", "9900333X");
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));

        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
}