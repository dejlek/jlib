package com.areen.jlib.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;

import com.areen.jlib.gui.accordion.*;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class AccordionTestPanel extends JPanel implements ActionListener {
	AccordionPrefs ap;
	Accordion a;
	
	public AccordionTestPanel() {
		super();
		
		JPanel p1 = new JPanel();
		p1.setBackground(Color.YELLOW);
		JButton button = new JButton("SAVE prefs");
		button.addActionListener(this);
		button.setActionCommand("save");
		p1.add(button);
		
		JButton button2 = new JButton("LOAD prefs");
		button2.addActionListener(this);
		button2.setActionCommand("load");
		p1.add(button2);
		
		JPanel p2 = new JPanel();
		p2.setBackground(Color.GREEN);
		
		JPanel p3 = new JPanel();
		p3.setBackground(Color.RED);
		
		JPanel p4 = new JPanel();
		p4.setBackground(Color.WHITE);
		
		
		JPanel p5 = new JPanel();
		p5.setBackground(Color.BLUE);

		boolean horizontal = true;
		
		TitledPane t1 = new TitledPane("bla", p1, horizontal);
		t1.setMinimumSize(new Dimension(100, 100));
		t1.setPreferredSize(new Dimension(35, 35));
		
		TitledPane t2 = new TitledPane("sra", p2, horizontal);
		t2.setMinimumSize(new Dimension(100, 100));
		t2.setPreferredSize(new Dimension(35, 35));
		
		TitledPane t3 = new TitledPane("3432", p3, horizontal);
		t3.setPreferredSize(new Dimension(230, 100));
		t3.setMinimumSize(new Dimension(100, 100));
		
		TitledPane t4 = new TitledPane("gfhgh", p4, horizontal);
		t4.setPreferredSize(new Dimension(210, 35));
		t4.setMinimumSize(new Dimension(100, 100));
		
		TitledPane t5 = new TitledPane("bnmbnmdf", p5, horizontal);
		t5.setPreferredSize(new Dimension(35, 35));
		t5.setMinimumSize(new Dimension(100, 100));
		
		a = new Accordion(horizontal, t1);
		a.addPane(t2);
		a.addPane(t3);
		a.addPane(t4);
		a.addPane(t5);
		a.setTitleBackground(Color.PINK);
	
		ap = new AccordionPrefs(a, new HashMap<String, String>(), "/test_dialog");
		

		TitledPane t6 = new TitledPane("Horizontal", a, false);
		t6.setPreferredSize(new Dimension(35, 35));
		t6.setMinimumSize(new Dimension(100, 100));

		JPanel p7 = new JPanel();
		p7.setBackground(Color.PINK);
		TitledPane t7 = new TitledPane("other corizontal", p7, false);
		a.setPreferredSize(new Dimension(a.getModel().calculateTotalMinimumWidth(), 
                        a.getModel().calculateGreatestMinimumHeight()));
		Accordion a2 = new Accordion(false, t6, t7);
		a2.setFixedSize(t6, true, null);
		JScrollPane pane = new JScrollPane(a2);
		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
		
	//	System.out.println(a.getMinimumSize());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		if (e.getActionCommand().equals("save")) {
			ap.updatePrefs();
		} else if (e.getActionCommand().equals("load")) {
			ap.loadPrefs();
		}
	}
}
