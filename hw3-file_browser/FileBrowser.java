
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Sapountzi Athanasia Despoina 2624
package ce326.hw3;

import java.awt.Button;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author User
 */
public class FileBrowser extends javax.swing.JFrame {
    private String homePath;
    private StringBuilder breadcrumb;
    private boolean isMac=false;
    private boolean prevMarked = false;
    private boolean markFlag = false;
    private boolean cutFlag = false;
    private boolean copyFlag = false;
    private boolean SearchField = false;
    private String propertiesPath;
    private boolean homeRemoved = false;
    public LinkedList<String> namesList;
    // namesList = new LinkedList<String>();
     // private boolean pasteFlag = false;
    File cutFile;
    File copyFile;
  
//    private String currPath;
//    private String separator;
    private String splitSeparator;
    private JLabel prevMarkedLabel;
    private String xmlPath;
    String currPath = new String();
    JMenuItem pastePop = new JMenuItem("Paste");
    
    String separator = new String(System.getProperty("file.separator"));
    /**
     * Creates new form FileBrowser
     */ 
    public FileBrowser() {
        homePath = new String(System.getProperty("user.home"));
//        System.getProperty("user.home").toString();
        initComponents();
        
        
         xmlPath = new String(homePath + separator + ".java-file-browser/properties.xml");
        addHometoFaves();
        // scroll for centerPanel.
        centerScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        centerScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        

        // scroll for favPanel.
        scrollFaves.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFaves.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        
        centerPanel.setLayout(new WrapLayout(FlowLayout.LEFT));
        breadcrumbPanel.setLayout(new BoxLayout(breadcrumbPanel, BoxLayout.X_AXIS));
        currPath = homePath;
//        updateDirectory(homePath);
        
        BoxLayout boxlayout = new BoxLayout(favPanel, BoxLayout.Y_AXIS);
        favPanel.setLayout(boxlayout);
        
        readXMLFile();
        addToFavourites.addActionListener(menuItemListener);
        addToFavourites.setActionCommand("Add To Favourites");

        
        updateDirectory(homePath);

    }
    public class ActionListenerTest implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            centerPanel.removeAll();
            centerPanel.revalidate();
            centerPanel.setLayout(new WrapLayout(FlowLayout.LEFT));
            updateDirectory(e.getActionCommand());

            SwingUtilities.updateComponentTreeUI(FileBrowser.this);
        }
    }

    
        // Method to find the icon for every directory/file.  
    public void selectIcon(File file, JLabel label) {

        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);

        String path = new String((System.getProperty("user.dir")) + separator + "icons");

        if (file.isDirectory()) {
            path = path + separator + "folder.png";
            label.setIcon(new ImageIcon(path));
        } else {
            String extension = "";
            int i = (file.getName()).lastIndexOf('.');
            if (i >= 0) {
                extension = (file.getName()).substring(i + 1);
            }
            path = path + separator + extension + ".png";
            File temp = new File(path);
            if (temp.exists()) {
                label.setIcon(new ImageIcon(path));
            } else {
                path =(System.getProperty("user.dir")) + separator + "icons" + separator + "question.png";  
                label.setIcon(new ImageIcon(path));
            }
        }
    }
    

    public void updateDirectory(String filepath) {

        ActionListenerTest actionEar = new ActionListenerTest();
        
        markFlag = false;
        currPath = filepath;
        // System.out.println(currPath);

        if(System.getProperty("os.name").split(" ")[0].equals("Windows")){
            separator="\\";
            splitSeparator = "\\\\";
        }
        else if(System.getProperty("os.name").split(" ")[0].equals("Linux")){
            separator="/";
            splitSeparator="/";
        }
        else{
            separator="/";
            splitSeparator="/";
            isMac=true;
        }
        
        String []token = currPath.split(splitSeparator);
        breadcrumb = new StringBuilder("");

        breadcrumbPanel.removeAll();
        breadcrumbPanel.updateUI();

        // clickedlabel.setText(findName(currPath));
        String interPath = new String();
        for(int i=0;i<token.length;i++){
            if((System.getProperty("os.name").split(" ")[0].equals("Linux") && i==0) || (isMac==true && i==0)){
                continue;
            }
            //For each token, create a button and add it to the path panel.
            interPath = interPath + token[i] + separator ;
            JButton button = new JButton(token[i]);
            button.setActionCommand(interPath);
            button.addActionListener(actionEar);
            button.setFocusPainted(false);
            breadcrumbPanel.add(button);
            breadcrumb.append(token[i]);
            if(i!=token.length-1){
                breadcrumb.append(">");
                JLabel label = new JLabel(">");
                breadcrumbPanel.add(label);
            }
        }
       
        breadcrumbPanel.updateUI();

        // Define folders and files of current workspace.
        File dir = new File(filepath);
        File[] files = dir.listFiles();
        List<File> dirList = new ArrayList<File>();
        List<File> fileList = new ArrayList<File>();


        if(dir.length() > 0){
            if(files != null){
                for(File element: files){
                    if(element.isDirectory()){
                        dirList.add(element);
                    }
                    else{
                        fileList.add(element);
                    }
                }
                //Sort alphabetiacally.
                Collections.sort(dirList, new Comparator<File>(){
                    public int compare(File f1, File f2){
                        return ((f1.getName()).toLowerCase()).compareTo(((f2.getName()).toLowerCase()));
                    } });
                Collections.sort(fileList, new Comparator<File>(){
                    public int compare(File f1, File f2){
                        return ((f1.getName()).toLowerCase()).compareTo(((f2.getName()).toLowerCase()));
                    } });

                JLabel[] directoryLabels = new JLabel[dirList.size()];
                JLabel[] fileLabels = new JLabel[fileList.size()];
                MouseListener mouseListener = new MouseListener();
                
                for(int i = 0; i < dirList.size(); i++){
                    directoryLabels[i] = new JLabel((dirList.get(i)).getName());
                    directoryLabels[i].setPreferredSize(new Dimension(60, 85));
                    //in case the name of the directory is too big
                    directoryLabels[i].setToolTipText(directoryLabels[i].getText());
                    // ??
                    if(dirList.get(i).isHidden()){
                        directoryLabels[i].setVisible(false);
                    }
                    else{
                        directoryLabels[i].setVisible(true);
                        directoryLabels[i].addMouseListener(mouseListener);
                    }
                    //select the appropriate icon 
                    selectIcon(dirList.get(i), directoryLabels[i]);
                    centerPanel.add(directoryLabels[i]);
                }

                for(int i = 0; i < fileList.size(); i++){
                    fileLabels[i] = new JLabel((fileList.get(i)).getName());
                    //Set preferred size for folders to be aligned, full name appears as tooltip text.
                    fileLabels[i].setPreferredSize(new Dimension(60, 85));
                    fileLabels[i].setToolTipText(fileLabels[i].getText());
                    if(fileList.get(i).isHidden()){
                        fileLabels[i].setVisible(false);
                    }
                    else{
                        fileLabels[i].setVisible(true);
                        fileLabels[i].addMouseListener(mouseListener);
                    }
                    //select the appropriate icon
                    selectIcon(fileList.get(i), fileLabels[i]);
                    centerPanel.add(fileLabels[i]);
                }
                dirList.clear();
                fileList.clear();
            }
        }   
    }
    //mouse handling
     class MouseListener extends MouseAdapter {
//        JLabel labelOld;
        Color background;

        MouseListener() {
//            labelOld = new JLabel();
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent event) {
            if(SwingUtilities.isLeftMouseButton(event)){
                // 1 left click
                if(event.getClickCount()==1){
                    if(prevMarked == false){
                        JLabel label = (JLabel) event.getSource();
                        prevMarkedLabel = label;
                        markFlag = true;
                        prevMarked = true;
                        label.setBackground(new Color(192,192,192));
                        label.setOpaque(true);
                    }
                    else if(prevMarked == true){
                        JLabel label = (JLabel) event.getSource();
                        //save old backround
                        background = label.getBackground();
                        label.setBackground(new Color(192,192,192));
                        label.setOpaque(true);
                        markFlag = true; //?
                        if (prevMarkedLabel.getText() != label.getText()) {
                            prevMarkedLabel.setBackground(background);
                            prevMarkedLabel.setOpaque(true);
                            prevMarkedLabel = label;
                        }
                    }
                }
                //double left click
                else if(event.getClickCount()==2){
                    String tempPath="";
                        JLabel label = (JLabel) event.getSource();
                        
                        //Update the current path.
                        tempPath=currPath;
                        if(currPath.charAt(currPath.length()-1)!=separator.charAt(0)){
                            currPath+=separator;
                        }
                        currPath+=label.getText();
                        // currPath+=separator;

                        File fileDoubleClicked = new File(currPath);
                        if(fileDoubleClicked.isDirectory()){
                            centerPanel.removeAll();
                            centerPanel.revalidate();
                            updateDirectory(currPath);

                        }
                        else{
                            currPath = tempPath;
                            try {
                                Desktop.getDesktop().open(fileDoubleClicked);
                            } 
                            catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            }

                        }
                        SwingUtilities.updateComponentTreeUI(FileBrowser.this);
                   
                }
            }
            //right click
            else if(SwingUtilities.isRightMouseButton(event)){
                    if(prevMarked == false){
//                        System.out.println("hi 1");
                        JLabel label = (JLabel) event.getSource();
                        prevMarkedLabel = label;
                        prevMarked = true;
                        markFlag = true;
                        label.setBackground(new Color(192,192,192));
                        label.setOpaque(true);
                    }
                    else if(prevMarked == true){
//                        System.out.println("hi 2");
                        JLabel label = (JLabel) event.getSource();
                        //save old backround
                        markFlag = true;
                        background = label.getBackground();
                        label.setBackground(new Color(192,192,192));
                        label.setOpaque(true);
                        
                        if (prevMarkedLabel.getText() != label.getText()) {
                            prevMarkedLabel.setBackground(background);
                            prevMarkedLabel.setOpaque(true);
                            prevMarkedLabel = label;
                        }
                    }
                
//            }
         
            //MouseEvent occured by right click on a selected label, pop up edit menu shows up.
//            if (event.getButton() == MouseEvent.BUTTON3) {
                Color selectColor = new Color(192,192,192);
                JLabel label = (JLabel) event.getSource();

                if (label.getBackground().equals(selectColor)) {
                    JPopupMenu options = new JPopupMenu("Edit");

                    JMenuItem cutPop = new JMenuItem("Cut");
                    cutPop.setActionCommand(label.getText());
                    cutPop.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cutFlag = true;
                            copyFlag=false;
                            cutFile = new File(currPath + separator + prevMarkedLabel.getText());
                            paste.setEnabled(true);
                        }
                    });

                    JMenuItem copyPop = new JMenuItem("Copy");
                    copyPop.setActionCommand(label.getText());
                    copyPop.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            copyFlag=true;
                            cutFlag=false;
                            copyFile = new File(currPath + separator + prevMarkedLabel.getText());
                            paste.setEnabled(true);
                        }
                    });

//                    JMenuItem pastePop = new JMenuItem("Paste");
                    pastePop.setActionCommand(label.getText());
                    pastePop.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (copyFlag) {
                    String localPath = new String();
                    localPath = copyFile.getName().substring(copyFile.getName().lastIndexOf(separator) + 1, copyFile.getName().length());
                    File pasteFile = new File(
                            currPath /*+ separator + prevMarkedLabel.getText()*/ + separator + copyFile.getName());
                    System.out.println(pasteFile.getAbsolutePath());
                    if (pasteFile.exists()) {
                        //Directory already exists.WARNING modal shows up.
                        JFrame window = new JFrame();
                        JDialog d = new JDialog(window, "are you sure for this replacement???", true);
                        d.setLayout(new FlowLayout());

                        Button replace = new Button("replace");
                        Button noReplace = new Button("do not replace");
                        replace.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                d.setVisible(false);
                                if(copyFile.isDirectory()){
                                    
                                        copyFolder(copyFile, pasteFile);
                                   
                                }
                                else {
                                    try {
                                        Files.copy(copyFile.toPath(), pasteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    } catch (IOException ex) {
                                        java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                
                            }
                        });
                        noReplace.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                //Hide dialog
                                d.setVisible(false);
                                return;
                            }
                        });

                        d.add(new Label("Directory already exists!\nDo you want to replace it?"));
                        d.add(replace);
                        d.add(noReplace);

                        // Show dialog
                        d.pack();
                        d.setVisible(true);
                    } else {
                        if(copyFile.isDirectory()){
                            copyFolder(copyFile, pasteFile);
                        }
                        else {
                            try {
                                Files.copy(copyFile.toPath(), pasteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    copyFlag = false;
                    paste.setEnabled(false);

                    centerPanel.removeAll();
                    centerPanel.revalidate();
                    
                    String localCurrPath = new String();
                    localCurrPath = currPath.substring(currPath.lastIndexOf(separator) + 1, currPath.length());
                    prevMarkedLabel.setText(localCurrPath);
                    updateDirectory(currPath);
                } else if (cutFlag) {
                    String localPath = new String();
                    localPath = cutFile.getName().substring(cutFile.getName().lastIndexOf(separator) + 1, cutFile.getName().length());
                    File pasteFile = new File(currPath + separator + prevMarkedLabel.getText() + separator + localPath);
                    if (pasteFile.exists()) {
                        //Directory already exists.WARNING modal shows up.
                        JFrame window = new JFrame();
                        JDialog d = new JDialog(window, "are you sure for this replacement???", true);
                        d.setLayout(new FlowLayout());

                        Button replace = new Button("replace");
                        Button noReplace = new Button("do not replace");
                        replace.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                d.setVisible(false);
                                if(copyFile.isDirectory()){
                                    
                                        copyFolder(copyFile, pasteFile);
//                                        
                                  
                                }
                                else {
                                    try {
                                        Files.copy(copyFile.toPath(), pasteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    } catch (IOException ex) {
                                        java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                
                            }
                        });
                        noReplace.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                //Hide dialog
                                d.setVisible(false);
                                return;
                            }
                        });

                        d.add(new Label("Directory already exists!\nDo you want to replace it?"));
                        d.add(replace);
                        d.add(noReplace);

                        //Show dialog
                        d.pack();
                        d.setVisible(true);
                    } else {
                        if(copyFile.isDirectory()){
                             
                                    copyFolder(copyFile, pasteFile);
//                                    recursiveCopy(copyFile.getAbsolutePath(), pasteFile.getAbsolutePath() );
                                
//                        copyFolder(copyFile, pasteFile);
                        }
                        else {
                            try {
                                Files.copy(copyFile.toPath(), pasteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    cutFlag = false;
                    paste.setEnabled(false);
                    // File copied, now deleting it from previous directory to finish cut option.
//                    deleteDirectory(cutFile);
                    String localCurrPath = new String();
                    localCurrPath = currPath.substring(currPath.lastIndexOf(separator) + 1, currPath.length());
                    
                    centerPanel.removeAll();
                    centerPanel.revalidate();
                    prevMarkedLabel.setText(localCurrPath);
                    updateDirectory(currPath);
                } else {
                    paste.setEnabled(false);
                        }
                        }
                    });
//                    pastePop.setenabled
//                    pastePop.setEnabled(false);
                    JMenuItem renamePop = new JMenuItem("Rename");
                    renamePop.setActionCommand(label.getText());
                    renamePop.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                        File dir = new File(currPath);
                        File renameFile = new File(currPath + separator + prevMarkedLabel.getText());

                        File files[] = dir.listFiles();
                        if(files != null){
                                for (File element : files) {
                                    if (renameFile.equals(element)) {
                                        JFrame window = new JFrame();

                                        JDialog d = new JDialog(window, "Please type new name", true);

                                        d.setLayout(new FlowLayout());

                                        JTextField newName = new JTextField(35);
                                        newName.setText(prevMarkedLabel.getText());
                                        Button rename = new Button("Rename");

                                        rename.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                // Hide dialog
                                                d.setVisible(false);

                                                File newFile = new File(currPath +separator + newName.getText());
                                                renameFile.renameTo(newFile);

                                                centerPanel.removeAll();
                                                centerPanel.revalidate();
                                                updateDirectory(currPath);
                                            }
                                        });

                                        d.add(newName);
                                        d.add(rename);

                                        // Show dialog
                                        d.pack();
                                        d.setVisible(true);
	                    }
	                }
	            }
                centerPanel.revalidate();
                        }
                    
                    });

                    JMenuItem deletePop = new JMenuItem("Delete");
                    deletePop.setActionCommand(label.getText());
                    deletePop.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            File dir = new File(currPath);
                File delFile = new File(currPath + separator + prevMarkedLabel.getText());
                
                File files[] = dir.listFiles();
                if(files != null){
	                for (File element : files) {
	                    if (delFile.equals(element)) {

	                        JFrame window = new JFrame();
	                        JDialog d = new JDialog(window, "WARNING", true);
	                        
                                d.setLayout(new FlowLayout());

	                        Button yes = new Button("delete");
	                        
                                
	                        yes.addActionListener(new ActionListener() {
	                            public void actionPerformed(ActionEvent e) {
	                                d.setVisible(false);

	                                if (delFile.isDirectory()) {
                                            deleteDirectory(delFile); 
	                                } 
                                        else { //just a file
	                                    delFile.delete();
	                                }

	                                centerPanel.removeAll();
	                                centerPanel.revalidate();
	                                updateDirectory(currPath);
	                            }
	                        });
                                Button no = new Button("do not delete");
	                        no.addActionListener(new ActionListener() {
	                            public void actionPerformed(ActionEvent e) {
	                                
	                                d.setVisible(false);
	                            }
	                        });

	                        d.add(new Label("Are you sure you want to delete it?"));
	                        d.add(yes);
	                        d.add(no);

	                        //visible modal 
	                        d.pack();
	                        d.setVisible(true);
	                    }
	                }
	            }
                        }
                    });

                    JMenuItem favouritePop = new JMenuItem("Add To Favourites");
                    favouritePop.setActionCommand(label.getText());
                    favouritePop.setActionCommand("Add To Favourites");
                    favouritePop.addActionListener(menuItemListener);

                    JMenuItem propertiesPop = new JMenuItem("Properties");
                    propertiesPop.setActionCommand(label.getText());
//                    propertiesPop.addActionListener(menuItemListener);
                    propertiesPop.addActionListener(new ActionListener() {
                        @Override
	                            public void actionPerformed(ActionEvent e) {
	                                // Hide dialog
	                               if(markFlag == true){
                    propertiesPath = currPath + separator + prevMarkedLabel.getText();
                    File propertiesFile = new File(propertiesPath);
                    //create modal window
                    JFrame window = new JFrame();
                    JDialog d = new JDialog(window, "Properties", true);
                    d.setLayout(new GridLayout(4, 1));

                    d.add(new Label("Name: " + prevMarkedLabel.getText() /*+ "\n"*/));

                    d.add(new Label("Location: " + propertiesPath));

                    if(propertiesFile.isFile()){
                        String sizeLabel = new String("size: " + String.valueOf(propertiesFile.length()+" bytes"));
                        d.add(new Label(sizeLabel));
                    }
                    else{
                        String sizeLabel = new String("size: " + String.valueOf(DirectorySize(propertiesFile)+" bytes"));
                        d.add(new Label(sizeLabel));
                    }
                   
                    d.pack();
                d.setVisible(true);

                }
	                            }
	                        });
                    options.add(cutPop);
                    options.add(copyPop);
                    options.add(pastePop);
                    options.add(renamePop);
                    options.add(deletePop);
                    options.add(favouritePop);
                    options.add(propertiesPop);

                    options.show(event.getComponent(), 50, 50);
                    repaint();
                }
            }
        }
    }

    public void xmlFileInit() {

        try {

            File xmlFile = new File(xmlPath);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("favourites");
            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);

            transformer.transform(source, result);

        }
        catch (Exception pce) {
            pce.printStackTrace();
        }
    }



    //Method to add a node in the XML file.
    public void addToXMLFile(String favName, String favPath) {

        try {
            File xmlFile = new File(xmlPath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);

            Element root = document.getDocumentElement();
            Element rootElement = document.getDocumentElement();

            // append type(= directory).
            Element type = document.createElement("directory");
            rootElement.appendChild(type);

            // append name.
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(favName));
            type.appendChild(name);

            // append path.
            Element pathDir = document.createElement("path");
            pathDir.appendChild(document.createTextNode(favPath));
            type.appendChild(pathDir);

            // append the whole element to the file.
            root.appendChild(type);

            // update the existing xmlFile.
            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(xmlPath);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    ActionListener menuItemListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){

            String menuString = e.getActionCommand();
//            System.out.println(menuString);
//            System.out.println(prevMarkedLabel.getText());
//            System.out.println(prevMarkedLabel.getName());
            if(menuString.equals("Exit") ) {
                System.exit(0);   
            }
            else if(menuString.equals("Rename")){
                File dir = new File(currPath);
                File renameFile = new File(currPath + separator + prevMarkedLabel.getText());

                File files[] = dir.listFiles();
                if(files != null){
	                for (File element : files) {
	                    if (renameFile.equals(element)) {
	                        JFrame window = new JFrame();

	                        JDialog d = new JDialog(window, "Please type new name", true);

	                        d.setLayout(new FlowLayout());

	                        JTextField newName = new JTextField(35);
	                        newName.setText(prevMarkedLabel.getText());
	                        Button rename = new Button("Rename");

	                        rename.addActionListener(new ActionListener() {
	                            public void actionPerformed(ActionEvent e) {
	                                // Hide dialog
	                                d.setVisible(false);

	                                File newFile = new File(currPath +separator + newName.getText());
	                                renameFile.renameTo(newFile);

	                                centerPanel.removeAll();
	                                centerPanel.revalidate();
	                                updateDirectory(currPath);
	                            }
	                        });

	                        d.add(newName);
	                        d.add(rename);

	                        // Show dialog
	                        d.pack();
	                        d.setVisible(true);
	                    }
	                }
	            }
                centerPanel.revalidate();
            }
            else if(menuString.equals("Cut")){
                cutFlag=true;
                copyFlag=false;
                // pasteFlag=false; //  ????

                cutFile = new File(currPath + separator + prevMarkedLabel.getText());
                paste.setEnabled(true);
//                pastePop.setEnabled(true);

            }
            else if(menuString.equals("Copy")){
                copyFlag=true;
                cutFlag=false;
                // pasteFlag=false; 
                copyFile = new File(currPath + separator + prevMarkedLabel.getText());
                paste.setEnabled(true);
                
            }
            else if(menuString.equals("Paste")){
                if (copyFlag) {
                    String localPath = new String();
                    localPath = copyFile.getName().substring(copyFile.getName().lastIndexOf(separator) + 1, copyFile.getName().length());
                    File pasteFile = new File(
                            currPath /*+ separator + prevMarkedLabel.getText()*/ + separator + copyFile.getName());
                    // System.out.println(pasteFile.getAbsolutePath());
                    if (pasteFile.exists()) {
                        //Directory already exists.WARNING modal shows up.
                        JFrame window = new JFrame();
                        JDialog d = new JDialog(window, "are you sure for this replacement???", true);
                        d.setLayout(new FlowLayout());

                        Button replace = new Button("replace");
                        Button noReplace = new Button("do not replace");
                        replace.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                d.setVisible(false);
                                if(copyFile.isDirectory()){
                                    
                                        copyFolder(copyFile, pasteFile);
                                   
                                }
                                else {
                                    try {
                                        Files.copy(copyFile.toPath(), pasteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    } catch (IOException ex) {
                                        java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                
                            }
                        });
                        noReplace.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                //Hide dialog
                                d.setVisible(false);
                                return;
                            }
                        });

                        d.add(new Label("Directory already exists!\nDo you want to replace it?"));
                        d.add(replace);
                        d.add(noReplace);

                        // Show dialog
                        d.pack();
                        d.setVisible(true);
                    } else {
                        if(copyFile.isDirectory()){
                            copyFolder(copyFile, pasteFile);                          
                        }
                        else {
                            try {
                                Files.copy(copyFile.toPath(), pasteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    copyFlag = false;
                    paste.setEnabled(false);

                    centerPanel.removeAll();
                    centerPanel.revalidate();
                    
                    String localCurrPath = new String();
                    localCurrPath = currPath.substring(currPath.lastIndexOf(separator) + 1, currPath.length());
                    prevMarkedLabel.setText(localCurrPath);
                    updateDirectory(currPath);
                } else if (cutFlag) {
                    String localPath = new String();
                    localPath = cutFile.getName().substring(cutFile.getName().lastIndexOf(separator) + 1, cutFile.getName().length());
                    File pasteFile = new File(currPath + separator + prevMarkedLabel.getText() + separator + localPath);
                    if (pasteFile.exists()) {
                        //Directory already exists.WARNING modal shows up.
                        JFrame window = new JFrame();
                        JDialog d = new JDialog(window, "are you sure for this replacement???", true);
                        d.setLayout(new FlowLayout());

                        Button replace = new Button("replace");
                        Button noReplace = new Button("do not replace");
                        replace.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                d.setVisible(false);
                                if(copyFile.isDirectory()){
                                    
                                        copyFolder(copyFile, pasteFile);
                                  
                                }
                                else {
                                    try {
                                        Files.copy(copyFile.toPath(), pasteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    } catch (IOException ex) {
                                        java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                
                            }
                        });
                        noReplace.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                //Hide dialog
                                d.setVisible(false);
                                return;
                            }
                        });

                        d.add(new Label("Directory already exists!\nDo you want to replace it?"));
                        d.add(replace);
                        d.add(noReplace);

                        //Show dialog
                        d.pack();
                        d.setVisible(true);
                    } else {
                        if(copyFile.isDirectory()){
                             
                                    copyFolder(copyFile, pasteFile);
                                
                        }
                        else {
                            try {
                                Files.copy(copyFile.toPath(), pasteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    cutFlag = false;
                    paste.setEnabled(false);
                    // File copied, now deleting it from previous directory to finish cut option.
//                    deleteDirectory(cutFile);
                    String localCurrPath = new String();
                    localCurrPath = currPath.substring(currPath.lastIndexOf(separator) + 1, currPath.length());
                    
                    centerPanel.removeAll();
                    centerPanel.revalidate();
                    prevMarkedLabel.setText(localCurrPath);
                    updateDirectory(currPath);
                } else {
                    paste.setEnabled(false);
                }
                
            }
            else if(menuString.equals("Delete")){
                File dir = new File(currPath);
                File delFile = new File(currPath + separator + prevMarkedLabel.getText());
                
                File files[] = dir.listFiles();
                if(files != null){
	                for (File element : files) {
	                    if (delFile.equals(element)) {

	                        JFrame window = new JFrame();
	                        JDialog d = new JDialog(window, "WARNING", true);
	                        
                                d.setLayout(new FlowLayout());

	                        Button yes = new Button("delete");
	                        
                                
	                        yes.addActionListener(new ActionListener() {
	                            public void actionPerformed(ActionEvent e) {
	                                d.setVisible(false);

	                                if (delFile.isDirectory()) {
                                            deleteDirectory(delFile); 
	                                } 
                                        else { //just a file
	                                    delFile.delete();
	                                }

	                                centerPanel.removeAll();
	                                centerPanel.revalidate();
	                                updateDirectory(currPath);
	                            }
	                        });
                                Button no = new Button("do not delete");
	                        no.addActionListener(new ActionListener() {
	                            public void actionPerformed(ActionEvent e) {
	                                
	                                d.setVisible(false);
	                            }
	                        });

	                        d.add(new Label("Are you sure you want to delete it?"));
	                        d.add(yes);
	                        d.add(no);

	                        //visible modal 
	                        d.pack();
	                        d.setVisible(true);
	                    }
	                }
	            }
                
            }
            else if(menuString.equals("Properties")){
                if(markFlag == true){
                    propertiesPath = currPath + separator + prevMarkedLabel.getText();
                    File propertiesFile = new File(propertiesPath);
                    //create modal window
                    JFrame window = new JFrame();
                    JDialog d = new JDialog(window, "Properties", true);
                    d.setLayout(new GridLayout(4, 1));

                    d.add(new Label("Name: " + prevMarkedLabel.getText() /*+ "\n"*/));

                    d.add(new Label("Location: " + propertiesPath));

                    if(propertiesFile.isFile()){
                        String sizeLabel = new String("size: " + String.valueOf(propertiesFile.length()+" bytes"));
                        d.add(new Label(sizeLabel));
                    }
                    else{
                        String sizeLabel = new String("size: " + String.valueOf(DirectorySize(propertiesFile)+" bytes"));
                        d.add(new Label(sizeLabel));
                    }
                   
                    d.pack();
                d.setVisible(true);

                }
                
            }
//            System.out.println("hi from fav");
//            System.out.println(menuString);
            if(menuString.equals("Add To Favourites")){
                     // System.out.println("hi from fav");
                    File dir = new File(homePath + separator + ".java-file-browser");
                    if(!dir.exists()){
                        // System.out.println("sad");
                        dir.mkdir();
                    }
                    File prop = new File(xmlPath);
                    if(!prop.exists()){
                        try {
                            System.out.println("hi2");
                            prop.createNewFile();
                        } catch (IOException ex) {
                            Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        xmlFileInit();
                    }

                    File file = new File(currPath + separator + prevMarkedLabel.getText());
                    // System.out.println(file.getName());
                    if (file.isDirectory()) {
                        String favouritePath = new String(currPath + separator + prevMarkedLabel.getText());
                        if (!findInXML(prevMarkedLabel.getText())) {
                            
                            System.out.println(favouritePath);
                            addToXMLFile(prevMarkedLabel.getText(), favouritePath);
                            favouritesInit(prevMarkedLabel.getText());
                        }
                        favPanel.revalidate();
                    }

               
            }
            
           
            
            
        }
    };
    public boolean findInXML(String name) {
        try {
            File xmlFile = new File(xmlPath);
            if (xmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder;
                dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("directory");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if (eElement.getElementsByTagName("name").item(0).getTextContent().equals(name)) {
                            return true;
                        }
                    }
                }
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //This is the recursive delete directory function.
    public void deleteDirectory(File dir){
        
        File []filelist = dir.listFiles();
        int i;

        for(i=0;i<filelist.length;i++){
            
            if(filelist[i].isDirectory()){
                deleteDirectory(filelist[i].getAbsoluteFile());
            }
            else{
                if(filelist[i].delete()){
                    //System.out.println("Deleted successfully file");
                }
                else{
                    JFrame window = new JFrame();
                    JDialog d = new JDialog(window, "WARNING", true);

                    d.setLayout(new FlowLayout());
                    
                    d.add(new Label("delete file failed"));
//                    System.out.println("Could not delete file");
                }
            }
        }
        if(dir.delete()){
            //System.out.println("Delete directory ok");
        }
        else{
            System.out.println("Could not delete directory");
        }
    }
     public void copyDirectory(File olddirectory,File newdirectory) throws FileNotFoundException, IOException{
        
        File []filelist = olddirectory.listFiles();
        int i;

        for(i=0;i<filelist.length;i++){
            
            if(filelist[i].isDirectory()){
                File newdir = new File(newdirectory.getAbsolutePath()+separator+filelist[i].getName());
                newdir.mkdir();
                copyDirectory(filelist[i],newdir);
            }
            else{
                File newfile = new File(newdirectory.getAbsolutePath()+separator+filelist[i].getName());
                try {
                    copyFile(filelist[i],newfile);
                } 
                catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
     public void copyFile(File oldfile,File newfile) throws FileNotFoundException, IOException{
        
        FileInputStream in = new FileInputStream(oldfile);
        FileOutputStream out = new FileOutputStream(newfile);
        byte []array=new byte[256];
        int readBytes;
        
        while(in.available()>0){
            readBytes = in.read(array);
            out.write(array,0,readBytes);
        }
        out.flush();
        //Close the references in order to avoid collisions during the delete function.
        in.close();
        out.close();
    }
    
    public void copyFolder(File sourceFolder, File destinationFolder) {
        // Check if sourceFolder is a directory or file.
        if (sourceFolder.isDirectory()) {
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();
            }
            String files[] = sourceFolder.list();

            // Iterate over all files and copy them to destinationFolder one by one
            for (String file : files) {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);

                // Recursive function call
                copyFolder(srcFile, destFile);
            }

        } else {
            // Copy the file content from one place to another
            try {
                Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

public final File[] CurrentDirList(String curPath){
   
    File currentDir = new File(curPath);
    try{
    File files[] = currentDir.listFiles();
    if(files !=null){
        if(files.length > 0){
            ArrayList<File> dirList = new ArrayList<>();
            ArrayList<File> filesList = new ArrayList<>();
            for(File f: files){
                if(f.isDirectory()){
                    dirList.add(f);
                }
                else{
                    filesList.add(f);
                }
            }
            
            
            ArrayList<File> allFiles = new ArrayList<>();
            if(!dirList.isEmpty()){
                Collections.sort(dirList);
                allFiles.addAll(dirList);
            }
            if(!filesList.isEmpty()){
                Collections.sort(filesList);
                allFiles.addAll(filesList);
            }
            files = allFiles.toArray(files);
        }
    }
    return (files);
    }catch(SecurityException se){
        System.out.println("SECURITY EXCEPTION");
    }
   // }
    return null;
  }

public String CheckFileType(File f){
    if(!f.isDirectory()){
      int i = f.getName().lastIndexOf('.');
        if (i > -1) {
            String extension;
            extension = f.getName().substring(i+1);
            return extension;
       }
    }
    else{
        return "dir";
    }
      return null;
}

public static long DirectorySize(File directory) {
        long length = 0;
        File []filelist = directory.listFiles();
        int i;
        
        if(filelist == null){
            return 0;
        }

        for(i=0;i<filelist.length;i++){
            if(filelist[i].isDirectory()){
                length += DirectorySize(filelist[i]);
            }
            else{
                length += filelist[i].length();
            }
        }
        return length;
    }

    public void addHometoFaves(){

        JButton favourite = new JButton("Home");
        favPanel.add(favourite);

        favourite.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu fav = new JPopupMenu("Remove From Favourites");
                    JMenuItem remove = new JMenuItem("Remove From Favourites");
                    remove.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent event) {
                            favPanel.removeAll();
                            favPanel.revalidate();
                            homeRemoved = true;
                            readXMLFile();
                            SwingUtilities.updateComponentTreeUI(FileBrowser.this);
                        }
                    });
                    fav.add(remove);
                    fav.show(event.getComponent(), 30, 30);
                    repaint();
                }
            }
        });

        favourite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                try {
                    centerPanel.removeAll();
                    centerPanel.revalidate();
                    updateDirectory(homePath);
                    SwingUtilities.updateComponentTreeUI(FileBrowser.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

//    //Method to read the XML file .
   public void readXMLFile() {

       try {
           File xmlFile = new File(xmlPath);
           if (xmlFile.exists()) {
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               Document doc = dBuilder.parse(xmlFile);

               doc.getDocumentElement().normalize();

               NodeList nList = doc.getElementsByTagName("directory");

               for (int curr = 0; curr < nList.getLength(); curr++) {

                   Node nNode = nList.item(curr);

                   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                       Element eElement = (Element) nNode;
                       favouritesInit(eElement.getElementsByTagName("name").item(0).getTextContent());
                   }
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   //Method to initialize favourites panel after reading the XML file.
    public void favouritesInit(String name) {

        MouseListenerFav fav = new MouseListenerFav();

        JButton favourite = new JButton(name);
        favPanel.add(favourite);

        favourite.addMouseListener(fav);

        favourite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                try {
                    JButton button = new JButton();
                    button = (JButton) event.getSource();

                    String path;
                    path = findFavouritePath(button.getText());

                    centerPanel.removeAll();
                    centerPanel.revalidate();

                    updateDirectory(path);

                    SwingUtilities.updateComponentTreeUI(FileBrowser.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    class MouseListenerFav extends MouseAdapter {
        public void mouseClicked(MouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON3) {
                JButton button = (JButton) event.getSource();
                JPopupMenu fav = new JPopupMenu("Remove From Favourites");

                JMenuItem remove = new JMenuItem("Remove From Favourites");

                remove.setActionCommand(button.getText());
                remove.addActionListener(new removeListener());

                fav.add(remove);
                fav.show(event.getComponent(), 25, 25);
                repaint();
            }
        }
    }
    
    class removeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                removeFromXMLFile(event.getActionCommand());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     public void removeFromXMLFile(String name) throws Exception {
        
        File xmlFile = new File(xmlPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("directory");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                if(eElement.getElementsByTagName("name").item(0).getTextContent().equals(name)){
                    Node parent = eElement.getParentNode();
                    if ( parent != null ) 
                        parent.removeChild(eElement);
                    doc.normalize();

                    //Save the file.
                    DOMSource source = new DOMSource(doc);
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    StreamResult result = new StreamResult(xmlPath);
                    transformer.transform(source, result);

                    favPanel.removeAll();
                    favPanel.revalidate();
                    if(!homeRemoved){
                        addHometoFaves();
                    }
                    readXMLFile();
                    SwingUtilities.updateComponentTreeUI(FileBrowser.this);
                }
            }
        }
    }
//        //Method to search for a name in the XML file and return its path.
   public String findFavouritePath(String favouriteName) throws ParserConfigurationException, SAXException, IOException  {

       String path = new String();
       File xmlFile = new File(xmlPath);
       if (xmlFile.exists()) {
           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
           Document doc = dBuilder.parse(xmlFile);

           doc.getDocumentElement().normalize();
           NodeList nList = doc.getElementsByTagName("directory");
           for (int temp = 0; temp < nList.getLength(); temp++) {

               Node nNode = nList.item(temp);

               if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                   Element eElement = (Element) nNode;
                   if (eElement.getElementsByTagName("name").item(0).getTextContent().equals(favouriteName)) {
                       return (eElement.getElementsByTagName("path").item(0).getTextContent());
                   }
               }
           }
       }
       return path;
   }

  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

//        basicPanel = new javax.swing.JPanel();
        favPanel = new javax.swing.JPanel();
        centerPanel = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        searchText = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        breadcrumbPanel = new javax.swing.JPanel();
        centerScrollPanel = new javax.swing.JScrollPane();
        scrollFaves = new javax.swing.JScrollPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exit = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cut = new javax.swing.JMenuItem();
        copy = new javax.swing.JMenuItem();
        paste = new javax.swing.JMenuItem();
        rename = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenuItem();
        addToFavourites = new javax.swing.JMenuItem();
        properties = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        searchItem =  new javax.swing.JCheckBoxMenuItem();
        hiddenItem =  new javax.swing.JMenuItem();


        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("File Browser");

        favPanel.setBackground(new java.awt.Color(240, 190, 240));

        javax.swing.GroupLayout favPanelLayout = new javax.swing.GroupLayout(favPanel);
        favPanel.setLayout(favPanelLayout);
        favPanelLayout.setHorizontalGroup(
            favPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );
        favPanelLayout.setVerticalGroup(
            favPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE) 
        );

        scrollFaves.setViewportView(favPanel);

        searchText.setToolTipText("Please type what you wish to search");

        searchText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextActionPerformed(evt);
            }
        });


        centerScrollPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout centerPanelLayout = new javax.swing.GroupLayout(centerPanel);
        centerPanel.setLayout(centerPanelLayout);
        centerPanelLayout.setHorizontalGroup(
            centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
        );
        centerPanelLayout.setVerticalGroup(
            centerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );

        centerScrollPanel.setViewportView(centerPanel);

        // jButton1.setForeground(new java.awt.Color(170, 85, 127));
        jButton1.setText("Search");

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addComponent(searchText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)))
        );

        breadcrumbPanel.setBackground(new java.awt.Color(253, 253, 150));

        javax.swing.GroupLayout breadcrumbPanelLayout = new javax.swing.GroupLayout(breadcrumbPanel);
        breadcrumbPanel.setLayout(breadcrumbPanelLayout);
        breadcrumbPanelLayout.setHorizontalGroup(
            breadcrumbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
       breadcrumbPanelLayout.setVerticalGroup(
            breadcrumbPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        
        menuBar.setBackground(new java.awt.Color(197, 180, 227));
        menuBar.setForeground(new java.awt.Color(170, 85, 127));

        
        fileMenu.setForeground(new java.awt.Color(0, 0, 0));
        fileMenu.setText("File");
        //File options
        exit.setBackground(new java.awt.Color(195, 199, 209));
        exit.setForeground(new java.awt.Color(0, 0, 0));
        exit.setText("Exit");
        exit.addActionListener(menuItemListener);
        fileMenu.add(exit);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        //Edit options
        cut.setText("Cut");
        cut.addActionListener(menuItemListener);
        editMenu.add(cut);
        
        copy.setText("Copy");
        copy.addActionListener(menuItemListener);
        editMenu.add(copy);

        paste.setText("Paste");
        
//        paste.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                pasteActionPerformed(evt);
//            }
//        });
        paste.addActionListener(menuItemListener);
        editMenu.add(paste);
        paste.setEnabled(false);

        rename.setText("Rename");
        rename.addActionListener(menuItemListener);
        editMenu.add(rename);

        delete.setText("Delete");
        delete.addActionListener(menuItemListener);
        editMenu.add(delete);

        addToFavourites.setText("Add To Favourites");
        addToFavourites.addActionListener(menuItemListener);
        editMenu.add(addToFavourites);

        properties.setText("Properties");
        properties.addActionListener(menuItemListener);
        editMenu.add(properties);

        menuBar.add(editMenu);

        //view menu
        viewMenu.setText("View");

        searchItem.setText("Search");
        searchItem.addActionListener(menuItemListener);
        viewMenu.add(searchItem);

        SearchField=false;

        searchPanel.setVisible(false);

        searchItem.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                //Enable-Disable the north panel.
                if (e.getButton() == MouseEvent.BUTTON1){
                    if(SearchField==true){
                        searchPanel.setVisible(false);
                        SearchField=false;
                    }
                    else{
                        searchPanel.setVisible(true);
                        SearchField=true;
                    }
                } 
            }
        });

        hiddenItem.setText("Hidden Files/Folders");
        hiddenItem.addActionListener(menuItemListener);
        viewMenu.add(hiddenItem);


        menuBar.add(viewMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollFaves, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(centerScrollPanel)
                            .addComponent(breadcrumbPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(breadcrumbPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(centerScrollPanel)
                .addContainerGap())
            .addComponent(scrollFaves)
        );

        setBounds(0, 0, 600, 500);
    }// </editor-fold>                        

    private void newWindowActionPerformed(java.awt.event.ActionEvent evt)     {                                          
       
    }                                         

    private void pasteActionPerformed(java.awt.event.ActionEvent evt) 
    { 

    }                                     

    private void searchTextActionPerformed(java.awt.event.ActionEvent evt) {                                           
       
    }                                          


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileBrowser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileBrowser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JMenuItem addToFavourites;
    private javax.swing.JPanel breadcrumbPanel;
    private javax.swing.JMenuItem copy;
    private javax.swing.JMenuItem cut;
    private javax.swing.JMenuItem delete;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exit;
    private javax.swing.JPanel favPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane scrollFaves;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem paste;
    private javax.swing.JMenuItem properties;
    private javax.swing.JMenuItem rename;
    private javax.swing.JMenuItem searchItem;
    private javax.swing.JMenuItem hiddenItem;
    private javax.swing.JScrollPane centerScrollPanel;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTextField searchText;
    // End of variables declaration                   
}
