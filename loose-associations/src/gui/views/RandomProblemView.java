package gui.views;

import gui.controllers.RandomProblemController;

import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class RandomProblemView.
 */
public class RandomProblemView extends BaseProblemView {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1571695411212881068L;
	
	/** The text attributes. */
	private JTextField textAttributes;
	
	/** The text max value. */
	private JTextField textMaxValue;	
	
	/** The text tuples. */
	private JTextField textTuples;

	/**
	 * Create the panel.
	 *
	 * @param controller the controller
	 */
	public RandomProblemView(RandomProblemController controller) {
		super(controller);
		
		SpringLayout sl_input = new SpringLayout();
		input.setLayout(sl_input);
		
		JLabel lblTuples = new JLabel("number of tuples");
		input.add(lblTuples);
		
		textTuples = new JTextField();
		textTuples.setToolTipText("numer of tuples in the generated table");
		sl_input.putConstraint(SpringLayout.NORTH, textTuples, -2, SpringLayout.NORTH, lblTuples);
		sl_input.putConstraint(SpringLayout.EAST, textTuples, -10, SpringLayout.EAST, input);
		input.add(textTuples);
		textTuples.setColumns(10);
		
		JLabel lblInput = new JLabel("random input configuration");
		sl_input.putConstraint(SpringLayout.NORTH, lblTuples, 15, SpringLayout.SOUTH, lblInput);
		sl_input.putConstraint(SpringLayout.WEST, lblTuples, 0, SpringLayout.WEST, lblInput);
		sl_input.putConstraint(SpringLayout.NORTH, lblInput, 12, SpringLayout.NORTH, this);
		sl_input.putConstraint(SpringLayout.WEST, lblInput, 10, SpringLayout.WEST, this);
		lblInput.setFont(new Font("Dialog", Font.BOLD, 12));
		input.add(lblInput);
		
		JLabel lblAttributes = new JLabel("number of attributes");
		sl_input.putConstraint(SpringLayout.NORTH, lblAttributes, 15, SpringLayout.SOUTH, lblTuples);
		sl_input.putConstraint(SpringLayout.WEST, lblAttributes, 10, SpringLayout.WEST, input);
		input.add(lblAttributes);
		
		textAttributes = new JTextField();
		textAttributes.setToolTipText("number of columns in the generated table");
		sl_input.putConstraint(SpringLayout.WEST, textAttributes, 30, SpringLayout.EAST, lblAttributes);
		sl_input.putConstraint(SpringLayout.WEST, textTuples, 0, SpringLayout.WEST, textAttributes);
		sl_input.putConstraint(SpringLayout.NORTH, textAttributes, -2, SpringLayout.NORTH, lblAttributes);
		sl_input.putConstraint(SpringLayout.EAST, textAttributes, -10, SpringLayout.EAST, input);
		input.add(textAttributes);
		textAttributes.setColumns(10);
		
		JLabel lblMaxValue = new JLabel("maximum value");
		sl_input.putConstraint(SpringLayout.NORTH, lblMaxValue, 15, SpringLayout.SOUTH, lblAttributes);
		sl_input.putConstraint(SpringLayout.WEST, lblMaxValue, 10, SpringLayout.WEST, input);
		input.add(lblMaxValue);
		
		textMaxValue = new JTextField();
		textMaxValue.setToolTipText("maximum number generable by the generator. All the table cells will be filled with number in the range from 0 to this variable");
		sl_input.putConstraint(SpringLayout.NORTH, textMaxValue, -2, SpringLayout.NORTH, lblMaxValue);
		sl_input.putConstraint(SpringLayout.WEST, textMaxValue, 0, SpringLayout.WEST, textAttributes);
		sl_input.putConstraint(SpringLayout.EAST, textMaxValue, -10, SpringLayout.EAST, input);
		input.add(textMaxValue);
		textMaxValue.setColumns(10);

	}
	
	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#setFieldsEnabled(boolean)
	 */
	@Override
	protected void setFieldsEnabled(boolean enabled) {
		super.setFieldsEnabled(enabled);
		textAttributes.setEnabled(enabled);
		textMaxValue.setEnabled(enabled);
		textTuples.setEnabled(enabled);
	}
	
	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#createTable()
	 */
	@Override
	protected void createTable() throws Exception {
		((RandomProblemController) controller).createTable(textTuples.getText(), textAttributes.getText(), textMaxValue.getText());
	}

	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#getProblemName()
	 */
	@Override
	protected String getProblemName() {
		return new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
	}
	
	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#getInputXml(org.w3c.dom.Document)
	 */
	@Override
	public Element getInputXml(Document doc) {
		Element input = doc.createElement("input");
		input.setAttribute("tuples", textTuples.getText());
		input.setAttribute("attributes", textAttributes.getText());
		input.setAttribute("maxvalue", textMaxValue.getText());
		return input;
	}
	
	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#loadInputFromXml(org.w3c.dom.Element)
	 */
	@Override
	public void loadInputFromXml(Element input) throws Exception {
		textTuples.setText(input.getAttribute("tuples"));
		textAttributes.setText(input.getAttribute("attributes"));
		textMaxValue.setText(input.getAttribute("maxvalue"));
	}

	/* (non-Javadoc)
	 * @see gui.views.BaseProblemView#getProblemType()
	 */
	@Override
	protected String getProblemType() {
		return "random";
	}

}
