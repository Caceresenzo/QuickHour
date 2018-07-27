package caceresenzo.apps.quickhour.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.logger.Logger;
import caceresenzo.libs.string.StringUtils;

public class NewUserDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static final String ACTION_OK = "action.ok";
	private static final String ACTION_CANCEL = "action.cancel";
	
	private final JPanel contentPanel = new JPanel();
	private JTextField firstnameTextField;
	private JTextField lastnameTextField;
	private JTextField displayTextField;
	private JTextField keyTextField;
	
	private DialogCallback callback;
	private JLabel keyLabel;
	private JButton okButton;
	private JPanel buttonPane;
	private JButton cancelButton;
	private JLabel lastnameLabel;
	private JPanel autoConfigPanel;
	private JLabel firstnameLabel;
	private JLabel displayLabel;
	private JCheckBox automaticConfigCheckBox;
	
	private String firstname, lastname, display, key;
	
	public NewUserDialog() {
		this(null);
	}
	
	public NewUserDialog(DialogCallback dialogCallback) {
		this.callback = dialogCallback;
		
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		firstnameLabel = new JLabel(i18n.getString("ui.dialog.new-user.label.firstname"));
		
		firstnameTextField = new JTextField();
		firstnameTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent event) {
				updateAutoConfig();
			}
			
			@Override
			public void removeUpdate(DocumentEvent event) {
				updateAutoConfig();
			}
			
			@Override
			public void changedUpdate(DocumentEvent event) {
				updateAutoConfig();
			}
		});
		firstnameTextField.setColumns(10);
		
		lastnameLabel = new JLabel(i18n.getString("ui.dialog.new-user.label.lastname"));
		
		lastnameTextField = new JTextField();
		lastnameTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent event) {
				updateAutoConfig();
			}
			
			@Override
			public void removeUpdate(DocumentEvent event) {
				updateAutoConfig();
			}
			
			@Override
			public void changedUpdate(DocumentEvent event) {
				updateAutoConfig();
			}
		});
		lastnameTextField.setColumns(10);
		
		autoConfigPanel = new JPanel();
		autoConfigPanel.setBorder(new TitledBorder(null, "Configuration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		automaticConfigCheckBox = new JCheckBox(i18n.getString("ui.dialog.new-user.checkbox.auto-config"));
		automaticConfigCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displayLabel.setEnabled(!automaticConfigCheckBox.isSelected());
				displayTextField.setEnabled(!automaticConfigCheckBox.isSelected());
				displayTextField.setEditable(!automaticConfigCheckBox.isSelected());
			}
		});
		automaticConfigCheckBox.setSelected(true);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup().addContainerGap().addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING).addComponent(automaticConfigCheckBox).addComponent(autoConfigPanel, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addGroup(gl_contentPanel.createSequentialGroup().addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addComponent(firstnameLabel).addComponent(lastnameLabel)).addGap(20).addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING).addComponent(firstnameTextField, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE).addComponent(lastnameTextField)))).addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE).addComponent(firstnameLabel).addComponent(firstnameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE).addComponent(lastnameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lastnameLabel)).addPreferredGap(ComponentPlacement.RELATED, 47, Short.MAX_VALUE).addComponent(automaticConfigCheckBox).addPreferredGap(ComponentPlacement.RELATED).addComponent(autoConfigPanel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		
		displayLabel = new JLabel(i18n.getString("ui.dialog.new-user.label.display"));
		displayLabel.setEnabled(false);
		
		displayTextField = new JTextField();
		displayTextField.setEnabled(false);
		displayTextField.setEditable(false);
		displayTextField.setColumns(10);
		
		keyLabel = new JLabel(i18n.getString("ui.dialog.new-user.label.key"));
		keyLabel.setEnabled(false);
		
		keyTextField = new JTextField();
		keyTextField.setEnabled(false);
		keyTextField.setEditable(false);
		keyTextField.setColumns(10);
		GroupLayout gl_autoConfigPanel = new GroupLayout(autoConfigPanel);
		gl_autoConfigPanel.setHorizontalGroup(gl_autoConfigPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_autoConfigPanel.createSequentialGroup().addContainerGap().addGroup(gl_autoConfigPanel.createParallelGroup(Alignment.LEADING).addComponent(keyLabel).addComponent(displayLabel)).addPreferredGap(ComponentPlacement.RELATED, 22, Short.MAX_VALUE).addGroup(gl_autoConfigPanel.createParallelGroup(Alignment.LEADING).addComponent(displayTextField, GroupLayout.PREFERRED_SIZE, 316, GroupLayout.PREFERRED_SIZE).addComponent(keyTextField, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 316, GroupLayout.PREFERRED_SIZE)).addContainerGap()));
		gl_autoConfigPanel.setVerticalGroup(gl_autoConfigPanel.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING, gl_autoConfigPanel.createSequentialGroup().addGroup(gl_autoConfigPanel.createParallelGroup(Alignment.BASELINE).addComponent(displayLabel).addComponent(displayTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_autoConfigPanel.createParallelGroup(Alignment.BASELINE).addComponent(keyLabel).addComponent(keyTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(37, Short.MAX_VALUE)));
		autoConfigPanel.setLayout(gl_autoConfigPanel);
		contentPanel.setLayout(gl_contentPanel);
		
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton(i18n.getString("ui.dialog.x.button.ok"));
				okButton.setActionCommand(ACTION_OK);
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton(i18n.getString("ui.dialog.x.button.cancel"));
				cancelButton.setActionCommand(ACTION_CANCEL);
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
			case ACTION_OK:
				boolean success = false;
				
				display = displayTextField.getText();
				
				if (firstname == null || firstname.isEmpty() || lastname == null || lastname.isEmpty() || display == null || display.isEmpty()) {
					Utils.showErrorDialog("ui.dialog.new-user.error.empty-data");
				} else {
					if (Utils.showWarningConfirm("ui.dialog.new-user.warning.add-confirmation", (lastname + " " + firstname).toUpperCase())) {
						new QuickHourUser(key).withDisplay(display).addToMainManager();
						success = true;
					}
				}

				if (callback != null) {
					callback.callback(this, success);
				}
			case ACTION_CANCEL:
				setVisible(false);
				break;
			
			default:
				break;
		}
	}
	
	public void updateAutoConfig() {
		lastname = lastnameTextField.getText();
		firstname = firstnameTextField.getText();
		
		if (automaticConfigCheckBox.isSelected()) {
			displayTextField.setText(display = String.format("%s %s.", lastname.toUpperCase(), StringUtils.cutIfTooLong(firstname.toUpperCase(), 1)));
		}
		
		keyTextField.setText(key = String.format("%s.%s", lastname.toLowerCase(), firstname.toLowerCase()));
	}
	
}
