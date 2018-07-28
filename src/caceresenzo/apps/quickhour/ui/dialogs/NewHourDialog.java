package caceresenzo.apps.quickhour.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.models.ReferenceFormat;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.logger.Logger;
import caceresenzo.libs.parse.ParseUtils;

@SuppressWarnings("rawtypes")
public class NewHourDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static final String ACTION_NEXT_DAY = "action.next_day";
	private static final String ACTION_NEXT_INPUT = "action.next_input";
	private static final String ACTION_VALIDATE_HOUR = "action.validate_hour";
	private static final String ACTION_CLOSE = "action.close";
	
	private static String lastDay = null;
	private static ReferenceFormat lastReferenceFormat = null;
	
	private DialogCallback callback;
	private boolean autoclose, cancelled = false;
	private NewHourDialogResult result = null;
	
	/*
	 * UI
	 */
	private final JPanel contentPanel = new JPanel();
	private JPanel constantPanel;
	private JTextField referenceTextField;
	private JTextField timeTextField;
	private JComboBox<QuickHourUser> userComboBox;
	private JButton validateHourButton;
	private JComboBox<ReferenceFormat> referenceFormatComboBox;
	private JPanel buttonPane;
	private JButton nextEntryButton;
	private JLabel timeLabel;
	private JLabel referenceSelectionLabel;
	private JLabel daySelectionLabel;
	private JButton closeButton;
	private JLabel referenceLabel;
	private JComboBox<String> dayComboBox;
	private JPanel hourEntryPanel;
	private JLabel userSelectionLabel;
	private JButton nextDayButton;
	private JLabel realTimeValueLabel;
	
	/*
	 * Managers
	 */
	private DaySelectionComboBoxCellRenderer daySelectionComboBoxCellRenderer;
	private DaySelectionComboBoxModel daySelectionComboBoxModel;
	private UserSelectionComboBoxCellRenderer userSelectionComboBoxCellRenderer;
	private UserSelectionComboBoxModel userSelectionComboBoxModel;
	private ReferenceSelectionComboBoxCellRenderer referenceFormatComboBoxCellRenderer;
	private ReferenceSelectionComboBoxModel referenceFormatComboBoxModel;
	
	public NewHourDialog(QuickHourUser selectedUser, boolean autoclose) {
		this(selectedUser, autoclose, null);
	}
	
	@SuppressWarnings("unchecked")
	public NewHourDialog(QuickHourUser selectedUser, boolean autoclose, DialogCallback callback) {
		this.autoclose = autoclose;
		this.callback = callback;
		
		setModal(true);
		setBounds(100, 100, 600, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			constantPanel = new JPanel();
			constantPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), i18n.getString("ui.dialog.quick-hour.container.constants"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		}
		
		nextDayButton = new JButton(i18n.getString("ui.dialog.quick-hour.button.next-day"));
		nextDayButton.setActionCommand(ACTION_NEXT_DAY);
		nextDayButton.addActionListener(this);
		
		hourEntryPanel = new JPanel();
		hourEntryPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), i18n.getString("ui.dialog.quick-hour.container.hour-entry"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		validateHourButton = new JButton(i18n.getString("ui.dialog.quick-hour.button.validate-hour"));
		validateHourButton.addActionListener(this);
		validateHourButton.setActionCommand(ACTION_VALIDATE_HOUR);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPanel.createSequentialGroup().addComponent(hourEntryPanel, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addComponent(validateHourButton, GroupLayout.PREFERRED_SIZE, 233, Short.MAX_VALUE).addComponent(nextDayButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addComponent(constantPanel, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)).addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(constantPanel, GroupLayout.PREFERRED_SIZE, 101, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING).addComponent(hourEntryPanel, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE).addGroup(gl_contentPanel.createSequentialGroup().addComponent(validateHourButton, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(nextDayButton, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))).addGap(8)));
		
		referenceLabel = new JLabel(i18n.getString("ui.dialog.quick-hour.label.reference"));
		
		referenceTextField = new JTextField();
		referenceTextField.setColumns(10);
		
		timeTextField = new JTextField();
		timeTextField.setColumns(10);
		
		timeLabel = new JLabel(i18n.getString("ui.dialog.quick-hour.label.time"));
		
		realTimeValueLabel = new JLabel(i18n.getString("ui.dialog.quick-hour.label.time-real-value", "0.0"));
		GroupLayout gl_hourEntryPanel = new GroupLayout(hourEntryPanel);
		gl_hourEntryPanel.setHorizontalGroup(gl_hourEntryPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_hourEntryPanel.createSequentialGroup().addContainerGap().addGroup(gl_hourEntryPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_hourEntryPanel.createSequentialGroup().addGap(10).addComponent(timeTextField, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)).addGroup(gl_hourEntryPanel.createSequentialGroup().addGap(10).addComponent(referenceTextField, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)).addComponent(referenceLabel).addGroup(gl_hourEntryPanel.createSequentialGroup().addComponent(timeLabel).addPreferredGap(ComponentPlacement.RELATED, 71, Short.MAX_VALUE).addComponent(realTimeValueLabel))).addContainerGap()));
		gl_hourEntryPanel.setVerticalGroup(gl_hourEntryPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_hourEntryPanel.createSequentialGroup().addContainerGap().addComponent(referenceLabel).addPreferredGap(ComponentPlacement.RELATED).addComponent(referenceTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(18).addGroup(gl_hourEntryPanel.createParallelGroup(Alignment.BASELINE).addComponent(timeLabel).addComponent(realTimeValueLabel)).addPreferredGap(ComponentPlacement.RELATED).addComponent(timeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		hourEntryPanel.setLayout(gl_hourEntryPanel);
		
		userSelectionLabel = new JLabel(i18n.getString("ui.dialog.quick-hour.label.selection-user"));
		
		referenceSelectionLabel = new JLabel(i18n.getString("ui.dialog.quick-hour.label.selection-reference-format"));
		
		daySelectionLabel = new JLabel(i18n.getString("ui.dialog.quick-hour.label.selection-day"));
		
		dayComboBox = new JComboBox<String>();
		
		userComboBox = new JComboBox<QuickHourUser>();
		
		referenceFormatComboBox = new JComboBox<ReferenceFormat>();
		GroupLayout gl_constantPanel = new GroupLayout(constantPanel);
		gl_constantPanel.setHorizontalGroup(gl_constantPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_constantPanel.createSequentialGroup().addContainerGap().addGroup(gl_constantPanel.createParallelGroup(Alignment.LEADING).addComponent(userSelectionLabel).addComponent(referenceSelectionLabel).addComponent(daySelectionLabel)).addGap(33).addGroup(gl_constantPanel.createParallelGroup(Alignment.LEADING).addComponent(dayComboBox, 0, 214, Short.MAX_VALUE).addComponent(referenceFormatComboBox, 0, 214, Short.MAX_VALUE).addComponent(userComboBox, 0, 214, Short.MAX_VALUE)).addContainerGap()));
		gl_constantPanel.setVerticalGroup(gl_constantPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_constantPanel.createSequentialGroup().addGroup(gl_constantPanel.createParallelGroup(Alignment.BASELINE).addComponent(dayComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(daySelectionLabel)).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_constantPanel.createParallelGroup(Alignment.BASELINE).addComponent(userSelectionLabel).addComponent(userComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_constantPanel.createParallelGroup(Alignment.BASELINE).addComponent(referenceSelectionLabel).addComponent(referenceFormatComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(16)));
		constantPanel.setLayout(gl_constantPanel);
		contentPanel.setLayout(gl_contentPanel);
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				nextEntryButton = new JButton(i18n.getString("ui.dialog.x.button.next-entry"));
				nextEntryButton.setActionCommand(ACTION_NEXT_INPUT);
				nextEntryButton.addActionListener(this);
				buttonPane.add(nextEntryButton);
				getRootPane().setDefaultButton(nextEntryButton);
			}
			{
				closeButton = new JButton(i18n.getString("ui.dialog.x.button.close"));
				closeButton.setActionCommand(ACTION_CLOSE);
				closeButton.addActionListener(this);
				buttonPane.add(closeButton);
			}
		}
		
		/*
		 * Adding to ComboBox
		 */
		
		// Days
		daySelectionComboBoxCellRenderer = new DaySelectionComboBoxCellRenderer();
		dayComboBox.setRenderer(daySelectionComboBoxCellRenderer);
		
		daySelectionComboBoxModel = new DaySelectionComboBoxModel();
		dayComboBox.setModel(daySelectionComboBoxModel);
		
		if (lastDay == null) {
			lastDay = Config.STARTING_DAY;
		}
		
		dayComboBox.setSelectedItem(lastDay);
		
		dayComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				lastDay = (String) daySelectionComboBoxModel.getSelectedItem();
				Logger.info("Selected new Day: " + lastDay);
			}
		});
		
		// Users
		userSelectionComboBoxCellRenderer = new UserSelectionComboBoxCellRenderer();
		userComboBox.setRenderer(userSelectionComboBoxCellRenderer);
		
		userSelectionComboBoxModel = new UserSelectionComboBoxModel(QuickHourManager.getQuickHourManager().getUsers());
		userComboBox.setModel(userSelectionComboBoxModel);
		
		userComboBox.setSelectedItem(selectedUser);
		
		userComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				QuickHourUser selectedUsed = (QuickHourUser) userSelectionComboBoxModel.getSelectedItem();
				Logger.info("Selected new QuickHourUser: " + selectedUsed.getName());
				
				// FIXME: Updating display if new user selected, working but only hour list
				// QuickHourWindow.getQuickHourWindow().selectUser(selectedUsed);
				// QuickHourWindow.getQuickHourWindow().updateUsers();
			}
		});
		
		// References Formats
		referenceFormatComboBoxCellRenderer = new ReferenceSelectionComboBoxCellRenderer();
		referenceFormatComboBox.setRenderer(referenceFormatComboBoxCellRenderer);
		
		referenceFormatComboBoxModel = new ReferenceSelectionComboBoxModel(QuickHourManager.getQuickHourManager().getReferencesFormats());
		referenceFormatComboBox.setModel(referenceFormatComboBoxModel);
		
		if (lastReferenceFormat == null) {
			lastReferenceFormat = QuickHourManager.getQuickHourManager().getReferencesFormats().get(0);
		}
		
		referenceFormatComboBox.setSelectedItem(lastReferenceFormat);
		
		referenceFormatComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				lastReferenceFormat = (ReferenceFormat) referenceFormatComboBoxModel.getSelectedItem();
				Logger.info("Selected new ReferenceFormat: " + lastReferenceFormat.getName());
			}
		});
		
		timeTextField.addKeyListener(timeTextFieldFloatKeyAdapter);
	}
	
	private KeyAdapter timeTextFieldFloatKeyAdapter = new KeyAdapter() {
		/**
		 * All that fucking shit just to check and block more than two coma, for a fucking float number
		 */
		@Override
		public void keyTyped(KeyEvent event) {
			char charactere = event.getKeyChar();
			
			if (charactere == ',') {
				event.setKeyChar(charactere = '.');
			}
			
			boolean dotTest = false;
			String actualString = timeTextField.getText();
			if (actualString == null || actualString.isEmpty() || actualString.split("[\\,\\.]").length < 2) {
				dotTest = true;
			}
			
			if (!dotTest) {
				dotTest = true;
				int count = charactere == '.' ? 1 : 0;
				
				for (char localCharactere : actualString.toCharArray()) {
					if (localCharactere == '.') {
						if (++count > 1) {
							dotTest = false;
							break;
						}
					}
				}
			}
			
			if (!(((charactere >= '0') && (charactere <= '9') || charactere == '.' || (charactere == KeyEvent.VK_BACK_SPACE) || (charactere == KeyEvent.VK_DELETE)) && dotTest)) {
				if (charactere != KeyEvent.VK_ENTER) {
					getToolkit().beep();
				}
				
				event.consume();
			}
			
			if (!event.isConsumed()) {
				realTimeValueLabel.setText(i18n.getString("ui.dialog.quick-hour.label.time-real-value", ParseUtils.parseFloat(actualString + charactere, 0.0F)));
			}
		}
	};
	
	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
			case ACTION_NEXT_INPUT: {
				if (referenceTextField.getText() == null || referenceTextField.getText().isEmpty()) {
					referenceTextField.grabFocus();
				} else if (timeTextField.getText() == null || timeTextField.getText().isEmpty()) {
					timeTextField.grabFocus();
				} else {
					validateHourButton.grabFocus();
				}
				break;
			}
			
			case ACTION_VALIDATE_HOUR: {
				String rawReferenceValue = referenceTextField.getText();
				String rawTimeValue = timeTextField.getText();
				float timeFloat = ParseUtils.parseFloat(rawTimeValue, Float.POSITIVE_INFINITY);
				
				if (rawReferenceValue == null || rawReferenceValue.isEmpty()) {
					Utils.showErrorDialog("ui.dialog.quick-hour.error.field-reference-empty");
					return;
				}
				if (rawTimeValue == null || rawTimeValue.isEmpty()) {
					Utils.showErrorDialog(timeFloat == Float.POSITIVE_INFINITY ? "ui.dialog.quick-hour.error.field-time-invalid-number" : "ui.dialog.quick-hour.error.field-time-empty");
					return;
				}
				
				// Seems to be good at this point
				String dayRawValue = (String) daySelectionComboBoxModel.getSelectedItem();
				QuickHourUser user = (QuickHourUser) userSelectionComboBoxModel.getSelectedItem();
				ReferenceFormat referenceFormat = (ReferenceFormat) referenceFormatComboBoxModel.getSelectedItem();
				
				String formattedReference = String.format(referenceFormat.getFormat(), rawReferenceValue);
				
				// Resolving day
				List<QuickHourDay> days = user.getDays();
				if (days == null) {
					user.applyDays(days = new ArrayList<>());
				}
				
				QuickHourDay day = null;
				for (QuickHourDay localDay : days) {
					if (localDay.getDayName().equalsIgnoreCase(dayRawValue)) {
						day = localDay;
						break;
					}
				}
				
				if (day == null) {
					days.add(day = new QuickHourDay(dayRawValue));
				}
				
				// Resolving references
				List<QuickHourReference> references = day.getReferences();
				
				if (references == null) {
					day.applyReferences(references = new ArrayList<QuickHourReference>());
				}
				
				QuickHourReference reference = null;
				for (QuickHourReference localReference : references) {
					if (localReference.getReference().equalsIgnoreCase(formattedReference)) {
						reference = localReference;
						break;
					}
				}
				
				if (reference == null) {
					references.add(reference = new QuickHourReference(formattedReference).updateHourCount(timeFloat));
				} else {
					reference.addToCount(timeFloat);
				}
				
				Logger.info("New hour added to user \"%s\", day %s, reference \"%s\", new value: %s", user.getName(), day.getDayName(), reference.getReference(), reference.getHourCount());
				
				result = new NewHourDialogResult(user, day, reference);
				if (callback != null) {
					callback.callback(this, true);
				}
				
				if (autoclose) {
					Logger.info("Auto-closing NewHourDialog modal.");
					setVisible(false);
					return;
				}
				
				// Clearing old value
				referenceTextField.setText("");
				
				timeTextField.removeKeyListener(timeTextFieldFloatKeyAdapter);
				timeTextField.setText("");
				timeTextField.addKeyListener(timeTextFieldFloatKeyAdapter);
				
				referenceTextField.grabFocus();
				
				break;
			}
			
			case ACTION_NEXT_DAY: {
				int actualDayIndex = Config.getIndexByDay((String) daySelectionComboBoxModel.getSelectedItem());
				
				if (++actualDayIndex > 6) {
					actualDayIndex = 0;
				}
				
				daySelectionComboBoxModel.setSelectedItem(Config.getDayByIndex(actualDayIndex));
				dayComboBox.repaint();
				
				actionPerformed(new ActionEvent(this, 0, ACTION_NEXT_INPUT));
				break;
			}
			
			case ACTION_CLOSE: {
				cancelled = true;
				setVisible(false);
				break;
			}
			
			default: {
				break;
			}
		}
	}
	
	public boolean hasBeenCancelled() {
		return cancelled;
	}
	
	public NewHourDialogResult getResult() {
		return result;
	}
	
	public static class NewHourDialogResult {
		private final QuickHourUser targetUser;
		private final QuickHourDay quickHourDay;
		private final QuickHourReference newReference;
		
		public NewHourDialogResult(QuickHourUser targetUser, QuickHourDay quickHourDay, QuickHourReference newReference) {
			this.targetUser = targetUser;
			this.quickHourDay = quickHourDay;
			this.newReference = newReference;
		}
		
		public QuickHourUser getTargetUser() {
			return targetUser;
		}
		
		public QuickHourDay getQuickHourDay() {
			return quickHourDay;
		}
		
		public QuickHourReference getNewReference() {
			return newReference;
		}
		
	}
	
	private class DaySelectionComboBoxCellRenderer extends BasicComboBoxRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			String day = (String) value;
			
			setText(String.format("%s (%s)", Config.POSSIBLE_DAYS.get(day), day));
			
			return this;
		}
		
	}
	
	private class DaySelectionComboBoxModel implements ComboBoxModel<String> {
		
		private String selected = null;
		
		@Override
		public String getElementAt(int index) {
			return Config.getDayByIndex(index);
		}
		
		@Override
		public int getSize() {
			return Config.POSSIBLE_DAYS.size();
		}
		
		@Override
		public Object getSelectedItem() {
			return selected;
		}
		
		@Override
		public void setSelectedItem(Object item) {
			selected = (String) item;
		}
		
		@Override
		public void addListDataListener(ListDataListener listDataListener) {
			;
		}
		
		@Override
		public void removeListDataListener(ListDataListener listDataListener) {
			;
		}
		
	}
	
	private class UserSelectionComboBoxCellRenderer extends BasicComboBoxRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			QuickHourUser user = (QuickHourUser) value;
			
			setText(String.format("%s (%s)", user.getDisplay(), user.getName()));
			
			return this;
		}
		
	}
	
	private class UserSelectionComboBoxModel implements ComboBoxModel<QuickHourUser> {
		
		private List<QuickHourUser> users;
		private QuickHourUser selected = null;
		
		public UserSelectionComboBoxModel(List<QuickHourUser> users) {
			super();
			
			this.users = users;
		}
		
		@Override
		public QuickHourUser getElementAt(int index) {
			return users.get(index);
		}
		
		@Override
		public int getSize() {
			return users.size();
		}
		
		@Override
		public Object getSelectedItem() {
			return selected;
		}
		
		@Override
		public void setSelectedItem(Object item) {
			selected = (QuickHourUser) item;
		}
		
		@Override
		public void addListDataListener(ListDataListener listDataListener) {
			;
		}
		
		@Override
		public void removeListDataListener(ListDataListener listDataListener) {
			;
		}
		
	}
	
	private class ReferenceSelectionComboBoxCellRenderer extends BasicComboBoxRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			ReferenceFormat referenceFormat = (ReferenceFormat) value;
			
			setText(String.format("%s: %s", referenceFormat.getDisplay(), referenceFormat.getFormat().replace("%s", "[x]")));
			
			return this;
		}
		
	}
	
	private class ReferenceSelectionComboBoxModel implements ComboBoxModel<ReferenceFormat> {
		
		private List<ReferenceFormat> referencesFormats;
		private ReferenceFormat selected = null;
		
		public ReferenceSelectionComboBoxModel(List<ReferenceFormat> referencesFormats) {
			super();
			
			this.referencesFormats = referencesFormats;
		}
		
		@Override
		public ReferenceFormat getElementAt(int index) {
			return referencesFormats.get(index);
		}
		
		@Override
		public int getSize() {
			return referencesFormats.size();
		}
		
		@Override
		public Object getSelectedItem() {
			return selected;
		}
		
		@Override
		public void setSelectedItem(Object item) {
			selected = (ReferenceFormat) item;
		}
		
		@Override
		public void addListDataListener(ListDataListener listDataListener) {
			;
		}
		
		@Override
		public void removeListDataListener(ListDataListener listDataListener) {
			;
		}
		
	}
	
	public JLabel getRealTimeValueLabel() {
		return realTimeValueLabel;
	}
}
