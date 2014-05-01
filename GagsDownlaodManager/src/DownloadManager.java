import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import mysql.JsonArrayCreater;
import mysql.MySQLAccess;

// The Download Manager.
public class DownloadManager extends JFrame implements Observer {
    
    // Add download text field.
    private JTextField addTextField;
    
    // Download table's data model.
    private DownloadsTableModel tableModel;
    
    // Table listing downloads.
    private JTable table;
    
    // These are the buttons for managing the selected download.
    private JButton pauseButton, resumeButton;
    private JButton cancelButton, clearButton;
    
    // Currently selected download.
    private Download selectedDownload;
    
    // Flag for whether or not table selection is being cleared.
    private boolean clearing;
    
    // Constructor for Download Manager.
    public DownloadManager() {
        // Set application title.
        setTitle("Download Manager");
        
        // Set window size.
        setSize(640, 480);
        
        // Handle window closing events.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });
        
        // Set up file menu.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileExitMenuItem = new JMenuItem("Exit",
                KeyEvent.VK_X);
        fileExitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });
        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        // Set up add panel.
        JPanel addPanel = new JPanel();
        addTextField = new JTextField(30);
        addPanel.add(addTextField);
        JButton addButton = new JButton("Add Download");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionAdd();
            }
        });
        addPanel.add(addButton);
        
        // Set up Downloads table.
        tableModel = new DownloadsTableModel();
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(new
                ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        });
        // Allow only one row at a time to be selected.
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set up ProgressBar as renderer for progress column.
        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true); // show progress text
        table.setDefaultRenderer(JProgressBar.class, renderer);
        
        // Set table's row height large enough to fit JProgressBar.
        table.setRowHeight(
                (int) renderer.getPreferredSize().getHeight());
        
        // Set up downloads panel.
        JPanel downloadsPanel = new JPanel();
        downloadsPanel.setBorder(
                BorderFactory.createTitledBorder("Downloads"));
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(table),
                BorderLayout.CENTER);
        
        // Set up buttons panel.
        JPanel buttonsPanel = new JPanel();
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPause();
            }
        });
        pauseButton.setEnabled(false);
        buttonsPanel.add(pauseButton);
        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionResume();
            }
        });
        resumeButton.setEnabled(false);
        buttonsPanel.add(resumeButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });
        cancelButton.setEnabled(false);
        buttonsPanel.add(cancelButton);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionClear();
            }
        });
        clearButton.setEnabled(false);
        buttonsPanel.add(clearButton);
        
        // Add panels to display.
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(addPanel, BorderLayout.NORTH);
        getContentPane().add(downloadsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    // Exit this program.
    private void actionExit() {
        System.exit(0);
    }
    
    // Add a new download.
    private void actionAdd() {
      /*  URL verifiedUrl = verifyUrl(addTextField.getText());
        if (verifiedUrl != null) {
            tableModel.addDownload(new Download(verifiedUrl));
            addTextField.setText(""); // reset add text field
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
        }*/
		
//		final int SIZE_OF_IT = 3;
//		String[] myURL = new String[SIZE_OF_IT+1];
//		String[] myURL = createNewArray(addTextField.getText());
//		myURL[0] = "http://www.oxforduniversityimages.com/images/rotate/image_m14_10.jpg";
//		myURL[1] = "http://static3.businessinsider.com/image/52cddfb169beddee2a6c2246/the-29-coolest-us-air-force-images-of-the-year.jpg";
//		myURL[2] = "http://blog.gettyimages.com/wp-content/uploads/2013/04/The-Renewables-Project_London-Exhibit_April-2013_Toby-Smith_Reportage-by-Getty-Images.jpg";
//		myURL[3] = "http://lolsnaps.com/upload_pic/Goodguylocalartist-66831.jpg";
//				
//		for(int i=0; i<=10; i++) {
//			 URL verifiedUrl = verifyUrl(myURL[i]);
//			if (verifiedUrl != null) {
//				tableModel.addDownload(new Download(verifiedUrl));
//			} else {
//				JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
//			}
//		}
    	createNewArray("");
    }
    
    
	
    private void createNewArray(String fileName) {
		// TODO Auto-generated method stub        
        ArrayList<String> myList = new ArrayList<String>();
        ArrayList<HashMap<String, String>> myBigList = new ArrayList<HashMap<String,String>>();
    	MySQLAccess msacces = new MySQLAccess();
    	JsonArrayCreater jsonArrayCreater = new JsonArrayCreater();
    	try {
			myList = msacces.getJsonArrayFormTable("gags", "gagking", "jsons");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				for(int i=0; i<=myList.size(); i++) {
					myBigList = jsonArrayCreater.getGagListFromString(myList.get(i));					
					for(int j=0; j<=2; j++) {
						URL verifiedUrl = verifyUrl(myBigList.get(j).get(JsonArrayCreater.TAG_SRC));
						if (verifiedUrl != null) {
							tableModel.addDownload(new Download(verifiedUrl,myBigList.get(j).get(JsonArrayCreater.TAG_ALT)));
						} else {
							JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}catch (Exception e) {
				
			}
		}
	}

    
	// Verify download URL.
    private URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://"))
            return null;
        
        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
        
        // Make sure URL specifies a file.
        if (verifiedUrl.getFile().length() < 2)
            return null;
        
        return verifiedUrl;
    }
    
    // Called when table row selection changes.
    private void tableSelectionChanged() {
    /* Unregister from receiving notifications
       from the last selected download. */
        if (selectedDownload != null)
            selectedDownload.deleteObserver(DownloadManager.this);
        
    /* If not in the middle of clearing a download,
       set the selected download and register to
       receive notifications from it. */
        if (!clearing) {
            selectedDownload = tableModel.getDownload(table.getSelectedRow());
            selectedDownload.addObserver(DownloadManager.this);
            updateButtons();
        }
    }
    
    // Pause the selected download.
    private void actionPause() {
        selectedDownload.pause();
        updateButtons();
    }
    
    // Resume the selected download.
    private void actionResume() {
        selectedDownload.resume();
        updateButtons();
    }
    
    // Cancel the selected download.
    private void actionCancel() {
        selectedDownload.cancel();
        updateButtons();
    }
    
    // Clear the selected download.
    private void actionClear() {
        clearing = true;
        tableModel.clearDownload(table.getSelectedRow());
        clearing = false;
        selectedDownload = null;
        updateButtons();
    }
    
  /* Update each button's state based off of the
     currently selected download's status. */
    private void updateButtons() {
        if (selectedDownload != null) {
            int status = selectedDownload.getStatus();
            switch (status) {
                case Download.DOWNLOADING:
                    pauseButton.setEnabled(true);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(false);
                    break;
                case Download.PAUSED:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(false);
                    break;
                case Download.ERROR:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
                    break;
                default: // COMPLETE or CANCELLED
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
            }
        } else {
            // No download is selected in table.
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(false);
            cancelButton.setEnabled(false);
            clearButton.setEnabled(false);
        }
    }
    
  /* Update is called when a Download notifies its
     observers of any changes. */
    public void update(Observable o, Object arg) {
        // Update buttons if the selected download has changed.
        if (selectedDownload != null && selectedDownload.equals(o))
            updateButtons();
    }
    
    // Run the Download Manager.
    public static void main(String[] args) {
        DownloadManager manager = new DownloadManager();
        manager.show();
    }
}