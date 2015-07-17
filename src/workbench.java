import java.io.File;
import java.util.Iterator;

import data.*;
import gui.*;

import org.garret.perst.*;


/**
 * TUHH SWP 2014/15 - MachineLearner v1.0
 * 
 *  Team 008
 * @author Sazan Hoti, Famara Kassama, Anton Semjonov
 *
 */


public class workbench extends Persistent {
	
	public static void main(String[] args) {
		String dbPathArgument = null;
		Storage db = null;
		if (args.length >=1 && args[0] != null) {
			dbPathArgument = args[0];
		} else {
			dbPathArgument = "learndata.dbs";
		}
		try {
			// DATABASE
			File dbFile = new File(dbPathArgument);
			dbFile.createNewFile();
			String dbPath = dbFile.getCanonicalPath();
			db = StorageFactory.getInstance().createStorage();
			db.open(dbPath, 100);
			
			// Open or Create LearnData
			LearnData learndata = (LearnData)db.getRoot();
			if (learndata == null) {
				learndata = new LearnData(db);
				db.setRoot(learndata);
			}
			
			Iterator<Example> i = learndata.intKeyIndex.iterator();
			
			while(i.hasNext()){
				Example ex = (Example)i.next();
				learndata.addExample(ex);
			}
			
			// Start workbench and GUI
			Gui window = new Gui(db, learndata, "tuhh swp2014 - team 008 - MachineLearner v1.0");
			window.frame.setVisible(true);
			
		} catch (Exception e) {
			if(db != null && db.isOpened())
				db.close();
			
			e.printStackTrace();
		}
	}
	
}