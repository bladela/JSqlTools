package gui.events;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTabbedPane;

import gui.frames.TabPanel;

public class MainWindowEventAdapter extends WindowAdapter {
	JTabbedPane tabbedPane;
	public MainWindowEventAdapter(JTabbedPane panes)
	{
		super();
		this.tabbedPane=panes;		
	}
	public void windowClosing(WindowEvent winEvt) {
		for(int i=0;i<tabbedPane.getTabCount();i++)
		{
			if(tabbedPane.getComponentAt(i).getClass()==TabPanel.class)
			{
				TabPanel tab=((TabPanel)tabbedPane.getComponentAt(i));
				if(tab!=null)
					tab.disconnect();
			}
		}
		System.exit(0);
	}
}
