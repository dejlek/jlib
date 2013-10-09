package com.areen.jlib.test;

import com.areen.jlib.gui.VoList;
import com.areen.jlib.gui.model.VoListModel;
import com.areen.jlib.gui.renderer.VoListCellRenderer;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class VoListTest extends JPanel {

	/**
	 * Create the panel.
	 */
	public VoListTest() {
        super();
        
		setLayout(new BorderLayout(0, 0));
		
		JButton btnNewButton = new JButton("New button");
		add(btnNewButton, BorderLayout.SOUTH);
		
        DefaultListModel model2 = new DefaultListModel(); // here just so i can ctrl-click on it and see...
        VoListModel<User> model = new VoListModel<User>();
        model.add(new User("LED01", "ITC", "Dejan Lekic", "blabla"));
        model.add(new User("NUM01", "ITC", "Mehjabeen Nujurally", "labla"));
        model.add(new User("DYM01", "ITC", "Mateusz Dykiert", "labla"));
        model.add(new User("DRD01", "ITC", "David Driscoll", "labla"));
        
		final VoList<User> list = new VoList<User>(model);
        list.setCellRenderer(new VoListCellRenderer<User>("<b>$2</b><br/>$0, $1, $3"));
        
		add(list, BorderLayout.CENTER);
	}
} // VoListTest class

// $Id$
