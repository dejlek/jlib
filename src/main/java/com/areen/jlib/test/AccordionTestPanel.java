package com.areen.jlib.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.areen.jlib.gui.accordion.*;


public class AccordionTestPanel extends JPanel {

	public AccordionTestPanel() {
		super();
		
		JPanel p1 = new JPanel();
		p1.setBackground(Color.YELLOW);
		
		JPanel p2 = new JPanel();
		p2.setBackground(Color.GREEN);
		
		JPanel p3 = new JPanel();
		p3.setBackground(Color.RED);
		
		JPanel p4 = new JPanel();
		p4.setBackground(Color.WHITE);
		
		
		JPanel p5 = new JPanel();
		p5.setBackground(Color.BLUE);

		boolean horizontal = false;
		
		TitledPane t1 = new TitledPane("bla", p1, horizontal);
		t1.setMinimumSize(new Dimension(35, 35));
		
		TitledPane t2 = new TitledPane("sra", p2, horizontal);
		t2.setMinimumSize(new Dimension(35, 35));
		
		TitledPane t3 = new TitledPane("3432", p3, horizontal);
		t3.setMinimumSize(new Dimension(35, 35));
		
		TitledPane t4 = new TitledPane("gfhgh", p4, horizontal);
		t4.setMinimumSize(new Dimension(35, 35));
		
		TitledPane t5 = new TitledPane("bnmbnmdf", p5, horizontal);
		t5.setMinimumSize(new Dimension(35, 35));
		
		Accordion acc = new Accordion(horizontal, t1, t2, t3, t4, t5);
		
	//	JScrollPane pane = new JScrollPane(acc);
		setLayout(new BorderLayout());
		add(acc, BorderLayout.CENTER);
	}
}
