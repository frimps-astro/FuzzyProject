package ui.utils;

import storage.BasisStorage;
import storage.SetObjectStorage;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UIDialogs {
    private static UIDialogs UIDIALOGS = null;

    public static UIDialogs getInstance() {
        if (UIDIALOGS == null) UIDIALOGS = new UIDialogs();
        return UIDIALOGS;
    }

    public Dialog notifyDialog(String note, Rectangle pos) {
        JFrame frame = new JFrame();
        Dialog notice = new Dialog(frame, "Notification", true);
        notice.setSize(300, 250);
        notice.setLocationRelativeTo(null);
        notice.setBackground(Color.WHITE);

        JLabel noteL = new JLabel(note, SwingConstants.CENTER);
        noteL.setBounds(0, 50, notice.getWidth(), 30);

        JButton okBtn = new JButton ("OK");
        okBtn.addActionListener(e->notice.setVisible(false));
        okBtn.setBounds(notice.getSize().width/4, notice.getHeight()-80, UIConstants.BUTTONWIDTH,UIConstants.BUTTONHEIGHT+15);

        notice.add(noteL);
        notice.add(okBtn);

        notice.setResizable(false);
        notice.setLayout(null);
        notice.setVisible(true);

        return notice;
    }

    public JDialog createSetObjectDialog(JFrame frame){
        final JDialog modelDialog = new JDialog(frame, "New SetObject",
                Dialog.ModalityType.DOCUMENT_MODAL);
        modelDialog.setSize(300, 300);
        modelDialog.setLocationRelativeTo(null);
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

        sets.addActionListener(e-> {
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
        });

        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(e-> {
            int selectedIndex = sets.getSelectedIndex();
            String component1 = null;
            String component2 = null;

            if ( selectedIndex== 1 || selectedIndex == 2) {
                if (setListing1.getSelectedIndex() != 0 && setListing2.getSelectedIndex() != 0){
                    component1 = Objects.requireNonNull(setListing1.getSelectedItem()).toString();
                    component2 = Objects.requireNonNull(setListing2.getSelectedItem()).toString();
                    UIMethods.getInstance().createSetObject(selectedIndex, component1, component2);
                    modelDialog.setVisible(false);
                }
            } else if (selectedIndex == 3) {
                if (setListing1.getSelectedIndex() != 0 && basisListing.getSelectedIndex() != 0){
                    component1 = Objects.requireNonNull(basisListing.getSelectedItem()).toString();
                    component2 = Objects.requireNonNull(setListing1.getSelectedItem()).toString();
                    UIMethods.getInstance().createSetObject(selectedIndex, component1, component2);
                    modelDialog.setVisible(false);
                }
            }
        });

        dialogContainer.add(setSelection, BorderLayout.CENTER);
        dialogPanel.add(createBtn);
        dialogContainer.add(dialogPanel, BorderLayout.SOUTH);

        return modelDialog;
    }
}
