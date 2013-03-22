package com.areen.jlib.test;

import java.awt.BorderLayout;
import java.awt.Color;
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
		
		TitledPane t2 = new TitledPane("sra", p2, horizontal);
		
		TitledPane t3 = new TitledPane("3432", p3, horizontal);
		
		TitledPane t4 = new TitledPane("gfhgh", p4, horizontal);
		
		TitledPane t5 = new TitledPane("bnmbnmdf", p5, horizontal);

		
		a = new Accordion(horizontal);
		a.add(t1);
		a.add(t2);
		a.add(t3);
		a.add(t4);
		a.add(t5);
	//	a.setTitleBackground(Color.PINK);
	
		ap = new AccordionPrefs(a, new HashMap<String, String>(), "/test_dialog");
		
		
		Accordion verticalAccordion = new Accordion(false);
		verticalAccordion.add(a, "Horizontal");
		JScrollPane pane = new JScrollPane(verticalAccordion);
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
