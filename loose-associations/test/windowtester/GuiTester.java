package windowtester;

import java.io.File;
import java.util.Locale;

import javax.swing.JTextArea;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.swing.UITestCaseSwing;
import com.windowtester.runtime.swing.condition.WindowDisposedCondition;
import com.windowtester.runtime.swing.condition.WindowShowingCondition;
import com.windowtester.runtime.swing.locator.JButtonLocator;
import com.windowtester.runtime.swing.locator.JComboBoxLocator;
import com.windowtester.runtime.swing.locator.JMenuItemLocator;
import com.windowtester.runtime.swing.locator.JRadioButtonLocator;
import com.windowtester.runtime.swing.locator.JTextComponentLocator;
import com.windowtester.runtime.swing.locator.LabeledTextLocator;
import com.windowtester.runtime.swing.locator.NamedWidgetLocator;

public class GuiTester extends UITestCaseSwing {
	
	File resources = new File("test", "resources");
	File temp = new File(resources, "temp");

	public GuiTester() {
		super(gui.LooseApp.class);
		deleteRecursive(temp);
	}
	
	@Before
	protected void setUp() throws Exception {
		Locale.setDefault(new Locale("de", "DE"));
		WT.setLocaleToCurrent();
		temp.mkdirs();
	}
	
	private void deleteRecursive(File file) {
		if (file.isDirectory())
			for(File child: file.listFiles())
				deleteRecursive(child);
		file.delete();
	}
	
	@After
	protected void tearDown() throws Exception {
		deleteRecursive(temp);
	};
	
	@Test
	public void testRandomProblem() throws Exception {
		IUIContext ui = getUI();
		ui.click(new JMenuItemLocator("Problems/New random problem"));
		
		ui.click(new LabeledTextLocator("number of tuples"));
		ui.enterText("100");
		ui.click(new LabeledTextLocator("number of attributes"));
		ui.enterText("4");
		ui.click(new LabeledTextLocator("maximum value"));
		ui.enterText("20");
		ui.click(new LabeledTextLocator("fragments"));
		ui.enterText("[[0,1],[2,3]]");
		ui.click(new LabeledTextLocator("constraints"));
		ui.enterText("[[0,1,2,3]]");
		ui.click(new LabeledTextLocator("k list"));
		ui.enterText("[2,3]");
		
		ui.click(new NamedWidgetLocator("outputH2"));
		
		ui.click(new NamedWidgetLocator("chooseOutput"));
		ui.wait(new WindowShowingCondition("Open"));
		ui.enterText(temp.getAbsolutePath());
		ui.click(new JButtonLocator("Open"));
		ui.wait(new WindowDisposedCondition("Open"));
		
		ui.click(new JComboBoxLocator("DEBUG"));
		
		ui.click(new JButtonLocator("Start"));
		ui.wait(new JButtonLocator("Start").isEnabled());
		String result = new JTextComponentLocator(JTextArea.class).getText(ui);
		assertTrue(result.contains("second scan done"));
		assertTrue(result.contains("Exported"));
		
		ui.click(new JRadioButtonLocator("sqlite"));
		
		ui.click(new JButtonLocator("Start"));
		ui.wait(new JButtonLocator("Start").isEnabled());
		result = new JTextComponentLocator(JTextArea.class).getText(ui);
		assertTrue(result.contains("second scan done"));
		assertTrue(result.contains("Exported"));
		
		ui.click(new JButtonLocator("x"));
	}
	
	public void testRealProblem() throws Exception {
		IUIContext ui = getUI();
		
		ui.click(new JMenuItemLocator("Problems/New real problem"));		

		ui.click(new NamedWidgetLocator("inputSqlite"));
		
		ui.click(new NamedWidgetLocator("chooseInput"));
		ui.wait(new WindowShowingCondition("Open"));
		ui.enterText(new File(resources, "ipums100.db").getAbsolutePath());
		ui.click(new JButtonLocator("Open"));
		ui.wait(new WindowDisposedCondition("Open"));
		
		ui.click(new LabeledTextLocator("table"));
		ui.enterText("ipums");
		ui.click(new LabeledTextLocator("order by"));
		ui.enterText("educ");
		
		ui.click(new NamedWidgetLocator("outputSqlite"));
		
		ui.click(new NamedWidgetLocator("chooseOutput"));
		ui.wait(new WindowShowingCondition("Open"));
		ui.enterText(temp.getAbsolutePath());
		ui.click(new JButtonLocator("Open"));
		ui.wait(new WindowDisposedCondition("Open"));
		
		ui.click(new LabeledTextLocator("fragments"));
		ui.enterText("[[region, statefip, age, sex, marst, ind, incwage, inctot], [educ, occ, hrswork, health]]");
		
		ui.click(new LabeledTextLocator("constraints"));
		ui.enterText("[[statefip, ind, educ, occ, health], [age, sex, marst, educ, occ, health]]");
		
		ui.click(new LabeledTextLocator("k list"));
		ui.enterText("[2, 2]");
		
		ui.click(new JComboBoxLocator("DEBUG"));
		
		ui.click(new JButtonLocator("Start"));
		ui.wait(new JButtonLocator("Start").isEnabled());
		String result = new JTextComponentLocator(JTextArea.class).getText(ui);
		assertTrue(result.contains("second scan done"));
		assertTrue(result.contains("Exported"));
		
		ui.click(new JButtonLocator("x"));
	}
	
	@Test
	public void testQuerier() throws Exception {
		IUIContext ui = getUI();
		ui.click(new JMenuItemLocator("Utilities/New querier"));
		
		ui.click(new NamedWidgetLocator("chooseQueryDatabase"));
		ui.wait(new WindowShowingCondition("Open"));
		ui.enterText(new File(resources, "ipums100.loose.db").getAbsolutePath());
		ui.click(new JButtonLocator("Open"));
		ui.wait(new WindowDisposedCondition("Open"));
		
		ui.click(new JComboBoxLocator("DEBUG"));
		ui.click(new JButtonLocator("Connect"));
		
		ui.click(new NamedWidgetLocator("textQuery"));
		ui.enterText("select * from associations limit 10");
		ui.click(new JButtonLocator("run"));
		ui.wait(new JButtonLocator("run").isEnabled());
		
		ui.click(new NamedWidgetLocator("textQuery"));
		ui.enterText("select ?educ from ? limit 10");
		ui.click(new JButtonLocator("run"));
		ui.wait(new JButtonLocator("run").isEnabled());

		ui.click(new NamedWidgetLocator("textQuery"));
		ui.enterText("select ?educ, ?age from ? limit 10");
		ui.click(new JButtonLocator("run"));
		ui.wait(new JButtonLocator("run").isEnabled());
		
		ui.click(new JButtonLocator("Disconnect"));
		ui.click(new JButtonLocator("x"));
	}
	
	@Test
	public void testSaveAndLoad() throws Exception {
		IUIContext ui = getUI();
		ui.click(new JMenuItemLocator("Problems/New random problem"));
		ui.click(new LabeledTextLocator("number of tuples"));
		ui.enterText("1000");
		ui.click(new LabeledTextLocator("number of attributes"));
		ui.enterText("4");
		ui.click(new LabeledTextLocator("maximum value"));
		ui.enterText("50");
		ui.click(new LabeledTextLocator("fragments"));
		ui.enterText("[[0,1],[2,3]]");
		ui.click(new LabeledTextLocator("constraints"));
		ui.enterText("[[0,1,2,3]]");
		ui.click(new LabeledTextLocator("k list"));
		ui.enterText("[2,3]");
		ui.click(new JRadioButtonLocator("sqlite"));

		ui.click(new NamedWidgetLocator("chooseOutput"));
		ui.wait(new WindowShowingCondition("Open"));
		ui.enterText(temp.getAbsolutePath());
		ui.click(new JButtonLocator("Open"));
		ui.wait(new WindowDisposedCondition("Open"));
		
		ui.click(new JMenuItemLocator("Problems/Save"));
		ui.wait(new WindowShowingCondition("Save"));
		ui.enterText(new File(temp, "save").getAbsolutePath());
		ui.click(new JButtonLocator("Save"));
		ui.wait(new WindowDisposedCondition("Save"));
		
		String result = new JTextComponentLocator(JTextArea.class).getText(ui);
		result.contains("Exported");
		
		ui.click(new JButtonLocator("x"));
		ui.click(new JMenuItemLocator("Problems/Open"));
		ui.wait(new WindowShowingCondition("Open"));
		ui.enterText(new File(temp, "save.xml").getAbsolutePath());
		ui.click(new JButtonLocator("Open"));
		ui.wait(new WindowDisposedCondition("Open"));
		
		assertTrue(new LabeledTextLocator("number of tuples").getText(ui).equals("1000"));
		assertTrue(new LabeledTextLocator("number of attributes").getText(ui).equals("4"));
		assertTrue(new LabeledTextLocator("maximum value").getText(ui).equals("50"));
		assertTrue(new LabeledTextLocator("fragments").getText(ui).equals("[[0,1],[2,3]]"));
		assertTrue(new LabeledTextLocator("constraints").getText(ui).equals("[[0,1,2,3]]"));
		assertTrue(new LabeledTextLocator("k list").getText(ui).equals("[2,3]"));

		ui.click(new JButtonLocator("x"));
	}
	
}