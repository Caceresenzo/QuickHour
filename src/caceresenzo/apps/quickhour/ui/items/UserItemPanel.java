package caceresenzo.apps.quickhour.ui.items;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;

import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.ui.QuickHourWindow;
import caceresenzo.libs.logger.Logger;
import java.awt.Font;

public class UserItemPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Color COLOR_BASE = new Color(240, 240, 240);
	private static final Color COLOR_SELECTED = new Color(209, 240, 240);
	
	public static final List<UserItemPanel> INSTANCES = new ArrayList<UserItemPanel>();
	
	private float hourCount = 0.0F;
	
	private QuickHourUser user;
	private JLabel totalHourLabel;
	private JLabel nameLabel;
	
	public UserItemPanel(QuickHourUser user) {
		INSTANCES.add(this);
		
		this.user = user;
		
		setSize(120, 49);
		setMaximumSize(getSize());
		
		nameLabel = new JLabel(user.getDisplay());
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		totalHourLabel = new JLabel(String.valueOf(hourCount = QuickHourManager.getQuickHourManager().countHour(user)));
		totalHourLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		totalHourLabel.setForeground(hourCount == Config.TARGET_HOUR_COUNT ? Color.GREEN : Color.RED);
		
		JSeparator separator = new JSeparator();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addGap(10).addComponent(nameLabel, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE).addComponent(totalHourLabel).addContainerGap()).addComponent(separator, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addGap(11).addComponent(nameLabel, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)).addGroup(groupLayout.createSequentialGroup().addComponent(totalHourLabel).addGap(11))).addGap(13).addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)));
		setLayout(groupLayout);
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				;
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Logger.info("Clicked on user: " + UserItemPanel.this.user);
				
				updateColorSelection(UserItemPanel.this.user);
				
				QuickHourWindow.getQuickHourWindow().selectUser(UserItemPanel.this.user);
			}
		});
	}
	
	public static void updateColorSelection(QuickHourUser selectedUser) {
		for (UserItemPanel userPanel : INSTANCES) {
			if (userPanel.getUser() == selectedUser) {
				userPanel.setForeground(COLOR_SELECTED);
				userPanel.setBackground(COLOR_SELECTED);
				continue;
			}
			
			userPanel.setForeground(COLOR_BASE);
			userPanel.setBackground(COLOR_BASE);
		}
	}
	
	public QuickHourUser getUser() {
		return user;
	}
	
	public static void destroyInstances() {
		INSTANCES.clear();
	}
	
}