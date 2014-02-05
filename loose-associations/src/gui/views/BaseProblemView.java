package gui.views;

import gui.DBType;
import gui.controllers.ObservableDelegate;
import gui.controllers.ProblemController;
import gui.controllers.RandomProblemController;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultCaret;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.tuples.Pair;
import utils.tuples.Triplet;
import core.Status;

/**
 * The Class BaseProblemView.
 */
public abstract class BaseProblemView extends JPanel implements Observer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3592840037663708821L;
	
	/** The text output path. */
	private final JTextField textOutputPath;
	
	/** The output type. */
	private final ButtonGroup outputType = new ButtonGroup();
	
	/** The text fragments. */
	private final JTextField textFragments;
	
	/** The text constraints. */
	private final JTextField textConstraints;
	
	/** The text k list. */
	private final JTextField textKList;
	
	/** The txtr logger. */
	private JTextArea txtrLogger;
	
	/** The txt status. */
	private JLabel txtStatus;
	
	/** The txt dropped. */
	private JLabel txtDropped;
	
	/** The txt opstack. */
	private JLabel txtOpstack;
	
	/** The progress first scan. */
	private JProgressBar progressFirstScan;
	
	/** The btn start. */
	private JButton btnStart;
	
	/** The btn stop. */
	private JButton btnStop;
	
	/** The btn choose output path. */
	private JButton btnChooseOutputPath;
	
	/** The rdbtn output h2. */
	private JRadioButton rdbtnOutputH2;
	
	/** The rdbtn output sqlite. */
	private JRadioButton rdbtnOutputSqlite;
	
	/** The input. */
	protected final JPanel input;
	
	/** The controller. */
	protected final ProblemController controller;
	
	/** The twodecimals. */
	protected DecimalFormat twodecimals = new DecimalFormat("0.00");

	/** The tuples. */
	private int tuples;

	/**
	 * Create the view.
	 *
	 * @param controller the controller
	 */
	public BaseProblemView(final ProblemController controller) {
		// fix the designer pane
		if (controller == null)
			this.controller = new RandomProblemController(999);
		else
			this.controller = controller;
			
		
		this.controller.addObserver(this);
		
		SpringLayout sl_realDatabase = new SpringLayout();
		this.setLayout(sl_realDatabase);
		
		input = new JPanel();
		sl_realDatabase.putConstraint(SpringLayout.NORTH, input, 24, SpringLayout.NORTH, this);
		sl_realDatabase.putConstraint(SpringLayout.WEST, input, 27, SpringLayout.WEST, this);
		sl_realDatabase.putConstraint(SpringLayout.SOUTH, input, 160, SpringLayout.NORTH, this);
		sl_realDatabase.putConstraint(SpringLayout.EAST, input, -457, SpringLayout.EAST, this);
		input.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.add(input);
		
		JPanel output = new JPanel();
		sl_realDatabase.putConstraint(SpringLayout.NORTH, output, 15, SpringLayout.SOUTH, input);
		sl_realDatabase.putConstraint(SpringLayout.WEST, output, 27, SpringLayout.WEST, this);
		sl_realDatabase.putConstraint(SpringLayout.SOUTH, output, 90, SpringLayout.SOUTH, input);
		sl_realDatabase.putConstraint(SpringLayout.EAST, output, -457, SpringLayout.EAST, this);
		output.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.add(output);
		SpringLayout sl_output = new SpringLayout();
		output.setLayout(sl_output);
		
		JLabel lblOutput = new JLabel("output");
		sl_output.putConstraint(SpringLayout.NORTH, lblOutput, 12, SpringLayout.NORTH, output);
		sl_output.putConstraint(SpringLayout.WEST, lblOutput, 10, SpringLayout.WEST, output);
		output.add(lblOutput);
		
		JLabel lblOutputPath = new JLabel("path");
		sl_output.putConstraint(SpringLayout.NORTH, lblOutputPath, 15, SpringLayout.SOUTH, lblOutput);
		sl_output.putConstraint(SpringLayout.WEST, lblOutputPath, 10, SpringLayout.WEST, output);
		output.add(lblOutputPath);
		
		textOutputPath = new JTextField();
		textOutputPath.setToolTipText("the output directory");
		textOutputPath.setEnabled(false);
		sl_output.putConstraint(SpringLayout.NORTH, textOutputPath, -2, SpringLayout.NORTH, lblOutputPath);
		sl_output.putConstraint(SpringLayout.WEST, textOutputPath, 85, SpringLayout.WEST, output);
		output.add(textOutputPath);
		textOutputPath.setColumns(10);
		textOutputPath.setEnabled(false);
		
		rdbtnOutputH2 = new JRadioButton("H2");
		rdbtnOutputH2.setName("outputH2");
		rdbtnOutputH2.setActionCommand("H2");
		outputType.add(rdbtnOutputH2);
		rdbtnOutputH2.setSelected(true);
		output.add(rdbtnOutputH2);
		
		rdbtnOutputSqlite = new JRadioButton("sqlite");
		rdbtnOutputSqlite.setName("outputSqlite");
		rdbtnOutputSqlite.setActionCommand("SQLITE");
		outputType.add(rdbtnOutputSqlite);
		sl_output.putConstraint(SpringLayout.NORTH, rdbtnOutputH2, 0, SpringLayout.NORTH, rdbtnOutputSqlite);
		sl_output.putConstraint(SpringLayout.EAST, rdbtnOutputH2, -6, SpringLayout.WEST, rdbtnOutputSqlite);
		sl_output.putConstraint(SpringLayout.EAST, rdbtnOutputSqlite, -10, SpringLayout.EAST, output);
		sl_output.putConstraint(SpringLayout.NORTH, rdbtnOutputSqlite, -4, SpringLayout.NORTH, lblOutput);
		output.add(rdbtnOutputSqlite);
		
		btnChooseOutputPath = new JButton("...");
		btnChooseOutputPath.setName("chooseOutput");
		btnChooseOutputPath.setToolTipText("choose the output directory");
		btnChooseOutputPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(BaseProblemView.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					textOutputPath.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		});
		
		sl_output.putConstraint(SpringLayout.EAST, textOutputPath, -10, SpringLayout.WEST, btnChooseOutputPath);
		sl_output.putConstraint(SpringLayout.NORTH, btnChooseOutputPath, 0, SpringLayout.NORTH, textOutputPath);
		sl_output.putConstraint(SpringLayout.WEST, btnChooseOutputPath, -40, SpringLayout.EAST, output);
		sl_output.putConstraint(SpringLayout.SOUTH, btnChooseOutputPath, 0, SpringLayout.SOUTH, textOutputPath);
		sl_output.putConstraint(SpringLayout.EAST, btnChooseOutputPath, -10, SpringLayout.EAST, output);
		output.add(btnChooseOutputPath);
		
		JPanel configuration = new JPanel();
		sl_realDatabase.putConstraint(SpringLayout.NORTH, configuration, 0, SpringLayout.NORTH, input);
		sl_realDatabase.putConstraint(SpringLayout.WEST, configuration, 15, SpringLayout.EAST, input);
		sl_realDatabase.putConstraint(SpringLayout.SOUTH, configuration, 0, SpringLayout.SOUTH, input);
		sl_realDatabase.putConstraint(SpringLayout.EAST, configuration, -27, SpringLayout.EAST, this);
		configuration.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.add(configuration);
		SpringLayout sl_configuration = new SpringLayout();
		configuration.setLayout(sl_configuration);
		
		JLabel lblConfiguration = new JLabel("configuration");
		sl_configuration.putConstraint(SpringLayout.NORTH, lblConfiguration, 12, SpringLayout.NORTH, configuration);
		sl_configuration.putConstraint(SpringLayout.WEST, lblConfiguration, 10, SpringLayout.WEST, configuration);
		configuration.add(lblConfiguration);
		
		JLabel lblFragments = new JLabel("fragments");
		sl_configuration.putConstraint(SpringLayout.NORTH, lblFragments, 15, SpringLayout.SOUTH, lblConfiguration);
		sl_configuration.putConstraint(SpringLayout.WEST, lblFragments, 10, SpringLayout.WEST, configuration);
		configuration.add(lblFragments);
		
		textFragments = new JTextField();
		textFragments.setToolTipText("list of list of attributes. e.g. [[1, 2], [3, 4, 5], ...]");
		sl_configuration.putConstraint(SpringLayout.NORTH, textFragments, -2, SpringLayout.NORTH, lblFragments);
		sl_configuration.putConstraint(SpringLayout.WEST, textFragments, 100, SpringLayout.WEST, configuration);
		sl_configuration.putConstraint(SpringLayout.EAST, textFragments, -12, SpringLayout.EAST, configuration);
		configuration.add(textFragments);
		textFragments.setColumns(10);
		
		JLabel lblConstraints = new JLabel("constraints");
		sl_configuration.putConstraint(SpringLayout.NORTH, lblConstraints, 15, SpringLayout.SOUTH, lblFragments);
		sl_configuration.putConstraint(SpringLayout.WEST, lblConstraints, 0, SpringLayout.WEST, lblConfiguration);
		configuration.add(lblConstraints);
		
		textConstraints = new JTextField();
		textConstraints.setToolTipText("list of list of attributes. e.g. [[3, 4], [1, 4, 6], ...]");
		sl_configuration.putConstraint(SpringLayout.NORTH, textConstraints, -2, SpringLayout.NORTH, lblConstraints);
		sl_configuration.putConstraint(SpringLayout.WEST, textConstraints, 0, SpringLayout.WEST, textFragments);
		sl_configuration.putConstraint(SpringLayout.EAST, textConstraints, 0, SpringLayout.EAST, textFragments);
		configuration.add(textConstraints);
		textConstraints.setColumns(10);
		
		JLabel lblKList = new JLabel("k list");
		sl_configuration.putConstraint(SpringLayout.NORTH, lblKList, 15, SpringLayout.SOUTH, lblConstraints);
		sl_configuration.putConstraint(SpringLayout.WEST, lblKList, 0, SpringLayout.WEST, lblConfiguration);
		configuration.add(lblKList);
		
		textKList = new JTextField();
		textKList.setToolTipText("list of protection factors. e.g. [3, 4, 3, ...]");
		sl_configuration.putConstraint(SpringLayout.NORTH, textKList, -2, SpringLayout.NORTH, lblKList);
		sl_configuration.putConstraint(SpringLayout.WEST, textKList, 0, SpringLayout.WEST, textFragments);
		sl_configuration.putConstraint(SpringLayout.EAST, textKList, 0, SpringLayout.EAST, textFragments);
		configuration.add(textKList);
		textKList.setColumns(10);
		
		btnStart = new JButton("Start");
		btnStart.setToolTipText("start elaboration");
		btnStop = new JButton("Stop");
		btnStop.setToolTipText("stop elaboration");
		txtrLogger = new JTextArea();
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					if (textOutputPath.getText().isEmpty())
						appendToLogger("Output path not specified");
					else
						start();
				} catch (Exception e) {
					setErrorState(e.getMessage());
				}
			}
		});
		sl_realDatabase.putConstraint(SpringLayout.SOUTH, btnStart, 0, SpringLayout.SOUTH, output);
		this.add(btnStart);
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
				btnStop.setEnabled(false);
				btnStart.setEnabled(true);
			}
		});
		
		
		btnStop.setEnabled(false);
		sl_realDatabase.putConstraint(SpringLayout.EAST, btnStart, -34, SpringLayout.WEST, btnStop);
		sl_realDatabase.putConstraint(SpringLayout.SOUTH, btnStop, 0, SpringLayout.SOUTH, output);
		sl_realDatabase.putConstraint(SpringLayout.EAST, btnStop, 0, SpringLayout.EAST, configuration);
		this.add(btnStop);
		
		JPanel results = new JPanel();
		sl_realDatabase.putConstraint(SpringLayout.NORTH, results, 15, SpringLayout.SOUTH, output);
		sl_realDatabase.putConstraint(SpringLayout.SOUTH, results, 78, SpringLayout.SOUTH, output);
		sl_realDatabase.putConstraint(SpringLayout.WEST, results, 27, SpringLayout.WEST, this);
		sl_realDatabase.putConstraint(SpringLayout.EAST, results, -27, SpringLayout.EAST, this);
		results.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.add(results);
		SpringLayout sl_results = new SpringLayout();
		results.setLayout(sl_results);
		
		JLabel lblStatus = new JLabel("status:");
		sl_results.putConstraint(SpringLayout.NORTH, lblStatus, 10, SpringLayout.NORTH, results);
		sl_results.putConstraint(SpringLayout.WEST, lblStatus, 12, SpringLayout.WEST, results);
		results.add(lblStatus);
		
		JLabel lblDropped = new JLabel("dropped tuples:");
		sl_results.putConstraint(SpringLayout.NORTH, lblDropped, 10, SpringLayout.SOUTH, lblStatus);
		sl_results.putConstraint(SpringLayout.WEST, lblDropped, 10, SpringLayout.WEST, results);
		results.add(lblDropped);
		
		JLabel lblFirstScanProgress = new JLabel("first scan progress");
		sl_results.putConstraint(SpringLayout.NORTH, lblFirstScanProgress, 0, SpringLayout.NORTH, lblStatus);
		sl_results.putConstraint(SpringLayout.WEST, lblFirstScanProgress, 300, SpringLayout.WEST, results);
		results.add(lblFirstScanProgress);
		
		progressFirstScan = new JProgressBar();
		sl_results.putConstraint(SpringLayout.SOUTH, progressFirstScan, 0, SpringLayout.SOUTH, lblFirstScanProgress);
		sl_results.putConstraint(SpringLayout.EAST, progressFirstScan, -10, SpringLayout.EAST, results);
		results.add(progressFirstScan);
		
		JLabel lblOperationStackSize = new JLabel("operation stack size:");
		sl_results.putConstraint(SpringLayout.NORTH, lblOperationStackSize, 7, SpringLayout.SOUTH, lblFirstScanProgress);
		sl_results.putConstraint(SpringLayout.WEST, lblOperationStackSize, 0, SpringLayout.WEST, lblFirstScanProgress);
		results.add(lblOperationStackSize);
		
		JButton btnClose = new JButton("x");
		btnClose.setMargin(new Insets(0, 0, 0, 0));
		btnClose.setToolTipText("close this problem");
		sl_realDatabase.putConstraint(SpringLayout.NORTH, btnClose, 0, SpringLayout.NORTH, this);
		sl_realDatabase.putConstraint(SpringLayout.WEST, btnClose, -27, SpringLayout.EAST, this);
		sl_realDatabase.putConstraint(SpringLayout.SOUTH, btnClose, 18, SpringLayout.NORTH, this);
		sl_realDatabase.putConstraint(SpringLayout.EAST, btnClose, 0, SpringLayout.EAST, this);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		add(btnClose);
		
		JScrollPane logger = new JScrollPane();
		sl_realDatabase.putConstraint(SpringLayout.NORTH, logger, 15, SpringLayout.SOUTH, results);
		sl_realDatabase.putConstraint(SpringLayout.WEST, logger, 0, SpringLayout.WEST, results);
		sl_realDatabase.putConstraint(SpringLayout.SOUTH, logger, -24, SpringLayout.SOUTH, this);
		sl_realDatabase.putConstraint(SpringLayout.EAST, logger, 0, SpringLayout.EAST, results);
		
		txtStatus = new JLabel("IDLE");
		sl_results.putConstraint(SpringLayout.SOUTH, txtStatus, 0, SpringLayout.SOUTH, lblStatus);
		results.add(txtStatus);
		
		txtDropped = new JLabel("");
		sl_results.putConstraint(SpringLayout.WEST, txtStatus, 0, SpringLayout.WEST, txtDropped);
		sl_results.putConstraint(SpringLayout.WEST, txtDropped, 20, SpringLayout.EAST, lblDropped);
		sl_results.putConstraint(SpringLayout.SOUTH, txtDropped, 0, SpringLayout.SOUTH, lblDropped);
		results.add(txtDropped);
		
		txtOpstack = new JLabel("");
		sl_results.putConstraint(SpringLayout.WEST, progressFirstScan, 0, SpringLayout.WEST, txtOpstack);
		sl_results.putConstraint(SpringLayout.WEST, txtOpstack, 20, SpringLayout.EAST, lblOperationStackSize);
		sl_results.putConstraint(SpringLayout.SOUTH, txtOpstack, 0, SpringLayout.SOUTH, lblOperationStackSize);
		results.add(txtOpstack);
		add(logger);
		
		txtrLogger.setWrapStyleWord(true);
		logger.setViewportView(txtrLogger);
		txtrLogger.setEditable(false);
		txtrLogger.setLineWrap(true);
		txtrLogger.setForeground(Color.WHITE);
		txtrLogger.setBackground(Color.DARK_GRAY);
		
		// move the caret to follow the text added with append
		DefaultCaret caret = (DefaultCaret) txtrLogger.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		final JComboBox<ErrorLevel> comboErrorLevel = new JComboBox<ErrorLevel>();
		comboErrorLevel.setToolTipText("change the logger level");
		sl_realDatabase.putConstraint(SpringLayout.NORTH, comboErrorLevel, 0, SpringLayout.NORTH, btnStart);
		sl_realDatabase.putConstraint(SpringLayout.WEST, comboErrorLevel, -140, SpringLayout.WEST, btnStart);
		sl_realDatabase.putConstraint(SpringLayout.SOUTH, comboErrorLevel, 0, SpringLayout.SOUTH, btnStart);
		comboErrorLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BaseProblemView.this.controller.setLoggerLevel(((ErrorLevel) comboErrorLevel.getSelectedItem()).getValue());
			}
		});
		
		comboErrorLevel.setModel(new DefaultComboBoxModel<ErrorLevel>(ErrorLevel.values()));
		comboErrorLevel.setSelectedIndex(2);
		sl_realDatabase.putConstraint(SpringLayout.EAST, comboErrorLevel, -40, SpringLayout.WEST, btnStart);
		add(comboErrorLevel);
		
		JLabel lbllogLevel = new JLabel("log level");
		sl_realDatabase.putConstraint(SpringLayout.NORTH, lbllogLevel, 4, SpringLayout.NORTH, comboErrorLevel);
		sl_realDatabase.putConstraint(SpringLayout.EAST, lbllogLevel, -20, SpringLayout.WEST, comboErrorLevel);
		add(lbllogLevel);

	}
	
	/**
	 * Sets the fields enabled.
	 *
	 * @param enabled the new fields enabled
	 */
	protected void setFieldsEnabled(boolean enabled) {
		textConstraints.setEnabled(enabled);
		textFragments.setEnabled(enabled);
		textKList.setEnabled(enabled);
		rdbtnOutputH2.setEnabled(enabled);
		rdbtnOutputSqlite.setEnabled(enabled);
	}
	
	/**
	 * Append to logger.
	 *
	 * @param message the message
	 */
	public void appendToLogger(String message) {
		txtrLogger.append(message);
		txtrLogger.append("\n");
		txtrLogger.setCaretPosition(txtrLogger.getDocument().getLength());
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		try {
			if (o instanceof ObservableDelegate) {
				// message from logger
				appendToLogger(arg.toString());
			} else if (arg instanceof Triplet) {
				// message from model
				updateFromModel(arg);
			} else {
				// message from controller
				updateFromController(arg);
			}
		} catch (Exception e) {
			appendToLogger("Unexpected message: " + arg);
			e.printStackTrace();
		}
	}

	/**
	 * Update from controller.
	 *
	 * @param arg the arg
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	private void updateFromController(Object arg) throws Exception {
		Pair<ProblemController.ProblemWorkerMessages, Object> message = ((Pair<ProblemController.ProblemWorkerMessages, Object>) arg);
		switch (message.getFirst()) {
		
		case ASSOCIATING:
			// OK
			break;
			
		case EXPORTING:
			appendToLogger("Exporting to " + message.getSecond());
			btnStop.setEnabled(false);
			break;
			
		case DONE:
			if ((Boolean) message.getSecond())
				appendToLogger("Exported");
			else
				appendToLogger("Exportation failed");
			btnStart.setEnabled(true);
			setFieldsEnabled(true);
			break;
			
		case MESSAGE:
			appendToLogger(message.getSecond().toString());
			break;
		
		case TABLEFETCHED:
			tuples = (Integer) message.getSecond();
			btnStop.setEnabled(true);
			progressFirstScan.setMaximum(tuples - 1);
			progressFirstScan.setValue(0);
			if (controller.initializeLoose(textFragments.getText(), textConstraints.getText(), textKList.getText())) {
				String outputNameWithoutExtension = new File(textOutputPath.getText(), getProblemName()).getAbsolutePath();
				controller.setOutput(outputNameWithoutExtension, DBType.valueOf(outputType.getSelection().getActionCommand()));
				controller.start(this);
			}
			break;
			
		case ERROR:
			setErrorState(message.getSecond().toString());
			break;
			
		default:
			appendToLogger("Malformed message from controller: " + arg);
		}
	}
	
	public void setErrorState(String message) {
		txtStatus.setText("ERROR");
		btnStop.setEnabled(false);
		btnStart.setEnabled(true);
		setFieldsEnabled(true);
		appendToLogger(message);
	}
	

	/**
	 * Update from model.
	 *
	 * @param arg the arg
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	private void updateFromModel(Object arg) throws Exception {
		Triplet<Status, Integer, Integer> message = ((Triplet<Status, Integer, Integer>) arg);
		double dropPercent = 100.0 * message.getThird() / tuples;
		txtDropped.setText(message.getThird().toString() + " (" + twodecimals.format(dropPercent) + "%)");
		switch (message.getFirst()) {
		
		case DONE:
			txtStatus.setText("DONE in " + message.getSecond() + "s");
			progressFirstScan.setValue(progressFirstScan.getMaximum());
			txtOpstack.setText("0");
			break;
			
		case FIRST_SCAN:
			txtStatus.setText("FIRST SCAN");
			progressFirstScan.setValue(message.getSecond());
			txtOpstack.setText("");
			break;
			
		case IDLE:
			txtStatus.setText("IDLE");
			txtOpstack.setText("");
			break;
			
		case SECOND_SCAN:
			txtStatus.setText("SECOND SCAN");
			progressFirstScan.setValue(progressFirstScan.getMaximum());
			txtOpstack.setText(message.getSecond().toString());
			break;
			
		default:
			appendToLogger("Malformed message from model: " + arg);
		}
	}

	/**
	 * Creates the table.
	 * 
	 * @throws Exception the exception
	 */
	protected abstract void createTable() throws Exception;
	
	/**
	 * Start.
	 *
	 * @throws Exception the exception
	 */
	protected void start() throws Exception {
		txtrLogger.setText("");
		btnStart.setEnabled(false);
		setFieldsEnabled(false);
		txtStatus.setText("FETCHING TABLE");
		createTable();
		// Then wait for the message TABLEFETCHED
	}
	
	/**
	 * Stop.
	 */
	protected void stop() {
		txtStatus.setText("CANCELED");
		setFieldsEnabled(true);
		controller.stop();
	}
	
	/**
	 * Checks if is running.
	 *
	 * @return true, if is running
	 */
	protected boolean isRunning() {
		return controller.isRunning();
	}
	
	/**
	 * Close.
	 */
	protected void close() {
		if (isRunning())
			if (JOptionPane.showConfirmDialog(this,
					"The problem is still running. Do you want to stop it and exit?",
					"Problem still running", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
					== JOptionPane.NO_OPTION)
				// skip the stop
				return;
			
		stop();
		getParent().remove(BaseProblemView.this);
	}
	
	/**
	 * Save to xml.
	 */
	public void saveToXml() {
		
		JFileChooser chooser = new JFileChooser();
		chooser.setSelectedFile(new File(getProblemName() + ".xml"));
		
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;
		
		String outputfile = chooser.getSelectedFile().getAbsolutePath();
		if (!outputfile.endsWith(".xml"))
			outputfile = outputfile.concat(".xml");

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			Element rootElement = doc.createElement("problem");
			rootElement.setAttribute("type", getProblemType());
			doc.appendChild(rootElement);

			Element input = getInputXml(doc);
			rootElement.appendChild(input);

			Element output = doc.createElement("output");
			output.setAttribute("type", outputType.getSelection().getActionCommand());
			output.appendChild(doc.createTextNode(textOutputPath.getText()));
			rootElement.appendChild(output);

			Element config = doc.createElement("configuration");

			Element fragments = doc.createElement("fragments");
			fragments.appendChild(doc.createTextNode(textFragments.getText()));
			config.appendChild(fragments);

			Element constraints = doc.createElement("constraints");
			constraints.appendChild(doc.createTextNode(textConstraints.getText()));
			config.appendChild(constraints);

			Element kList = doc.createElement("k_list");
			kList.appendChild(doc.createTextNode(textKList.getText()));
			config.appendChild(kList);

			rootElement.appendChild(config);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			// pretty print
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);

			StreamResult file = new StreamResult(new File(outputfile));
			transformer.transform(source, file);
			
			appendToLogger("Exported to " + outputfile);

		} catch (Exception e) {
			appendToLogger("Error saving to XML. " + e.getMessage());
		}
	}
	
	/**
	 * Load from xml.
	 *
	 * @param doc the doc
	 */
	public void loadFromXML(Document doc) {
		try {
			Element input = (Element) doc.getElementsByTagName("input").item(0);
			loadInputFromXml(input);
			
			Element output = (Element) doc.getElementsByTagName("output").item(0);
			boolean isH2 = output.getAttribute("type").equalsIgnoreCase("H2");
			rdbtnOutputH2.setSelected(isH2);
			rdbtnOutputSqlite.setSelected(!isH2);
			textOutputPath.setText(output.getTextContent());
			
			Element config = (Element) doc.getElementsByTagName("configuration").item(0);
			textFragments.setText(config.getElementsByTagName("fragments").item(0).getTextContent());
			textConstraints.setText(config.getElementsByTagName("constraints").item(0).getTextContent());
			textKList.setText(config.getElementsByTagName("k_list").item(0).getTextContent());

			doc.getElementsByTagName("output");
		} catch (Exception e) {
			appendToLogger("Error loading XML. " + e.getMessage());
		}
	}
	
	/**
	 * Gets the problem type.
	 *
	 * @return the problem type
	 */
	protected abstract String getProblemType();

	/**
	 * Gets the problem name.
	 *
	 * @return the problem name
	 */
	protected abstract String getProblemName();

	/**
	 * Gets the input xml.
	 *
	 * @param doc the doc
	 * @return the input xml
	 * @throws Exception the exception
	 */
	protected abstract Element getInputXml(Document doc) throws Exception;
	
	/**
	 * Load input from xml.
	 *
	 * @param input the input
	 * @throws Exception the exception
	 */
	protected abstract void loadInputFromXml(Element input) throws Exception;
	
}
