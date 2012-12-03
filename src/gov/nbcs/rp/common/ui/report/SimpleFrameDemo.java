/**
 * Copyright 浙江易桥 版权所有
 * 
 * 滚动项目库子系统
 * 
 * @author 钱自成
 * 
 * @version 1.0
 */

package gov.nbcs.rp.common.ui.report;

/**
 */
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.JFrame;

import gov.nbcs.rp.common.datactrl.DataSet;
import gov.nbcs.rp.common.tree.HierarchyListGenerator;
import gov.nbcs.rp.common.tree.Node;
import gov.nbcs.rp.common.ui.report.cell.PropertyProvider;
import gov.nbcs.rp.common.ui.report.cell.editors.spinner.NumberSpinnerCellEditor;
import com.fr.cell.editor.CellEditor;

public class SimpleFrameDemo extends JFrame {
    public SimpleFrameDemo() throws Exception {
        super("Simple Frame Demo...");
        DataSet ds = DataSet.createClient();
        for (int i = 0; i < 5000; i++) {
            int j = 0;
            ds.append();
            for (; j < 10; j++) {
                try {
                    ds.fieldByName("" + j).setValue(new Integer(j));
                } catch (Error ex) {
                    System.out.println("error on " + j);
                    ex.printStackTrace();
                    System.exit(0);
                }
            }
        }
        DataSet ds2 = DataSet.createClient();
        for (int j = 0; j < 10; j++) {
            ds2.append();
            ds2.fieldByName("id").setValue("" + j);
        }
        Node n = HierarchyListGenerator.getInstance().generate(ds2, "id", "",
                null);
        TableHeader header = new TableHeader(n);
        Report report = new Report(header, ds, new PropertyProvider() {

            public boolean isEditable(String bookmark, Object fieldId) {
                if (fieldId.equals("1")) {
					return false;
				}
                return true;
            }

            public CellEditor getEditor(String bookmark, Object fieldId) {
                if (fieldId.equals("2")) {
					return new NumberSpinnerCellEditor();
				}
                return null;
            }

            public double getColumnWidth(Object fieldId) {
                // TODO Auto-generated method stub
                return 0;
            }

            public Format getFormat(String bookmark, Object fieldId) {
                if (fieldId.equals("3")) {
					return new DecimalFormat("#,##0.00");
				}
                return null;
            }

            public String getFieldName(Object fieldId) {
                // TODO Auto-generated method stub
                return fieldId.toString();
            }

            public Object getFieldId(String fieldName) {
                // TODO Auto-generated method stub
                return fieldName;
            }

        });
        ReportUI ui = new ReportUI(report);
        this.getContentPane().add(ui);
        this.setSize(800, 480);
        this.setVisible(true);
    }

    public static void main(String args[]) throws Exception {

        // DataSet ds = DataSet.createClient();
        // ds.append();
        // ds.fieldByName("A").setValue("0");
        // ds.toogleBookmark();
        // ds.append();
        // ds.fieldByName("A").setValue("3");
        // String insertPos = ds.toogleBookmark();
        // ds.applyUpdate();
        // ds.gotoBookmark(insertPos);
        // ds.insert();
        // ds.fieldByName("A").setValue("1");
        // String b1 = ds.toogleBookmark();
        // ds.insert();
        // ds.fieldByName("A").setValue("2");
        // String b2 = ds.toogleBookmark();
        // System.out.println();
        // ds.applyUpdate();
        // ds.gotoBookmark(b1);
        // ds.delete();
        // ds.gotoBookmark(b2);
        // ds.delete();
        // ds.applyUpdate();
        // System.out.println();
        new SimpleFrameDemo();
        // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // DataSet ds = DataSet.createClient();
        // for(int i=0;i<10;i++) {
        // ds.append();
        // ds.fieldByName("id").setValue(new Integer(i));
        // ds.fieldByName("name").setValue("name"+i);
        // }
        // JFrame frm = new JFrame();
        // frm.setSize(500, 300);
        // final CustomTable table = new CustomTable(new String[] { "AA", "BB"
        // },
        // new String[] { "ID", "NAME" }, ds, false, new String[]{"ID","NAME"});
        // table.setPreferredScrollableViewportSize(new Dimension(500,200));
        // table.setSelectList("NAME",new JComboBox(new String[]{"A","B","C"}));
        // table.reset();
        // table.getTable().addKeyListener(new Ks(ds));
        // table.addListSelectionListener(new ListSelectionListener() {
        // public void valueChanged(ListSelectionEvent e) {
        // if(!e.getValueIsAdjusting()) {
        // int ii[] = table.getTable().getSelectedRows();
        // for(int i=0;i<ii.length;i++) {
        // System.out.println(ii[i]);
        // }
        // }
        // }
        // });
        //                
        // frm.getContentPane().add(table);
        // frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // frm.setVisible(true);
        // Pattern p =
        // Pattern.compile("\\D*(\\d+)\\D*",Pattern.CASE_INSENSITIVE);
        // Matcher mat = p.matcher("f18");
        // mat.find();
        // System.out.println(mat.group(1));
        // Node root = TreeFactory.getInstance().createTreeNode(null);
        // Node n1 = TreeFactory.getInstance().createTreeNode(null);
        // Node n2 = TreeFactory.getInstance().createTreeNode(null);
        // Node n21 = TreeFactory.getInstance().createTreeNode(null);
        // Node n22 = TreeFactory.getInstance().createTreeNode(null);
        // root.append(n1);root.append(n2);
        // n2.append(n21);n2.append(n22);
        // System.out.println(n1.getLevel());
        // System.out.println(root.getWidth());
        // System.out.println(root.getHeight());

    }
}