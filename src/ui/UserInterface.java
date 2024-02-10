package ui;

import main.Project;
import ui.utils.UIConstants;
import ui.utils.UIDialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import static classutils.LoadPaths.DATAPATH;


public class UserInterface extends JFrame{
    private static UserInterface USERINTERFACE = null;
    private Project project;
    JButton loadProjectBtn, declarationBtn, addProjectBtn;
    JComboBox<String> projectsList;
//    JFrame frame;
    public static UserInterface getInstance() {
        if (USERINTERFACE == null) USERINTERFACE = new UserInterface();
        return USERINTERFACE;
    }

    public void createUI(){
//        frame = new JFrame("Social Choice Theory");
        setTitle("Social Choice Theory");
        setSize(400, 400);
        setLocationRelativeTo(null);

        loadProjectBtn = new JButton("Load Project");
        declarationBtn = new JButton("Declaration");

        projectsList = new JComboBox<>(readProjectLists());
        projectsList.insertItemAt("--choose project--", 0);
        projectsList.setSelectedIndex(0);

        projectsList.setBounds(20, 20, 180,40);
        loadProjectBtn.setBounds(200, 20, 150, UIConstants.BUTTONHEIGHT);
        loadProjectBtn.addActionListener(this::loadProjectAction);

        add(projectsList);
        add(loadProjectBtn);
        add(newProject());
        add(footer());

        setLayout(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadProjectAction(ActionEvent e){
        if (projectsList.getSelectedIndex() != 0) {
            UIConstants.PROJECTNAME = projectsList.getSelectedItem().toString();

            project = Project.getInstance();
            project.setName(UIConstants.PROJECTNAME);
            project.setProject();

            JDialog dialog = new JDialog(new ProjectUI());
        } else {
            UIDialogs.getInstance().notifyDialog("Please select a project to load", getBounds());
        }
    }

    private String[] readProjectLists(){
        File file = new File(DATAPATH);
        String[] projects = file.list((current, name) -> new File(current, name).isDirectory());

        return projects;
    }

    private JComponent newProject(){
        addProjectBtn = new JButton("New Project");
        addProjectBtn.setBounds(getSize().width/3, 100, 150,UIConstants.BUTTONHEIGHT);

        addProjectBtn.addActionListener( e ->
                createProjectDialog().setVisible(true));

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
                projectsList.addItem(name);
            else
                deleteProjectDirectory(new File(projectPath));
        }
    }

    private JDialog createProjectDialog(){
        final JDialog modelDialog = new JDialog(new Frame(), "Add New Project",
                Dialog.ModalityType.DOCUMENT_MODAL);
        modelDialog.setBounds(getBounds().x+50, getBounds().y+120, 300, 200);
        Container dialogContainer = modelDialog.getContentPane();
        dialogContainer.setLayout(new BorderLayout());

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new FlowLayout());

        JTextField projectNameField = new JTextField();
        projectNameField.setBounds(90, 0, 150, 30);
        projectNameField.setVisible(true);

        JLabel projectNameLabel = new JLabel("Project Name:");
        JButton createBtn = new JButton("Create");
        createBtn.setSize(UIConstants.BUTTONWIDTH, UIConstants.BUTTONHEIGHT);
        createBtn.addActionListener(e -> {
                createNewProject(projectNameField.getText());
                modelDialog.setVisible(false);
        });

        dialogContainer.add(projectNameLabel, BorderLayout.PAGE_START);
        dialogContainer.add(projectNameField, BorderLayout.NORTH);
        dialogPanel.add(createBtn);
        dialogContainer.add(dialogPanel, BorderLayout.SOUTH);

        return modelDialog;
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

    private JComponent footer() {
        String details = "Student: "+UIConstants.STUDENT+" || Supervisor: "+UIConstants.SUPERVISOR;
        JLabel footL = new JLabel(details, SwingConstants.CENTER);
        footL.setFont(new Font("", Font.ITALIC, 12));
        footL.setBounds(0, getHeight()-55, getWidth(),UIConstants.BUTTONHEIGHT);
        return footL;
    }
}
