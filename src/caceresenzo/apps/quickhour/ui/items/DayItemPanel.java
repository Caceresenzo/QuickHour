package caceresenzo.apps.quickhour.ui.items;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import caceresenzo.apps.quickhour.models.QuickHourDay;
import java.awt.Font;

public class DayItemPanel extends JPanel {
	private JPanel itemPanel;
	
	/**
	 * Create the panel.
	 */
	public DayItemPanel(QuickHourDay quickHourDay) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		itemPanel = new JPanel();
		itemPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JPanel dayPanel = new JPanel();
		dayPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(itemPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
						.addComponent(dayPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(dayPanel, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(itemPanel, GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JLabel dayLabel = new JLabel(quickHourDay.getI18nDayName(), SwingConstants.CENTER);
		dayLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout gl_dayPanel = new GroupLayout(dayPanel);
		gl_dayPanel.setHorizontalGroup(
			gl_dayPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(dayLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
		);
		gl_dayPanel.setVerticalGroup(
			gl_dayPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(dayLabel, GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
		);
		dayPanel.setLayout(gl_dayPanel);
		itemPanel.setLayout(new GridLayout(0, 1, 0, 0));
		setLayout(groupLayout);
		
	}
	public JPanel getItemPanel() {
		return itemPanel;
	}
}
