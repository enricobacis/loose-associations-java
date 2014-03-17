package cli;

import exporter.SqliteExporter;
import gui.controllers.ControllerUtils;

import java.io.File;
import java.util.Set;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.tuples.Triplet;
import core.GreedyLoose;
import core.associations.BaseAssociations;
import core.fragments.BaseFragments;
import core.tables.SqliteTable;

public class CLI {
	
	private static Logger logger = LoggerFactory.getLogger(CLI.class);

	public static void main(String[] args) throws Exception {
		
		ArgumentParser parser = ArgumentParsers.newArgumentParser("loose-cli")
				.defaultHelp(true)
				.description("Create a Loose Association")
				.epilog("This tool has Super Cow Powers");
		
		parser.addArgument("database").help("input Sqlite database");
		parser.addArgument("output").help("output Sqlite database");
		parser.addArgument("tablename").help("input table name");
		parser.addArgument("orderby").help("orderby clause for the input");
		parser.addArgument("constraints").help("constraints list of list e.g. [[0,1,2], [1,2,3]]");
		parser.addArgument("fragments").help("fragments list of list e.g. [[0,1], [2,3]]");
		parser.addArgument("klist").help("kilst (privacy factors) e.g. [2,2]");
		parser.addArgument("--limit").type(Integer.class).help("limit clause for the input");
		parser.addArgument("--loglevel").setDefault("INFO").help("log level (TRACE, DEBUG, INFO, WARN, ERROR");
		
		try {
			Namespace ns = parser.parseArgs(args);
			
			Level level = Level.toLevel(ns.getString("loglevel"));
			org.apache.log4j.LogManager.getLogger(CLI.class).setLevel(level);
			logger.info("Logger set to: {}", level);
			
			SqliteTable table = new SqliteTable(ns.getString("database"),
					ns.getString("tablename"), ns.getString("orderby"), ns.getInt("limit"), logger);
			
			GreedyLoose loose = new GreedyLoose(table,
					ControllerUtils.getListList(ns.getString("constraints")),
					ControllerUtils.getListList(ns.getString("fragments")), logger);
			
			Triplet<BaseAssociations, BaseFragments, Set<Integer>> result = loose.associate(
					ControllerUtils.getList(ns.getString("klist"), Integer.class));
			
			String preSQL = "CREATE TABLE info (key VARCHAR(128) PRIMARY KEY, value VARCHAR(128));" +
			        "INSERT INTO info VALUES ('time', "    + loose.getElapsedTime()   + ");" +
			        "INSERT INTO info VALUES ('dropped', " + result.getThird().size() + ");" ;
			
			File output = new File(ns.getString("output"));
			if (output.exists())
				output.delete();
			
			SqliteExporter exporter = new SqliteExporter(table, result.getSecond(), result.getFirst(), logger);
			exporter.export(output.getAbsolutePath(), preSQL);
			
	    } catch (ArgumentParserException e) {
	    	parser.handleError(e);
	        System.exit(1);
	    }
	}

}
