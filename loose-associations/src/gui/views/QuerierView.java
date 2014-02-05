package gui.views;

import gui.controllers.ObservableDelegate;
import gui.controllers.QuerierController;
import gui.controllers.QuerierController.QuerierWorkerStatus;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

import utils.tuples.Pair;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

/**
 * The Class QuerierView.
 */
public class QuerierView extends JPanel implements Observer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7323299027190218490L;
	
	/** The txt database. */
	private JTextField txtDatabase;
	
	/** The text query. */
	private JTextField textQuery;
	
	/** The controller. */
	private final QuerierController controller;
	
	/** The txtr result. */
	private JTextArea txtrResult;
	
	/** The btn run. */
	private JButton btnRun;

	/**
	 * Create the panel.
	 *
	 * @param controller the controller
	 */
	public QuerierView(final QuerierController controller) {
		
		this.controller = controller;
		this.controller.addObserver(this);
		
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		JPanel connection = new JPanel();
		connection.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		springLayout.putConstraint(SpringLayout.NORTH, connection, 24, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, connection, 27, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, connection, 102, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, connection, -27, SpringLayout.EAST, this);
		add(connection);
		SpringLayout sl_connection = new SpringLayout();
		connection.setLayout(sl_connection);
		
		JLabel lblDatabase = new JLabel("loose database");
		sl_connection.putConstraint(SpringLayout.NORTH, lblDatabase, 12, SpringLayout.NORTH, connection);
		sl_connection.putConstraint(SpringLayout.WEST, lblDatabase, 10, SpringLayout.WEST, connection);
		connection.add(lblDatabase);
		
		JButton btnClose = new JButton("x");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		btnClose.setMargin(new Insets(0, 0, 0, 0));
		springLayout.putConstraint(SpringLayout.NORTH, btnClose, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnClose, -27, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, btnClose, 18, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, btnClose, 0, SpringLayout.EAST, this);
		add(btnClose);
		
		JPanel query = new JPanel();
		query.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		springLayout.putConstraint(SpringLayout.NORTH, query, 15, SpringLayout.SOUTH, connection);
		springLayout.putConstraint(SpringLayout.WEST, query, 0, SpringLayout.WEST, connection);
		springLayout.putConstraint(SpringLayout.SOUTH, query, -24, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, query, 0, SpringLayout.EAST, connection);
		
		final JButton btnChooseDatabase = new JButton("...");
		btnChooseDatabase.setName("chooseQueryDatabase");
		btnChooseDatabase.setToolTipText("choose a loose database to query");
		btnChooseDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Loose Database (*.loose.db, *.loose.h2.db)", "db");
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(QuerierView.this);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					txtDatabase.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		});
		sl_connection.putConstraint(SpringLayout.WEST, btnChooseDatabase, -60, SpringLayout.EAST, connection);
		sl_connection.putConstraint(SpringLayout.EAST, btnChooseDatabase, -10, SpringLayout.EAST, connection);
		connection.add(btnChooseDatabase);
		
		txtDatabase = new JTextField();
		txtDatabase.setToolTipText("loose datase to query");
		sl_connection.putConstraint(SpringLayout.NORTH, btnChooseDatabase, 0, SpringLayout.NORTH, txtDatabase);
		sl_connection.putConstraint(SpringLayout.SOUTH, btnChooseDatabase, 0, SpringLayout.SOUTH, txtDatabase);
		sl_connection.putConstraint(SpringLayout.NORTH, txtDatabase, -2, SpringLayout.NORTH, lblDatabase);
		sl_connection.putConstraint(SpringLayout.WEST, txtDatabase, 25, SpringLayout.EAST, lblDatabase);
		sl_connection.putConstraint(SpringLayout.EAST, txtDatabase, -10, SpringLayout.WEST, btnChooseDatabase);
		txtDatabase.setEnabled(false);
		connection.add(txtDatabase);
		txtDatabase.setColumns(10);
		
		final JButton btnConnect = new JButton("Connect");
		btnConnect.setToolTipText("connect to the database");
		final JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setToolTipText("disconnect from the database");
		btnDisconnect.setEnabled(false);
		btnRun = new JButton("run");
		btnRun.setToolTipText("run the query");
		btnRun.setEnabled(false);
		txtrResult = new JTextArea();
		
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (txtDatabase.getText().isEmpty())
					appendToLogger("Please choose a database");
				else {
					try {
						btnConnect.setEnabled(false);
						btnChooseDatabase.setEnabled(false);
						connect();
						btnDisconnect.setEnabled(true);
						btnRun.setEnabled(true);
						textQuery.setEnabled(true);
						txtrResult.setText("");
						appendToLogger("Connected to " + txtDatabase.getText());

					} catch (Exception e) {
						appendToLogger(e.getMessage());
						btnChooseDatabase.setEnabled(true);
						btnConnect.setEnabled(true);
					}
				}
			}
		});
		
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDisconnect.setEnabled(false);
				btnRun.setEnabled(false);
				textQuery.setEnabled(false);
				textQuery.setText("");
				disconnect();
				btnConnect.setEnabled(true);
				btnChooseDatabase.setEnabled(true);
				
			}
		});
		
		sl_connection.putConstraint(SpringLayout.SOUTH, btnDisconnect, -10, SpringLayout.SOUTH, connection);
		sl_connection.putConstraint(SpringLayout.EAST, btnDisconnect, 0, SpringLayout.EAST, txtDatabase);
		connection.add(btnDisconnect);
		
		sl_connection.putConstraint(SpringLayout.NORTH, btnConnect, 0, SpringLayout.NORTH, btnDisconnect);
		sl_connection.putConstraint(SpringLayout.EAST, btnConnect, -19, SpringLayout.WEST, btnDisconnect);
		connection.add(btnConnect);
		
		final JComboBox<ErrorLevel> comboErrorLevel = new JComboBox<ErrorLevel>();
		comboErrorLevel.setToolTipText("change the logger level");
		
		sl_connection.putConstraint(SpringLayout.NORTH, comboErrorLevel, 0, SpringLayout.NORTH, btnConnect);
		sl_connection.putConstraint(SpringLayout.SOUTH, comboErrorLevel, 0, SpringLayout.SOUTH, btnConnect);
		comboErrorLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setLoggerLevel(((ErrorLevel) comboErrorLevel.getSelectedItem()).getValue());
			}
		});
		comboErrorLevel.setModel(new DefaultComboBoxModel<ErrorLevel>(ErrorLevel.values()));
		comboErrorLevel.setSelectedIndex(2);
		connection.add(comboErrorLevel);
		
		JLabel lblLogLevel = new JLabel("log level");
		sl_connection.putConstraint(SpringLayout.WEST, comboErrorLevel, 20, SpringLayout.EAST, lblLogLevel);
		sl_connection.putConstraint(SpringLayout.EAST, comboErrorLevel, 120, SpringLayout.EAST, lblLogLevel);
		sl_connection.putConstraint(SpringLayout.NORTH, lblLogLevel, 4, SpringLayout.NORTH, comboErrorLevel);
		sl_connection.putConstraint(SpringLayout.WEST, lblLogLevel, 0, SpringLayout.WEST, txtDatabase);
		connection.add(lblLogLevel);
		add(query);
		SpringLayout sl_query = new SpringLayout();
		query.setLayout(sl_query);
		
		JLabel lblQuery = new JLabel("query");
		sl_query.putConstraint(SpringLayout.NORTH, lblQuery, 12, SpringLayout.NORTH, query);
		sl_query.putConstraint(SpringLayout.WEST, lblQuery, 10, SpringLayout.WEST, query);
		query.add(lblQuery);
		
		JScrollPane resultPane = new JScrollPane();
		sl_query.putConstraint(SpringLayout.NORTH, resultPane, 15, SpringLayout.SOUTH, lblQuery);
		sl_query.putConstraint(SpringLayout.WEST, resultPane, 10, SpringLayout.WEST, query);
		sl_query.putConstraint(SpringLayout.SOUTH, resultPane, -10, SpringLayout.SOUTH, query);
		sl_query.putConstraint(SpringLayout.EAST, resultPane, -10, SpringLayout.EAST, query);
		query.add(resultPane);
		
		txtrResult.setBackground(Color.DARK_GRAY);
		txtrResult.setWrapStyleWord(true);
		txtrResult.setLineWrap(true);
		txtrResult.setEditable(false);
		txtrResult.setForeground(Color.WHITE);
		resultPane.setViewportView(txtrResult);
		
		textQuery = new JTextField();
		textQuery.setName("textQuery");
		textQuery.setToolTipText("the query in the meta-sql format");
		textQuery.setEnabled(false);
		sl_query.putConstraint(SpringLayout.NORTH, textQuery, -2, SpringLayout.NORTH, lblQuery);
		sl_query.putConstraint(SpringLayout.WEST, textQuery, 25, SpringLayout.EAST, lblQuery);
		sl_query.putConstraint(SpringLayout.SOUTH, textQuery, -11, SpringLayout.NORTH, resultPane);
		query.add(textQuery);
		textQuery.setColumns(10);
		
		
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textQuery.setEnabled(false);
				btnRun.setEnabled(false);
				starQuery();
			}
		});
		
		btnRun.setMargin(new Insets(0, 0, 0, 0));
		sl_query.putConstraint(SpringLayout.EAST, textQuery, -10, SpringLayout.WEST, btnRun);
		sl_query.putConstraint(SpringLayout.NORTH, btnRun, 0, SpringLayout.NORTH, textQuery);
		sl_query.putConstraint(SpringLayout.WEST, btnRun, -60, SpringLayout.EAST, query);
		sl_query.putConstraint(SpringLayout.SOUTH, btnRun, 0, SpringLayout.SOUTH, textQuery);
		sl_query.putConstraint(SpringLayout.EAST, btnRun, -10, SpringLayout.EAST, query);
		query.add(btnRun);
		
		// move the caret to follow the text added with append
		DefaultCaret caret = (DefaultCaret) txtrResult.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

	}
	
	/**
	 * Append to logger.
	 *
	 * @param message the message
	 */
	public void appendToLogger(String message) {
		txtrResult.append(message);
		txtrResult.append("\n");
	}
	
	/**
	 * Connect.
	 *
	 * @throws Exception the exception
	 */
	protected void connect() throws Exception {
		controller.connect(txtDatabase.getText());
	}
	
	/**
	 * Disconnect.
	 */
	protected void disconnect() {
		controller.disconnect();
	}
	
	/**
	 * Star query.
	 */
	protected void starQuery() {
		String query = textQuery.getText();
		appendToLogger("\n");
		appendToLogger(query);
		textQuery.setText("");
		controller.startQuery(query);
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
		Pair<QuerierWorkerStatus, Object> message = ((Pair<QuerierWorkerStatus, Object>) arg);
		switch (message.getFirst()) {
		
		case QUERYING:
			// OK
			break;
			
		case DONE:
			List<List<Object>> result = ((List<List<Object>>) message.getSecond());
			for (List<Object> row: result)
				appendToLogger(row.toString());
			textQuery.setEnabled(true);
			btnRun.setEnabled(true);
			break;
			
		case ERROR:
			appendToLogger("Error: " + message.getSecond().toString());
			textQuery.setEnabled(true);
			btnRun.setEnabled(true);
			break;
		
		default:
			appendToLogger("Malformed message from controller: " + arg);
		}
	}
	
	/**
	 * Close.
	 */
	protected void close() {
		if (controller.isRunning())
			if (JOptionPane.showConfirmDialog(this,
					"The query is still running. Do you want to stop it and exit?",
					"Query still running", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
					== JOptionPane.NO_OPTION)
				// skip the stop
				return;
			
		controller.stop();
		getParent().remove(QuerierView.this);
	}
	
}
