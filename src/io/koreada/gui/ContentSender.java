package io.koreada.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.koreada.supportfactory.APIFactory;
import io.koreada.util.CommonConst;
import io.koreada.util.CommonUtil;
import io.koreada.util.Debug;
import io.koreada.util.Install;

public class ContentSender { 

	private JFrame frmContentsender;
	private Dialog dialog;
	private JTable jTable;
	protected static Install mInstall = null;
	private APIFactory api = null;
	private JTextField textField;
	private JFileChooser jFileChooser;
	private JPanel panel = null;
	private JButton btnNewButton = null;
	private JSplitPane splitPane = null;
	private JScrollPane scrollPane = null;
	private JPanel panel_1 = null;
	private JTextArea textArea = null;
	
	/**
	 * Launch the application. 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		mInstall = new Install(args);
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//			System.out.println(info.getName()); 
	    	if ("Nimbus".equals(info.getName())) {
	            UIManager.setLookAndFeel(info.getClassName());
//	            break;
	        }
	    }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContentSender window = new ContentSender();
					window.frmContentsender.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ContentSender() {
		initialize();
	}

	public JTable getJTable() {
        if (jTable == null) {
        }
		return jTable;
    }
	
	public void resizeColumnWidth(JTable table) { 
		final TableColumnModel columnModel = table.getColumnModel(); 
		for (int column = 0; column < table.getColumnCount(); column++) { 
			int width = 20; // Min width 
			for (int row = 0; row < table.getRowCount(); row++) { 
				TableCellRenderer renderer = table.getCellRenderer(row, column); 
				Component comp = table.prepareRenderer(renderer, row, column); 
				width = Math.max(comp.getPreferredSize().width +1 , width); 
			} 
			columnModel.getColumn(column).setPreferredWidth(width); 
		}
	}
	
	void clear() {
        DefaultTableModel tableModel = new DefaultTableModel();
        if(this.getJTable().getRowCount() > 0) {
            this.getJTable().setModel(tableModel);   
            this.textArea.setText("");
        }
    }
	
	/**
	 * Initialize the contents of the frame.
	 */
	
	
	private void initialize() {
		frmContentsender = new JFrame();
		frmContentsender.setIconImage(Toolkit.getDefaultToolkit().getImage(mInstall.getLibDir()+File.separator+"CenGoldLogo360.png"));
		frmContentsender.setTitle("Transaction Information Sender");
		frmContentsender.setBounds(0, 0, 1366, 768);
		frmContentsender.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmContentsender.getContentPane().setLayout(new BorderLayout(0, 0));
		frmContentsender.setLocationRelativeTo(null);
		
		panel = new JPanel();
		frmContentsender.getContentPane().add(panel, BorderLayout.NORTH);
		
		textField = new JTextField();
		btnNewButton = new JButton("Select Transaction Information");
		
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(btnNewButton, BorderLayout.EAST);
		panel.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
		frmContentsender.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
        splitPane.setDividerLocation(625);
        
		jTable = new JTable();
		
		panel_1 = new JPanel();
		textArea = new JTextArea();
		
		textArea.setEditable(false);
		
		JButton btnSendToBackend = new JButton("Send to Backend");
		
		jTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textArea.setText(jTable.getValueAt(jTable.getSelectedRow(), 12).toString());
			}
		});
		jTable.setSurrendersFocusOnKeystroke(true);
		jTable.setFillsViewportHeight(true);
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowVerticalLines(true);
		jTable.getTableHeader().setReorderingAllowed(true);
		
		scrollPane.setViewportView(jTable);
		
		textArea.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				int length = textArea.getSelectionEnd() - textArea.getSelectionStart();
				if(length > 0) {
					textArea.copy();
					dialog.showMessage("'"+textArea.getSelectedText()+"' has been copied to the Clipboard");
				} 
			}
		});
		
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.add(btnSendToBackend, BorderLayout.SOUTH);
		panel_1.add(textArea, BorderLayout.CENTER);
		
		splitPane.setRightComponent(panel_1);
		
		ObjectMapper mapper = new ObjectMapper();
		dialog = new Dialog(frmContentsender);
		api = new APIFactory(mInstall);
		
		btnSendToBackend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getJTable().getRowCount() > 0) {
					if(getJTable().getSelectedRowCount()>0){
	                	try {
							if(api.deposit2API("["+mapper.writeValueAsString(jTable.getValueAt(jTable.getSelectedRow(), jTable.getColumnCount()-1))+"]"))
								dialog.showMessage("Delivery success");
							else
								dialog.showMessage("Fail to send");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							dialog.showMessage("Can't send a transaction to BackEnd");
							e1.printStackTrace();
							Debug.traceError(this.getClass().getCanonicalName(), e1, e1.getLocalizedMessage());
						}
	                }else {
	                	dialog.showMessage("Select a row first, Please");
	                }
				}else {
					dialog.showMessage("Read transaction information first, Please");
				}
			}
		});
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				jTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
				
				ArrayList<?> aDatumArr = null;
				BufferedReader bufferedReader = null;
				
				try {
					StringBuffer sb = new StringBuffer();
					String filePath = mInstall.getLogDir() + File.separator + CommonConst.ACCOUNT_INFO_NAME + CommonConst.CURRENT_DIR + CommonConst.JSON_EXTENSION;
//					String filePath = CommonConst.ACCOUNT_INFO_NAME + CommonConst.CURRENT_DIR + CommonConst.JSON_EXTENSION;
					filePath = openAction(e, filePath);
		            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF8"));
		            String currentLine;
		            while((currentLine=bufferedReader.readLine()) != null) {
		            	sb.append(currentLine);
		            } 
		            currentLine = sb.toString();
		            currentLine = currentLine.replaceAll("\\].\\[", ",");
//		            System.out.println(currentLine);
		            aDatumArr = mapper.readValue(currentLine, ArrayList.class);
					
				} catch(Exception ex) {
					dialog.showMessage("The transaction file is not found, Reselect Please");
					ex.printStackTrace();
					Debug.traceError(this.getClass().getCanonicalName(), ex, ex.getLocalizedMessage());
		        }
				
				Vector<?> aDatum = CommonUtil.toVector(aDatumArr);
		        
				Vector<String> aColNames = new Vector<String>();
		        aColNames.add("no");
		        aColNames.add("regData");
		        aColNames.add("widthdraw");
		        aColNames.add("deposit");
		        aColNames.add("balance");
		        aColNames.add("contents");
		        aColNames.add("srcAccNo");
		        aColNames.add("srcBank");
		        aColNames.add("CMSCode");
		        aColNames.add("trType");
		        aColNames.add("nonPay");
		        aColNames.add("hashCode");
		        aColNames.add("msg");
		        
				DefaultTableModel dataModel = getTableModel(aDatum, aColNames); 
				
				jTable.setModel(dataModel);
				TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jTable.getModel());
				jTable.setRowSorter(sorter);
				resizeColumnWidth(jTable);
				
				jTable.getColumnModel().getColumn(jTable.getColumnCount()-1).setWidth(0);
				jTable.getColumnModel().getColumn(jTable.getColumnCount()-1).setMaxWidth(0);
				jTable.getColumnModel().getColumn(jTable.getColumnCount()-1).setMinWidth(0);
			}
		});
	}
	
	private String openAction(java.awt.event.ActionEvent evt, String aFilePath){
		this.jFileChooser = new JFileChooser();
		jFileChooser.setCurrentDirectory(new File(aFilePath));
//		System.out.println(aFilePath);
		jFileChooser.setSelectedFile(new File(aFilePath));
		int ret = jFileChooser.showOpenDialog(this.frmContentsender);
		if (ret !=JFileChooser.APPROVE_OPTION) {
			return null;
		}
		
		File f = jFileChooser.getSelectedFile();
		
		return f.getAbsolutePath();
	}
	
	private DefaultTableModel getTableModel(Vector aDatum, Vector<String> aColNames){
		DefaultTableModel tableModel = new DefaultTableModel(aDatum, aColNames){
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
    	return tableModel;
    }

}
