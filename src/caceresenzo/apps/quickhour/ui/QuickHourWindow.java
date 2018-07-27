package caceresenzo.apps.quickhour.ui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import caceresenzo.apps.quickhour.codec.implementations.JsonQuickHourFileCodec;
import caceresenzo.apps.quickhour.codec.implementations.JsonUserCodec;
import caceresenzo.apps.quickhour.config.Language;
import caceresenzo.apps.quickhour.models.QuickHourDay;
import caceresenzo.apps.quickhour.models.QuickHourFile;
import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.ui.items.DayItemPanel;
import caceresenzo.apps.quickhour.ui.items.HourItemPanel;
import caceresenzo.apps.quickhour.ui.items.UserItemPanel;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.logger.Logger;
import caceresenzo.libs.thread.ThreadUtils;

public class QuickHourWindow {
	
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
	private JMenu mnNewMenu;
	private JProgressBar mainProgressBar;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					
					QuickHourWindow window = new QuickHourWindow();
					window.frame.setVisible(true);
					
					SwingUtilities.updateComponentTreeUI(window.frame);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 */
	public QuickHourWindow() throws Exception {
		WINDOW = this;
		
		Language.getLanguage().initialize();
		
		initialize();
		
		/*
		 * Loading data
		 */
		List<QuickHourUser> users = new JsonUserCodec().read(new File("config/users.json"));
		QuickHourFile quickHourFile = new JsonQuickHourFileCodec().read(new File("myhour/WEEK 30.qhr"));
		
		/*
		 * Updating UI
		 */
		for (QuickHourUser user : users) {
			userPanel.add(new UserItemPanel(user));
		}
		
		// for (QuickHourUser user : quickHourFile.getUsersHours()) {
		// Logger.info("\t| user: " + user.getName());
		//
		// for (QuickHourDay day : user.getDays()) {
		// Logger.info("\t| \t| day: " + day.getDayName());
		//
		// DayItemPanel dayItemPanel = new DayItemPanel(day);
		// hourPanel.add(dayItemPanel);
		//
		// for (QuickHourReference reference : day.getReferences()) {
		// Logger.info("\t| \t| \t| ref: " + reference.getReference() + " [time: " + reference.getHourCount() + "]");
		// dayItemPanel.getItemPanel().add(new HourItemPanel(reference));
		// }
		// }
		// }
	}
	
	/**
	 * Initialize the contents of the frame.
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
		userContainerPanel.setBorder(BorderFactory.createTitledBorder(i18n.getString("ui.window.user.container.border.title")));
		splitPane.setLeftComponent(userContainerPanel);
		
		newUserButton = new JButton(i18n.getString("ui.window.user.button.new"));
		
		userScrollPane = new JScrollPane();
		GroupLayout gl_userContainerPanel = new GroupLayout(userContainerPanel);
		gl_userContainerPanel.setHorizontalGroup(gl_userContainerPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_userContainerPanel.createSequentialGroup().addContainerGap().addGroup(gl_userContainerPanel.createParallelGroup(Alignment.LEADING).addComponent(userScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE).addComponent(newUserButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
		gl_userContainerPanel.setVerticalGroup(gl_userContainerPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_userContainerPanel.createSequentialGroup().addContainerGap().addComponent(userScrollPane, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(newUserButton).addGap(6)));
		
		userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(0, 1, 0, 0));
		userScrollPane.setViewportView(userPanel);
		userContainerPanel.setLayout(gl_userContainerPanel);
		
		hourContainerPanel = new JPanel();
		hourContainerPanel.setBorder(BorderFactory.createTitledBorder(i18n.getString("ui.window.hour.container.border.title")));
		splitPane.setRightComponent(hourContainerPanel);
		
		hourScrollPane = new JScrollPane();
		
		newHourButton = new JButton(i18n.getString("ui.window.hour.button.new"));
		
		quickHourButton = new JButton(i18n.getString("ui.window.hour.button.new.quick"));
		GroupLayout gl_hourContainerPanel = new GroupLayout(hourContainerPanel);
		gl_hourContainerPanel.setHorizontalGroup(gl_hourContainerPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_hourContainerPanel.createSequentialGroup().addContainerGap().addGroup(gl_hourContainerPanel.createParallelGroup(Alignment.LEADING).addComponent(hourScrollPane, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE).addGroup(gl_hourContainerPanel.createSequentialGroup().addComponent(newHourButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(quickHourButton))).addContainerGap()));
		gl_hourContainerPanel.setVerticalGroup(gl_hourContainerPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_hourContainerPanel.createSequentialGroup().addContainerGap().addComponent(hourScrollPane, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_hourContainerPanel.createParallelGroup(Alignment.BASELINE).addComponent(newHourButton).addComponent(quickHourButton)).addGap(6)));
		
		hourPanel = new JPanel();
		hourScrollPane.setViewportView(hourPanel);
		hourPanel.setLayout(new BoxLayout(hourPanel, BoxLayout.Y_AXIS));
		hourContainerPanel.setLayout(gl_hourContainerPanel);
		
		mainProgressBar = new JProgressBar();
		mainProgressBar.setValue(50);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(splitPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
						.addComponent(mainProgressBar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(mainProgressBar, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
					.addGap(6))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("New menu");
		menuBar.add(mnNewMenu);
		
		/*
		 * Scrollbars
		 */
		userScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		hourScrollPane.getVerticalScrollBar().setUnitIncrement(16);
	}
	
	public static QuickHourWindow getQuickHourWindow() {
		return WINDOW;
	}
	
	public void selectUser(QuickHourUser user) {
		hourPanel.removeAll();
		
		if (user.getDays() == null || user.getDays().isEmpty()) {
			hourPanel.add(new JLabel(i18n.getString("ui.window.hour.container.info.empty-hour")));
		} else {
			for (QuickHourDay day : user.getDays()) {
				Logger.info("\t| \t| day: " + day.getDayName());
				
				DayItemPanel dayItemPanel = new DayItemPanel(day);
				hourPanel.add(dayItemPanel);
				
				for (QuickHourReference reference : day.getReferences()) {
					Logger.info("\t| \t| \t| ref: " + reference.getReference() + " [time: " + reference.getHourCount() + "]");
					dayItemPanel.getItemPanel().add(new HourItemPanel(reference));
				}
			}
		}
		
		new Thread(new Runnable() {
			
			int i = 0;
			@Override
			public void run() {
				while (true) {
					i++;
					
					mainProgressBar.setValue(i);
					
					if (i > 99) {
						i = 0;
					}
					ThreadUtils.sleep(10L);
				}
			}
		}).start();
		
		hourPanel.repaint();
		hourPanel.revalidate();
	}
	public JProgressBar getMainProgressBar() {
		return mainProgressBar;
	}
}