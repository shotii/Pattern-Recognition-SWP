package gui;

import javax.swing.JFrame;

import java.awt.GridLayout;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.garret.perst.Storage;

import algorithm.AbstractAlgorithm;
import algorithm.KMeanAlgorithm;
import algorithm.KnnAlgorithm;
import algorithm.PrototypeManager;
import converter.AbstractConverter;
import converter.CsvConv;
import converter.MinstConv;
import converter.PngConv;
import data.Cluster;
import data.Example;
import data.LearnData;
import data.Statistic;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.DropMode;
import javax.swing.JScrollPane;

public class Gui {

	public JFrame frame;
	private JTextField txtK;
	private JTextField readimageClassification;
	private JTextField txtIndex;
	private JTextField textViewClassification;
	private Storage db;
	private LearnData learndata;
	private AbstractConverter Converter;
	private JFileChooser imageDialog = new JFileChooser();
	private JFileChooser saveDialog = new JFileChooser();
	private JFileChooser pathDialog = new JFileChooser();
	private String readimageOpenDialogPath = null;
	private String labelsPath;
	private String imagesPath;
	private JLabel readimagePreview;
	private JTextField txtFromParse;
	private JTextField txtToParse;
	private AbstractAlgorithm algorithm;
	private JTextField txtMaxIterations;
	private JTextField txtConvergenceFactor;
	private JButton btnRun;
	private JButton btnErkenneKlasse;
	private JButton btnReclassify;
	private int loadImageIndex;
	private JTextField txtClusterClass;
	private JTextField txtClusterNo;
	private Cluster[] KMeanClusters;
	private JLabel lblNumberOfClusters;
	private JButton btnClusterLaden;
	private JButton btnNextCluster;
	private JButton btnClassifyCluster;
	private JLabel lblClusterPreview;
	private int clustersLength;
	private int clusterIndex;
	private JButton btnExportieren = new JButton("Export");
	private JButton btnParseMinst = new JButton("einlesen");
	private String title;
	private JButton btnPrevCluster;
	private JButton btnPrevImage;
	private JButton btnNextImage;
	private Statistic stats = Statistic.getInstance();
	private String lastStat = stats.toString();
	private int evaluatedClassification = 0;
	

	/**
	 * Create the application.
	 */
	public Gui(Storage db, LearnData learndata, String title) {
		this.db = db;
		this.learndata = learndata;
		this.title = title;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "serial" })
	private void initialize() {
		
		// Bildfilter
        String[] bildFilterEXT = {"png", "csv"};
        String bildFilterNAME = "*.png, *.csv";
        FileFilter bildFilter = new FileNameExtensionFilter(bildFilterNAME, bildFilterEXT);
        imageDialog.setFileFilter(bildFilter);
        saveDialog.setFileFilter(bildFilter);
		
		
		
		frame = new JFrame(title);
		frame.setBounds(100, 100, 701, 437);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                db.close();
            }

        });
		
		ButtonGroup RadioAlgorithm = new ButtonGroup();
		
		ButtonGroup RadioDistance = new ButtonGroup();
		
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabs);
		
		////////////////////////////////////////////////////////////////////////
		// Lerndaten einlesen
		
		JPanel panel_ReadData = new JPanel();
		tabs.addTab("Lerndaten einlesen", null, panel_ReadData, null);
		panel_ReadData.setLayout(null);
		
		final JLabel lblLabelsFilePath = new JLabel("");
		lblLabelsFilePath.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblLabelsFilePath.setBounds(28, 81, 183, 16);
		panel_ReadData.add(lblLabelsFilePath);
		
		JButton btnOpenLabels = new JButton( new AbstractAction("Labels File") {
			public void actionPerformed( ActionEvent e ) {
				int rueckgabeWert = pathDialog.showOpenDialog(null);
		        if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
		            try {
		            	lblLabelsFilePath.setText("File: " + pathDialog.getSelectedFile().getName());
						labelsPath = pathDialog.getSelectedFile().getCanonicalPath();
					} catch (IOException e1) {
						e1.printStackTrace();
					} 
		        } else {
		        	lblLabelsFilePath.setText(null);
		        	labelsPath = null;
		        }
		        checkLoadedPaths();
	        }
		});
		btnOpenLabels.setText("Labels Datei");
		btnOpenLabels.setBounds(28, 57, 116, 26);
		panel_ReadData.add(btnOpenLabels);
		
		final JLabel lblImageDataPath = new JLabel("");
		lblImageDataPath.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblImageDataPath.setBounds(28, 123, 183, 16);
		panel_ReadData.add(lblImageDataPath);
		
		JButton btnOpenImages = new JButton( new AbstractAction("Image Data") {
			public void actionPerformed( ActionEvent e ) {
				int rueckgabeWert = pathDialog.showOpenDialog(null);
		        if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
		            try {
		            	lblImageDataPath.setText("File: " + pathDialog.getSelectedFile().getName());
						imagesPath = pathDialog.getSelectedFile().getCanonicalPath();
					} catch (IOException e1) {
						e1.printStackTrace();
					} 
		        } else {
		        	lblImageDataPath.setText(null);
		        	imagesPath = null;
		        }
		        checkLoadedPaths();
	        }
		});
		btnOpenImages.setText("Bilddaten");
		btnOpenImages.setBounds(28, 95, 116, 26);
		panel_ReadData.add(btnOpenImages);
		
		txtFromParse = new JTextField();
		txtFromParse.setBounds(28, 167, 114, 20);
		panel_ReadData.add(txtFromParse);
		txtFromParse.setColumns(10);
		txtFromParse.setText("0");
		
		txtToParse = new JTextField();
		txtToParse.setBounds(30, 206, 114, 20);
		panel_ReadData.add(txtToParse);
		txtToParse.setColumns(10);
		txtToParse.setText("100");
		
		// Parse MINST
		btnParseMinst.addActionListener( new AbstractAction("einlesen") {
			public void actionPerformed( ActionEvent e ) {
				long from = Long.parseLong(txtFromParse.getText(), 10);
				long to = Long.parseLong(txtToParse.getText(), 10);
				Converter = new MinstConv();
				try {
					learndata.addExmaples(Converter.importFile(labelsPath, imagesPath, from, to));
					checkBtnRunState();
					checkLoadImageIndex();
				} catch (IOException e1) {e1.printStackTrace();}
	        }
		});
		btnParseMinst.setText("Einlesen");
		btnParseMinst.setBounds(28, 256, 116, 26);
		checkLoadedPaths();
		panel_ReadData.add(btnParseMinst);
		
		
		JLabel lblMinstImport = new JLabel("MINST import:");
		lblMinstImport.setBounds(28, 29, 98, 16);
		panel_ReadData.add(lblMinstImport);
		
		JLabel lblAbIndexNr = new JLabel("AB index Nr. ...");
		lblAbIndexNr.setBounds(29, 151, 115, 16);
		panel_ReadData.add(lblAbIndexNr);
		
		JLabel lblBisIndexNr = new JLabel("BIS index Nr. ...");
		lblBisIndexNr.setBounds(29, 190, 115, 16);
		panel_ReadData.add(lblBisIndexNr);
		
		final JLabel lblImportPath = new JLabel("");
		lblImportPath.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblImportPath.setBounds(239, 81, 183, 16);
		panel_ReadData.add(lblImportPath);
		
		JButton btnClearData = new JButton("DB leeren");
		btnClearData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Iterator i = learndata.intKeyIndex.iterator();
				
				while (i.hasNext()) {
					Example ex = (Example)i.next();
					i.remove();
					ex.deallocate();
				}
				learndata.clearExamples();
				checkBtnRunState();
				checkLoadImageIndex();
			}
		});
		btnClearData.setBounds(28, 321, 116, 26);
		panel_ReadData.add(btnClearData);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(168, 13, 500, 345);
		panel_ReadData.add(scrollPane);
		
		JTextArea txtrReadmeFor = new JTextArea();
		txtrReadmeFor.setBackground(Color.DARK_GRAY);
		txtrReadmeFor.setForeground(Color.WHITE);
		scrollPane.setViewportView(txtrReadmeFor);
		txtrReadmeFor.setWrapStyleWord(true);
		txtrReadmeFor.setLineWrap(true);
		txtrReadmeFor.setFont(new Font("Consolas", Font.PLAIN, 11));
		txtrReadmeFor.setEditable(false);
		txtrReadmeFor.setText("\tREADME FOR MACHINELEARNER v1.0\r\n\t\tby Anton Semjo, Famara Kassama, Sazan Hoti\r\n\r\n\t\t\r\n1. Der erste Schritt bei der Benutzung des MachineLearners ist es, Daten im Minst-Format einzulesen und anzugeben, wie viele der tatsaechlich vorhandenen Bilder eingelesen werden sollen. Dazu waehlt man den ersten Tab \"Lerndaten einlesen\" aus, waehlt ueber \"Labels Datei\" und \"Lerndaten\" die jeweiligen laedt die Dateien aus. Labels sowie die Images und gibt den Bereich der Minstdatei ein, aus dem die Images und die zugehoerigen Labels genommen werden sollen. Hat man Daten in der lokalen Datenbankdatei gespeichert, so kann man diesen ersten Schritt einfach ueberspringen oder die Datenbankdatei leeren und sie wie oben beschrieben neu belegen. Zusaetzlich hat man die M\u0161glichkeit weitere Lerndaten ueber den oben genanneten ersten Schritt in die Datenbank einzulesen.\r\n\r\n2. Als naechstes waehlt man seinen Algorithmus unter dem Tab \"Algorithmen\" aus sowie die dazugehoerige Distanzmessfunktion und die weiteren Parameter, die fuer den jeweiligen gewaehlten Algorithmus benoetigt werden. Darauffolgend klickt man den Button \"Run\" und der Algorithmus startet seine Lernprozedur. Beim KMean-Algorithmus muss man an dieser Stelle nach der Lernprozedur noch die Cluster klassifizieren.\r\n\r\n3. Unter dem Tab \"Anzeigen\" kann man sich die geladenen Images mit dem zugehoerigen Index als Bild anzeigen lassen, indem man einfach den Index eingibt oder ueber die Pfeile einen auswaehlt und auf den \"Laden\" Button drueckt. Hier laesst sich eine falsche Klassifizierung auch korrigieren und man kann diese korrigierte/nicht-korrigierte Datei als .png oder .csv Datei exportieren, dazu klickt man einfach auf den Button \"Export\".\r\n   \r\n4. Hat man nach Schritt 2 den Algorithmus lernen lassen und moechte ihn auf die Probe stellen, so kann man das mit dem 4. und letzten Tab namens \"Bild einlesen\" erreichen. Durch den Button \".csv / .png oeffnen\" laedt man zunaechst das zu klassifizierende Bild und laesst es durch den in Schritt 2 ausgewaehlten Algorithmus klassifizieren, indem man auf den Button \"Erkenne Klasse\" drueckt. Wurde dieser richtig klassifiziert, so laesst er sich auch zu der Lerndatenmenge des Algorithmus hinzufuegen.\r\n\r\n5. Unter dem Tab \"Statistiken\" hat der User die Moeglichkeit Statistiken des aktuellen und wenn bereits vorhanden des vorherigen Algorithmusdurchlaufs anzuzeigen.");
		
		
		///////////////////////////////////////////////////////////////////////
		// Algorithmen ausführen
		
		JPanel panel_Learner = new JPanel();
		tabs.addTab("Algorithmen", null, panel_Learner, null);
		panel_Learner.setLayout(null);
		
		
		JLabel lblChooseAlgorithm = new JLabel("Method:");
		lblChooseAlgorithm.setBounds(12, 12, 45, 16);
		panel_Learner.add(lblChooseAlgorithm);
		
		final JRadioButton rdbtnKnn = new JRadioButton("K-NearestNeighbor");
		rdbtnKnn.setBounds(12, 25, 133, 24);
		panel_Learner.add(rdbtnKnn);
		rdbtnKnn.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnKnn.setSelected(true);
		
		final JRadioButton rdbtnKmean = new JRadioButton("K-Mean");
		rdbtnKmean.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (rdbtnKmean.isSelected()) {
					txtMaxIterations.setEnabled(true);
					txtConvergenceFactor.setEnabled(true);
				} else {
					txtMaxIterations.setEnabled(false);
					txtConvergenceFactor.setEnabled(false);
				}
			}
		});
		rdbtnKmean.setBounds(12, 46, 68, 24);
		panel_Learner.add(rdbtnKmean);
		rdbtnKmean.setHorizontalAlignment(SwingConstants.LEFT);
		RadioAlgorithm.add(rdbtnKmean);
		RadioAlgorithm.add(rdbtnKnn);
		
		JLabel lblChooseDistanceMeasurement = new JLabel("Distance:");
		lblChooseDistanceMeasurement.setBounds(12, 78, 53, 16);
		panel_Learner.add(lblChooseDistanceMeasurement);
		
		final JRadioButton rdbtnEuclidean = new JRadioButton("Euclidean");
		rdbtnEuclidean.setBounds(12, 91, 80, 24);
		panel_Learner.add(rdbtnEuclidean);
		rdbtnEuclidean.setSelected(true);
		
		final JRadioButton rdbtnManhattan = new JRadioButton("Manhattan");
		rdbtnManhattan.setBounds(12, 112, 85, 24);
		panel_Learner.add(rdbtnManhattan);
		RadioDistance.add(rdbtnEuclidean);
		RadioDistance.add(rdbtnManhattan);
		
		JLabel lblK = new JLabel("k:");
		lblK.setHorizontalAlignment(SwingConstants.RIGHT);
		lblK.setBounds(12, 146, 35, 16);
		panel_Learner.add(lblK);
		
		txtK = new JTextField();
		txtK.setText("10");
		txtK.setBounds(52, 144, 35, 20);
		panel_Learner.add(txtK);
		txtK.setColumns(10);
		
		JLabel lblMaxIt = new JLabel("MaxIterations:");
		lblMaxIt.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMaxIt.setBounds(12, 199, 124, 16);
		panel_Learner.add(lblMaxIt);
		
		txtMaxIterations = new JTextField();
		txtMaxIterations.setText("10");
		txtMaxIterations.setColumns(10);
		txtMaxIterations.setBounds(143, 199, 35, 20);
		txtMaxIterations.setEnabled(false);
		panel_Learner.add(txtMaxIterations);
		
		JLabel lblConvergence = new JLabel("ConvergenceFactor:");
		lblConvergence.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConvergence.setBounds(12, 229, 124, 16);
		panel_Learner.add(lblConvergence);
		
		txtConvergenceFactor = new JTextField();
		txtConvergenceFactor.setText("5");
		txtConvergenceFactor.setColumns(10);
		txtConvergenceFactor.setBounds(143, 229, 35, 20);
		txtConvergenceFactor.setEnabled(false);
		panel_Learner.add(txtConvergenceFactor);
		
		
		btnRun = new JButton("Run!");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lastStat = stats.toString();
				stats.reset();
				// select algorithm
				if (rdbtnKnn.isSelected()) {
					algorithm = new KnnAlgorithm();
					stats.setUsedAlgorithm("K-NearestNeighbor");
				} else if (rdbtnKmean.isSelected()) {
					int Iterations = Integer.parseInt(txtMaxIterations.getText());
					int Convergence = Integer.parseInt(txtConvergenceFactor.getText());
					algorithm = new KMeanAlgorithm(Iterations, Convergence);
					stats.setUsedAlgorithm("K-Mean");
				}
				stats.setLearnObjectCounter(learndata.getExamples().size());
				if (rdbtnEuclidean.isSelected()) {
					stats.setDistFunction("Euklidische Distanz");
				} else if (rdbtnManhattan.isSelected()) {
					stats.setDistFunction("Manhattan Distanz");
				}
				try {
					algorithm.doLearn(learndata, Integer.parseInt(txtK.getText()), rdbtnEuclidean.isSelected(), rdbtnManhattan.isSelected());
					checkAlgorithmState();
					if (rdbtnKmean.isSelected()){
						startClusterClassification();
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnRun.setBounds(52, 318, 59, 26);
		checkBtnRunState();		
		panel_Learner.add(btnRun);
		
		lblClusterPreview = new JLabel("");
		lblClusterPreview.setIcon(new ImageIcon(Gui.class.getResource("/javax/swing/plaf/basic/icons/image-failed.png")));
		lblClusterPreview.setHorizontalAlignment(SwingConstants.CENTER);
		lblClusterPreview.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblClusterPreview.setBounds(292, 14, 329, 280);
		panel_Learner.add(lblClusterPreview);
		
		JLabel lblClusterClass = new JLabel("Klassifizierung:");
		lblClusterClass.setBounds(302, 295, 98, 16);
		panel_Learner.add(lblClusterClass);
		
		txtClusterClass = new JTextField();
		txtClusterClass.setColumns(10);
		txtClusterClass.setBounds(405, 293, 59, 20);
		panel_Learner.add(txtClusterClass);
		
		btnClassifyCluster = new JButton("Klassifizieren");
		btnClassifyCluster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int classification = Integer.parseInt(txtClusterClass.getText());
				KMeanClusters[clusterIndex-1].classifyCluster(classification);
				int index = Integer.parseInt(txtClusterNo.getText())+1;
				loadCluster(index);
			}
		});
		btnClassifyCluster.setBounds(476, 290, 124, 26);
		panel_Learner.add(btnClassifyCluster);
		
		JLabel lblClusterNo = new JLabel("Cluster:");
		lblClusterNo.setBounds(322, 330, 55, 16);
		panel_Learner.add(lblClusterNo);
		
		txtClusterNo = new JTextField();
		txtClusterNo.setBounds(379, 328, 53, 20);
		panel_Learner.add(txtClusterNo);
		txtClusterNo.setColumns(10);
		
		btnClusterLaden = new JButton("Laden");
		btnClusterLaden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadCluster(Integer.parseInt(txtClusterNo.getText()));
			}
		});
		btnClusterLaden.setBounds(521, 328, 85, 16);
		panel_Learner.add(btnClusterLaden);
		
		btnNextCluster = new JButton("");
		btnNextCluster.setIcon( new RotatedIcon(new ImageIcon(Gui.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")), RotatedIcon.Rotate.UP));
		btnNextCluster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = Integer.parseInt(txtClusterNo.getText())+1;
				loadCluster(index);
			}
		});
		btnNextCluster.setBounds(614, 330, 28, 12);
		panel_Learner.add(btnNextCluster);
		
		lblNumberOfClusters = new JLabel("/ x");
		lblNumberOfClusters.setBounds(434, 330, 53, 16);
		panel_Learner.add(lblNumberOfClusters);
		
		btnPrevCluster = new JButton("");
		btnPrevCluster.setIcon( new RotatedIcon(new ImageIcon(Gui.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")), RotatedIcon.Rotate.DOWN));
		btnPrevCluster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = Integer.parseInt(txtClusterNo.getText())-1;
				loadCluster(index);
			}
		});
		btnPrevCluster.setBounds(486, 330, 28, 12);
		panel_Learner.add(btnPrevCluster);
		
		
		
		
		////////////////////////////////////////////////////////////////////////////////
		// Bild anzeigen
		
		JPanel panel_View = new JPanel();
		tabs.addTab("Anzeigen / Exportieren", null, panel_View, null);
		panel_View.setLayout(null);
		
		txtIndex = new JTextField("0");
		txtIndex.setBounds(12, 62, 59, 20);
		panel_View.add(txtIndex);
		txtIndex.setColumns(10);
		
		JLabel lblIndex = new JLabel("Index:");
		lblIndex.setHorizontalAlignment(SwingConstants.LEFT);
		lblIndex.setBounds(12, 44, 83, 16);
		panel_View.add(lblIndex);
		
		JLabel lblClassification_2 = new JLabel("Klassifizierung:");
		lblClassification_2.setBounds(235, 309, 93, 16);
		panel_View.add(lblClassification_2);
		
		final JLabel lblShowimage = new JLabel("none selected");
		lblShowimage.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblShowimage.setHorizontalAlignment(SwingConstants.CENTER);
		lblShowimage.setIcon(new ImageIcon(Gui.class.getResource("/javax/swing/plaf/basic/icons/image-failed.png")));
		lblShowimage.setBounds(235, 12, 329, 280);
		panel_View.add(lblShowimage);
		
		textViewClassification = new JTextField();
		textViewClassification.setBounds(333, 307, 59, 20);
		panel_View.add(textViewClassification);
		textViewClassification.setColumns(10);
		
		btnReclassify = new JButton("Klassifizierung korrigieren");
		btnReclassify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int classification = Integer.parseInt(textViewClassification.getText());
				learndata.getSpecificExample(loadImageIndex).setClassification(classification);
			}
		});
		btnReclassify.setBounds(404, 304, 199, 26);
		checkLoadImageIndex();
		panel_View.add(btnReclassify);
		
		JButton btnLoadIndex = new JButton("Laden");
		btnLoadIndex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImageIndex = Integer.parseInt(txtIndex.getText());
				if ( loadImageIndex < 0 || loadImageIndex >= learndata.examplesCount()){
					lblShowimage.setText("Invalid index! / Nothing here!");
					lblShowimage.setIcon(null);
					textViewClassification.setText(null);
				} else {
					Example tempex = learndata.getSpecificExample(loadImageIndex);
					lblShowimage.setIcon(imageLoader.exportImageIcon(tempex));
					lblShowimage.setText(null);
					textViewClassification.setText(Integer.toString(tempex.getClassification()));
				}
				checkLoadImageIndex();
			}
		});
		btnLoadIndex.setBounds(28, 94, 82, 26);
		panel_View.add(btnLoadIndex);
		
		btnNextImage = new JButton("");
		btnNextImage.setIcon( new RotatedIcon(new ImageIcon(Gui.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")), RotatedIcon.Rotate.UP));
		btnNextImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImageIndex = Integer.parseInt(txtIndex.getText())+1;
				txtIndex.setText(Integer.toString(loadImageIndex));
				if ( loadImageIndex < 0 || loadImageIndex >= learndata.examplesCount()){
					lblShowimage.setText("Invalid index! / Nothing here!");
					lblShowimage.setIcon(null);
					textViewClassification.setText(null);
				} else {
					Example tempex = learndata.getSpecificExample(loadImageIndex);
					lblShowimage.setIcon(imageLoader.exportImageIcon(tempex));
					lblShowimage.setText(null);
					textViewClassification.setText(Integer.toString(tempex.getClassification()));
				}
				checkLoadImageIndex();
			}
		});
		btnNextImage.setBounds(112, 94, 14, 26);
		panel_View.add(btnNextImage);
		
		btnPrevImage = new JButton("");
		btnPrevImage.setIcon( new RotatedIcon(new ImageIcon(Gui.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")), RotatedIcon.Rotate.DOWN));
		btnPrevImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImageIndex = Integer.parseInt(txtIndex.getText())-1;
				txtIndex.setText(Integer.toString(loadImageIndex));
				if ( loadImageIndex < 0 || loadImageIndex >= learndata.examplesCount()){
					lblShowimage.setText("Invalid index! / Nothing here!");
					lblShowimage.setIcon(null);
					textViewClassification.setText(null);
				} else {
					Example tempex = learndata.getSpecificExample(loadImageIndex);
					lblShowimage.setIcon(imageLoader.exportImageIcon(tempex));
					lblShowimage.setText(null);
					textViewClassification.setText(Integer.toString(tempex.getClassification()));
				}
				checkLoadImageIndex();
			}
		});
		btnPrevImage.setBounds(12, 94, 14, 26);
		panel_View.add(btnPrevImage);
		
		JLabel lblExportiereAls = new JLabel("Exportiere als:");
		lblExportiereAls.setBounds(12, 212, 98, 16);
		panel_View.add(lblExportiereAls);
		
		final JRadioButton rdbtnPng = new JRadioButton("*.png");
		rdbtnPng.setBounds(8, 236, 121, 24);
		rdbtnPng.setSelected(true);
		panel_View.add(rdbtnPng);
		
		final JRadioButton rdbtnCsv = new JRadioButton("*.csv");
		rdbtnCsv.setBounds(8, 256, 121, 24);
		panel_View.add(rdbtnCsv);
		
		final JRadioButton rdbtnMinst = new JRadioButton("MINST Format");
		rdbtnMinst.setBounds(8, 276, 121, 24);
		panel_View.add(rdbtnMinst);
		
		ButtonGroup ExportGroup = new ButtonGroup();
		ExportGroup.add(rdbtnCsv);
		ExportGroup.add(rdbtnPng);
		ExportGroup.add(rdbtnMinst);
		
		btnExportieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Example ex = learndata.getSpecificExample(loadImageIndex);
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				Date date = new Date();
				if (rdbtnPng.isSelected()){
					saveDialog.setDialogTitle("Als *.png speichern");
					saveDialog.setSelectedFile(new File("learndata_index"+loadImageIndex+"_class"+ex.getClassification()+".png"));
				} else if (rdbtnCsv.isSelected()){
					saveDialog.setDialogTitle("Als *.csv speichern");
					saveDialog.setSelectedFile(new File("learndata_index"+loadImageIndex+"_class"+ex.getClassification()+".csv"));
				} else if (rdbtnMinst.isSelected()) {
					saveDialog.setDialogTitle("Im MINST Format speichern - Labels Datei ...");
					saveDialog.setSelectedFile(new File("learndata_"+dateFormat.format(date)+"-labels.idx1-ubyte"));
				}
				int rueckgabeWert = saveDialog.showSaveDialog(frame);
		        if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
		        	try {
						File save = saveDialog.getSelectedFile();
						if (save.exists()){
							int no = JOptionPane.showConfirmDialog(frame, "Overwrite?");
							if (no != JOptionPane.YES_OPTION){
								return;
							}
						}
						String path = save.getCanonicalPath();
						if (rdbtnPng.isSelected()) {
							Converter = new PngConv();
							Converter.exportFile(path, ex);
						} else if (rdbtnCsv.isSelected()) {
							Converter = new CsvConv();
							Converter.exportFile(path, ex);
						} else if (rdbtnMinst.isSelected()) {
							String pathLabels = path;
							String pathImages = null;
							saveDialog
									.setDialogTitle("Im MINST Format speichern - Images Datei ...");
							saveDialog.setSelectedFile(new File("learndata_"
									+ dateFormat.format(date)
									+ "-images.idx3-ubyte"));
							int rueckgabeWertImages = saveDialog
									.showSaveDialog(frame);
							if (rueckgabeWertImages == JFileChooser.APPROVE_OPTION) {
								try {
									save = saveDialog.getSelectedFile();

									File saveImages = saveDialog
											.getSelectedFile();
									if (saveImages.exists()) {

										int no = JOptionPane.showConfirmDialog(
												frame, "Overwrite?");
										if (no != JOptionPane.YES_OPTION) {
											return;
										}
									}
									pathImages = save.getCanonicalPath();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								return;
							}
							MinstConv MinstConverter = new MinstConv();
							MinstConverter.exportFile(pathLabels, pathImages,
									learndata.getExamples());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
			}
		});
		btnExportieren.setBounds(12, 304, 98, 26);
		panel_View.add(btnExportieren);
		
		
		///////////////////////////////////////////////////////////////////////////////
		//	Bild erkennen
		
		JPanel panel_ReadImage = new JPanel();
		tabs.addTab("Bild erkennen", null, panel_ReadImage, null);
		panel_ReadImage.setLayout(null);
		
		// Pfad-Label
		final JLabel lblPath = new JLabel("");
		lblPath.setFont(new Font("Dialog", Font.PLAIN, 10));
		lblPath.setBounds(12, 50, 330, 16);
		panel_ReadImage.add(lblPath);
		
		// Bild öffnen
		JButton btnLoadPath = new JButton( new AbstractAction("CSV / PNG \u00F6ffnen") {
			public void actionPerformed( ActionEvent e ) {
				int rueckgabeWert = imageDialog.showOpenDialog(null);
		        if(rueckgabeWert == JFileChooser.APPROVE_OPTION) {
		            try {
		            	lblPath.setText("File: " + imageDialog.getSelectedFile().getName());
						readimageOpenDialogPath = imageDialog.getSelectedFile().getCanonicalPath();
						if (readimageOpenDialogPath.endsWith(".png")) {
							readimagePreview.setIcon(imageLoader.createImageIcon(readimageOpenDialogPath));
						} else if (readimageOpenDialogPath.endsWith(".csv")) {
							Converter = new CsvConv();
							Example ex = Converter.importFile(readimageOpenDialogPath);
							readimagePreview.setIcon(imageLoader.exportImageIcon(ex));
						}
						readimagePreview.setText(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					} 
		        } else {
		        	lblPath.setText(null);
		        	readimagePreview.setText("none selected");
		        	readimagePreview.setIcon(new ImageIcon(Gui.class.getResource("/javax/swing/plaf/basic/icons/image-failed.png")));
		        	readimageOpenDialogPath = null;
		        }
	        }
		});
		btnLoadPath.setText("*.csv / *.png \u00F6ffnen");
		btnLoadPath.setBounds(12, 22, 184, 26);
		panel_ReadImage.add(btnLoadPath);
		
		// Preview Feld
		readimagePreview = new JLabel("none selected");
		readimagePreview.setFont(new Font("Dialog", Font.PLAIN, 12));
		readimagePreview.setIcon(new ImageIcon(Gui.class.getResource("/javax/swing/plaf/basic/icons/image-failed.png")));
		readimagePreview.setHorizontalAlignment(SwingConstants.CENTER);
		readimagePreview.setBounds(214, 12, 454, 347);
		panel_ReadImage.add(readimagePreview);
		
		// Bild einordnen
		JLabel lblClassification = new JLabel("Classification:");
		lblClassification.setBounds(12, 103, 98, 16);
		panel_ReadImage.add(lblClassification);
		
		readimageClassification = new JTextField();
		readimageClassification.setBounds(12, 123, 63, 20);
		panel_ReadImage.add(readimageClassification);
		readimageClassification.setColumns(10);
		
		JButton btnAddToLearndata = new JButton("Zu Lerndaten hinzuf\u00FCgen");
		btnAddToLearndata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Example ex;
					if (readimageOpenDialogPath.endsWith(".png")) {
						Converter = new PngConv();
						ex = Converter.importFile(readimageOpenDialogPath);
						ex.setClassification(Integer.parseInt(readimageClassification.getText()));
						learndata.addExample(ex);
					} else if (readimageOpenDialogPath.endsWith(".csv")) {
						Converter = new CsvConv();
						ex = Converter.importFile(readimageOpenDialogPath);
						ex.setClassification(Integer.parseInt(readimageClassification.getText()));
						learndata.addExample(Converter.importFile(readimageOpenDialogPath));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnAddToLearndata.setBounds(13, 155, 183, 26);
		panel_ReadImage.add(btnAddToLearndata);
		
		btnErkenneKlasse = new JButton("Erkenne Klasse");
		btnErkenneKlasse.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				Example ex = null;
				try {
					if (readimageOpenDialogPath.endsWith(".png")) {
						Converter = new PngConv();
						ex = Converter.importFile(readimageOpenDialogPath);
					} else if (readimageOpenDialogPath.endsWith(".csv")) {
						Converter = new CsvConv();
						ex = Converter.importFile(readimageOpenDialogPath);
					}
					evaluatedClassification = algorithm.getClassification(ex.getImageValueObj());
					readimageClassification.setText(Integer.toString(evaluatedClassification));
					stats.testObjectIncrement(evaluatedClassification);					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnErkenneKlasse.setBounds(12, 65, 184, 26);
		checkAlgorithmState();
		panel_ReadImage.add(btnErkenneKlasse);
		
		JButton btnKorrigiere = new JButton("korrigiere ..");
		btnKorrigiere.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stats.wrongIncrement();
				stats.testObjectDecrement(evaluatedClassification);
				stats.testObjectIncrement(Integer.parseInt(readimageClassification.getText()));
			}
		});
		btnKorrigiere.setBounds(79, 125, 117, 16);
		panel_ReadImage.add(btnKorrigiere);
		
		
//		/////////////////////////////////////////////////////////////////////////////
		// STATISTIKEN
		JPanel panel_Statistics = new JPanel();
		
		final JTextPane txtPaneStats = new JTextPane();
		txtPaneStats.setForeground(Color.WHITE);
		txtPaneStats.setBackground(Color.DARK_GRAY);
		txtPaneStats.setText("<emtpy>");
		txtPaneStats.setFont(new Font("Consolas", Font.PLAIN, 11));
		txtPaneStats.setBounds(12, 37, 656, 322);
		panel_Statistics.add(txtPaneStats);
		
		final JRadioButton rdbtnStatSaved = new JRadioButton("Gespeicherte Statistik");
		rdbtnStatSaved.setBounds(8, 8, 150, 24);
		panel_Statistics.add(rdbtnStatSaved);
		
		final JRadioButton rdbtnStatThis = new JRadioButton("Aktuelle Statistik");
		rdbtnStatThis.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (rdbtnStatThis.isSelected()){
					txtPaneStats.setText(stats.toString());
				} else {
					txtPaneStats.setText(lastStat);
				}
			}
		});
		rdbtnStatThis.setBounds(165, 8, 132, 24);
		rdbtnStatThis.setSelected(true);
		panel_Statistics.add(rdbtnStatThis);
		
		tabs.addTab("Statistiken", null, panel_Statistics, null);
		panel_Statistics.setLayout(null);
		
		ButtonGroup RadioStats = new ButtonGroup();
		RadioStats.add(rdbtnStatThis);
		RadioStats.add(rdbtnStatSaved);
		
		JButton btnAktualisieren = new JButton("Aktualisieren");
		btnAktualisieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnStatThis.isSelected()){
					txtPaneStats.setText(stats.toString());
				} else {
					txtPaneStats.setText(lastStat);
				}
			}
		});
		btnAktualisieren.setIcon(new ImageIcon(Gui.class.getResource("/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png")));
		btnAktualisieren.setBounds(305, 6, 137, 26);
		panel_Statistics.add(btnAktualisieren);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Checker-methoden
	
	private void startClusterClassification() {
		KMeanClusters = PrototypeManager.getInstance(0).getClusters();
		clustersLength = KMeanClusters.length;
		lblNumberOfClusters.setText("/ " + Integer.toString(clustersLength));
		clusterIndex = 1;
		loadCluster(clusterIndex);
	}
	
	private void loadCluster(int index) {
		if (index > 0 && index <= clustersLength) {
			Example ex = new Example(KMeanClusters[index-1].getAverageImage(),KMeanClusters[index-1].getClassification());
			lblClusterPreview.setIcon(imageLoader.exportImageIcon(ex));
			lblClusterPreview.setText(null);
			txtClusterNo.setText(Integer.toString(index));
			txtClusterClass.setText(Integer.toString(ex.getClassification()));
			clusterIndex = index;
		} else {
			lblClusterPreview.setIcon(null);
			lblClusterPreview.setText("Invalid Cluster index");
			txtClusterClass.setText("n/a");
		}
	}
	
	private void checkLoadImageIndex() {
		if ( loadImageIndex >= 0 && loadImageIndex < learndata.examplesCount()){
			btnReclassify.setEnabled(true);
			btnExportieren.setEnabled(true);
		} else {
			btnReclassify.setEnabled(false);
			btnExportieren.setEnabled(false);
		}
	}

	private void checkBtnRunState(){
		if (learndata.examplesCount() > 0 ) {
			btnRun.setEnabled(true);
		} else {
			btnRun.setEnabled(false);
		}
	}
	
	private void checkAlgorithmState() {
		if (algorithm == null){
			btnErkenneKlasse.setEnabled(false);
		} else {
			btnErkenneKlasse.setEnabled(true);
		}
	}
	
	public void checkLoadedPaths() {
		if (imagesPath != null && labelsPath != null) {
			btnParseMinst.setEnabled(true);
		} else {
			btnParseMinst.setEnabled(false);
		}
	}
}
