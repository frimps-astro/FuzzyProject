package ui;

import main.Project;
import storage.BasisStorage;
import storage.HeytingAlgebraStorage;
import storage.SetObjectStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

import static classutils.LoadPaths.DATAPATH;

public class UserInterface {
    private static UserInterface USERINTERFACE = null;
    private JComponent setObjectPanel, heytingPanel, basisPanel;
    JButton addProjectBtn;
    JComboBox<String> rightCombox;
    JFrame frame;
    private Project project;
    JSplitPane jSplitPane = new JSplitPane();


    public static UserInterface getInstance() {
        if (USERINTERFACE == null) USERINTERFACE = new UserInterface();
        return USERINTERFACE;
    }

    public void createUI(){
        project = Project.getInstance();

        //create main frame
        frame = createFrame();

        //setup split pane
        jSplitPane.setResizeWeight(1);

        frame.add(jSplitPane);

        //create right split pane components
        JTabbedPane rightTabbedPane = new JTabbedPane();

        rightCombox = new JComboBox<>(loadProjects());
        rightCombox.insertItemAt("--choose project--", 0);
        rightCombox.setSelectedIndex(0);

        setObjectPanel = new JPanel();
        heytingPanel = new JPanel();
        basisPanel = new JPanel();

        rightTabbedPane.add("SetObject", setObjectPanel);
        rightTabbedPane.add("HeytingAlgebra", heytingPanel);
        rightTabbedPane.add("Basis", basisPanel);

        JPanel projectManager = new JPanel();
        projectManager.add(rightCombox);
        projectManager.add(addProject());

        //right pane splitter
        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, projectManager, rightTabbedPane);
        rightSplit.setDividerSize(0);

        rightSplit.setSize(new Dimension(200,400));
        jSplitPane.setRightComponent(rightSplit);
        projectListener(rightCombox);

    }

    public void addView(Component component){
        component.setSize(new Dimension(200,400));
        jSplitPane.setLeftComponent(component);
    }

    private String[] loadProjects(){
        File file = new File(DATAPATH);
        String[] projects = file.list((current, name) -> new File(current, name).isDirectory());

        return projects;
    }

    private JList<String> storageItems(String storage){
        JList<String> entities;

        switch (storage){
            case "basis" -> entities = new JList<>(BasisStorage.getInstance().getEntityNames());
            case "heyting" -> entities = new JList<>(HeytingAlgebraStorage.getInstance().getEntityNames());
            default -> entities = new JList<>(SetObjectStorage.getInstance().getEntityNames());
        }
        return entities;
    }

    private JFrame createFrame(){
        JFrame frame = new JFrame("Social Choice Theory");
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        return frame;
    }

    private void projectListener(JComboBox<String> comboBox){
        comboBox.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedIndex() != 0){
                    processProject(Objects.requireNonNull(comboBox.getSelectedItem()).toString());
                }
            }
        });
    }

    private void processProject(String projectName){
        project.setName(projectName);
        project.setProject();

        emptyPanels();
        setObjectPanel.add(storageItems(""));
        heytingPanel.add(storageItems("heyting"));
        basisPanel.add(storageItems("basis"));

        //add button for new set object
        setObjectPanel.add(setObjectCreateBtn());

        SwingUtilities.updateComponentTreeUI(frame);
    }

    private Component setObjectCreateBtn(){
        JButton createSetObjectBtn = new JButton("Create SetObject");
        createSetObjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSetObjectDialog().setVisible(true);
            }
        });
        return createSetObjectBtn;
    }

    private void emptyPanels(){
        setObjectPanel.removeAll();
        heytingPanel.removeAll();
        basisPanel.removeAll();
    }

    private JComponent addProject(){
        addProjectBtn = new JButton("New Project");

        addProjectBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createProjectDialog().setVisible(true);
                }
            });

        return addProjectBtn;
    }

    private void createNewProject(String name){
        String[] directories = {"/Basis", "/HeytingAlgebras", "/SetObjects", "/Relations"};
        String projectPath = DATAPATH + name;
        int counter = 0;

        if (!new File(projectPath).exists()){
            for (String directory : directories) {
                File dir = new File(projectPath + directory);
                if (!dir.exists()) {
                    if (dir.mkdirs())
                        counter++;
                }
            }

            if (counter == 4)
                rightCombox.addItem(name);
            else
                deleteProjectDirectory(new File(projectPath));
        }
    }

    private JDialog createProjectDialog(){
        final JDialog modelDialog = new JDialog(frame, "Add New Project",
                Dialog.ModalityType.DOCUMENT_MODAL);
        modelDialog.setBounds(frame.getWidth()/3, frame.getHeight()/3, 300, 150);
        Container dialogContainer = modelDialog.getContentPane();
        dialogContainer.setLayout(new BorderLayout());

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new FlowLayout());

        JTextField projectNameField = new JTextField();
        projectNameField.setBounds(90, 0, 150, 25);
        projectNameField.setVisible(true);

        JLabel projectNameLabel = new JLabel("Project Name:");
        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewProject(projectNameField.getText());
                modelDialog.setVisible(false);
            }
        });

        dialogContainer.add(projectNameLabel, BorderLayout.PAGE_START);
        dialogContainer.add(projectNameField, BorderLayout.NORTH);
        dialogPanel.add(createBtn);
        dialogContainer.add(dialogPanel, BorderLayout.SOUTH);

        return modelDialog;
    }

    private JDialog createSetObjectDialog(){
        final JDialog modelDialog = new JDialog(frame, "New SetObject",
                Dialog.ModalityType.DOCUMENT_MODAL);
        modelDialog.setBounds(frame.getWidth()/3, frame.getHeight()/3, 300, 300);
        Container dialogContainer = modelDialog.getContentPane();
        dialogContainer.setLayout(new BorderLayout());

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new FlowLayout());

        JComboBox<String> sets = new JComboBox<>(new String[]{"--choose set--", "Sum", "Product", "Power"});
        sets.setSelectedIndex(0);

        dialogContainer.add(sets, BorderLayout.NORTH);

        //set body
        JPanel setSelection = new JPanel();
        String[] setlists = SetObjectStorage.getInstance().getEntityNames();

        JComboBox<String> setListing1= new JComboBox<>(setlists);
        setListing1.insertItemAt("--choose set component 1--", 0);
        setListing1.setSelectedIndex(0);

        JComboBox<String> setListing2= new JComboBox<>(setlists);
        setListing2.insertItemAt("--choose set component 2--", 0);
        setListing2.setSelectedIndex(0);

        String[] basislists = BasisStorage.getInstance().getEntityNames();
        JComboBox<String> basisListing= new JComboBox<>(basislists);
        basisListing.insertItemAt("--choose basis component--", 0);
        basisListing.setSelectedIndex(0);

        sets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = sets.getSelectedIndex();
                if ( selectedIndex== 1 || selectedIndex == 2) {
                    setSelection.removeAll();
                    setSelection.add(setListing1);
                    setSelection.add(setListing2);

                } else if (selectedIndex == 3) {
                    setListing1.removeItemAt(0);
                    setListing1.insertItemAt("--choose body component--", 0);
                    setListing1.setSelectedIndex(0);

                    setSelection.removeAll();
                    setSelection.add(basisListing);
                    setSelection.add(setListing1);
                }
                SwingUtilities.updateComponentTreeUI(dialogContainer);
            }
        });

        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = sets.getSelectedIndex();
                String component1 = null;
                String component2 = null;

                if ( selectedIndex== 1 || selectedIndex == 2) {
                    if (setListing1.getSelectedIndex() != 0 && setListing2.getSelectedIndex() != 0){
                        component1 = Objects.requireNonNull(setListing1.getSelectedItem()).toString();
                        component2 = Objects.requireNonNull(setListing2.getSelectedItem()).toString();
                        createSetObject(selectedIndex, component1, component2);
                        modelDialog.setVisible(false);
                    }
                } else if (selectedIndex == 3) {
                        if (setListing1.getSelectedIndex() != 0 && basisListing.getSelectedIndex() != 0){
                            component1 = Objects.requireNonNull(basisListing.getSelectedItem()).toString();
                            component2 = Objects.requireNonNull(setListing1.getSelectedItem()).toString();
                            createSetObject(selectedIndex, component1, component2);
                            modelDialog.setVisible(false);
                        }
                }
            }
        });

        dialogContainer.add(setSelection, BorderLayout.CENTER);
        dialogPanel.add(createBtn);
        dialogContainer.add(dialogPanel, BorderLayout.SOUTH);

        return modelDialog;
    }

    private void createSetObject(int selectedIndex, String component1, String component2) {
       if (component1 != null && component2 != null){
           switch (selectedIndex){
               case 1 -> SetObjectStorage.getInstance()
                       .createSumSetObject(component1+"+"+component2, component1, component2);
               case 2 -> SetObjectStorage.getInstance()
                       .createProductSetObject(component1+"x"+component2, component1, component2);
               case 3 -> SetObjectStorage.getInstance()
                       .createPowerSetObject(component1+"^"+component2, component2, component1);
           }
       }
    }

    private boolean deleteProjectDirectory(File dir) {
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteProjectDirectory(file);
            }
        }
        return dir.delete();
    }
}
