package ui;

import relations.Relation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Michael Winter
 * based on RowHeaderTable.java, author unknown
 */

public class RelationDisplay extends JScrollPane {

    public static RelationDisplay RELATIONDISPLAY = null;

    public static RelationDisplay getInstance() {
        if (RELATIONDISPLAY == null) RELATIONDISPLAY = new RelationDisplay();
        return RELATIONDISPLAY;
    }
    private final int ROW_HEIGHT = 32;

    private int numRowEls;
    private int numColEls;
    private Function<Integer,String> rowElements ;
    private Function<Integer,String> columnElements;
    private String[] matrixElements;
    private int[][] matrix;

    private int zoomLevel = 0;

    private class MatrixTableModel extends AbstractTableModel {

        public MatrixTableModel() {
        }

        @Override
        public int getRowCount() {
            return numRowEls;
        }

        @Override
        public int getColumnCount() {
            return numColEls+1;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnIndex==0? "" : columnElements.apply(columnIndex-1);
        }

        @Override
        public Class<String> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return columnIndex==0? rowElements.apply(rowIndex) : matrixElements[matrix[rowIndex][columnIndex-1]];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            int val = Arrays.asList(matrixElements).indexOf(aValue);
            if (val!=-1) {
                matrix[rowIndex][columnIndex-1] = val;
            }
        }
    }

    public void setRelation(Relation rel, JPanel panel, JButton accept, int[][] oldM) {
        this.numRowEls = rel.getDom().getNumElements();
        this.rowElements = rel.getDom()::getElementNames;
        this.numColEls = rel.getCod().getNumElements();
        this.columnElements = rel.getCod()::getElementNames;
        this.matrixElements = rel.getAlgebra().getElementNames();
        this.matrix = rel.getMatrix();
        TableColumnModel columnModel = new DefaultTableColumnModel() {
            boolean column1 = true;
            @Override
            public void addColumn(TableColumn col) {
                if (column1) {
                    column1 = false;
                } else {
                    super.addColumn(col);
                }
            }
        };
        TableColumnModel column1Model = new DefaultTableColumnModel() {
            boolean column1 = true;
            @Override
            public void addColumn(TableColumn col) {
                if (column1) {
                    col.setMaxWidth(col.getPreferredWidth());
                    super.addColumn(col);
                    column1 = false;
                }
            }
            public String getToolTipText(MouseEvent e) {
                int index = columnModel.getColumnIndexAtX(e.getPoint().x);
                return rowElements.apply(index);
            }
        };
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        MatrixTableModel model = new MatrixTableModel();
        JTable table = new JTable(model,columnModel);
        table.createDefaultColumnsFromModel();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(ROW_HEIGHT);
        table.setRowSelectionAllowed(false);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                int btn = evt.getButton();
                if (btn == 1) {
                    int val = matrix[row][col];
                    val = (val == 0)? matrixElements.length - 1 : val - 1;
                    matrix[row][col] = val;
                    table.updateUI();
                } else if (btn == 3) {
                    int val = matrix[row][col];
                    val = (val == matrixElements.length - 1)? 0 : val + 1;
                    matrix[row][col] = val;
                    table.updateUI();
                }
                accept.setEnabled(!Arrays.deepEquals(oldM, rel.getMatrix()));
                panel.updateUI();
            }
        });
        DefaultCellEditor editor = new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                Component c = super.getTableCellEditorComponent(table,"",isSelected,row,column);
                return c;
            }
        };
        editor.setClickCountToStart(1);

        table.setDefaultEditor(String.class,editor);
        table.setDefaultRenderer(String.class,centerRenderer);

        JTableHeader tableHeader = new JTableHeader(columnModel) {
            @Override
            public String getToolTipText(MouseEvent e) {
                String result = null;
                int index = columnModel.getColumnIndexAtX(e.getPoint().x);
                if (index!=-1) result = columnElements.apply(index);
                return result;
            }
        };
        tableHeader.setBackground(this.getBackground());
        tableHeader.setPreferredSize(new Dimension(0,ROW_HEIGHT));
        tableHeader.setReorderingAllowed(false);
        table.setTableHeader(tableHeader);

        JTable column1 = new JTable(model,column1Model) {
            @Override
            public void changeSelection(int row, int col, boolean toggle, boolean expand ) {}
            @Override
            public String getToolTipText(MouseEvent e) {
                String result = null;
                int index = rowAtPoint(e.getPoint());
                if (index!=-1) result = rowElements.apply(index);
                return result;
            }
        };
        column1.createDefaultColumnsFromModel();
        column1.setBackground(this.getBackground());
        column1.setRowHeight(ROW_HEIGHT);
        column1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        column1.getTableHeader().setReorderingAllowed(false);
        column1.setDefaultRenderer(String.class,centerRenderer);

        JViewport jv = new JViewport();
        jv.setView(column1);
        jv.setPreferredSize(column1.getMaximumSize());


        this.getViewport().add(table);
        this.setRowHeader(jv);
        this.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER,column1.getTableHeader());
    }

    public int[][] getMatrix() {
        return matrix;
    }

}
