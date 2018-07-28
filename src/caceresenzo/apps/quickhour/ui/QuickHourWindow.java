package caceresenzo.apps.quickhour.ui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import caceresenzo.apps.quickhour.handler.ClosingHandler;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.ui.dialogs.DialogCallback;
import caceresenzo.apps.quickhour.ui.dialogs.NewHourDialog;
import caceresenzo.apps.quickhour.ui.dialogs.NewHourDialog.NewHourDialogResult;
import caceresenzo.apps.quickhour.ui.dialogs.NewUserDialog;
import caceresenzo.apps.quickhour.ui.items.DayItemPanel;
import caceresenzo.apps.quickhour.ui.items.HourItemPanel;
import caceresenzo.apps.quickhour.ui.items.UserItemPanel;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.logger.Logger;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class QuickHourWindow implements ActionListener {
	
	private static final String ACTION_MENU_FILE_NEW = "action.menu.file.new";
	private static final String ACTION_MENU_FILE_OPEN = "action.menu.file.open";
	private static final String ACTION_MENU_FILE_SAVE = "action.menu.file.save";
	private static final String ACTION_MENU_FILE_SAVEAS = "action.menu.file.saveas";
	
	private static final String ACTION_USER_NEW = "action.user.new";
	private static final String ACTION_HOUR_NEW = "action.hour.new";
	private static final String ACTION_HOUR_QUICK = "action.hour.QUICK";
	
	private static QuickHourWindow WINDOW;
	
	private JFrame frame;
	private JSplitPane splitPane;
	private JPanel userContainerPanel;
	private JPanel hourContainerPanel;
	private JScrollPane userScrollPane;
	private JButton newUserButton;
	private JButton quickHourButton;
	private JScrollPane hourScrollPane;
	private JButton newHourButton;
	private JPanel userPanel;
	private JPanel hourPanel;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JProgressBar mainProgressBar;
	
	private QuickHourUser selectedUser;
	private JMenuItem fileSaveMenuItem;
	private JMenuItem fileOpenMenuItem;
	private JMenuItem fileNewMenuItem;
	private JMenuItem fileSaveAsMenuItem;
	private JMenuItem fileCloseMenuItem;
	private JMenu quickHourMenu;
	private JMenuItem quickHourQuitApplicationItemMenu;
	
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					
					WINDOW = new QuickHourWindow();
					WINDOW.initialize();
					WINDOW.frame.setVisible(true);
					
					SwingUtilities.updateComponentTreeUI(WINDOW.frame);
					
					WINDOW.updateUsers();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 854, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.3);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		splitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		userContainerPanel = new JPanel();
		userContainerPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), i18n.getString("ui.window.user.container.border.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setLeftComponent(userContainerPanel);
		
		newUserButton = new JButton(i18n.getString("ui.window.user.button.new"));
		newUserButton.setActionCommand(ACTION_USER_NEW);
		newUserButton.addActionListener(this);
		
		userScrollPane = new JScrollPane();
		GroupLayout gl_userContainerPanel = new GroupLayout(userContainerPanel);
		gl_userContainerPanel.setHorizontalGroup(gl_userContainerPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_userContainerPanel.createSequentialGroup().addContainerGap().addGroup(gl_userContainerPanel.createParallelGroup(Alignment.LEADING).addComponent(userScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE).addComponent(newUserButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
		gl_userContainerPanel.setVerticalGroup(gl_userContainerPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_userContainerPanel.createSequentialGroup().addContainerGap().addComponent(userScrollPane, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(newUserButton).addGap(6)));
		
		userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(0, 1, 0, 0));
		userScrollPane.setViewportView(userPanel);
		userContainerPanel.setLayout(gl_userContainerPanel);
		
		hourContainerPanel = new JPanel();
		hourContainerPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), i18n.getString("ui.window.hour.container.border.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane.setRightComponent(hourContainerPanel);
		
		hourScrollPane = new JScrollPane();
		
		newHourButton = new JButton(i18n.getString("ui.window.hour.button.new"));
		newHourButton.setActionCommand(ACTION_HOUR_NEW);
		newHourButton.addActionListener(this);
		
		quickHourButton = new JButton(i18n.getString("ui.window.hour.button.new.quick"));
		quickHourButton.setActionCommand(ACTION_HOUR_QUICK);
		quickHourButton.addActionListener(this);
		GroupLayout gl_hourContainerPanel = new GroupLayout(hourContainerPanel);
		gl_hourContainerPanel.setHorizontalGroup(gl_hourContainerPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_hourContainerPanel.createSequentialGroup().addContainerGap().addGroup(gl_hourContainerPanel.createParallelGroup(Alignment.LEADING).addComponent(hourScrollPane, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE).addGroup(gl_hourContainerPanel.createSequentialGroup().addComponent(newHourButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(quickHourButton))).addContainerGap()));
		gl_hourContainerPanel.setVerticalGroup(gl_hourContainerPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_hourContainerPanel.createSequentialGroup().addContainerGap().addComponent(hourScrollPane, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_hourContainerPanel.createParallelGroup(Alignment.BASELINE).addComponent(newHourButton).addComponent(quickHourButton)).addGap(6)));
		
		hourPanel = new JPanel();
		hourScrollPane.setViewportView(hourPanel);
		hourPanel.setLayout(new BoxLayout(hourPanel, BoxLayout.Y_AXIS));
		hourContainerPanel.setLayout(gl_hourContainerPanel);
		
		mainProgressBar = new JProgressBar();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(splitPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE).addComponent(mainProgressBar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup().addContainerGap().addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(mainProgressBar, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE).addGap(6)));
		frame.getContentPane().setLayout(groupLayout);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		quickHourMenu = new JMenu("QuickHour");
		menuBar.add(quickHourMenu);
		
		quickHourQuitApplicationItemMenu = new JMenuItem("Quit application");
		quickHourQuitApplicationItemMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		quickHourQuitApplicationItemMenu.setIcon(new ImageIcon(QuickHourWindow.class.getResource("/caceresenzo/assets/icons/shutdown-48.png")));
		quickHourMenu.add(quickHourQuitApplicationItemMenu);
		
		fileMenu = new JMenu(i18n.getString("menu.file.title"));
		menuBar.add(fileMenu);
		
		fileNewMenuItem = new JMenuItem(i18n.getString("menu.file.item.new"));
		fileNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		fileNewMenuItem.setIcon(new ImageIcon(QuickHourWindow.class.getResource("/caceresenzo/assets/icons/add-new-48.png")));
		fileNewMenuItem.setActionCommand(ACTION_MENU_FILE_NEW);
		fileNewMenuItem.addActionListener(this);
		fileMenu.add(fileNewMenuItem);
		
		fileOpenMenuItem = new JMenuItem(i18n.getString("menu.file.item.open"));
		fileOpenMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		fileOpenMenuItem.setIcon(new ImageIcon(QuickHourWindow.class.getResource("/caceresenzo/assets/icons/open-48.png")));
		fileOpenMenuItem.setActionCommand(ACTION_MENU_FILE_OPEN);
		fileOpenMenuItem.addActionListener(this);
		fileMenu.add(fileOpenMenuItem);
		
		fileSaveMenuItem = new JMenuItem(i18n.getString("menu.file.item.save"));
		fileSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		fileSaveMenuItem.setIcon(new ImageIcon(QuickHourWindow.class.getResource("/caceresenzo/assets/icons/save-48.png")));
		fileSaveMenuItem.setActionCommand(ACTION_MENU_FILE_SAVE);
		fileSaveMenuItem.addActionListener(this);
		fileMenu.add(fileSaveMenuItem);
		
		fileSaveAsMenuItem = new JMenuItem(i18n.getString("menu.file.item.save-as"));
		fileSaveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));
		fileSaveAsMenuItem.setIcon(new ImageIcon(QuickHourWindow.class.getResource("/caceresenzo/assets/icons/save-as-48.png")));
		fileSaveAsMenuItem.setActionCommand(ACTION_MENU_FILE_SAVEAS);
		fileSaveAsMenuItem.addActionListener(this);
		fileMenu.add(fileSaveAsMenuItem);
		
		fileCloseMenuItem = new JMenuItem("Close file");
		fileCloseMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		fileCloseMenuItem.setIcon(new ImageIcon(QuickHourWindow.class.getResource("/caceresenzo/assets/icons/multiply-48.png")));
		fileMenu.add(fileCloseMenuItem);
		
		/*
		 * Scrollbars
		 */
		userScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		hourScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		/*
		 * Actions
		 */
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				Logger.info("Closing main window...");
				
				event.getWindow().dispose();
				
				ClosingHandler.handleClose();
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
			case ACTION_USER_NEW: {
				NewUserDialog newUserDialog = new NewUserDialog(new DialogCallback() {
					@Override
					public void callback(JDialog dialog, boolean hasSuccess) {
						if (hasSuccess) {
							updateUsers();
							
							if (selectedUser != null) {
								selectUser(selectedUser);
								UserItemPanel.updateColorSelection(selectedUser);
							}
						}
					}
				});
				
				newUserDialog.setVisible(true);
				break;
			}
			
			case ACTION_HOUR_NEW: {
				lunchHourAdder(false);
				break;
			}
			
			case ACTION_HOUR_QUICK: {
				lunchHourAdder(true);
				break;
			}
			
			default: {
				Utils.showErrorDialog("error.featrure.not-implemented", event.getActionCommand());
				break;
			}
		}
	}
	
	public static QuickHourWindow getQuickHourWindow() {
		return WINDOW;
	}
	
	public void updateUsers() {
		userPanel.removeAll();
		
		UserItemPanel.destroyInstances();
		
		for (QuickHourUser user : QuickHourManager.getQuickHourManager().sort().getUsers()) {
			userPanel.add(new UserItemPanel(user));
		}
		
		UserItemPanel.updateColorSelection(selectedUser);
		
		userPanel.repaint();
		userPanel.revalidate();
	}
	
	public void selectUser(QuickHourUser user) {
		this.selectedUser = user;
		
		hourPanel.removeAll();
		
		if (user.getDays() == null || user.getDays().isEmpty()) {
			hourPanel.add(new JLabel(i18n.getString("ui.window.hour.container.info.empty-hour")));
		} else {
			for (QuickHourDay day : user.getDays()) {
				Logger.info("\t| DAY: " + day.getI18nDayName());
				
				DayItemPanel dayItemPanel = new DayItemPanel(day);
				hourPanel.add(dayItemPanel);
				
				for (QuickHourReference reference : day.getReferences()) {
					Logger.info("\t| \t| REF: " + reference.getReference() + " [TIME= " + reference.getHourCount() + "]");
					dayItemPanel.getItemPanel().add(new HourItemPanel(reference));
				}
			}
		}
		
		hourPanel.repaint();
		hourPanel.revalidate();
	}
	
	public void lunchHourAdder(boolean loop) {
		if (selectedUser == null) {
			Utils.showErrorDialog("ui.dialog.quick-hour.error.no-user");
			return;
		}
		
		NewHourDialog newHourDialog = new NewHourDialog(selectedUser, !loop, new DialogCallback() {
			@Override
			public void callback(JDialog dialog, boolean hasSuccess) {
				if (!hasSuccess) {
					return;
				}
				
				handleNewHourDialogResult(((NewHourDialog) dialog).getResult());
			}
		});
		newHourDialog.setVisible(true); // Locking operation
		
		if (!newHourDialog.hasBeenCancelled()) {
			handleNewHourDialogResult(newHourDialog.getResult());
		}
	}
	
	private void handleNewHourDialogResult(NewHourDialogResult result) {
		if (result != null) {
			Logger.info("updating: " + result.getTargetUser());
			selectUser(result.getTargetUser());
			updateUsers();
		}
	}
	
}