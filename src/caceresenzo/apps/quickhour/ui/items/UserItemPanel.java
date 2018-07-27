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

import caceresenzo.apps.quickhour.manager.QuickHourManager;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.apps.quickhour.ui.QuickHourWindow;
import caceresenzo.libs.logger.Logger;

public class UserItemPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Color COLOR_BASE = new Color(240, 240, 240);
	private static final Color COLOR_SELECTED = new Color(209, 240, 240);
	
	public static final List<UserItemPanel> INSTANCES = new ArrayList<UserItemPanel>();
	
	private QuickHourUser user;
	private JLabel totalHourLabel;
	private JLabel nameLabel;
	
	public UserItemPanel(QuickHourUser user) {
		INSTANCES.add(this);
		
		this.user = user;
		
		setSize(120, 49);
		setMaximumSize(getSize());
		
		nameLabel = new JLabel(user.getDisplay());
		
		totalHourLabel = new JLabel(String.valueOf(QuickHourManager.getQuickHourManager().countHour(user)));
		
		JSeparator separator = new JSeparator();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addComponent(nameLabel, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
					.addComponent(totalHourLabel)
					.addContainerGap())
				.addComponent(separator, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(11)
							.addComponent(nameLabel, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(totalHourLabel)
							.addGap(11)))
					.addGap(13)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE))
		);
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
				
				updateColorSelection();
				
				QuickHourWindow.getQuickHourWindow().selectUser(UserItemPanel.this.user);
			}
		});
	}
	
	public void updateColorSelection() {
		for (UserItemPanel user : INSTANCES) {
			if (user == this) {
				continue;
			}
			
			user.setForeground(COLOR_BASE);
			user.setBackground(COLOR_BASE);
		}

		setForeground(COLOR_SELECTED);
		setBackground(COLOR_SELECTED);
	}
	
	public QuickHourUser getUser() {
		return user;
	}
}