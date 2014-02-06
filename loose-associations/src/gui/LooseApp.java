package gui;

import gui.controllers.Controller;
import gui.controllers.QuerierController;
import gui.controllers.RandomProblemController;
import gui.controllers.RealProblemController;
import gui.views.BaseProblemView;
import gui.views.QuerierView;
import gui.views.RandomProblemView;
import gui.views.RealProblemView;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class LooseApp is the one that launch the gui.
 */
public class LooseApp {

	/** The main frame loose associations. */
	private JFrame frmLooseAssociations;
	
	/** The tab. */
	private int tab = 0;
	
	/** The controllers. */
	Map<Integer, Controller> controllers = new HashMap<Integer, Controller>();
	
	/** The tabbed pane. */
	private JTabbedPane tabbedPane;
	
	/** The no tabs info. */
	private JPanel noTabsInfo;

	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LooseApp window = new LooseApp();
					
					// The following two commands center the frame in the screen
					window.frmLooseAssociations.pack();
					window.frmLooseAssociations.setLocationRelativeTo(null);
					window.frmLooseAssociations.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LooseApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLooseAssociations = new JFrame();
		frmLooseAssociations.setTitle("Loose Associations");
		frmLooseAssociations.setResizable(true);
		frmLooseAssociations.setMinimumSize(new Dimension(800, 560));
		frmLooseAssociations.setSize(800, 560);
		
		JPanel cards = new JPanel();
		frmLooseAssociations.getContentPane().add(cards, BorderLayout.CENTER);
		cards.setLayout(new CardLayout(0, 0));
		
		noTabsInfo = new JPanel();
		cards.add(noTabsInfo, BorderLayout.CENTER);
		noTabsInfo.setLayout(null);
		
		JLabel lblNoTabsInto = new JLabel("Add a new problem from the menu to start working");
		lblNoTabsInto.setForeground(Color.GRAY);
		lblNoTabsInto.setHorizontalAlignment(SwingConstants.CENTER);
		lblNoTabsInto.setBounds(12, 12, 770, 528);
		noTabsInfo.add(lblNoTabsInto);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 794, 552);
		tabbedPane.setVisible(false);
		cards.add(tabbedPane, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		frmLooseAssociations.setJMenuBar(menuBar);
		
		JMenu mnProblems = new JMenu("Problems");
		menuBar.add(mnProblems);
		
		JMenuItem mntmNewProblem = new JMenuItem("New real problem");
		mntmNewProblem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createRealDatabasePanel();
			}
		});
		mntmNewProblem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnProblems.add(mntmNewProblem);
		
		JMenuItem mntmNewRandomProblem = new JMenuItem("New random problem");
		mntmNewRandomProblem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mnProblems.add(mntmNewRandomProblem);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("Loose Problems (*.xml)", "xml"));
				int returnVal = chooser.showOpenDialog(frmLooseAssociations);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(chooser.getSelectedFile());
						Element problem = (Element) doc.getElementsByTagName("problem").item(0);
						BaseProblemView view;
						if (problem.getAttribute("type").equalsIgnoreCase("random"))
							view = createRandomDatabasePanel();
						else
							view = createRealDatabasePanel();
						view.loadFromXML(doc);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(frmLooseAssociations,
								"Error Parsing XML\n" + e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnProblems.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnProblems.add(mntmSave);
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component selected = tabbedPane.getSelectedComponent();
				if (selected instanceof BaseProblemView) {
					((BaseProblemView) selected).saveToXml();
				} else {
					JOptionPane.showMessageDialog(frmLooseAssociations,
							"Nothing to save on this tab", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		JMenu mnUtilities = new JMenu("Utilities");
		menuBar.add(mnUtilities);
		
		JMenuItem mntmNewQuerier = new JMenuItem("New querier");
		mntmNewQuerier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createQuerierView();
			}
		});
		mntmNewQuerier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnUtilities.add(mntmNewQuerier);
		mntmNewRandomProblem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createRandomDatabasePanel();
			}
		});
		

		frmLooseAssociations.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmLooseAssociations.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				// check if some instance is still running
				int running = 0;
				for (Controller controller: controllers.values())
					if (controller.isRunning())
						++running;
				
				if (running != 0) {
					int resultVal = JOptionPane.showConfirmDialog(frmLooseAssociations,
							running + " tabs still working. Do you want to stop them and exit?",
							"Tabs still working", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (resultVal == JOptionPane.YES_OPTION) {
						// call stop on every controller
						for (Controller controller: controllers.values())
							controller.stop();
					} else
						// skip the exit
						return;
				}
				
				System.exit(0);
			}
		});
		
	}
	
	/**
	 * Creates the real database panel.
	 *
	 * @return the base problem view
	 */
	private synchronized BaseProblemView createRealDatabasePanel() {
		RealProblemController controller = new RealProblemController(++tab);
		BaseProblemView view = new RealProblemView(controller);
		controller.addLoggerObserver(view);
		controllers.put(tab, controller);
		tabbedPane.addTab(tab + ". real", view);
		tabbedPane.setSelectedComponent(view);
		tabbedPane.setVisible(true);
		noTabsInfo.setVisible(false);
		return view;
	}
	
	/**
	 * Creates the random database panel.
	 *
	 * @return the base problem view
	 */
	private synchronized BaseProblemView createRandomDatabasePanel() {
		RandomProblemController controller = new RandomProblemController(++tab);
		BaseProblemView view = new RandomProblemView(controller);
		controller.addLoggerObserver(view);
		controllers.put(tab, controller);
		tabbedPane.addTab(tab + ". random", view);
		tabbedPane.setSelectedComponent(view);
		tabbedPane.setVisible(true);
		noTabsInfo.setVisible(false);
		return view;
	}
	
	/**
	 * Creates the querier view.
	 *
	 * @return the querier view
	 */
	private synchronized QuerierView createQuerierView() {
		QuerierController controller = new QuerierController(++tab);
		QuerierView view = new QuerierView(controller);
		controller.addLoggerObserver(view);
		controllers.put(tab, controller);
		tabbedPane.add(tab + ". querier", view);
		tabbedPane.setSelectedComponent(view);
		tabbedPane.setVisible(true);
		noTabsInfo.setVisible(false);
		return view;
	}
}
