package gui.views;

import gui.DBType;
import gui.controllers.RealProblemController;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class RealProblemView.
 */
public class RealProblemView extends BaseProblemView {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1508901659366630367L;
	
	/** The text table. */
	private JTextField textTable;
	
	/** The text order by. */
	private JTextField textOrderBy;	
	
	/** The input type. */
	private final ButtonGroup inputType = new ButtonGroup();
	
	/** The text input file. */
	private JTextField textInputFile;
	
	/** The btn choose input file. */
	private JButton btnChooseInputFile;
	
	/** The rdbtn input sqlite. */
	private JRadioButton rdbtnInputSqlite;
	
	/** The rdbtn input h2. */
	private JRadioButton rdbtnInputH2;

	/**
	 * Create the panel.
	 *
	 * @param controller the controller
	 */
	public RealProblemView(RealProblemController controller) {
		super(controller);
		
		SpringLayout sl_input = new SpringLayout();
		input.setLayout(sl_input);
		
		JLabel lblInputFile = new JLabel("file");
		input.add(lblInputFile);
		
		textInputFile = new JTextField();
		textInputFile.setEnabled(false);
		sl_input.putConstraint(SpringLayout.NORTH, textInputFile, -2, SpringLayout.NORTH, lblInputFile);
		sl_input.putConstraint(SpringLayout.WEST, textInputFile, 85, SpringLayout.WEST, input);
		input.add(textInputFile);
		textInputFile.setColumns(10);
		
		JLabel lblInput = new JLabel("input");
		sl_input.putConstraint(SpringLayout.NORTH, lblInputFile, 15, SpringLayout.SOUTH, lblInput);
		sl_input.putConstraint(SpringLayout.WEST, lblInputFile, 0, SpringLayout.WEST, lblInput);
		sl_input.putConstraint(SpringLayout.NORTH, lblInput, 12, SpringLayout.NORTH, this);
		sl_input.putConstraint(SpringLayout.WEST, lblInput, 10, SpringLayout.WEST, this);
		lblInput.setFont(new Font("Dialog", Font.BOLD, 12));
		input.add(lblInput);
		
		rdbtnInputH2 = new JRadioButton("H2");
		rdbtnInputH2.setName("inputH2");
		rdbtnInputH2.setActionCommand("H2");
		inputType.add(rdbtnInputH2);
		input.add(rdbtnInputH2);
		rdbtnInputH2.setSelected(true);
		
		rdbtnInputSqlite = new JRadioButton("sqlite");
		rdbtnInputSqlite.setName("inputSqlite");
		rdbtnInputSqlite.setActionCommand("SQLITE");
		inputType.add(rdbtnInputSqlite);
		sl_input.putConstraint(SpringLayout.NORTH, rdbtnInputH2, 0, SpringLayout.NORTH, rdbtnInputSqlite);
		sl_input.putConstraint(SpringLayout.EAST, rdbtnInputSqlite, -10, SpringLayout.EAST, input);
		sl_input.putConstraint(SpringLayout.EAST, rdbtnInputH2, -6, SpringLayout.WEST, rdbtnInputSqlite);
		sl_input.putConstraint(SpringLayout.NORTH, rdbtnInputSqlite, -4, SpringLayout.NORTH, lblInput);
		input.add(rdbtnInputSqlite);
		
		JLabel lblTable = new JLabel("table");
		sl_input.putConstraint(SpringLayout.NORTH, lblTable, 15, SpringLayout.SOUTH, lblInputFile);
		sl_input.putConstraint(SpringLayout.WEST, lblTable, 10, SpringLayout.WEST, input);
		input.add(lblTable);
		
		textTable = new JTextField();
		sl_input.putConstraint(SpringLayout.NORTH, textTable, -2, SpringLayout.NORTH, lblTable);
		sl_input.putConstraint(SpringLayout.WEST, textTable, 0, SpringLayout.WEST, textInputFile);
		input.add(textTable);
		textTable.setColumns(10);
		
		JLabel lblOrderBy = new JLabel("order by");
		sl_input.putConstraint(SpringLayout.NORTH, lblOrderBy, 15, SpringLayout.SOUTH, lblTable);
		sl_input.putConstraint(SpringLayout.WEST, lblOrderBy, 10, SpringLayout.WEST, input);
		input.add(lblOrderBy);
		
		textOrderBy = new JTextField();
		textOrderBy.setToolTipText("a string to use as ORDER BY (e.g. \"education, region\") or empty");
		sl_input.putConstraint(SpringLayout.NORTH, textOrderBy, -2, SpringLayout.NORTH, lblOrderBy);
		sl_input.putConstraint(SpringLayout.WEST, textOrderBy, 0, SpringLayout.WEST, textInputFile);
		input.add(textOrderBy);
		textOrderBy.setColumns(10);
		
		btnChooseInputFile = new JButton("...");
		btnChooseInputFile.setName("chooseInput");
		btnChooseInputFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter filter = getInputFilter();
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(RealProblemView.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					textInputFile.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		});
		
		sl_input.putConstraint(SpringLayout.EAST, textOrderBy, 0, SpringLayout.EAST, btnChooseInputFile);
		sl_input.putConstraint(SpringLayout.EAST, textTable, 0, SpringLayout.EAST, btnChooseInputFile);
		sl_input.putConstraint(SpringLayout.EAST, textInputFile, -10, SpringLayout.WEST, btnChooseInputFile);
		sl_input.putConstraint(SpringLayout.NORTH, btnChooseInputFile, 0, SpringLayout.NORTH, textInputFile);
		sl_input.putConstraint(SpringLayout.WEST, btnChooseInputFile, -40, SpringLayout.EAST, input);
		sl_input.putConstraint(SpringLayout.SOUTH, btnChooseInputFile, 0, SpringLayout.SOUTH, textInputFile);
		sl_input.putConstraint(SpringLayout.EAST, btnChooseInputFile, -10, SpringLayout.EAST, input);
		input.add(btnChooseInputFile);
		btnChooseInputFile.setToolTipText("Select input database");

	}
	
	/**
	 * Gets the input filter.
	 *
	 * @return the input filter
	 */
	private FileNameExtensionFilter getInputFilter() {
		DBType type = DBType.valueOf(inputType.getSelection().getActionCommand().toUpperCase());
		switch(type) {
		case H2:
			return new FileNameExtensionFilter("H2 Database (*.h2.db)", "db");
		case SQLITE:
			return new FileNameExtensionFilter("Sqlite Database (*.db)", "db");
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#setFieldsEnabled(boolean)
	 */
	protected void setFieldsEnabled(boolean enabled) {
		super.setFieldsEnabled(enabled);
		btnChooseInputFile.setEnabled(enabled);
		textTable.setEnabled(enabled);
		textOrderBy.setEnabled(enabled);
	}

	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#createTable()
	 */
	@Override
	protected void createTable() throws Exception {
		DBType type = DBType.valueOf(inputType.getSelection().getActionCommand().toUpperCase());
		((RealProblemController) controller).createTable(type, textInputFile.getText(), textTable.getText(), textOrderBy.getText());
	}

	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#getProblemName()
	 */
	@Override
	protected String getProblemName() {
		return new File(textInputFile.getText()).getName().trim().replaceAll("\\.db$", "").replaceAll("\\.h2$", "");
	}
	
	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#getInputXml(org.w3c.dom.Document)
	 */
	@Override
	public Element getInputXml(Document doc) {
		Element input = doc.createElement("input");
		input.setAttribute("type", inputType.getSelection().getActionCommand());
		input.setAttribute("table", textTable.getText());
		input.setAttribute("order_by", textOrderBy.getText());
		input.appendChild(doc.createTextNode(textInputFile.getText()));
		return input;
	}

	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#loadInputFromXml(org.w3c.dom.Element)
	 */
	@Override
	public void loadInputFromXml(Element input) throws Exception {
		boolean isH2 = input.getAttribute("type").equalsIgnoreCase("H2");
		rdbtnInputH2.setSelected(isH2);
		rdbtnInputSqlite.setSelected(!isH2);
		textInputFile.setText(input.getTextContent());
		textTable.setText(input.getAttribute("table"));
		textOrderBy.setText(input.getAttribute("order_by"));
	}

	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#getProblemType()
	 */
	@Override
	protected String getProblemType() {
		return "real";
	}

}
