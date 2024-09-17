/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.toedter.calendar.JCalendar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import model.Class1;
import model.Mysql;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.swing.JLabel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import java.util.logging.*;
/**
 *
 * @author WW
 */
public  class Home extends javax.swing.JFrame implements  Runnable,ThreadFactory {
    public static HashMap<String,Integer> subjectMap=new HashMap();
    public static HashMap<String,Integer> teacherMap=new HashMap();
    public static HashMap<String,Integer> classMap=new HashMap();
    public static HashMap<String,Integer> monthMap=new HashMap<>();
    public static HashMap<String,Class1> classMap2=new HashMap();
    public static HashMap<String,Integer> jobmap=new HashMap();
String path2=null;
String path3=null;

    /**
     * Creates new form Home
     */
    public Home() {
        initComponents();
        loadClass1();
        loadSubjecttable();
        loadStudent();
        loadSubject();
        loadMonth();
        loadClassTable();
        loadTeachers();
       initWebcam();
       loadjobs();
       loademployee();
       loadTeacherCombo();
       loadstudentSubjectTable();
        
    }
    
    
    private WebcamPanel panel = null;
    private Webcam webcam = null;

    private static final long serialVersionUID = 6441489157408381878L;
    private Executor executor = Executors.newSingleThreadExecutor(this);
    
    private void initWebcam() {
        Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0); //0 is default webcam
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        jPanel21.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 300));

        executor.execute((Runnable) this);
    }
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
            }

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                result = new MultiFormatReader().decode(bitmap);
            } catch (NotFoundException e) {
                //No result...
            }

            if (result != null) {
                jTextField6.setText(result.getText());
                jTextField5.setText(result.getText());
                jTextField76.setText(result.getText());
               jTextField70.setText(result.getText());
               jTextField68.setText(result.getText());
            jTextField109.setText(result.getText());
                
                
            }
        } while (true);
    }

    
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }
    public void loadjobs(){
    
        try {
            ResultSet rs=Mysql.search("SELECT * FROM job");
            Vector v=new Vector();
            v.add("SELECT");
            while(rs.next()){
             v.add(rs.getString("name"));
             jobmap.put(rs.getString("name"), rs.getInt("id"));
           }
            DefaultComboBoxModel m=new DefaultComboBoxModel(v);
            jComboBox12.setModel(m);
            jComboBox15.setModel(m);
            jComboBox16.setModel(m);
        } catch (Exception e) {
        }
    }
    public void loadclassstudent(String id){
        try {
          ResultSet rs=  Mysql.search("SELECT * FROM class INNER JOIN student_has_class1 ON student_has_class1.class1_class_no=class.Classno INNER JOIN student ON student.Sno=student_has_class1.student_student_no INNER JOIN teacher ON teacher.Tno=class.teacher_teacher_no INNER JOIN subject ON subject.Subno=class.subject_subject_id WHERE Sno='"+id+"' ");
        Vector v=new Vector();
        v.add("Select");
        
        while(rs.next()){
           v.add(rs.getString("subject_name")+" "+rs.getString("teacher_name"));
        }
        DefaultComboBoxModel m=new DefaultComboBoxModel(v);
        jComboBox8.setModel(m);
        jComboBox4.setModel(m);
        } catch (Exception ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void loadStudent1(String id){
        try {
                
              ResultSet rs1=Mysql.search("SELECT * FROM `student` WHERE Sno='"+id+"' ");
          
          
          DefaultTableModel m1=(DefaultTableModel)jTable2.getModel();
          DefaultTableModel m2=(DefaultTableModel)jTable24.getModel();
          DefaultTableModel m3=(DefaultTableModel)jTable23.getModel();
          m1.setRowCount(0);
          while(rs1.next()){
              Vector v=new Vector();
              v.add(rs1.getString("Sno"));
              v.add(rs1.getString("student_name"));
              v.add(rs1.getString("dob"));
              v.add(rs1.getString("address"));
              
             
              
              
              m1.addRow(v);
              m2.addRow(v);
              m3.addRow(v);
          }
        } catch (Exception e) {
        }
    }
    public void loadteacherclass(String id){
        try {
            ResultSet rs=Mysql.search("SELECt * FROM class INNER JOIN teacher ON teacher.Tno=class.teacher_teacher_no INNER JOIN subject ON subject.Subno=class.subject_subject_id WHERE teacher_teacher_no='"+id+"'");
             Vector v=new Vector();
       v.add("SELECT");
       while(rs.next()){
           v.add(rs.getString("subject_name")+" "+rs.getString("teacher_name"));
       }
       DefaultComboBoxModel m=new DefaultComboBoxModel(v);
       jComboBox27.setModel(m);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public void loadStudent(){
        try {
            
              ResultSet rs=Mysql.search("SELECT * FROM `student` ");
          
          
          DefaultTableModel m1=(DefaultTableModel)jTable2.getModel();
          DefaultTableModel m2=(DefaultTableModel)jTable8.getModel();
           DefaultTableModel m3=(DefaultTableModel)jTable24.getModel();
          DefaultTableModel m4=(DefaultTableModel)jTable23.getModel();
          
          m1.setRowCount(0);
          m2.setRowCount(0);
          m3.setRowCount(0);
          m4.setRowCount(0);
          while(rs.next()){
              Vector v=new Vector();
              v.add(rs.getString("Sno"));
              v.add(rs.getString("student_name"));
              v.add(rs.getString("dob"));
              v.add(rs.getString("address"));
              
             
              
              
              m1.addRow(v);
              m2.addRow(v);
               m3.addRow(v);
                m4.addRow(v);
          }
                 
                  
                   
           
          
          
          
        } catch (Exception e) {
        }
    }
    
    public void loadMonth(){
        try {
            ResultSet rs=Mysql.search("SELECT * FROM month");
              Vector v=new Vector();
                v.add("select");
            while(rs.next()){
              v.add(rs.getString("month_name"));
              monthMap.put(rs.getString("month_name"), rs.getInt("id"));
            }
            DefaultComboBoxModel m1=new DefaultComboBoxModel(v);
          jComboBox9.setModel(m1);
          jComboBox28.setModel(m1);
          jComboBox20.setModel(m1);
        } catch (Exception e) {
        }
    }
    public void loademployee(){
    
        try {
            ResultSet rs=Mysql.search("SELECT * FROM employee INNER JOIN employee_has_job ON employee.user_id=employee_has_job.employee_user_name INNER JOIN job ON employee_has_job.job_id=job.id");
            
            DefaultTableModel m=(DefaultTableModel)jTable15.getModel();
            DefaultTableModel m1=(DefaultTableModel)jTable16.getModel();
            DefaultTableModel m2=(DefaultTableModel)jTable17.getModel();
            DefaultTableModel m3=(DefaultTableModel)jTable11.getModel();
            m.setRowCount(0);
            m1.setRowCount(0);
             m2.setRowCount(0);
              m3.setRowCount(0);
            while(rs.next()){
            
                Vector v=new Vector();
                v.add(rs.getString("user_id"));
                v.add(rs.getString("name"));
                v.add(rs.getString("mobile"));
                v.add(rs.getString("job.name"));
                
                m.addRow(v);
                m1.addRow(v);
                m2.addRow(v);
                m3.addRow(v);
            }
        } catch (Exception ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void loadTeachers1(String id){
        try {
            
          ResultSet rs=Mysql.search("SELECT * FROM `teacher` WHERE Tno='"+id+"' ");
          
          
          DefaultTableModel m1=(DefaultTableModel)jTable3.getModel();
          m1.setRowCount(0);
          while(rs.next()){
              Vector v=new Vector();
              v.add(rs.getString("Tno"));
              v.add(rs.getString("teacher_name"));
              v.add(rs.getString("address"));
              v.add(rs.getString("dob"));
             
              
              
              m1.addRow(v);
          }
         
          
        } catch (Exception e) {
        }
    }
    public void loadTeachers(){
        try {
            
          ResultSet rs=Mysql.search("SELECT * FROM `teacher` ");
          
          
          DefaultTableModel m1=(DefaultTableModel)jTable3.getModel();
          DefaultTableModel m2=(DefaultTableModel)jTable26.getModel();
          DefaultTableModel m3=(DefaultTableModel)jTable27.getModel();
          DefaultTableModel m4=(DefaultTableModel)jTable30.getModel();
          m1.setRowCount(0);
          while(rs.next()){
              Vector v=new Vector();
              v.add(rs.getString("Tno"));
              v.add(rs.getString("teacher_name"));
              v.add(rs.getString("address"));
              v.add(rs.getString("dob"));
             
              
              
              m1.addRow(v);
              m2.addRow(v);
              m3.addRow(v);
              m4.addRow(v);
          }
         
          
        } catch (Exception e) {
        }
    }
    
    public void loadClassTable(){
        try {
            ResultSet rs=Mysql.search("SELECT * FROM class INNER JOIN teacher ON teacher.Tno=class.teacher_teacher_no INNER JOIN subject ON subject.Subno=class.subject_subject_id ");
            
            DefaultTableModel m=(DefaultTableModel)jTable6.getModel();
            DefaultTableModel m1=(DefaultTableModel)jTable28.getModel();
            DefaultTableModel m2=(DefaultTableModel)jTable29.getModel();
            m.setRowCount(0);
             m1.setRowCount(0);
              m2.setRowCount(0);
            while(rs.next()){
                Vector v=new Vector();
                v.add(rs.getString("Classno"));
                v.add(rs.getString("teacher_name"));
                v.add(rs.getString("subject_name"));
                v.add(rs.getString("timeslot"));
                
                m.addRow(v);
            }
        } catch (Exception e) {
        }
    }
    public void resetClass(){
        jComboBox6.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);
        jTextField10.setText("");
    }
    public void loadattendence2(String id){
        try {
            ResultSet rs=Mysql.search("SELECT * From eattendence ");
            
            
            DefaultTableModel m1=(DefaultTableModel)jTable20.getModel();
            m1.setRowCount(0);
            
            while(rs.next()){
                Vector v= new Vector();
                v.add(rs.getString("id"));
                
                v.add(rs.getString("date"));
                v.add(rs.getString("attendence"));
                
                m1.addRow(v);
            }
            
            
        } catch (Exception e) {
        }
    }
    public void loadattendence1(String id){
        try {
            ResultSet rs=Mysql.search("SELECT * From tattendence WHERE teacher_Tno='"+id+"'");
            
            
            DefaultTableModel m1=(DefaultTableModel)jTable33.getModel();
            m1.setRowCount(0);
            
            while(rs.next()){
                Vector v= new Vector();
                v.add(rs.getString("teacher_Tno"));
                
                v.add(rs.getString("date"));
                v.add(rs.getString("attendence"));
                
                m1.addRow(v);
            }
            
            
        } catch (Exception e) {
        }
    }
    public void loadattendence(String id){
        try {
            ResultSet rs=Mysql.search("SELECT * From attendence INNER JOIN student_has_class1 ON student_has_class1.class1_class_no=attendence.student_has_class1_shcno INNER JOIN student ON student.Sno=student_has_class1.student_student_no WHERE Sno='"+id+"'");
            
            
            DefaultTableModel m1=(DefaultTableModel)jTable7.getModel();
            m1.setRowCount(0);
            
            while(rs.next()){
                Vector v= new Vector();
                v.add(rs.getString("student_name"));
                v.add(rs.getString("class1_class_no"));
                v.add(rs.getString("date"));
                v.add(rs.getString("attendence"));
                
                m1.addRow(v);
            }
            
            
        } catch (Exception e) {
        }
    }
    
    public void loadSubject1(String id){
        try {
            ResultSet rs=Mysql.search("SELECT * From `subject` WHERE Subno='"+id+"'");
            
            
            DefaultTableModel m1=(DefaultTableModel)jTable1.getModel();
            m1.setRowCount(0);
            
            while(rs.next()){
                Vector v= new Vector();
                v.add(rs.getString("Subno"));
                v.add(rs.getString("subject_name"));
                v.add(rs.getString("price"));
                
                m1.addRow(v);
            }
            
            
        } catch (Exception e) {
        }
    }
    public void loadSubjecttable(){
        try {
            ResultSet rs=Mysql.search("SELECT * From `subject`");
            
            
            DefaultTableModel m1=(DefaultTableModel)jTable1.getModel();
            m1.setRowCount(0);
            
            while(rs.next()){
                Vector v= new Vector();
                v.add(rs.getString("Subno"));
                v.add(rs.getString("subject_name"));
                v.add(rs.getString("price"));
                
                m1.addRow(v);
            }
            
            
        } catch (Exception e) {
        }
    }
    public void loadstudentSubjectTable(){
        try {
          ResultSet rs=  Mysql.search("SELECT * FROM student_has_subject INNER JOIN student ON student.Sno=student_has_subject.student_student_no "
                    + "INNER JOIN subject ON subject.Subno=student_has_subject.subject_subject_id");
            
            DefaultTableModel m=(DefaultTableModel)jTable4.getModel();
            m.setRowCount(0);
            
            while(rs.next()){
                Vector v=new Vector();
                v.add(rs.getString("student_name"));
                v.add(rs.getString("subject_name"));
                
                m.addRow(v);
            }
        } catch (Exception e) {
        }
    }
    public void calculateTotal1(){
        
       int r=jTable34.getRowCount();
        int i;
        double total=0;
       for(i=0;i<r;i++){
       String price=  String.valueOf(jTable34.getValueAt(i, 4)) ;
       total=total+Double.parseDouble(price);
       }
       jLabel329.setText(String.valueOf(total));
    }
    public void calculateTotal(){
        
       int r=jTable5.getRowCount();
        int i;
        double total=0;
       for(i=0;i<r;i++){
       String price=  String.valueOf(jTable5.getValueAt(i, 4)) ;
       total=total+Double.parseDouble(price);
       }
       jLabel65.setText(String.valueOf(total));
    }
    public void loadClassSuubjectcombo(String id){
        try {
            ResultSet rs=Mysql.search("SELECT * FROM class INNER JOIN teacher ON teacher.Tno=class.teacher_teacher_no INNER JOIN subject ON subject.Subno=class.subject_subject_id WHERE Subno='"+id+"'");
       Vector v=new Vector();
       v.add("SELECT");
       while(rs.next()){
           v.add(rs.getString("subject_name")+" "+rs.getString("teacher_name"));
       }
       DefaultComboBoxModel m=new DefaultComboBoxModel(v);
       jComboBox7.setModel(m);
       
        } catch (Exception ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public void loadClass1(){
    
    try {
        ResultSet rs=Mysql.search("SELECT * FROM class INNER JOIN teacher ON teacher.Tno=class.teacher_teacher_no INNER JOIN subject ON subject.Subno=class.subject_subject_id ");
        ResultSet rs1=Mysql.search("SELECT * FROM month ");
        Vector v1=new Vector();
        v1.add("select");
      
      while(rs.next()){
          v1.add(rs.getString("subject_name")+" "+rs.getString("teacher_name"));
          classMap.put(rs.getString("subject_name")+" "+rs.getString("teacher_name"),rs.getInt("Classno"));
          
          Class1 class2=new Class1();
          
          class2.setId(rs.getString("Classno"));
          class2.setPrice(String.valueOf(rs.getDouble("price")));
          class2.setSubject(rs.getString("subject_name"));
          class2.setTeacher(rs.getString("teacher_name"));
          classMap2.put(rs.getString("Classno"), class2);
          
      }
      DefaultComboBoxModel m1=new DefaultComboBoxModel(v1);
          jComboBox7.setModel(m1);
         
         
          
    } catch (Exception e) {
    }
}
public void loadSubject(){
    try {
        ResultSet rs=Mysql.search("SELECT * FROM `subject`");
        
        Vector v1=new Vector();
        v1.add("select");
      
      while(rs.next()){
          v1.add(rs.getString("subject_name"));
          subjectMap.put(rs.getString("subject_name"),rs.getInt("Subno"));
          
      }
      DefaultComboBoxModel m1=new DefaultComboBoxModel(v1);
          
          jComboBox3.setModel(m1);
          jComboBox5.setModel(m1);
          jComboBox24.setModel(m1);
       jComboBox22.setModel(m1);
    } catch (Exception e) {
    }
}
public void loadTeacherCombo(){
    try {
        ResultSet rs=Mysql.search("SELECT * FROM `teacher`");
        Vector v1=new Vector();
        v1.add("select");
      
      while(rs.next()){
          v1.add(rs.getString("teacher_name"));
          teacherMap.put(rs.getString("teacher_name"),rs.getInt("Tno"));
          
      }
      DefaultComboBoxModel m1=new DefaultComboBoxModel(v1);
          jComboBox6.setModel(m1);
          jComboBox23.setModel(m1);
          jComboBox25.setModel(m1);
          
    } catch (Exception e) {
    }
}

public void studentreset(){
    
    jTextField26.setText("");
    jTextField29.setText("");
    jLabel3.setIcon(null);
    jDateChooser1.setDate(null);
    
}

public void teacherreset(){
    
    jTextField30.setText("");
    jTextField31.setText("");
    
    jDateChooser2.setDate(null);
    
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel303 = new javax.swing.JLabel();
        jLabel302 = new javax.swing.JLabel();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jLabel288 = new javax.swing.JLabel();
        jLabel301 = new javax.swing.JLabel();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton88 = new javax.swing.JButton();
        jButton90 = new javax.swing.JButton();
        jButton91 = new javax.swing.JButton();
        jButton92 = new javax.swing.JButton();
        jButton93 = new javax.swing.JButton();
        jButton94 = new javax.swing.JButton();
        jButton95 = new javax.swing.JButton();
        jButton96 = new javax.swing.JButton();
        jButton97 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jPanel37 = new javax.swing.JPanel();
        jButton54 = new javax.swing.JButton();
        jButton64 = new javax.swing.JButton();
        jButton65 = new javax.swing.JButton();
        jButton70 = new javax.swing.JButton();
        jButton72 = new javax.swing.JButton();
        jButton73 = new javax.swing.JButton();
        jButton74 = new javax.swing.JButton();
        jButton75 = new javax.swing.JButton();
        jPanel60 = new javax.swing.JPanel();
        jButton105 = new javax.swing.JButton();
        jButton106 = new javax.swing.JButton();
        jButton107 = new javax.swing.JButton();
        jButton108 = new javax.swing.JButton();
        jButton109 = new javax.swing.JButton();
        jButton110 = new javax.swing.JButton();
        jButton112 = new javax.swing.JButton();
        jButton113 = new javax.swing.JButton();
        jPanel65 = new javax.swing.JPanel();
        jButton111 = new javax.swing.JButton();
        jButton125 = new javax.swing.JButton();
        jButton126 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jTextField29 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton13 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTextField33 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel41 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton17 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jComboBox7 = new javax.swing.JComboBox<>();
        jLabel53 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel48 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton19 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jTextField7 = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jButton50 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jButton20 = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jButton26 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jLabel60 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jButton24 = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton25 = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jTextField39 = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jButton30 = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        jTextField27 = new javax.swing.JTextField();
        jTextField38 = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jButton23 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jDateChooser6 = new com.toedter.calendar.JDateChooser();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jTextField40 = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        jTextField41 = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        jDateChooser7 = new com.toedter.calendar.JDateChooser();
        jLabel96 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox<>();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jTextField11 = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel104 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jTextField42 = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jButton16 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        jButton51 = new javax.swing.JButton();
        jButton52 = new javax.swing.JButton();
        jDateChooser8 = new com.toedter.calendar.JDateChooser();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel112 = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel115 = new javax.swing.JLabel();
        jTextField43 = new javax.swing.JTextField();
        jLabel116 = new javax.swing.JLabel();
        jTextField44 = new javax.swing.JTextField();
        jButton53 = new javax.swing.JButton();
        jComboBox12 = new javax.swing.JComboBox<>();
        jLabel119 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel120 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jTextField52 = new javax.swing.JTextField();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTable15 = new javax.swing.JTable();
        jPanel33 = new javax.swing.JPanel();
        jLabel130 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        jTextField46 = new javax.swing.JTextField();
        jLabel132 = new javax.swing.JLabel();
        jTextField47 = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable();
        jButton58 = new javax.swing.JButton();
        jButton59 = new javax.swing.JButton();
        jButton60 = new javax.swing.JButton();
        jLabel133 = new javax.swing.JLabel();
        jTextField48 = new javax.swing.JTextField();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel139 = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel141 = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        jTextField49 = new javax.swing.JTextField();
        jLabel143 = new javax.swing.JLabel();
        jTextField50 = new javax.swing.JTextField();
        jLabel144 = new javax.swing.JLabel();
        jDateChooser10 = new com.toedter.calendar.JDateChooser();
        jLabel145 = new javax.swing.JLabel();
        jComboBox13 = new javax.swing.JComboBox<>();
        jButton61 = new javax.swing.JButton();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable14 = new javax.swing.JTable();
        jButton62 = new javax.swing.JButton();
        jLabel148 = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        jLabel153 = new javax.swing.JLabel();
        jComboBox14 = new javax.swing.JComboBox<>();
        jLabel154 = new javax.swing.JLabel();
        jLabel155 = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jButton63 = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jLabel114 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel157 = new javax.swing.JLabel();
        jTextField53 = new javax.swing.JTextField();
        jLabel158 = new javax.swing.JLabel();
        jTextField54 = new javax.swing.JTextField();
        jButton67 = new javax.swing.JButton();
        jComboBox15 = new javax.swing.JComboBox<>();
        jLabel159 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel160 = new javax.swing.JLabel();
        jLabel161 = new javax.swing.JLabel();
        jTextField55 = new javax.swing.JTextField();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTable16 = new javax.swing.JTable();
        jPanel40 = new javax.swing.JPanel();
        jLabel162 = new javax.swing.JLabel();
        jLabel163 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel164 = new javax.swing.JLabel();
        jTextField56 = new javax.swing.JTextField();
        jLabel165 = new javax.swing.JLabel();
        jTextField57 = new javax.swing.JTextField();
        jButton68 = new javax.swing.JButton();
        jComboBox16 = new javax.swing.JComboBox<>();
        jLabel166 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jLabel167 = new javax.swing.JLabel();
        jLabel168 = new javax.swing.JLabel();
        jTextField58 = new javax.swing.JTextField();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTable17 = new javax.swing.JTable();
        jPanel42 = new javax.swing.JPanel();
        jLabel169 = new javax.swing.JLabel();
        jLabel170 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel171 = new javax.swing.JLabel();
        jTextField59 = new javax.swing.JTextField();
        jLabel172 = new javax.swing.JLabel();
        jTextField60 = new javax.swing.JTextField();
        jButton69 = new javax.swing.JButton();
        jComboBox17 = new javax.swing.JComboBox<>();
        jLabel173 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jLabel174 = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        jTextField61 = new javax.swing.JTextField();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTable18 = new javax.swing.JTable();
        jPanel44 = new javax.swing.JPanel();
        jLabel176 = new javax.swing.JLabel();
        jLabel177 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel178 = new javax.swing.JLabel();
        jTextField62 = new javax.swing.JTextField();
        jLabel179 = new javax.swing.JLabel();
        jTextField63 = new javax.swing.JTextField();
        jButton71 = new javax.swing.JButton();
        jComboBox18 = new javax.swing.JComboBox<>();
        jLabel180 = new javax.swing.JLabel();
        jPanel45 = new javax.swing.JPanel();
        jLabel181 = new javax.swing.JLabel();
        jLabel182 = new javax.swing.JLabel();
        jTextField64 = new javax.swing.JTextField();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTable19 = new javax.swing.JTable();
        jPanel46 = new javax.swing.JPanel();
        jLabel183 = new javax.swing.JLabel();
        jLabel184 = new javax.swing.JLabel();
        jLabel185 = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        jLabel187 = new javax.swing.JLabel();
        jTextField65 = new javax.swing.JTextField();
        jTextField66 = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jButton76 = new javax.swing.JButton();
        jLabel190 = new javax.swing.JLabel();
        jLabel192 = new javax.swing.JLabel();
        jTextField67 = new javax.swing.JTextField();
        jLabel193 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jLabel195 = new javax.swing.JLabel();
        jLabel196 = new javax.swing.JLabel();
        jTextField68 = new javax.swing.JTextField();
        jLabel198 = new javax.swing.JLabel();
        jTextField69 = new javax.swing.JTextField();
        jButton79 = new javax.swing.JButton();
        jButton80 = new javax.swing.JButton();
        jPanel49 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTable20 = new javax.swing.JTable();
        jButton87 = new javax.swing.JButton();
        jTextField71 = new javax.swing.JTextField();
        jLabel202 = new javax.swing.JLabel();
        jLabel203 = new javax.swing.JLabel();
        jLabel204 = new javax.swing.JLabel();
        jButton81 = new javax.swing.JButton();
        jPanel50 = new javax.swing.JPanel();
        jLabel197 = new javax.swing.JLabel();
        jLabel199 = new javax.swing.JLabel();
        jTextField70 = new javax.swing.JTextField();
        jLabel205 = new javax.swing.JLabel();
        jTextField72 = new javax.swing.JTextField();
        jLabel206 = new javax.swing.JLabel();
        jTextField73 = new javax.swing.JTextField();
        jLabel208 = new javax.swing.JLabel();
        jButton82 = new javax.swing.JButton();
        jLabel209 = new javax.swing.JLabel();
        jLabel210 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTable21 = new javax.swing.JTable();
        jButton83 = new javax.swing.JButton();
        jLabel216 = new javax.swing.JLabel();
        jComboBox20 = new javax.swing.JComboBox<>();
        jLabel217 = new javax.swing.JLabel();
        jLabel218 = new javax.swing.JLabel();
        jLabel219 = new javax.swing.JLabel();
        jPanel51 = new javax.swing.JPanel();
        jButton84 = new javax.swing.JButton();
        jLabel316 = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jLabel207 = new javax.swing.JLabel();
        jLabel220 = new javax.swing.JLabel();
        jTextField75 = new javax.swing.JTextField();
        jPanel53 = new javax.swing.JPanel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTable22 = new javax.swing.JTable();
        jButton85 = new javax.swing.JButton();
        jLabel222 = new javax.swing.JLabel();
        jTextField77 = new javax.swing.JTextField();
        jLabel225 = new javax.swing.JLabel();
        jTextField78 = new javax.swing.JTextField();
        jLabel226 = new javax.swing.JLabel();
        jPanel54 = new javax.swing.JPanel();
        jLabel228 = new javax.swing.JLabel();
        jLabel229 = new javax.swing.JLabel();
        jLabel230 = new javax.swing.JLabel();
        jLabel231 = new javax.swing.JLabel();
        jPanel55 = new javax.swing.JPanel();
        jLabel232 = new javax.swing.JLabel();
        jTextField79 = new javax.swing.JTextField();
        jTextField80 = new javax.swing.JTextField();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTable23 = new javax.swing.JTable();
        jButton89 = new javax.swing.JButton();
        jButton98 = new javax.swing.JButton();
        jDateChooser11 = new com.toedter.calendar.JDateChooser();
        jLabel233 = new javax.swing.JLabel();
        jLabel234 = new javax.swing.JLabel();
        jLabel235 = new javax.swing.JLabel();
        jLabel236 = new javax.swing.JLabel();
        jLabel237 = new javax.swing.JLabel();
        jTextField81 = new javax.swing.JTextField();
        jLabel238 = new javax.swing.JLabel();
        jLabel239 = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        jLabel240 = new javax.swing.JLabel();
        jLabel241 = new javax.swing.JLabel();
        jLabel242 = new javax.swing.JLabel();
        jLabel243 = new javax.swing.JLabel();
        jPanel57 = new javax.swing.JPanel();
        jLabel244 = new javax.swing.JLabel();
        jTextField82 = new javax.swing.JTextField();
        jTextField83 = new javax.swing.JTextField();
        jScrollPane24 = new javax.swing.JScrollPane();
        jTable24 = new javax.swing.JTable();
        jButton101 = new javax.swing.JButton();
        jButton103 = new javax.swing.JButton();
        jDateChooser12 = new com.toedter.calendar.JDateChooser();
        jLabel245 = new javax.swing.JLabel();
        jLabel246 = new javax.swing.JLabel();
        jLabel247 = new javax.swing.JLabel();
        jLabel248 = new javax.swing.JLabel();
        jLabel249 = new javax.swing.JLabel();
        jTextField84 = new javax.swing.JTextField();
        jLabel250 = new javax.swing.JLabel();
        jLabel251 = new javax.swing.JLabel();
        jPanel58 = new javax.swing.JPanel();
        jLabel252 = new javax.swing.JLabel();
        jLabel253 = new javax.swing.JLabel();
        jTextField85 = new javax.swing.JTextField();
        jLabel254 = new javax.swing.JLabel();
        jTextField86 = new javax.swing.JTextField();
        jLabel255 = new javax.swing.JLabel();
        jTextField87 = new javax.swing.JTextField();
        jButton104 = new javax.swing.JButton();
        jComboBox21 = new javax.swing.JComboBox<>();
        jLabel256 = new javax.swing.JLabel();
        jPanel59 = new javax.swing.JPanel();
        jLabel257 = new javax.swing.JLabel();
        jLabel258 = new javax.swing.JLabel();
        jTextField88 = new javax.swing.JTextField();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTable25 = new javax.swing.JTable();
        jPanel61 = new javax.swing.JPanel();
        jLabel259 = new javax.swing.JLabel();
        jTextField89 = new javax.swing.JTextField();
        jLabel260 = new javax.swing.JLabel();
        jLabel261 = new javax.swing.JLabel();
        jTextField90 = new javax.swing.JTextField();
        jLabel262 = new javax.swing.JLabel();
        jDateChooser13 = new com.toedter.calendar.JDateChooser();
        jPanel62 = new javax.swing.JPanel();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTable26 = new javax.swing.JTable();
        jButton114 = new javax.swing.JButton();
        jButton115 = new javax.swing.JButton();
        jButton116 = new javax.swing.JButton();
        jTextField91 = new javax.swing.JTextField();
        jLabel263 = new javax.swing.JLabel();
        jLabel264 = new javax.swing.JLabel();
        jLabel265 = new javax.swing.JLabel();
        jLabel266 = new javax.swing.JLabel();
        jLabel267 = new javax.swing.JLabel();
        jLabel268 = new javax.swing.JLabel();
        jPanel63 = new javax.swing.JPanel();
        jLabel269 = new javax.swing.JLabel();
        jTextField92 = new javax.swing.JTextField();
        jLabel270 = new javax.swing.JLabel();
        jLabel271 = new javax.swing.JLabel();
        jTextField93 = new javax.swing.JTextField();
        jLabel272 = new javax.swing.JLabel();
        jDateChooser14 = new com.toedter.calendar.JDateChooser();
        jPanel64 = new javax.swing.JPanel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTable27 = new javax.swing.JTable();
        jButton117 = new javax.swing.JButton();
        jButton118 = new javax.swing.JButton();
        jButton119 = new javax.swing.JButton();
        jTextField94 = new javax.swing.JTextField();
        jLabel273 = new javax.swing.JLabel();
        jLabel274 = new javax.swing.JLabel();
        jLabel275 = new javax.swing.JLabel();
        jLabel276 = new javax.swing.JLabel();
        jLabel277 = new javax.swing.JLabel();
        jLabel278 = new javax.swing.JLabel();
        jPanel66 = new javax.swing.JPanel();
        jLabel279 = new javax.swing.JLabel();
        jLabel280 = new javax.swing.JLabel();
        jTextField95 = new javax.swing.JTextField();
        jLabel281 = new javax.swing.JLabel();
        jComboBox22 = new javax.swing.JComboBox<>();
        jLabel282 = new javax.swing.JLabel();
        jComboBox23 = new javax.swing.JComboBox<>();
        jScrollPane28 = new javax.swing.JScrollPane();
        jTable28 = new javax.swing.JTable();
        jButton121 = new javax.swing.JButton();
        jPanel67 = new javax.swing.JPanel();
        jLabel283 = new javax.swing.JLabel();
        jPanel68 = new javax.swing.JPanel();
        jLabel284 = new javax.swing.JLabel();
        jLabel285 = new javax.swing.JLabel();
        jTextField96 = new javax.swing.JTextField();
        jLabel286 = new javax.swing.JLabel();
        jComboBox24 = new javax.swing.JComboBox<>();
        jLabel287 = new javax.swing.JLabel();
        jComboBox25 = new javax.swing.JComboBox<>();
        jScrollPane29 = new javax.swing.JScrollPane();
        jTable29 = new javax.swing.JTable();
        jButton127 = new javax.swing.JButton();
        jPanel69 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jLabel289 = new javax.swing.JLabel();
        jLabel290 = new javax.swing.JLabel();
        jLabel291 = new javax.swing.JLabel();
        jLabel292 = new javax.swing.JLabel();
        jPanel71 = new javax.swing.JPanel();
        jLabel293 = new javax.swing.JLabel();
        jTextField97 = new javax.swing.JTextField();
        jTextField98 = new javax.swing.JTextField();
        jScrollPane30 = new javax.swing.JScrollPane();
        jTable30 = new javax.swing.JTable();
        jButton99 = new javax.swing.JButton();
        jButton100 = new javax.swing.JButton();
        jButton102 = new javax.swing.JButton();
        jButton128 = new javax.swing.JButton();
        jDateChooser15 = new com.toedter.calendar.JDateChooser();
        jLabel294 = new javax.swing.JLabel();
        jLabel295 = new javax.swing.JLabel();
        jLabel296 = new javax.swing.JLabel();
        jLabel297 = new javax.swing.JLabel();
        jLabel298 = new javax.swing.JLabel();
        jTextField99 = new javax.swing.JTextField();
        jLabel299 = new javax.swing.JLabel();
        jLabel300 = new javax.swing.JLabel();
        jPanel72 = new javax.swing.JPanel();
        jLabel304 = new javax.swing.JLabel();
        jLabel305 = new javax.swing.JLabel();
        jTextField100 = new javax.swing.JTextField();
        jPanel73 = new javax.swing.JPanel();
        jScrollPane31 = new javax.swing.JScrollPane();
        jTable31 = new javax.swing.JTable();
        jButton120 = new javax.swing.JButton();
        jLabel306 = new javax.swing.JLabel();
        jTextField101 = new javax.swing.JTextField();
        jLabel307 = new javax.swing.JLabel();
        jTextField102 = new javax.swing.JTextField();
        jLabel308 = new javax.swing.JLabel();
        jPanel74 = new javax.swing.JPanel();
        jLabel309 = new javax.swing.JLabel();
        jLabel310 = new javax.swing.JLabel();
        jTextField103 = new javax.swing.JTextField();
        jPanel75 = new javax.swing.JPanel();
        jScrollPane32 = new javax.swing.JScrollPane();
        jTable32 = new javax.swing.JTable();
        jButton122 = new javax.swing.JButton();
        jLabel311 = new javax.swing.JLabel();
        jTextField104 = new javax.swing.JTextField();
        jLabel312 = new javax.swing.JLabel();
        jTextField105 = new javax.swing.JTextField();
        jLabel313 = new javax.swing.JLabel();
        jPanel76 = new javax.swing.JPanel();
        jLabel221 = new javax.swing.JLabel();
        jLabel223 = new javax.swing.JLabel();
        jTextField76 = new javax.swing.JTextField();
        jLabel227 = new javax.swing.JLabel();
        jTextField106 = new javax.swing.JTextField();
        jLabel314 = new javax.swing.JLabel();
        jTextField107 = new javax.swing.JTextField();
        jLabel315 = new javax.swing.JLabel();
        jDateChooser16 = new com.toedter.calendar.JDateChooser();
        jButton21 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jPanel77 = new javax.swing.JPanel();
        jScrollPane33 = new javax.swing.JScrollPane();
        jTable33 = new javax.swing.JTable();
        jTextField108 = new javax.swing.JTextField();
        jLabel317 = new javax.swing.JLabel();
        jLabel318 = new javax.swing.JLabel();
        jLabel319 = new javax.swing.JLabel();
        jButton86 = new javax.swing.JButton();
        jPanel78 = new javax.swing.JPanel();
        jLabel224 = new javax.swing.JLabel();
        jLabel320 = new javax.swing.JLabel();
        jTextField109 = new javax.swing.JTextField();
        jLabel322 = new javax.swing.JLabel();
        jTextField110 = new javax.swing.JTextField();
        jLabel323 = new javax.swing.JLabel();
        jTextField111 = new javax.swing.JTextField();
        jLabel324 = new javax.swing.JLabel();
        jDateChooser17 = new com.toedter.calendar.JDateChooser();
        jLabel325 = new javax.swing.JLabel();
        jComboBox27 = new javax.swing.JComboBox<>();
        jButton34 = new javax.swing.JButton();
        jLabel326 = new javax.swing.JLabel();
        jLabel327 = new javax.swing.JLabel();
        jScrollPane34 = new javax.swing.JScrollPane();
        jTable34 = new javax.swing.JTable();
        jButton37 = new javax.swing.JButton();
        jLabel328 = new javax.swing.JLabel();
        jLabel329 = new javax.swing.JLabel();
        jLabel333 = new javax.swing.JLabel();
        jComboBox28 = new javax.swing.JComboBox<>();
        jLabel334 = new javax.swing.JLabel();
        jLabel335 = new javax.swing.JLabel();
        jLabel336 = new javax.swing.JLabel();
        jPanel79 = new javax.swing.JPanel();
        jButton38 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("student management system");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel303.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/2620515_employee_job_laptop_seeker_unemployee_icon.png"))); // NOI18N
        jPanel2.add(jLabel303, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 530, -1, 50));

        jLabel302.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/6570709_class_education_learning_online_student_icon.png"))); // NOI18N
        jPanel2.add(jLabel302, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, -1, 50));

        jButton31.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jButton31.setText("Class management");
        jButton31.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 170, 80));

        jButton32.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jButton32.setText("Employee Management");
        jButton32.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 170, 80));

        jLabel288.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/8541740_chalkboard_teacher_icon.png"))); // NOI18N
        jPanel2.add(jLabel288, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 240, -1, 40));

        jLabel301.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/user-interface (1).png"))); // NOI18N
        jPanel2.add(jLabel301, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, -1, 40));

        jButton35.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jButton35.setText("Student Management");
        jButton35.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 170, 80));

        jButton36.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jButton36.setText("Teacher management");
        jButton36.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton36, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 170, 80));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 190, 830));

        jPanel3.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 80)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ADYAPANA INSTITUTE");

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/rsz_6565bd61-0fe6-459e-9ae4-69ead84c1fc4-removebg-preview.png"))); // NOI18N
        jLabel22.setText(" ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(334, 334, 334)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 938, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(460, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, 0, 1880, 140));

        jPanel5.setBackground(new java.awt.Color(0, 204, 204));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 135, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 934, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel5);

        jPanel6.setBackground(new java.awt.Color(0, 204, 204));

        jButton88.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton88.setText("Registration");
        jButton88.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton88ActionPerformed(evt);
            }
        });

        jButton90.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton90.setText("History");
        jButton90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton90ActionPerformed(evt);
            }
        });

        jButton91.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton91.setText("Payment");
        jButton91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton91ActionPerformed(evt);
            }
        });

        jButton92.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton92.setText("Attendence");
        jButton92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton92ActionPerformed(evt);
            }
        });

        jButton93.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton93.setText("Issue card");
        jButton93.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton93ActionPerformed(evt);
            }
        });

        jButton94.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton94.setText("profile");
        jButton94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton94ActionPerformed(evt);
            }
        });

        jButton95.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton95.setText("Enrollment");
        jButton95.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton95ActionPerformed(evt);
            }
        });

        jButton96.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton96.setText("Remove");
        jButton96.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton96ActionPerformed(evt);
            }
        });

        jButton97.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton97.setText("Update");
        jButton97.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton97ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton96, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton88, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(jButton97, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton94, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton93, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton90, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton95, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton91, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton92, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(jButton88, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton97, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton96, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton95, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton94, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton93, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton92, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton91, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton90, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(222, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel6);

        jPanel4.setBackground(new java.awt.Color(0, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel4.setForeground(new java.awt.Color(204, 204, 204));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton4.setText("Student");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton5.setText("Teacher");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton6.setText("Subject");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(242, 242, 242)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(375, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel4);

        jPanel24.setBackground(new java.awt.Color(0, 204, 204));

        jButton44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton44.setText("Attendence");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton45.setText("Payments(student)");
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });

        jButton46.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton46.setText("Payments(Teacher)");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(489, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab4", jPanel24);

        jPanel37.setBackground(new java.awt.Color(0, 204, 204));

        jButton54.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton54.setText("Register");
        jButton54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton54ActionPerformed(evt);
            }
        });

        jButton64.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton64.setText("Update");
        jButton64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton64ActionPerformed(evt);
            }
        });

        jButton65.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton65.setText("Remove");
        jButton65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton65ActionPerformed(evt);
            }
        });

        jButton70.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton70.setText("profile");
        jButton70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton70ActionPerformed(evt);
            }
        });

        jButton72.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton72.setText("Issue card");
        jButton72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton72ActionPerformed(evt);
            }
        });

        jButton73.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton73.setText("Attendence");
        jButton73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton73ActionPerformed(evt);
            }
        });

        jButton74.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton74.setText("Payment");
        jButton74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton74ActionPerformed(evt);
            }
        });

        jButton75.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton75.setText("History");
        jButton75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton75ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton72, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                    .addComponent(jButton75, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton73)
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(jButton54, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton64, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton65, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton70, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton72, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jButton75, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(242, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab5", jPanel37);

        jPanel60.setBackground(new java.awt.Color(0, 204, 204));

        jButton105.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton105.setText("Registration");
        jButton105.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton105ActionPerformed(evt);
            }
        });

        jButton106.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton106.setText("History");
        jButton106.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton106ActionPerformed(evt);
            }
        });

        jButton107.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton107.setText("Payment");
        jButton107.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton107ActionPerformed(evt);
            }
        });

        jButton108.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton108.setText("Attendence");
        jButton108.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton108ActionPerformed(evt);
            }
        });

        jButton109.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton109.setText("Issue card");
        jButton109.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton109ActionPerformed(evt);
            }
        });

        jButton110.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton110.setText("profile");
        jButton110.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton110ActionPerformed(evt);
            }
        });

        jButton112.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton112.setText("Remove");
        jButton112.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton112ActionPerformed(evt);
            }
        });

        jButton113.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton113.setText("Update");
        jButton113.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton113ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel60Layout = new javax.swing.GroupLayout(jPanel60);
        jPanel60.setLayout(jPanel60Layout);
        jPanel60Layout.setHorizontalGroup(
            jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel60Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton110, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton112, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton105, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(jButton113, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton106, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton108, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton109, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton107, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel60Layout.setVerticalGroup(
            jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel60Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(jButton105, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton113, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton112, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton110, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton109, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButton108, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton107, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton106, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(249, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel60);

        jPanel65.setBackground(new java.awt.Color(0, 204, 204));

        jButton111.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton111.setText("Registration");
        jButton111.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton111ActionPerformed(evt);
            }
        });

        jButton125.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton125.setText("Remove");
        jButton125.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton125ActionPerformed(evt);
            }
        });

        jButton126.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton126.setText("Update");
        jButton126.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton126ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel65Layout = new javax.swing.GroupLayout(jPanel65);
        jPanel65.setLayout(jPanel65Layout);
        jPanel65Layout.setHorizontalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel65Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton125, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton111, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    .addComponent(jButton126, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel65Layout.setVerticalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel65Layout.createSequentialGroup()
                .addGap(206, 206, 206)
                .addComponent(jButton111, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton126, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton125, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(549, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel65);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, -30, 140, 1000));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("WELCOME TO MANAGEMENT SYSTEM");
        jPanel7.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 390, 973, 136));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/wp4385852.jpg"))); // NOI18N
        jPanel7.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1600, 900));

        jTabbedPane2.addTab("tab3", jPanel7);

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel4.setText("Student Registration");
        jPanel8.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 48, 410, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("User Name :");
        jPanel8.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, 20));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText(" Address   :");
        jPanel8.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 150, 20));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("Date of Birth :");
        jPanel8.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, 20));

        jPanel11.setBackground(new java.awt.Color(0, 0, 0));
        jPanel11.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText("Academic Details");
        jPanel8.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jTextField26.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel8.add(jTextField26, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 175, 50));

        jTextField29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel8.add(jTextField29, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 175, 50));

        jTable2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student id ", "Student Name  ", "BirthDay", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel8.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 140, 830, 522));

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton7.setText("Save");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 690, 150, 50));

        jButton9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton9.setText("Browse");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 310, 160, 40));
        jPanel8.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 175, 50));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jLabel3.setPreferredSize(new java.awt.Dimension(130, 170));
        jLabel3.setRequestFocusEnabled(false);
        jPanel8.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, 160, 180));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel8.setText("Before saving student make sure you have filled the user name,Address,Date of Birth ");
        jPanel8.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 530, 50));
        jPanel8.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, -1, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("And choose profile picture.");
        jPanel8.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel14.setText("Search here by using student id");
        jPanel8.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 570, 40));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel8.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 50, 810, 40));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel8.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(1480, 60, 40, 40));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel15.setText("Click on a row in table to get data into text fields and Update  or Delete Students using it.");
        jPanel8.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 570, 50));

        jTabbedPane2.addTab("tab2", jPanel8);

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel27.setText("Subject Registration");
        jPanel9.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 58, -1, -1));

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel29.setText("Subject Name :");
        jPanel9.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, 20));

        jTextField21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField21ActionPerformed(evt);
            }
        });
        jPanel9.add(jTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 150, 50));

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel30.setText("Price             :");
        jPanel9.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, -1, -1));

        jTextField22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel9.add(jTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 150, 50));

        jPanel12.setBackground(new java.awt.Color(0, 0, 0));
        jPanel12.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(442, 10, 10, 799));

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject id", "Subject Name", "price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAlignmentX(5.0F);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel9.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 110, 940, 460));

        jButton13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton13.setText("Save");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 700, 170, 41));

        jButton27.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton27.setText("Update");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton27, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 700, 170, 41));

        jButton28.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton28.setText("Delete");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton28, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 690, 170, 41));

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel31.setText("Subject ID:");
        jPanel9.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 20));

        jTextField23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });
        jPanel9.add(jTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 150, 50));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel16.setText("Before saving Subject fill the Subject name And Price");
        jPanel9.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 550, 50));

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        jPanel9.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, 930, 50));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel18.setText("Click on a row in table to get data into text fields and Update  or ");
        jPanel9.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 570, 50));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel19.setText("Search here by using student id");
        jPanel9.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 80, 570, 40));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel9.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(1440, 20, 40, 40));

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel25.setText("Delete Subjects using it.");
        jPanel9.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 190, 30));

        jTabbedPane2.addTab("tab3", jPanel9);

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel12.setText("Teacher Registration");

        jTextField30.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel23.setText("User Name :");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel24.setText("Address    :");

        jTextField31.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel26.setText("Date of Birth :");

        jPanel13.setBackground(new java.awt.Color(0, 0, 0));
        jPanel13.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jTable3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Teacher id ", "Teacher Name  ", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jButton8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton8.setText("Save");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel32.setText("Search here by using student id");

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel33.setText(",Date of Birth And choose profile picture.");

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel63.setText("Before saving a Teacher make sure you have filled the User name,Address ");

        jLabel72.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel72.setText("Click on a row in table to get data into text fields and Update  or ");

        jLabel73.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel73.setText("Delete Teachers using it.");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(34, 34, 34)
                        .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addGap(130, 130, 130)
                            .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField3)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE))
                .addGap(240, 240, 240)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(386, 386, 386))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(0, 23, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addGap(66, 66, 66)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(jLabel26)
                            .addGap(37, 37, 37)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(170, 170, 170)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(106, 106, 106)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 806, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 107, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel23))
                            .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel24)))
                .addGap(89, 89, 89)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel33))
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 201, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(0, 20, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addComponent(jLabel12)
                            .addGap(221, 221, 221)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addComponent(jLabel26))
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 20, Short.MAX_VALUE))
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(137, 137, 137)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("tab4", jPanel10);

        jLabel35.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel35.setText("Student subject Enrollment");

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel36.setText("Student Id  :");

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jLabel37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel38.setText("User Name :");

        jTextField32.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel39.setText("Address");

        jTextField33.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel40.setText("Date of Birth :");

        jLabel41.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel41.setText("Subject");

        jComboBox3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jButton17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton17.setText("Enroll ");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jTable4.setAutoCreateRowSorter(true);
        jTable4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "student_name", "subject_name"
            }
        ));
        jTable4.setSurrendersFocusOnKeystroke(true);
        jScrollPane4.setViewportView(jTable4);

        jComboBox7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel53.setText("Class");

        jButton18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton18.setText("Add Subject");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jPanel18.setBackground(new java.awt.Color(0, 0, 0));
        jPanel18.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jLabel75.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel75.setText("Search here by using student id");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(64, 64, 64)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addGap(53, 53, 53)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel14Layout.createSequentialGroup()
                                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                                        .addComponent(jLabel38)
                                                        .addGap(53, 53, 53)))
                                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel14Layout.createSequentialGroup()
                                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(14, 14, 14)))
                                        .addGap(50, 50, 50))
                                    .addComponent(jLabel75, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(114, 114, 114)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 825, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                    .addContainerGap(642, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(907, Short.MAX_VALUE)))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel35)
                        .addGap(61, 61, 61)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel14Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(jLabel36))
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel38))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel39)))
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel41)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel53))
                        .addGap(18, 18, 18)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(262, Short.MAX_VALUE))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("tab5", jPanel14);

        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel42.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel42.setText("Student Attendence");
        jPanel15.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 61, 244, -1));

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel43.setText("Student Id  :");
        jPanel15.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 159, -1, -1));

        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });
        jPanel15.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 139, 175, 50));

        jLabel44.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel15.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(436, 98, 160, 180));

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel45.setText("User Name :");
        jPanel15.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, -1, -1));

        jTextField34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel15.add(jTextField34, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 175, 50));

        jLabel46.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel46.setText("Address");
        jPanel15.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 150, -1));

        jTextField35.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel15.add(jTextField35, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 380, 175, 50));

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel47.setText("Date of Birth :");
        jPanel15.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 460, -1, -1));
        jPanel15.add(jDateChooser4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 440, 175, 50));

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel48.setText("Class");
        jPanel15.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, 76, -1));

        jComboBox4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });
        jPanel15.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 240, 175, 50));

        jButton19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton19.setText("Submit");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 580, 161, 55));

        jButton22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton22.setText("Print");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton22, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 710, 161, 55));

        jPanel19.setBackground(new java.awt.Color(0, 0, 0));
        jPanel19.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jTable7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "student", "Class", "Date", "Attendence"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable7);

        jPanel15.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 160, 840, 490));

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });
        jPanel15.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 40, 500, 40));

        jLabel74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel15.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(1490, 40, 40, 40));

        jLabel76.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel76.setText("Search here by using student id");
        jPanel15.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, 570, 40));

        jButton50.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton50.setText("Absent");
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton50, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 580, 161, 55));

        jButton42.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton42.setText("search");
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });
        jPanel15.add(jButton42, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 110, 40));

        jTabbedPane2.addTab("tab6", jPanel15);

        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel49.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel49.setText("Class registration");
        jPanel16.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 68, 244, -1));

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel50.setText("Time slot");
        jPanel16.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 291, -1, -1));

        jTextField10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel16.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 271, 175, 50));

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel51.setText("Subject");
        jPanel16.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 222, 76, -1));

        jComboBox5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });
        jPanel16.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 208, 175, 50));

        jButton20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton20.setText("Submit");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton20, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 680, 161, 55));

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel52.setText("Teacher");
        jPanel16.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 159, 76, -1));

        jComboBox6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });
        jPanel16.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 145, 175, 50));

        jTable6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "class_id", "teacher", "subject", "timeslot"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable6MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable6);

        jPanel16.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 120, 950, 520));

        jButton26.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton26.setText("Update");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton26, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 680, 161, 55));

        jButton29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton29.setText("Delete");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton29, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 680, 161, 55));

        jPanel20.setBackground(new java.awt.Color(0, 0, 0));
        jPanel20.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jPanel16.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(429, 13, 10, 850));

        jLabel78.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel78.setText("Select a Teacher ,Subject And  Time slot");
        jPanel16.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 570, 40));

        jTabbedPane2.addTab("tab7", jPanel16);

        jLabel54.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel54.setText("Student Payment");

        jLabel55.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel55.setText("Student Id  :");

        jTextField6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField6.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField6InputMethodTextChanged(evt);
            }
        });
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextField6PropertyChange(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        jLabel56.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel57.setText("User Name :");

        jTextField36.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel58.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel58.setText("Address");

        jTextField37.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel59.setText("Date of Birth :");

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel60.setText("Class");

        jComboBox8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox8ItemStateChanged(evt);
            }
        });
        jComboBox8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox8MouseClicked(evt);
            }
        });
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        jButton24.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton24.setText("Submit");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel61.setText("Price");

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel62.setText(" ");
        jLabel62.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "class_id", "subject", "Teacher", "month", "price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable5);

        jButton25.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton25.setText("Add item");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel64.setText("Amount");

        jLabel65.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel65.setText(" ");
        jLabel65.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel66.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel66.setText("Cash Payment");

        jTextField39.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField39.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField39KeyReleased(evt);
            }
        });

        jLabel67.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel67.setText("Cash Balance");

        jLabel68.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel68.setText(" ");
        jLabel68.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel69.setText("Month");

        jComboBox9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox9MouseClicked(evt);
            }
        });
        jComboBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox9ActionPerformed(evt);
            }
        });

        jLabel70.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel70.setText(" ");
        jLabel70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel71.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel71.setText("Invoive_id");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton30.setText("Search");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(261, 261, 261)
                                .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58)
                                .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addComponent(jLabel55)
                                        .addGap(53, 53, 53)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(64, 64, 64)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jComboBox8, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addComponent(jLabel59)
                                        .addGap(42, 42, 42)
                                        .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(64, 64, 64)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addComponent(jLabel57)
                                        .addGap(42, 42, 42)
                                        .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                .addComponent(jButton30)
                                .addGap(33, 33, 33)))
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(150, 150, 150)
                                .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel17Layout.createSequentialGroup()
                                                .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(31, 31, 31)
                                                .addComponent(jLabel67)))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                .addGap(557, 557, 557))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel71)
                                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel64)))))
                        .addGap(22, 22, 22)
                        .addComponent(jLabel66))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel54)
                        .addGap(72, 72, 72)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel55))
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jButton30)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel57)
                                .addGap(12, 12, 12))
                            .addComponent(jTextField36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel58))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel59))
                            .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel60))
                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jLabel61))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel69))
                            .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel67)
                                .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(204, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab8", jPanel17);

        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel17.setText("Student Card");
        jPanel22.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 48, 410, -1));

        jLabel79.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel79.setText("User Name :");
        jPanel22.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, 20));

        jLabel80.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel80.setText(" Address   :");
        jPanel22.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 150, 20));

        jLabel81.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel81.setText("Date of Birth :");
        jPanel22.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, 20));

        jPanel23.setBackground(new java.awt.Color(0, 0, 0));
        jPanel23.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel22.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jLabel82.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel82.setText("Student Details");
        jPanel22.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jTextField27.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel22.add(jTextField27, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 175, 50));

        jTextField38.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel22.add(jTextField38, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 175, 50));

        jTable8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student id ", "Student Name  ", "BirthDay", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable8MouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(jTable8);

        jPanel22.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 140, 820, 522));

        jButton23.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton23.setText("Save");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel22.add(jButton23, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 720, 150, 50));

        jButton39.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton39.setText("Create Card");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });
        jPanel22.add(jButton39, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 160, 40));

        jButton40.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton40.setText("Update");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });
        jPanel22.add(jButton40, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 720, 150, 50));

        jButton41.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton41.setText("Delete");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });
        jPanel22.add(jButton41, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 720, 150, 50));
        jPanel22.add(jDateChooser6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 175, 50));

        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jLabel83.setPreferredSize(new java.awt.Dimension(130, 170));
        jLabel83.setRequestFocusEnabled(false);
        jPanel22.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 160, 180));

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel84.setText("Before saving student make sure you have filled the user name,Address,Date of Birth ");
        jPanel22.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 550, 50));
        jPanel22.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, -1, -1));

        jLabel86.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel86.setText("And choose profile picture.");
        jPanel22.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        jLabel87.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel87.setText("Search here by using student id");
        jPanel22.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 570, 40));

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField8KeyReleased(evt);
            }
        });
        jPanel22.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 50, 550, 40));

        jLabel88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel22.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 50, 40, 40));

        jLabel89.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel89.setText("Click on a row in table to get data into text fields and Update  or Delete Students using it.");
        jPanel22.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 570, 50));

        jTabbedPane2.addTab("tab2", jPanel22);

        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel90.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel90.setText("Student Attendence");
        jPanel25.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 61, 244, -1));

        jLabel91.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel91.setText("Student Id  :");
        jPanel25.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 159, -1, -1));

        jTextField9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField9KeyReleased(evt);
            }
        });
        jPanel25.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 139, 175, 50));

        jLabel92.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel25.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(436, 98, 160, 180));

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel93.setText("User Name :");
        jPanel25.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, -1, -1));

        jTextField40.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel25.add(jTextField40, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 175, 50));

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel94.setText("Address");
        jPanel25.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 150, -1));

        jTextField41.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel25.add(jTextField41, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 380, 175, 50));

        jLabel95.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel95.setText("Date of Birth :");
        jPanel25.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 460, -1, -1));
        jPanel25.add(jDateChooser7, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 440, 175, 50));

        jLabel96.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel96.setText("Class");
        jPanel25.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, 76, -1));

        jComboBox10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox10ActionPerformed(evt);
            }
        });
        jPanel25.add(jComboBox10, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 240, 175, 50));

        jButton47.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton47.setText("Submit");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });
        jPanel25.add(jButton47, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 580, 161, 55));

        jButton48.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton48.setText("Absent");
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });
        jPanel25.add(jButton48, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 580, 161, 55));

        jPanel26.setBackground(new java.awt.Color(0, 0, 0));
        jPanel26.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jPanel25.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jTable9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "student Name", "Class name", "Date", "Attendence"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(jTable9);

        jPanel25.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 160, 770, 580));

        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField11KeyReleased(evt);
            }
        });
        jPanel25.add(jTextField11, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 40, 500, 40));

        jLabel97.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel97.setText("Search here by using student id");
        jPanel25.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 220, 30));

        jLabel98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel25.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(1490, 40, 40, 40));

        jLabel99.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel99.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel99.setText("Search here by using student id");
        jPanel25.add(jLabel99, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, 570, 40));

        jTabbedPane2.addTab("tab6", jPanel25);

        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel100.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel100.setText("Employee Registration");
        jPanel27.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 48, 410, -1));

        jLabel101.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel101.setText("User Name :");
        jPanel27.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, 20));

        jLabel102.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel102.setText(" Address   :");
        jPanel27.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 150, 20));

        jLabel103.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel103.setText("Date of Birth :");
        jPanel27.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, 20));

        jPanel28.setBackground(new java.awt.Color(0, 0, 0));
        jPanel28.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel27.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jLabel104.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel104.setText("Academic Details");
        jPanel27.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jTextField28.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel27.add(jTextField28, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 175, 50));

        jTextField42.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel27.add(jTextField42, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 175, 50));

        jTable10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee id ", "Employee Name  ", "BirthDay", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable10MouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(jTable10);

        jPanel27.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 140, 810, 540));

        jButton16.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton16.setText("Save");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel27.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 740, 150, 50));

        jButton49.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton49.setText("Browse");
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });
        jPanel27.add(jButton49, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 160, 40));

        jButton51.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton51.setText("Update");
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });
        jPanel27.add(jButton51, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 750, 150, 50));

        jButton52.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton52.setText("Delete");
        jButton52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton52ActionPerformed(evt);
            }
        });
        jPanel27.add(jButton52, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 740, 150, 50));
        jPanel27.add(jDateChooser8, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 175, 50));

        jLabel105.setForeground(new java.awt.Color(255, 255, 255));
        jLabel105.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jLabel105.setPreferredSize(new java.awt.Dimension(130, 170));
        jLabel105.setRequestFocusEnabled(false);
        jPanel27.add(jLabel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 160, 180));

        jLabel106.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel106.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel106.setText("Before saving student make sure you have filled the user name,Address,Date of Birth ");
        jPanel27.add(jLabel106, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 550, 50));
        jPanel27.add(jLabel107, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, -1, -1));

        jLabel108.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel108.setText("And choose profile picture.");
        jPanel27.add(jLabel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        jLabel109.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel109.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel109.setText("Search here by using student id");
        jPanel27.add(jLabel109, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 570, 40));

        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });
        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField12KeyReleased(evt);
            }
        });
        jPanel27.add(jTextField12, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 50, 560, 40));

        jLabel110.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel27.add(jLabel110, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 50, 40, 40));

        jLabel111.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel111.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel111.setText("Click on a row in table to get data into text fields and Update  or Delete Students using it.");
        jPanel27.add(jLabel111, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 570, 50));

        jTabbedPane2.addTab("tab2", jPanel27);

        jLabel112.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel112.setText("Employee Register");

        jLabel113.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel113.setText("Employee Id  :");

        jTextField13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField13KeyReleased(evt);
            }
        });

        jLabel115.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel115.setText("User Name :");

        jTextField43.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel116.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel116.setText("Address");

        jTextField44.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jButton53.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton53.setText("Register");
        jButton53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton53ActionPerformed(evt);
            }
        });

        jComboBox12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox12ActionPerformed(evt);
            }
        });

        jLabel119.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel119.setText("Role");

        jPanel30.setBackground(new java.awt.Color(0, 0, 0));
        jPanel30.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jLabel120.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel120.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel120.setText("Search here by using student id");

        jLabel117.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel117.setText("Mobile No");

        jTextField52.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTable15.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Mobile No", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane15.setViewportView(jTable15);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel113)
                                .addGap(53, 53, 53)
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel29Layout.createSequentialGroup()
                                                .addComponent(jLabel116, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(jPanel29Layout.createSequentialGroup()
                                                .addComponent(jLabel115)
                                                .addGap(53, 53, 53)))
                                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(50, 50, 50))
                                    .addComponent(jLabel120, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(172, 172, 172))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel117, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField52, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel119, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton53, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 904, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel112, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                    .addContainerGap(517, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1033, Short.MAX_VALUE)))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel112)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel113))
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel120, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel115))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel116))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField52, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel117))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel119)
                            .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jButton53, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 672, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(110, Short.MAX_VALUE))
            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("tab5", jPanel29);

        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel130.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel130.setText("Teacher payment History");
        jPanel33.add(jLabel130, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 58, -1, -1));

        jLabel131.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel131.setText("Subject Name :");
        jPanel33.add(jLabel131, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, 20));

        jTextField46.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField46ActionPerformed(evt);
            }
        });
        jPanel33.add(jTextField46, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 150, 50));

        jLabel132.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel132.setText("Price             :");
        jPanel33.add(jLabel132, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, -1, -1));

        jTextField47.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel33.add(jTextField47, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 150, 50));

        jPanel34.setBackground(new java.awt.Color(0, 0, 0));
        jPanel34.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel33.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, -1, 799));

        jTable13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable13.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject id", "Subject Name", "price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable13.setAlignmentX(5.0F);
        jTable13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable13MouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(jTable13);

        jPanel33.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 100, 840, 460));

        jButton58.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton58.setText("Save");
        jButton58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton58ActionPerformed(evt);
            }
        });
        jPanel33.add(jButton58, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 640, 170, 41));

        jButton59.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton59.setText("Update");
        jButton59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton59ActionPerformed(evt);
            }
        });
        jPanel33.add(jButton59, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 640, 170, 41));

        jButton60.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton60.setText("Delete");
        jButton60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton60ActionPerformed(evt);
            }
        });
        jPanel33.add(jButton60, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 630, 170, 41));

        jLabel133.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel133.setText("Subject ID:");
        jPanel33.add(jLabel133, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 20));

        jTextField48.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField48ActionPerformed(evt);
            }
        });
        jPanel33.add(jTextField48, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 150, 50));

        jLabel134.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel134.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel134.setText("Before saving Subject fill the Subject name And Price");
        jPanel33.add(jLabel134, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 550, 50));

        jLabel135.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel135.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel135.setText("Click on a row in table to get data into text fields and Update  or ");
        jPanel33.add(jLabel135, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 570, 50));

        jLabel136.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel136.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel136.setText("Search here by using student id");
        jPanel33.add(jLabel136, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 60, 570, 40));

        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField15KeyReleased(evt);
            }
        });
        jPanel33.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 600, 40));

        jLabel137.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel33.add(jLabel137, new org.netbeans.lib.awtextra.AbsoluteConstraints(1440, 20, 40, 40));

        jLabel138.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel138.setText("Delete Subjects using it.");
        jPanel33.add(jLabel138, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 190, 30));

        jTabbedPane2.addTab("tab3", jPanel33);

        jLabel139.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel139.setText("Teacher Payment");

        jLabel140.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel140.setText("Student Id  :");

        jTextField16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField16.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField16InputMethodTextChanged(evt);
            }
        });
        jTextField16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField16ActionPerformed(evt);
            }
        });
        jTextField16.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextField16PropertyChange(evt);
            }
        });
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField16KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField16KeyTyped(evt);
            }
        });

        jLabel141.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel142.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel142.setText("User Name :");

        jTextField49.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel143.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel143.setText("Address");

        jTextField50.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel144.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel144.setText("Date of Birth :");

        jLabel145.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel145.setText("Class");

        jComboBox13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox13.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox13ItemStateChanged(evt);
            }
        });
        jComboBox13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox13MouseClicked(evt);
            }
        });
        jComboBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox13ActionPerformed(evt);
            }
        });

        jButton61.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton61.setText("Submit");
        jButton61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton61ActionPerformed(evt);
            }
        });

        jLabel146.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel146.setText("Price");

        jLabel147.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel147.setText(" ");
        jLabel147.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable14.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "class_id", "subject", "Teacher", "month", "price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane14.setViewportView(jTable14);

        jButton62.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton62.setText("Add item");
        jButton62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton62ActionPerformed(evt);
            }
        });

        jLabel148.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel148.setText("Amount");

        jLabel149.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel149.setText(" ");
        jLabel149.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel153.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel153.setText("Month");

        jComboBox14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox14MouseClicked(evt);
            }
        });
        jComboBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox14ActionPerformed(evt);
            }
        });

        jLabel154.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel154.setText(" ");
        jLabel154.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel155.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel155.setText("Invoive_id");

        jLabel156.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel156.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel156.setText("Search here by using student id");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton63.setText("Search");
        jButton63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton63ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel35Layout.createSequentialGroup()
                            .addGap(155, 155, 155)
                            .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(156, 156, 156))
                        .addGroup(jPanel35Layout.createSequentialGroup()
                            .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel35Layout.createSequentialGroup()
                                    .addComponent(jLabel140)
                                    .addGap(53, 53, 53)
                                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel35Layout.createSequentialGroup()
                                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel146, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel145, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(64, 64, 64)
                                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jComboBox13, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel147, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel35Layout.createSequentialGroup()
                                    .addComponent(jLabel144)
                                    .addGap(42, 42, 42)
                                    .addComponent(jDateChooser10, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel35Layout.createSequentialGroup()
                                    .addComponent(jLabel153, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(64, 64, 64)
                                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel35Layout.createSequentialGroup()
                                    .addComponent(jLabel142)
                                    .addGap(42, 42, 42)
                                    .addComponent(jTextField49, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel35Layout.createSequentialGroup()
                                    .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField50, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel35Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton63)
                                    .addGap(172, 172, 172))
                                .addGroup(jPanel35Layout.createSequentialGroup()
                                    .addGap(38, 38, 38)
                                    .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(107, 107, 107)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(288, 288, 288))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel154, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(311, 311, 311))))
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(686, 686, 686)
                .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel35Layout.createSequentialGroup()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(119, 119, 119)
                                .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel154, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel148))
                                    .addComponent(jLabel155)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel35Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel139)
                        .addGap(72, 72, 72)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel140))
                            .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton63)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel142)
                                .addGap(12, 12, 12))
                            .addComponent(jTextField49, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField50, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel143))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel144))
                            .addComponent(jDateChooser10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel145))
                            .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jLabel146))
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel147, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel153))
                            .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(47, 47, 47)
                .addComponent(jButton62, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane2.addTab("tab8", jPanel35);

        jLabel114.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel114.setText("Employee Update");

        jLabel118.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel118.setText("Employee Id  :");

        jTextField17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField17KeyReleased(evt);
            }
        });

        jLabel157.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel157.setText("User Name :");

        jTextField53.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel158.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel158.setText("Address");

        jTextField54.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jButton67.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton67.setText("Update");
        jButton67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton67ActionPerformed(evt);
            }
        });

        jComboBox15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox15ActionPerformed(evt);
            }
        });

        jLabel159.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel159.setText("Role");

        jPanel39.setBackground(new java.awt.Color(0, 0, 0));
        jPanel39.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jLabel160.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel160.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel160.setText("Search here by using student id");

        jLabel161.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel161.setText("Mobile No");

        jTextField55.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTable16.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Mobile No", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable16MouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(jTable16);

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addComponent(jLabel118)
                                .addGap(53, 53, 53)
                                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel38Layout.createSequentialGroup()
                                                .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(jPanel38Layout.createSequentialGroup()
                                                .addComponent(jLabel157)
                                                .addGap(53, 53, 53)))
                                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField54, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField53, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(50, 50, 50))
                                    .addComponent(jLabel160, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(202, 202, 202))
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addComponent(jLabel161, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField55, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                                .addComponent(jLabel159, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton67, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 916, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                    .addContainerGap(575, Short.MAX_VALUE)
                    .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(975, Short.MAX_VALUE)))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel114)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel118))
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel160, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField53, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel157))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField54, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel158))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField55, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel161))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel159)
                            .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jButton67, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(211, Short.MAX_VALUE))
            .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("tab5", jPanel38);

        jLabel162.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel162.setText("Employee Remove");

        jLabel163.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel163.setText("Employee Id  :");

        jTextField18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField18KeyReleased(evt);
            }
        });

        jLabel164.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel164.setText("User Name :");

        jTextField56.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel165.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel165.setText("Address");

        jTextField57.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jButton68.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton68.setText("Remove");
        jButton68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton68ActionPerformed(evt);
            }
        });

        jComboBox16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox16ActionPerformed(evt);
            }
        });

        jLabel166.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel166.setText("Role");

        jPanel41.setBackground(new java.awt.Color(0, 0, 0));
        jPanel41.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jLabel167.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel167.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel167.setText("Search here by using student id");

        jLabel168.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel168.setText("Mobile No");

        jTextField58.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTable17.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Mobile No", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable17MouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(jTable17);

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel40Layout.createSequentialGroup()
                                .addComponent(jLabel163)
                                .addGap(53, 53, 53)
                                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel40Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel40Layout.createSequentialGroup()
                                                .addComponent(jLabel165, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(jPanel40Layout.createSequentialGroup()
                                                .addComponent(jLabel164)
                                                .addGap(53, 53, 53)))
                                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField57, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField56, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(50, 50, 50))
                                    .addComponent(jLabel167, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(82, 82, 82))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel40Layout.createSequentialGroup()
                                .addComponent(jLabel168, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField58, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                                .addComponent(jLabel166, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton68, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 1031, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel162, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                    .addContainerGap(449, Short.MAX_VALUE)
                    .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1101, Short.MAX_VALUE)))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel162)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel40Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel163))
                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel167, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField56, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel164))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField57, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel165))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField58, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel168))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel166)
                            .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jButton68, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(157, Short.MAX_VALUE))
            .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel41, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("tab5", jPanel40);

        jLabel169.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel169.setText("Class Enroll");

        jLabel170.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel170.setText("Employee Id  :");

        jTextField19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField19KeyReleased(evt);
            }
        });

        jLabel171.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel171.setText("User Name :");

        jTextField59.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel172.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel172.setText("Address");

        jTextField60.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jButton69.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton69.setText("Enroll");
        jButton69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton69ActionPerformed(evt);
            }
        });

        jComboBox17.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox17ActionPerformed(evt);
            }
        });

        jLabel173.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel173.setText("Class");

        jPanel43.setBackground(new java.awt.Color(0, 0, 0));
        jPanel43.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jLabel174.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel174.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel174.setText("Search here by using student id");

        jLabel175.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel175.setText("Mobile No");

        jTextField61.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTable18.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Class", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable18MouseClicked(evt);
            }
        });
        jScrollPane18.setViewportView(jTable18);

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addComponent(jLabel170)
                                .addGap(53, 53, 53)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel42Layout.createSequentialGroup()
                                                .addComponent(jLabel172, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                            .addGroup(jPanel42Layout.createSequentialGroup()
                                                .addComponent(jLabel171)
                                                .addGap(53, 53, 53)))
                                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField60, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField59, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(50, 50, 50))
                                    .addComponent(jLabel174, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(106, 106, 106))
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addComponent(jLabel175, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField61, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                                .addComponent(jLabel173, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton69, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 1007, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel169, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                    .addContainerGap(464, Short.MAX_VALUE)
                    .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1086, Short.MAX_VALUE)))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel169)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel170))
                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel174, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField59, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel171))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField60, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel172))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField61, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel175))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel173)
                            .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jButton69, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(64, Short.MAX_VALUE))
            .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("tab5", jPanel42);

        jLabel176.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel176.setText("Profile");

        jLabel177.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel177.setText("Employee Id  :");

        jTextField20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField20KeyReleased(evt);
            }
        });

        jLabel178.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel178.setText("User Name :");

        jTextField62.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel179.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel179.setText("Address");

        jTextField63.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jButton71.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton71.setText("Enroll");
        jButton71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton71ActionPerformed(evt);
            }
        });

        jComboBox18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox18ActionPerformed(evt);
            }
        });

        jLabel180.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel180.setText("Class");

        jPanel45.setBackground(new java.awt.Color(0, 0, 0));
        jPanel45.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jLabel181.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel181.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel181.setText("Search here by using student id");

        jLabel182.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel182.setText("Mobile No");

        jTextField64.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTable19.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Class", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable19MouseClicked(evt);
            }
        });
        jScrollPane19.setViewportView(jTable19);

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel44Layout.createSequentialGroup()
                                        .addComponent(jLabel179, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPanel44Layout.createSequentialGroup()
                                        .addComponent(jLabel178)
                                        .addGap(53, 53, 53)))
                                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField63, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField62, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50))
                            .addComponent(jLabel181, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addComponent(jLabel177)
                        .addGap(53, 53, 53)
                        .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel44Layout.createSequentialGroup()
                            .addComponent(jLabel182, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField64, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                            .addComponent(jLabel180, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox18, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel176, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 1088, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel44Layout.createSequentialGroup()
                        .addContainerGap(27, Short.MAX_VALUE)
                        .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel176)
                        .addGap(61, 61, 61)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel44Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel177))
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel181, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField62, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel178))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField63, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel179))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField64, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel182))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel180)
                            .addComponent(jComboBox18, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 813, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab5", jPanel44);

        jPanel46.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel183.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel183.setText("Employee Card");
        jPanel46.add(jLabel183, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 48, 410, -1));

        jLabel184.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel184.setText("User Name :");
        jPanel46.add(jLabel184, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, 20));

        jLabel185.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel185.setText(" Address   :");
        jPanel46.add(jLabel185, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 150, 20));

        jPanel47.setBackground(new java.awt.Color(0, 0, 0));
        jPanel47.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel46.add(jPanel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jLabel187.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel187.setText("Student Details");
        jPanel46.add(jLabel187, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jTextField65.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel46.add(jTextField65, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 175, 50));

        jTextField66.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel46.add(jTextField66, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 175, 50));

        jTable11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "emp id ", "Emp Name  ", "mobile", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable11MouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(jTable11);

        jPanel46.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 140, 850, 522));

        jButton76.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton76.setText("Create Card");
        jButton76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton76ActionPerformed(evt);
            }
        });
        jPanel46.add(jButton76, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 270, 160, 40));
        jPanel46.add(jLabel190, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, -1, -1));

        jLabel192.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel192.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel192.setText("Search here by using student id");
        jPanel46.add(jLabel192, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 570, 40));

        jTextField67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField67ActionPerformed(evt);
            }
        });
        jTextField67.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField67KeyReleased(evt);
            }
        });
        jPanel46.add(jTextField67, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 50, 820, 40));

        jLabel193.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel46.add(jLabel193, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 50, 40, 40));

        jTabbedPane2.addTab("tab2", jPanel46);

        jPanel48.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel195.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel195.setText("Employee Attendence");
        jPanel48.add(jLabel195, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 61, 310, -1));

        jLabel196.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel196.setText("Employee Id  :");
        jPanel48.add(jLabel196, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 159, -1, -1));

        jTextField68.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField68.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField68KeyReleased(evt);
            }
        });
        jPanel48.add(jTextField68, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 139, 175, 50));

        jLabel198.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel198.setText("User Name :");
        jPanel48.add(jLabel198, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, -1, -1));

        jTextField69.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel48.add(jTextField69, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 175, 50));

        jButton79.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton79.setText("Submit");
        jButton79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton79ActionPerformed(evt);
            }
        });
        jPanel48.add(jButton79, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 161, 55));

        jButton80.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton80.setText("Print");
        jButton80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton80ActionPerformed(evt);
            }
        });
        jPanel48.add(jButton80, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 770, 161, 55));

        jPanel49.setBackground(new java.awt.Color(0, 0, 0));
        jPanel49.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel48.add(jPanel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, -1, 850));

        jTable20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable20.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee Name", "Date", "Attendence"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane20.setViewportView(jTable20);

        jPanel48.add(jScrollPane20, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 970, 580));

        jButton87.setText("Search");
        jButton87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton87ActionPerformed(evt);
            }
        });
        jPanel48.add(jButton87, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, -1, -1));

        jTextField71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField71ActionPerformed(evt);
            }
        });
        jTextField71.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField71KeyReleased(evt);
            }
        });
        jPanel48.add(jTextField71, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, 680, 40));

        jLabel202.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel202.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel202.setText("Search here by using student id");
        jPanel48.add(jLabel202, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 220, 30));

        jLabel203.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel48.add(jLabel203, new org.netbeans.lib.awtextra.AbsoluteConstraints(1490, 40, 40, 40));

        jLabel204.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel204.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel204.setText("Search here by using student id");
        jPanel48.add(jLabel204, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 90, 570, 40));

        jButton81.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton81.setText("Absent");
        jButton81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton81ActionPerformed(evt);
            }
        });
        jPanel48.add(jButton81, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 430, 161, 55));

        jTabbedPane2.addTab("tab6", jPanel48);

        jLabel197.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel197.setText("Employee Payment");

        jLabel199.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel199.setText("Employee Id  :");

        jTextField70.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField70.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField70InputMethodTextChanged(evt);
            }
        });
        jTextField70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField70ActionPerformed(evt);
            }
        });
        jTextField70.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextField70PropertyChange(evt);
            }
        });
        jTextField70.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField70KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField70KeyTyped(evt);
            }
        });

        jLabel205.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel205.setText("User Name :");

        jTextField72.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel206.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel206.setText("Address");

        jTextField73.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel208.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel208.setText("Role");

        jButton82.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton82.setText("Submit");
        jButton82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton82ActionPerformed(evt);
            }
        });

        jLabel209.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel209.setText("Price");

        jLabel210.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel210.setText(" ");
        jLabel210.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable21.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee_id", "Role", "name", "month", "Salary"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane21.setViewportView(jTable21);

        jButton83.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton83.setText("Add item");
        jButton83.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton83ActionPerformed(evt);
            }
        });

        jLabel216.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel216.setText("Month");

        jComboBox20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox20MouseClicked(evt);
            }
        });
        jComboBox20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox20ActionPerformed(evt);
            }
        });

        jLabel217.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel217.setText(" ");
        jLabel217.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel218.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel218.setText("Invoive_id");

        jLabel219.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel219.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel219.setText("Search here by using student id");

        javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel51Layout.setVerticalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton84.setText("Search");
        jButton84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton84ActionPerformed(evt);
            }
        });

        jLabel316.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel316.setText(" ");
        jLabel316.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel50Layout.createSequentialGroup()
                            .addGap(155, 155, 155)
                            .addComponent(jLabel219, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(102, 102, 102))
                        .addGroup(jPanel50Layout.createSequentialGroup()
                            .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel216, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox20, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel209, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel316, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel208, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel210, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel199)
                                    .addGap(53, 53, 53)
                                    .addComponent(jTextField70, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButton83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel205)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField72, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel206, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField73, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton84)
                            .addGap(65, 65, 65)))
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel197, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addComponent(jPanel51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel218, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel217, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(286, 286, 286)
                        .addComponent(jButton82, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(221, 221, 221))))
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jPanel51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel218)
                                    .addComponent(jLabel217, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jButton82, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel197)
                        .addGap(72, 72, 72)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel199))
                            .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField70, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton84)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel219, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField72, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel205))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField73, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel206))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel208)
                            .addComponent(jLabel210, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel209)
                            .addComponent(jLabel316, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel216)
                            .addComponent(jComboBox20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(107, 107, 107)
                        .addComponent(jButton83, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(176, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab8", jPanel50);

        jPanel52.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel207.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel207.setText("Employee payment History");
        jPanel52.add(jLabel207, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 58, -1, -1));

        jLabel220.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel220.setText("Employee Name :");
        jPanel52.add(jLabel220, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, 20));

        jTextField75.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField75ActionPerformed(evt);
            }
        });
        jPanel52.add(jTextField75, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 150, 50));

        jPanel53.setBackground(new java.awt.Color(0, 0, 0));
        jPanel53.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel53Layout.setVerticalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel52.add(jPanel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, -1, 799));

        jTable22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable22.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee id", "Date", "price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable22.setAlignmentX(5.0F);
        jTable22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable22MouseClicked(evt);
            }
        });
        jScrollPane22.setViewportView(jTable22);

        jPanel52.add(jScrollPane22, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 100, 900, 590));

        jButton85.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton85.setText("Search");
        jButton85.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton85ActionPerformed(evt);
            }
        });
        jPanel52.add(jButton85, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 170, 41));

        jLabel222.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel222.setText("Employee ID:");
        jPanel52.add(jLabel222, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 20));

        jTextField77.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField77ActionPerformed(evt);
            }
        });
        jPanel52.add(jTextField77, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 150, 50));

        jLabel225.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel225.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel225.setText("Search here by using student id");
        jPanel52.add(jLabel225, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 60, 570, 40));

        jTextField78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField78ActionPerformed(evt);
            }
        });
        jTextField78.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField78KeyReleased(evt);
            }
        });
        jPanel52.add(jTextField78, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 580, 40));

        jLabel226.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel52.add(jLabel226, new org.netbeans.lib.awtextra.AbsoluteConstraints(1440, 20, 40, 40));

        jTabbedPane2.addTab("tab3", jPanel52);

        jPanel54.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel228.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel228.setText("Student Update");
        jPanel54.add(jLabel228, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 48, 410, -1));

        jLabel229.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel229.setText("User Name :");
        jPanel54.add(jLabel229, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, 20));

        jLabel230.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel230.setText(" Address   :");
        jPanel54.add(jLabel230, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 150, 20));

        jLabel231.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel231.setText("Date of Birth :");
        jPanel54.add(jLabel231, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, 20));

        jPanel55.setBackground(new java.awt.Color(0, 0, 0));
        jPanel55.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel54.add(jPanel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jLabel232.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel232.setText("Academic Details");
        jPanel54.add(jLabel232, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jTextField79.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel54.add(jTextField79, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 175, 50));

        jTextField80.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel54.add(jTextField80, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 175, 50));

        jTable23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable23.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student id ", "Student Name  ", "BirthDay", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable23MouseClicked(evt);
            }
        });
        jScrollPane23.setViewportView(jTable23);

        jPanel54.add(jScrollPane23, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 140, 840, 630));

        jButton89.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton89.setText("Browse");
        jButton89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton89ActionPerformed(evt);
            }
        });
        jPanel54.add(jButton89, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 160, 40));

        jButton98.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton98.setText("Update");
        jButton98.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton98ActionPerformed(evt);
            }
        });
        jPanel54.add(jButton98, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 500, 180, 50));
        jPanel54.add(jDateChooser11, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 175, 50));

        jLabel233.setForeground(new java.awt.Color(255, 255, 255));
        jLabel233.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jLabel233.setPreferredSize(new java.awt.Dimension(130, 170));
        jLabel233.setRequestFocusEnabled(false);
        jPanel54.add(jLabel233, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 160, 180));

        jLabel234.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel234.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel234.setText("Before saving student make sure you have filled the user name,Address,Date of Birth ");
        jPanel54.add(jLabel234, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 550, 50));
        jPanel54.add(jLabel235, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, -1, -1));

        jLabel236.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel236.setText("And choose profile picture.");
        jPanel54.add(jLabel236, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        jLabel237.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel237.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel237.setText("Search here by using student id");
        jPanel54.add(jLabel237, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 570, 40));

        jTextField81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField81ActionPerformed(evt);
            }
        });
        jTextField81.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField81KeyReleased(evt);
            }
        });
        jPanel54.add(jTextField81, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 50, 560, 40));

        jLabel238.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel54.add(jLabel238, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 50, 40, 40));

        jLabel239.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel239.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel239.setText("Click on a row in table to get data into text fields and Update  or Delete Students using it.");
        jPanel54.add(jLabel239, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 570, 50));

        jTabbedPane2.addTab("tab2", jPanel54);

        jPanel56.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel240.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel240.setText("Student Remove");
        jPanel56.add(jLabel240, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 48, 410, -1));

        jLabel241.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel241.setText("User Name :");
        jPanel56.add(jLabel241, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, 20));

        jLabel242.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel242.setText(" Address   :");
        jPanel56.add(jLabel242, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 150, 20));

        jLabel243.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel243.setText("Date of Birth :");
        jPanel56.add(jLabel243, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, 20));

        jPanel57.setBackground(new java.awt.Color(0, 0, 0));
        jPanel57.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel56.add(jPanel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jLabel244.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel244.setText("Academic Details");
        jPanel56.add(jLabel244, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jTextField82.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel56.add(jTextField82, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 175, 50));

        jTextField83.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel56.add(jTextField83, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 175, 50));

        jTable24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable24.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable24.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student id ", "Student Name  ", "BirthDay", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable24MouseClicked(evt);
            }
        });
        jScrollPane24.setViewportView(jTable24);

        jPanel56.add(jScrollPane24, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 140, 760, 640));

        jButton101.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton101.setText("Browse");
        jButton101.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton101ActionPerformed(evt);
            }
        });
        jPanel56.add(jButton101, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 160, 40));

        jButton103.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton103.setText("Delete");
        jButton103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton103ActionPerformed(evt);
            }
        });
        jPanel56.add(jButton103, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 530, 180, 60));
        jPanel56.add(jDateChooser12, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 175, 50));

        jLabel245.setForeground(new java.awt.Color(255, 255, 255));
        jLabel245.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jLabel245.setPreferredSize(new java.awt.Dimension(130, 170));
        jLabel245.setRequestFocusEnabled(false);
        jPanel56.add(jLabel245, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 160, 180));

        jLabel246.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel246.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel246.setText("Before saving student make sure you have filled the user name,Address,Date of Birth ");
        jPanel56.add(jLabel246, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 550, 50));
        jPanel56.add(jLabel247, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, -1, -1));

        jLabel248.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel248.setText("And choose profile picture.");
        jPanel56.add(jLabel248, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        jLabel249.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel249.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel249.setText("Search here by using student id");
        jPanel56.add(jLabel249, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 570, 40));

        jTextField84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField84ActionPerformed(evt);
            }
        });
        jTextField84.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField84KeyReleased(evt);
            }
        });
        jPanel56.add(jTextField84, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 50, 560, 40));

        jLabel250.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel56.add(jLabel250, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 50, 40, 40));

        jLabel251.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel251.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel251.setText("Click on a row in table to get data into text fields and Update  or Delete Students using it.");
        jPanel56.add(jLabel251, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 570, 50));

        jTabbedPane2.addTab("tab2", jPanel56);

        jLabel252.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel252.setText("Student Profile");

        jLabel253.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel253.setText("Student Id  :");

        jTextField85.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField85.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField85KeyReleased(evt);
            }
        });

        jLabel254.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel254.setText("User Name :");

        jTextField86.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel255.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel255.setText("Address");

        jTextField87.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jButton104.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton104.setText("Enroll");
        jButton104.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton104ActionPerformed(evt);
            }
        });

        jComboBox21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox21ActionPerformed(evt);
            }
        });

        jLabel256.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel256.setText("Class");

        jPanel59.setBackground(new java.awt.Color(0, 0, 0));
        jPanel59.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
        jPanel59.setLayout(jPanel59Layout);
        jPanel59Layout.setHorizontalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel59Layout.setVerticalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jLabel257.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel257.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel257.setText("Search here by using student id");

        jLabel258.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel258.setText("Mobile No");

        jTextField88.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTable25.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Class", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable25MouseClicked(evt);
            }
        });
        jScrollPane25.setViewportView(jTable25);

        javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
        jPanel58.setLayout(jPanel58Layout);
        jPanel58Layout.setHorizontalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel58Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel58Layout.createSequentialGroup()
                                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel58Layout.createSequentialGroup()
                                        .addComponent(jLabel255, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPanel58Layout.createSequentialGroup()
                                        .addComponent(jLabel254)
                                        .addGap(53, 53, 53)))
                                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField87, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField86, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50))
                            .addComponent(jLabel257, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel58Layout.createSequentialGroup()
                        .addComponent(jLabel253)
                        .addGap(53, 53, 53)
                        .addComponent(jTextField85, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel58Layout.createSequentialGroup()
                            .addComponent(jLabel258, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField88, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel58Layout.createSequentialGroup()
                            .addComponent(jLabel256, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton104, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox21, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel58Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel252, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel59, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 963, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(172, 172, 172))
        );
        jPanel58Layout.setVerticalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel58Layout.createSequentialGroup()
                        .addContainerGap(27, Short.MAX_VALUE)
                        .addComponent(jPanel59, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel58Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel252)
                        .addGap(61, 61, 61)
                        .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel58Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel253))
                            .addComponent(jTextField85, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel257, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField86, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel254))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField87, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel255))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField88, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel258))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel256)
                            .addComponent(jComboBox21, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jButton104, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("tab5", jPanel58);

        jLabel259.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel259.setText("Teacher Update");

        jTextField89.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel260.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel260.setText("User Name :");

        jLabel261.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel261.setText("Address    :");

        jTextField90.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel262.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel262.setText("Date of Birth :");

        jPanel62.setBackground(new java.awt.Color(0, 0, 0));
        jPanel62.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel62Layout = new javax.swing.GroupLayout(jPanel62);
        jPanel62.setLayout(jPanel62Layout);
        jPanel62Layout.setHorizontalGroup(
            jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel62Layout.setVerticalGroup(
            jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jTable26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable26.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable26.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Teacher id ", "Teacher Name  ", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable26MouseClicked(evt);
            }
        });
        jScrollPane26.setViewportView(jTable26);

        jButton114.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton114.setText("Save");
        jButton114.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton114ActionPerformed(evt);
            }
        });

        jButton115.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton115.setText("Update");
        jButton115.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton115ActionPerformed(evt);
            }
        });

        jButton116.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton116.setText("Delete");
        jButton116.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton116ActionPerformed(evt);
            }
        });

        jTextField91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField91ActionPerformed(evt);
            }
        });
        jTextField91.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField91KeyReleased(evt);
            }
        });

        jLabel263.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N

        jLabel264.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel264.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel264.setText("Search here by using student id");

        jLabel265.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel265.setText(",Date of Birth And choose profile picture.");

        jLabel266.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel266.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel266.setText("Before saving a Teacher make sure you have filled the User name,Address ");

        jLabel267.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel267.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel267.setText("Click on a row in table to get data into text fields and Update  or ");

        jLabel268.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel268.setText("Delete Teachers using it.");

        javax.swing.GroupLayout jPanel61Layout = new javax.swing.GroupLayout(jPanel61);
        jPanel61.setLayout(jPanel61Layout);
        jPanel61Layout.setHorizontalGroup(
            jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel61Layout.createSequentialGroup()
                .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel61Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel61Layout.createSequentialGroup()
                                .addComponent(jLabel260)
                                .addGap(34, 34, 34)
                                .addComponent(jTextField89, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel61Layout.createSequentialGroup()
                                    .addGap(130, 130, 130)
                                    .addComponent(jTextField90, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel261, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                        .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField91)
                            .addComponent(jLabel264, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE))
                        .addGap(240, 240, 240))
                    .addGroup(jPanel61Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton114, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)
                        .addComponent(jButton115, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(157, 157, 157)
                        .addComponent(jButton116, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)))
                .addComponent(jLabel263, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(jPanel61Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel266, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel267, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel61Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel265)
                            .addComponent(jLabel268, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel61Layout.createSequentialGroup()
                    .addGap(0, 23, Short.MAX_VALUE)
                    .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel61Layout.createSequentialGroup()
                            .addGap(66, 66, 66)
                            .addComponent(jLabel259, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel61Layout.createSequentialGroup()
                            .addComponent(jLabel262)
                            .addGap(37, 37, 37)
                            .addComponent(jDateChooser13, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(284, 284, 284)
                    .addComponent(jPanel62, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(31, 31, 31)
                    .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 831, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 49, Short.MAX_VALUE)))
        );
        jPanel61Layout.setVerticalGroup(
            jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel61Layout.createSequentialGroup()
                .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel61Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel61Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel260))
                            .addComponent(jTextField89, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel61Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel263, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField91, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jLabel264, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField90, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel61Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel261)))
                .addGap(89, 89, 89)
                .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel61Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel265))
                    .addComponent(jLabel266, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel267, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel61Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel268, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 243, Short.MAX_VALUE)
                .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton114, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton115, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton116, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(111, 111, 111))
            .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel61Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel61Layout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addComponent(jLabel259)
                            .addGap(221, 221, 221)
                            .addGroup(jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel61Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addComponent(jLabel262))
                                .addComponent(jDateChooser13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jPanel62, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel61Layout.createSequentialGroup()
                            .addGap(117, 117, 117)
                            .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("tab4", jPanel61);

        jLabel269.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel269.setText("Teacher Remove");

        jTextField92.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel270.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel270.setText("User Name :");

        jLabel271.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel271.setText("Address    :");

        jTextField93.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel272.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel272.setText("Date of Birth :");

        jPanel64.setBackground(new java.awt.Color(0, 0, 0));
        jPanel64.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel64Layout = new javax.swing.GroupLayout(jPanel64);
        jPanel64.setLayout(jPanel64Layout);
        jPanel64Layout.setHorizontalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel64Layout.setVerticalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jTable27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable27.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable27.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Teacher id ", "Teacher Name  ", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable27MouseClicked(evt);
            }
        });
        jScrollPane27.setViewportView(jTable27);

        jButton117.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton117.setText("Save");
        jButton117.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton117ActionPerformed(evt);
            }
        });

        jButton118.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton118.setText("Update");
        jButton118.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton118ActionPerformed(evt);
            }
        });

        jButton119.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton119.setText("Delete");
        jButton119.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton119ActionPerformed(evt);
            }
        });

        jTextField94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField94ActionPerformed(evt);
            }
        });
        jTextField94.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField94KeyReleased(evt);
            }
        });

        jLabel273.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N

        jLabel274.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel274.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel274.setText("Search here by using student id");

        jLabel275.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel275.setText(",Date of Birth And choose profile picture.");

        jLabel276.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel276.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel276.setText("Before saving a Teacher make sure you have filled the User name,Address ");

        jLabel277.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel277.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel277.setText("Click on a row in table to get data into text fields and Update  or ");

        jLabel278.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel278.setText("Delete Teachers using it.");

        javax.swing.GroupLayout jPanel63Layout = new javax.swing.GroupLayout(jPanel63);
        jPanel63.setLayout(jPanel63Layout);
        jPanel63Layout.setHorizontalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel63Layout.createSequentialGroup()
                .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel63Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel63Layout.createSequentialGroup()
                                .addComponent(jLabel270)
                                .addGap(34, 34, 34)
                                .addComponent(jTextField92, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel63Layout.createSequentialGroup()
                                    .addGap(130, 130, 130)
                                    .addComponent(jTextField93, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel271, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                        .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField94)
                            .addComponent(jLabel274, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)))
                    .addGroup(jPanel63Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton117, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(181, 181, 181)
                        .addComponent(jButton118, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)))
                .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel63Layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(jLabel273, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel63Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton119, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99))))
            .addGroup(jPanel63Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel276, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel277, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel63Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel275)
                            .addComponent(jLabel278, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel63Layout.createSequentialGroup()
                    .addGap(0, 23, Short.MAX_VALUE)
                    .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel63Layout.createSequentialGroup()
                            .addGap(66, 66, 66)
                            .addComponent(jLabel269, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel63Layout.createSequentialGroup()
                            .addComponent(jLabel272)
                            .addGap(37, 37, 37)
                            .addComponent(jDateChooser14, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(284, 284, 284)
                    .addComponent(jPanel64, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(31, 31, 31)
                    .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 64, Short.MAX_VALUE)))
        );
        jPanel63Layout.setVerticalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel63Layout.createSequentialGroup()
                .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel63Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel63Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel270))
                            .addComponent(jTextField92, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel63Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel273, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField94, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jLabel274, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField93, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel63Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel271)))
                .addGap(89, 89, 89)
                .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel63Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel275))
                    .addComponent(jLabel276, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel277, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel63Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel278, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 229, Short.MAX_VALUE)
                .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton117, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton118, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton119, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(125, 125, 125))
            .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel63Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel63Layout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addComponent(jLabel269)
                            .addGap(221, 221, 221)
                            .addGroup(jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel63Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addComponent(jLabel272))
                                .addComponent(jDateChooser14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jPanel64, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel63Layout.createSequentialGroup()
                            .addGap(117, 117, 117)
                            .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("tab4", jPanel63);

        jPanel66.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel279.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel279.setText("Class Update");
        jPanel66.add(jLabel279, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 68, 244, -1));

        jLabel280.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel280.setText("Time slot");
        jPanel66.add(jLabel280, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 291, -1, -1));

        jTextField95.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel66.add(jTextField95, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 271, 175, 50));

        jLabel281.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel281.setText("Subject");
        jPanel66.add(jLabel281, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 222, 76, -1));

        jComboBox22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox22ActionPerformed(evt);
            }
        });
        jPanel66.add(jComboBox22, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 208, 175, 50));

        jLabel282.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel282.setText("Teacher");
        jPanel66.add(jLabel282, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 159, 76, -1));

        jComboBox23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox23ActionPerformed(evt);
            }
        });
        jPanel66.add(jComboBox23, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 145, 175, 50));

        jTable28.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable28.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "class_id", "teacher", "subject", "timeslot"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable28MouseClicked(evt);
            }
        });
        jScrollPane28.setViewportView(jTable28);

        jPanel66.add(jScrollPane28, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 120, 940, 520));

        jButton121.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton121.setText("Update");
        jButton121.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton121ActionPerformed(evt);
            }
        });
        jPanel66.add(jButton121, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 430, 161, 55));

        jPanel67.setBackground(new java.awt.Color(0, 0, 0));
        jPanel67.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jPanel66.add(jPanel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 13, 10, 850));

        jLabel283.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel283.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel283.setText("Select a Teacher ,Subject And  Time slot");
        jPanel66.add(jLabel283, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 570, 40));

        jTabbedPane2.addTab("tab7", jPanel66);

        jPanel68.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel284.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel284.setText("Class Remove");
        jPanel68.add(jLabel284, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 68, 244, -1));

        jLabel285.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel285.setText("Time slot");
        jPanel68.add(jLabel285, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 291, -1, -1));

        jTextField96.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel68.add(jTextField96, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 271, 175, 50));

        jLabel286.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel286.setText("Subject");
        jPanel68.add(jLabel286, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 222, 76, -1));

        jComboBox24.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox24ActionPerformed(evt);
            }
        });
        jPanel68.add(jComboBox24, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 208, 175, 50));

        jLabel287.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel287.setText("Teacher");
        jPanel68.add(jLabel287, new org.netbeans.lib.awtextra.AbsoluteConstraints(96, 159, 76, -1));

        jComboBox25.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox25ActionPerformed(evt);
            }
        });
        jPanel68.add(jComboBox25, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 145, 175, 50));

        jTable29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable29.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "class_id", "teacher", "subject", "timeslot"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable29MouseClicked(evt);
            }
        });
        jScrollPane29.setViewportView(jTable29);

        jPanel68.add(jScrollPane29, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, 900, 650));

        jButton127.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton127.setText("Delete");
        jButton127.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton127ActionPerformed(evt);
            }
        });
        jPanel68.add(jButton127, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 360, 161, 55));

        jPanel69.setBackground(new java.awt.Color(0, 0, 0));
        jPanel69.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel69Layout = new javax.swing.GroupLayout(jPanel69);
        jPanel69.setLayout(jPanel69Layout);
        jPanel69Layout.setHorizontalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel69Layout.setVerticalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );

        jPanel68.add(jPanel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 13, 10, 850));

        jTabbedPane2.addTab("tab7", jPanel68);

        jPanel70.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel289.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel289.setText("Teacher Card");
        jPanel70.add(jLabel289, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 48, 410, -1));

        jLabel290.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel290.setText("User Name :");
        jPanel70.add(jLabel290, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, 20));

        jLabel291.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel291.setText(" Address   :");
        jPanel70.add(jLabel291, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 150, 20));

        jLabel292.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel292.setText("Date of Birth :");
        jPanel70.add(jLabel292, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, 20));

        jPanel71.setBackground(new java.awt.Color(0, 0, 0));
        jPanel71.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel71Layout = new javax.swing.GroupLayout(jPanel71);
        jPanel71.setLayout(jPanel71Layout);
        jPanel71Layout.setHorizontalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel71Layout.setVerticalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel70.add(jPanel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jLabel293.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel293.setText("Student Details");
        jPanel70.add(jLabel293, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 20, -1, -1));

        jTextField97.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel70.add(jTextField97, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 175, 50));

        jTextField98.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel70.add(jTextField98, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 175, 50));

        jTable30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable30.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable30.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student id ", "Student Name  ", "BirthDay", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable30MouseClicked(evt);
            }
        });
        jScrollPane30.setViewportView(jTable30);

        jPanel70.add(jScrollPane30, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 140, 810, 522));

        jButton99.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton99.setText("Save");
        jButton99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton99ActionPerformed(evt);
            }
        });
        jPanel70.add(jButton99, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 710, 150, 50));

        jButton100.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton100.setText("Create Card");
        jButton100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton100ActionPerformed(evt);
            }
        });
        jPanel70.add(jButton100, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 160, 40));

        jButton102.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton102.setText("Update");
        jButton102.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton102ActionPerformed(evt);
            }
        });
        jPanel70.add(jButton102, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 710, 150, 50));

        jButton128.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jButton128.setText("Delete");
        jButton128.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton128ActionPerformed(evt);
            }
        });
        jPanel70.add(jButton128, new org.netbeans.lib.awtextra.AbsoluteConstraints(1280, 700, 150, 50));
        jPanel70.add(jDateChooser15, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 175, 50));

        jLabel294.setForeground(new java.awt.Color(255, 255, 255));
        jLabel294.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jLabel294.setPreferredSize(new java.awt.Dimension(130, 170));
        jLabel294.setRequestFocusEnabled(false);
        jPanel70.add(jLabel294, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 160, 180));

        jLabel295.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel295.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel295.setText("Before saving student make sure you have filled the user name,Address,Date of Birth ");
        jPanel70.add(jLabel295, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 550, 50));
        jPanel70.add(jLabel296, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, -1, -1));

        jLabel297.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel297.setText("And choose profile picture.");
        jPanel70.add(jLabel297, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        jLabel298.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel298.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel298.setText("Search here by using student id");
        jPanel70.add(jLabel298, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, 570, 40));

        jTextField99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField99ActionPerformed(evt);
            }
        });
        jTextField99.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField99KeyReleased(evt);
            }
        });
        jPanel70.add(jTextField99, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 50, 560, 40));

        jLabel299.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel70.add(jLabel299, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 50, 40, 40));

        jLabel300.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel300.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel300.setText("Click on a row in table to get data into text fields and Update  or Delete Students using it.");
        jPanel70.add(jLabel300, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 570, 50));

        jTabbedPane2.addTab("tab2", jPanel70);

        jPanel72.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel304.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel304.setText("Student payment History");
        jPanel72.add(jLabel304, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 58, -1, -1));

        jLabel305.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel305.setText("Student Name :");
        jPanel72.add(jLabel305, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, 20));

        jTextField100.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField100ActionPerformed(evt);
            }
        });
        jPanel72.add(jTextField100, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 150, 50));

        jPanel73.setBackground(new java.awt.Color(0, 0, 0));
        jPanel73.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel73Layout = new javax.swing.GroupLayout(jPanel73);
        jPanel73.setLayout(jPanel73Layout);
        jPanel73Layout.setHorizontalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel73Layout.setVerticalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel72.add(jPanel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, -1, 799));

        jTable31.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable31.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee id", "Date", "price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable31.setAlignmentX(5.0F);
        jTable31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable31MouseClicked(evt);
            }
        });
        jScrollPane31.setViewportView(jTable31);

        jPanel72.add(jScrollPane31, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 100, 880, 550));

        jButton120.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton120.setText("Search");
        jButton120.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton120ActionPerformed(evt);
            }
        });
        jPanel72.add(jButton120, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 170, 41));

        jLabel306.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel306.setText("Student ID:");
        jPanel72.add(jLabel306, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 20));

        jTextField101.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField101.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField101ActionPerformed(evt);
            }
        });
        jPanel72.add(jTextField101, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 150, 50));

        jLabel307.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel307.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel307.setText("Search here by using student id");
        jPanel72.add(jLabel307, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 60, 570, 40));

        jTextField102.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField102ActionPerformed(evt);
            }
        });
        jTextField102.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField102KeyReleased(evt);
            }
        });
        jPanel72.add(jTextField102, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 600, 60));

        jLabel308.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel72.add(jLabel308, new org.netbeans.lib.awtextra.AbsoluteConstraints(1440, 20, 40, 40));

        jTabbedPane2.addTab("tab3", jPanel72);

        jPanel74.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel309.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel309.setText("Teacher payment History");
        jPanel74.add(jLabel309, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 58, -1, -1));

        jLabel310.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel310.setText("Teacher Name :");
        jPanel74.add(jLabel310, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, 20));

        jTextField103.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField103ActionPerformed(evt);
            }
        });
        jPanel74.add(jTextField103, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 150, 50));

        jPanel75.setBackground(new java.awt.Color(0, 0, 0));
        jPanel75.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel75Layout = new javax.swing.GroupLayout(jPanel75);
        jPanel75.setLayout(jPanel75Layout);
        jPanel75Layout.setHorizontalGroup(
            jPanel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel75Layout.setVerticalGroup(
            jPanel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel74.add(jPanel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, -1, 799));

        jTable32.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable32.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee id", "Date", "price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable32.setAlignmentX(5.0F);
        jTable32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable32MouseClicked(evt);
            }
        });
        jScrollPane32.setViewportView(jTable32);

        jPanel74.add(jScrollPane32, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 100, 890, 620));

        jButton122.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton122.setText("Search");
        jButton122.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton122ActionPerformed(evt);
            }
        });
        jPanel74.add(jButton122, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 170, 41));

        jLabel311.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel311.setText("Teacher ID:");
        jPanel74.add(jLabel311, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, 20));

        jTextField104.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField104.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField104ActionPerformed(evt);
            }
        });
        jPanel74.add(jTextField104, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 150, 50));

        jLabel312.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel312.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel312.setText("Search here by using student id");
        jPanel74.add(jLabel312, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 60, 570, 40));

        jTextField105.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField105ActionPerformed(evt);
            }
        });
        jTextField105.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField105KeyReleased(evt);
            }
        });
        jPanel74.add(jTextField105, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, 600, 40));

        jLabel313.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel74.add(jLabel313, new org.netbeans.lib.awtextra.AbsoluteConstraints(1440, 20, 40, 40));

        jTabbedPane2.addTab("tab3", jPanel74);

        jPanel76.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel221.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel221.setText("Teacher Attendence");
        jPanel76.add(jLabel221, new org.netbeans.lib.awtextra.AbsoluteConstraints(87, 61, 244, -1));

        jLabel223.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel223.setText("Teacher Id  :");
        jPanel76.add(jLabel223, new org.netbeans.lib.awtextra.AbsoluteConstraints(57, 159, -1, -1));

        jTextField76.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField76.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField76KeyReleased(evt);
            }
        });
        jPanel76.add(jTextField76, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 139, 175, 50));

        jLabel227.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel227.setText("User Name :");
        jPanel76.add(jLabel227, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, -1, -1));

        jTextField106.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel76.add(jTextField106, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 175, 50));

        jLabel314.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel314.setText("Address");
        jPanel76.add(jLabel314, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, 150, -1));

        jTextField107.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel76.add(jTextField107, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 380, 175, 50));

        jLabel315.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel315.setText("Date of Birth :");
        jPanel76.add(jLabel315, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 460, -1, -1));
        jPanel76.add(jDateChooser16, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 440, 175, 50));

        jButton21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton21.setText("Submit");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel76.add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 580, 161, 55));

        jButton33.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton33.setText("Print");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });
        jPanel76.add(jButton33, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 700, 161, 55));

        jPanel77.setBackground(new java.awt.Color(0, 0, 0));
        jPanel77.setPreferredSize(new java.awt.Dimension(2, 0));

        javax.swing.GroupLayout jPanel77Layout = new javax.swing.GroupLayout(jPanel77);
        jPanel77.setLayout(jPanel77Layout);
        jPanel77Layout.setHorizontalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel77Layout.setVerticalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel76.add(jPanel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 13, -1, 850));

        jTable33.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable33.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Teacher", "Date", "Attendence"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane33.setViewportView(jTable33);

        jPanel76.add(jScrollPane33, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 160, 860, 490));

        jTextField108.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField108ActionPerformed(evt);
            }
        });
        jTextField108.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField108KeyReleased(evt);
            }
        });
        jPanel76.add(jTextField108, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 70, 840, 40));

        jLabel317.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel317.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel317.setText("Search here by using teacher id");
        jPanel76.add(jLabel317, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 220, 30));

        jLabel318.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search-interface-symbol.png"))); // NOI18N
        jPanel76.add(jLabel318, new org.netbeans.lib.awtextra.AbsoluteConstraints(1490, 40, 40, 40));

        jLabel319.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel319.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel319.setText("Search here by using student id");
        jPanel76.add(jLabel319, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 110, 570, 40));

        jButton86.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton86.setText("Absent");
        jButton86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton86ActionPerformed(evt);
            }
        });
        jPanel76.add(jButton86, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 580, 161, 55));

        jTabbedPane2.addTab("tab6", jPanel76);

        jLabel224.setFont(new java.awt.Font("Trebuchet MS", 1, 24)); // NOI18N
        jLabel224.setText("Teacher Payment");

        jLabel320.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel320.setText("Teacher Id  :");

        jTextField109.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTextField109.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField109InputMethodTextChanged(evt);
            }
        });
        jTextField109.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField109ActionPerformed(evt);
            }
        });
        jTextField109.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextField109PropertyChange(evt);
            }
        });
        jTextField109.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField109KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField109KeyTyped(evt);
            }
        });

        jLabel322.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel322.setText("User Name :");

        jTextField110.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel323.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel323.setText("Address");

        jTextField111.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel324.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel324.setText("Date of Birth :");

        jLabel325.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel325.setText("Class");

        jComboBox27.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox27.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox27ItemStateChanged(evt);
            }
        });
        jComboBox27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox27MouseClicked(evt);
            }
        });
        jComboBox27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox27ActionPerformed(evt);
            }
        });

        jButton34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton34.setText("Submit");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jLabel326.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel326.setText("Price");

        jLabel327.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel327.setText(" ");
        jLabel327.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable34.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "class_id", "subject", "Teacher", "month", "price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane34.setViewportView(jTable34);

        jButton37.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton37.setText("Add item");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jLabel328.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel328.setText("Amount");

        jLabel329.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel329.setText(" ");
        jLabel329.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel333.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel333.setText("Month");

        jComboBox28.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jComboBox28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox28MouseClicked(evt);
            }
        });
        jComboBox28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox28ActionPerformed(evt);
            }
        });

        jLabel334.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel334.setText(" ");
        jLabel334.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel335.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel335.setText("Invoive_id");

        jLabel336.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel336.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lamp.png"))); // NOI18N
        jLabel336.setText("Search here by using student id");

        javax.swing.GroupLayout jPanel79Layout = new javax.swing.GroupLayout(jPanel79);
        jPanel79.setLayout(jPanel79Layout);
        jPanel79Layout.setHorizontalGroup(
            jPanel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel79Layout.setVerticalGroup(
            jPanel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton38.setText("Search");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel78Layout = new javax.swing.GroupLayout(jPanel78);
        jPanel78.setLayout(jPanel78Layout);
        jPanel78Layout.setHorizontalGroup(
            jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel78Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel78Layout.createSequentialGroup()
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addComponent(jLabel320)
                                .addGap(53, 53, 53)
                                .addComponent(jTextField109, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel326, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel325, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(64, 64, 64)
                                .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox27, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel327, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addComponent(jLabel324)
                                .addGap(42, 42, 42)
                                .addComponent(jDateChooser17, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addComponent(jLabel333, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox28, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addComponent(jLabel322)
                                .addGap(42, 42, 42)
                                .addComponent(jTextField110, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addComponent(jLabel323, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField111, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(jButton38)
                        .addGap(86, 86, 86))
                    .addGroup(jPanel78Layout.createSequentialGroup()
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(155, 155, 155)
                                .addComponent(jLabel336, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel224, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(129, 129, 129)
                                .addComponent(jPanel79, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(7, 7, 7)))
                .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel78Layout.createSequentialGroup()
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel335, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel328, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addComponent(jLabel329, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 374, Short.MAX_VALUE))
                            .addComponent(jLabel334, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(252, Short.MAX_VALUE))
                    .addGroup(jPanel78Layout.createSequentialGroup()
                        .addComponent(jScrollPane34, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel78Layout.setVerticalGroup(
            jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel78Layout.createSequentialGroup()
                .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel78Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel224)
                        .addGap(72, 72, 72)
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel320))
                            .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField109, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton38)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel336, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel322)
                                .addGap(12, 12, 12))
                            .addComponent(jTextField110, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField111, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel323))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel324))
                            .addComponent(jDateChooser17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel325))
                            .addComponent(jComboBox27, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(jLabel326))
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel327, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel333))
                            .addComponent(jComboBox28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel78Layout.createSequentialGroup()
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jPanel79, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jScrollPane34, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel335)
                            .addComponent(jLabel334, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel329)
                            .addGroup(jPanel78Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel328)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(109, 109, 109))
        );

        jTabbedPane2.addTab("tab8", jPanel78);

        jPanel1.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 1550, 920));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        
        String sname=jTextField21.getText();
        String price=jTextField22.getText();

        if(sname.isEmpty()){
            JOptionPane.showMessageDialog(this,"Empty subject Name","Warning",JOptionPane.WARNING_MESSAGE);
        jTextField21.grabFocus();
        }else if(price.isEmpty()){
            JOptionPane.showMessageDialog(this,"Empty price","Warning",JOptionPane.WARNING_MESSAGE);
        jTextField22.grabFocus();
        }else{
        try  {
            Mysql.iud("INSERT INTO subject (subject_name,price) VALUES ('"+sname+"','"+price+"')") ;
jTextField21.setText("");
jTextField22.setText("");
jTextField23.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        loadSubjecttable();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jTextField21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField21ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
int r=jTable3.getSelectedRow();
String id=(jTable3.getModel().getValueAt(r, 0).toString());

        try {
        ResultSet rs=    Mysql.search("SELECT * FROM teacher WHERE Tno='"+id+"'");
            if(rs.next()){
            String teacher_name=rs.getString("teacher_name");
            Date date_birth=rs.getDate("dob");
            String address=rs.getString("address");
            
            
            jTextField30.setText(teacher_name);
            jTextField31.setText(address);
            jDateChooser2.setDate(date_birth);
            }
        } catch (Exception e) {
        }




        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
String name=jTextField30.getText();
String address=jTextField31.getText();
String date1=String.valueOf(jDateChooser2.getDate());

  if(name.isEmpty()){
            
            JOptionPane.showMessageDialog(this, "Empty user name", "warning", JOptionPane.WARNING_MESSAGE);
            jTextField30.grabFocus();
        }else if(address.isEmpty()){
            JOptionPane.showMessageDialog(this, "Empty Address", "warning", JOptionPane.WARNING_MESSAGE);
            jTextField31.grabFocus();
        }else if(date1=="null"){
            JOptionPane.showMessageDialog(this, "Empty Date", "warning", JOptionPane.WARNING_MESSAGE);
            jTextField31.grabFocus();
        }
        else{
           
            try  {
                   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String date=sdf.format(jDateChooser2.getDate()); 
                System.out.println(date);
                 Mysql.iud("INSERT INTO teacher (teacher_name,address,dob) VALUES ('"+name+"', '"+address+"','"+date+"')");
                

                

            } catch (Exception e) {
                e.printStackTrace();
            }
            loadTeachers();

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        try {
            String id=jTextField4.getText();
            String time=jComboBox7.getSelectedItem().toString();
            String subject=jComboBox3.getSelectedItem().toString();
//int subjectid=subjectMap.get(subject);
int classno=classMap.get(time);
System.out.println(classno);

// Mysql.iud("INSERT INTO student_has_subject (subject_subject_id,student_student_no)VALUES('"+subjectid+"','"+id+"')");
ResultSet rs=Mysql.search("SELECT * FROM student_has_class1 WHERE student_student_no='"+id+"' AND  class1_class_no='"+classno+"'");
if(rs.next()){
JOptionPane.showMessageDialog(this, "Already have enrolled ", "Success", JOptionPane.WARNING_MESSAGE);
}else{
    Mysql.iud("INSERT INTO student_has_class1 (student_student_no,class1_class_no)VALUES('"+id+"','"+classno+"')");
JOptionPane.showMessageDialog(this, "Successfully subject enrolled", "Success", JOptionPane.WARNING_MESSAGE);
}

// TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
 java.util.Date date1=new java.util.Date();
                String id=jTextField5.getText();
                java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd ");
                String date2=df.format(date1);
                System.out.println(date2);
String class1=jComboBox4.getSelectedItem().toString();
int schno=classMap.get(class1);
        try {
            Mysql.iud("INSERT INTO attendence (student_has_class1_shcno,date,attendence)VALUES('"+schno+"','"+date2+"','present') ");
            JOptionPane.showMessageDialog(this, "Successfully Attendence marked", "Success", JOptionPane.WARNING_MESSAGE);
            loadattendence(id);
        } catch (Exception e) {
            
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed

        String teacher=jComboBox6.getSelectedItem().toString();
        String subject=jComboBox5.getSelectedItem().toString();
        String time=jTextField10.getText();
        int teacherid=teacherMap.get(teacher);
        int subjectid=subjectMap.get(subject);
        
        
        try {
         Mysql.iud("INSERT INTO class (`teacher_teacher_no`,`subject_subject_id`,`timeslot`)VAlUES('"+teacherid+"','"+subjectid+"','"+time+"')");  
         JOptionPane.showMessageDialog(this, "Successfully subject enrolled", "Success", JOptionPane.WARNING_MESSAGE);
         loadClassTable();
         resetClass();
        } catch (Exception e) {
        }
        
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
HashMap<String,Object> parameters=new HashMap<>();

parameters.put("Parameter1",jTextField34.getText() );

       
try {
    
//             JRTableModelDataSource  datasource=new JRTableModelDataSource(jTable1.getModel());
             JRTableModelDataSource datasource=new JRTableModelDataSource(jTable7.getModel());
              
             JasperViewer.viewReport(JasperFillManager.fillReport("src/reports/attendence1.jasper", parameters, datasource),false);
             System.out.println(parameters);
             System.out.println(datasource);
             
            
        } catch (Exception e) {
            System.err.println(e);
        }

        try {
           
        } catch (Exception e) {
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        JFileChooser chooser=new JFileChooser();
        chooser.showOpenDialog(null);
        File f=chooser.getSelectedFile(); 
        String path=f.getAbsolutePath();

        try {
            BufferedImage b1=ImageIO.read(new File(path));
            Image img=b1.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
            ImageIcon icon=new ImageIcon(img);
            jLabel3.setIcon(icon);
            path2=path;

            // TODO add your handling code here:
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

     
        String fname=jTextField26.getText();
        String address=jTextField29.getText();
String date1=String.valueOf(jDateChooser1.getDate());
      String icon=String.valueOf(jLabel3.getIcon());   
System.out.println(date1);
System.out.println(icon);

        if(fname.isEmpty()){
            System.out.println("Invalid First name");
            JOptionPane.showMessageDialog(this, "Empty user name", "warning", JOptionPane.WARNING_MESSAGE);
            jTextField26.grabFocus();
        }else if(address.isEmpty()){
            JOptionPane.showMessageDialog(this, "Empty Contact", "warning", JOptionPane.WARNING_MESSAGE);
            jTextField29.grabFocus();
        }else if(date1=="null"){
        JOptionPane.showMessageDialog(this, "Empty Date", "warning", JOptionPane.WARNING_MESSAGE);
            jTextField29.grabFocus();
        }else if(icon=="null"){
            JOptionPane.showMessageDialog(this, "Empty Image", "warning", JOptionPane.WARNING_MESSAGE);
            jTextField31.grabFocus();
        }
        else{
            String url = "jdbc:mysql://localhost:3306/adyapana";
            String username = "root";
            String password = "200301403251A";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sql = "INSERT INTO student (student_name,dob,address,image) VALUES ( ?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(sql);

                String value1 = jTextField26.getText();  // Retrieve value from text field

                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String date=sdf.format(jDateChooser1.getDate());
                InputStream is=new FileInputStream(new File(path2));

                
                statement.setString(1, value1);
                statement.setString(2, date);
                statement.setString(3, address);
                statement.setBlob(4, is);
                // Set other parameter values as needed

                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student Registered Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            loadStudent();
            studentreset();

        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int r=jTable2.getSelectedRow();
        String id=(jTable2.getModel().getValueAt(r, 0).toString());

       

        try {
            ResultSet rs=Mysql.search("SELECT * FROM `student` WHERE `Sno`='"+id+"'");

            if(rs.next()){
                String stid=Integer.toString(rs.getInt("Sno"));
                String name=rs.getString("student_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                byte[]imageData =rs.getBytes("image");
                ImageIcon format = new ImageIcon(imageData);
                Image mm=format.getImage();
                Image img2=mm.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(img2);

                jLabel3.setIcon(image);

                jTextField26.setText(rs.getString("student_name"));
               
                jDateChooser1.setDate(dob);
                jTextField29.setText(address);
            }

        } catch (Exception e) {
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed

      java.util.Date date=new java.util.Date();
        SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
        
      String date1=  formate.format(date);
HashMap<String,Object> parameters=new HashMap<>();

parameters.put("Parameter1",jTextField36.getText() );
parameters.put("Parameter2",jTextField37.getText() );
parameters.put("Parameter3",date1 );
parameters.put("Parameter4",jLabel65.getText() );
parameters.put("Parameter5",jTextField39.getText() );
parameters.put("Parameter6",jLabel68.getText() );
parameters.put("Parameter7",jLabel65.getText() );
parameters.put("Parameter8",jLabel70.getText() );

try {
     Mysql.iud("INSERT  INTO invoice (in_id,student_Sno,total,date) VALUES('"+jLabel70.getText()+"','"+jTextField6.getText()+"','"+jLabel65.getText()+"','"+date1+"')");
            
            
          int row=  jTable5.getRowCount();
          int i;
          for(i=0;i<row;i++){
            String classId=String.valueOf(jTable5.getValueAt(i,0 ))  ;
             String month=String.valueOf(jTable5.getValueAt(i,3 )) ;
            String month_id=String.valueOf(monthMap.get(month)) ;
            Mysql.iud("INSERT INTO invoice_item (class_class_no,month_id,invoice_in_id) VALUES('"+classId+"','"+month_id+"','"+jLabel70.getText()+"')");
          }
//             JRTableModelDataSource  datasource=new JRTableModelDataSource(jTable1.getModel());
             JRTableModelDataSource datasource=new JRTableModelDataSource(jTable5.getModel());
             JasperViewer.viewReport(JasperFillManager.fillReport("src/reports/adyapana.jasper", parameters, datasource),false);
             System.out.println(parameters);
             
            
        } catch (Exception e) {
        }
        try {
           
        } catch (Exception e) {
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
String student_id=jTextField6.getText();
String class1=jComboBox8.getSelectedItem().toString();

        String month=jComboBox9.getSelectedItem().toString();
        int month_id=monthMap.get(month);
int class_id=classMap.get(class1);
        Class1 class2=classMap2.get(String.valueOf(class_id));
System.out.println(class2);
              Vector<String> v=new Vector<>();
v.add(String.valueOf(class_id));
v.add(class2.getSubject());
v.add(class2.getTeacher());
v.add(month);
v.add(class2.getPrice());
DefaultTableModel m=(DefaultTableModel)jTable5.getModel();
m.addRow(v);

calculateTotal();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jComboBox8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox8MouseClicked

        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8MouseClicked

    private void jComboBox9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox9MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox9MouseClicked

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jTextField39KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField39KeyReleased
String total=jLabel65.getText();
String cashpay=jTextField39.getText();
double balance=Double.parseDouble(cashpay)-Double.parseDouble(total);
jLabel68.setText(String.valueOf(balance));

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField39KeyReleased

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
 
        String sname=jTextField21.getText();
        String sid=jTextField23.getText();
        String price=jTextField22.getText();

        if(sname.isEmpty()){
            JOptionPane.showMessageDialog(this,"Empty subject Name","Warning",JOptionPane.WARNING_MESSAGE);
        jTextField21.grabFocus();
        }else if(price.isEmpty()){
            JOptionPane.showMessageDialog(this,"Empty price","Warning",JOptionPane.WARNING_MESSAGE);
        jTextField22.grabFocus();
        }else{
        try  {
            Mysql.iud("UPDATE subject SET subject_name='"+sname+"',price='"+price+"' WHERE Subno='"+sid+"' ") ;
JOptionPane.showMessageDialog(this,"Successfully subject updated","success",JOptionPane.WARNING_MESSAGE);
jTextField21.setText("");
jTextField22.setText("");
jTextField23.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadSubjecttable();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton27ActionPerformed
    
    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed


        String sname=jTextField21.getText();
        String sid=jTextField23.getText();
        String price=jTextField22.getText();

        try  {
            Mysql.iud("DELETE FROM subject WHERE Subno='"+sid+"' ") ;
JOptionPane.showMessageDialog(this,"Successfully subject DELETED","success",JOptionPane.WARNING_MESSAGE);
jTextField21.setText("");
jTextField22.setText("");
jTextField23.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadSubjecttable();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

int row=jTable1.getSelectedRow();

String sid=String.valueOf(jTable1.getValueAt(row, 0));
String sname=String.valueOf(jTable1.getValueAt(row, 1));
String sprice=String.valueOf(jTable1.getValueAt(row, 2));


jTextField21.setText(sname);
jTextField22.setText(sprice);
jTextField23.setText(sid);
loadSubjecttable();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
String subject=jComboBox3.getSelectedItem().toString();

String sub_id=String.valueOf(subjectMap.get(subject));
        
        loadClassSuubjectcombo(sub_id);
        

        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
String id=jTextField5.getText();
loadclassstudent(id);
        try {
            ResultSet rs=Mysql.search("SELECT * FROM class INNER JOIN student_has_class1 ON student_has_class1.class1_class_no=class.Classno INNER JOIN student ON student_has_class1.student_student_no=student.Sno INNER JOIN teacher ON teacher.Tno=class.teacher_teacher_no INNER JOIN subject ON subject.Subno=class.subject_subject_id WHERE Sno='"+id+"'");
        
            Vector v1=new Vector();
         
         if(rs.next()){
                String stid=Integer.toString(rs.getInt("Sno"));
                String name=rs.getString("student_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");
                

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                
                   
        
      
      
          
      
        
                
                byte[]imageData =rs.getBytes("image");
                ImageIcon format = new ImageIcon(imageData);
                Image mm=format.getImage();
                Image img2=mm.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(img2);

                jLabel44.setIcon(image);

                jTextField34.setText(rs.getString("student_name"));
                jTextField5.setText(stid);
                jDateChooser4.setDate(dob);
                jTextField35.setText(address);
               
                jTextField34.setEditable(false);
               
                
                jTextField35.setEditable(false);
            }
            
        } catch (Exception e) {
        }


        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
String id=jTextField4.getText();

        try {
            ResultSet rs=Mysql.search("SELECT * FROM student WHERE Sno='"+id+"'");
             if(rs.next()){
                String stid=Integer.toString(rs.getInt("Sno"));
                String name=rs.getString("student_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                byte[]imageData =rs.getBytes("image");
                ImageIcon format = new ImageIcon(imageData);
                Image mm=format.getImage();
                Image img2=mm.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(img2);

                jLabel37.setIcon(image);

                jTextField32.setText(rs.getString("student_name"));
                jTextField4.setText(stid);
                jDateChooser3.setDate(dob);
                jTextField33.setText(address);
                
                jTextField32.setEditable(false);
                
                
                jTextField33.setEditable(false);
            }
            
        } catch (Exception e) {
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
String id=jTextField6.getText();

String invoiceid="#" + Math.random();
jLabel70.setText(invoiceid);
        loadclassstudent(id);
        try {
            ResultSet rs=Mysql.search("SELECT * FROM class INNER JOIN student_has_class1 ON student_has_class1.class1_class_no=class.Classno INNER JOIN student ON student_has_class1.student_student_no=student.Sno INNER JOIN teacher ON teacher.Tno=class.teacher_teacher_no INNER JOIN subject ON subject.Subno=class.subject_subject_id WHERE Sno='"+id+"'");
        
            if(rs.next()){
                String stid=Integer.toString(rs.getInt("Sno"));
                String name=rs.getString("student_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");
                

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                
            
         
      
                
                byte[]imageData =rs.getBytes("image");
                ImageIcon format = new ImageIcon(imageData);
                Image mm=format.getImage();
                Image img2=mm.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(img2);

                jLabel56.setIcon(image);

                jTextField36.setText(rs.getString("student_name"));
                jTextField6.setText(stid);
                jDateChooser5.setDate(dob);
                jTextField37.setText(address);
               
                 jTextField36.setEditable(false);
                
                jTextField37.setEditable(false);
            }
            
        } catch (Exception e) {
        } 
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jComboBox8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox8ItemStateChanged
String class1=jComboBox8.getSelectedItem().toString();

int class_id=classMap.get(class1);


        try {
            ResultSet rs=Mysql.search("SELECT * FROM class INNER JOIN subject ON class.subject_subject_id=subject.Subno WHERE Classno='"+class_id+"'");
            if(rs.next()){
              jLabel62.setText(String.valueOf(rs.getDouble("subject.price")));   
            }
           
        } catch (Exception e) {
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8ItemStateChanged

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
int row=jTable6.getSelectedRow();


       

String classid= String.valueOf(jTable6.getValueAt(row, 0));
 String teacher=jComboBox6.getSelectedItem().toString();
        String subject=jComboBox5.getSelectedItem().toString();
        String time=jTextField10.getText();
        int teacherid=teacherMap.get(teacher);
        int subjectid=subjectMap.get(subject);
        
        
        try {
         Mysql.iud("UPDATE class SET timeslot='"+time+"' WHERE Classno='"+classid+"'");  
         JOptionPane.showMessageDialog(this, "Successfully Class Updated", "Success", JOptionPane.WARNING_MESSAGE);
         loadClassTable();
         
         resetClass();
        } catch (Exception e) {
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable6MouseClicked
int row=jTable6.getSelectedRow();
String techer= String.valueOf(jTable6.getValueAt(row, 1));
String subject= String.valueOf(jTable6.getValueAt(row, 2));
String time= String.valueOf(jTable6.getValueAt(row, 3));

jComboBox6.setSelectedItem(techer);
jComboBox5.setSelectedItem(subject);
jTextField10.setText(time);
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable6MouseClicked

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed

int row=jTable6.getSelectedRow();
String classid= String.valueOf(jTable6.getValueAt(row, 0));
 String teacher=jComboBox6.getSelectedItem().toString();
        String subject=jComboBox5.getSelectedItem().toString();
        String time=jTextField10.getText();
        int teacherid=teacherMap.get(teacher);
        int subjectid=subjectMap.get(subject);
        
        
        try {
         Mysql.iud("DELETE FROM class WHERE Classno='"+classid+"'");  
         JOptionPane.showMessageDialog(this, "Successfully class DELETED", "Success", JOptionPane.WARNING_MESSAGE);
         loadClassTable();
         resetClass();
        } catch (Exception e) {
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed

String id=jTextField4.getText();
String time=jComboBox7.getSelectedItem().toString();
String subject=jComboBox3.getSelectedItem().toString();
int subjectid=subjectMap.get(subject);



// Mysql.iud("INSERT INTO student_has_subject (subject_subject_id,student_student_no)VALUES('"+subjectid+"','"+id+"')");
 Mysql.iud("INSERT INTO student_has_subject (student_student_no,subject_subject_id)VALUES('"+id+"','"+subjectid+"')");
 JOptionPane.showMessageDialog(this, "Successfully subject enrolled", "Success", JOptionPane.WARNING_MESSAGE);
 loadSubjecttable();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        String id=jTextField1.getText();
        
        loadStudent1(id);

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased

String id=jTextField2.getText();
        
        loadSubject1(id);
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased

String id=jTextField3.getText();
        
        loadTeachers1(id);
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased

        String id=jTextField7.getText();
        
        loadattendence(id);

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed


        
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField6InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField6InputMethodTextChanged
       

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6InputMethodTextChanged

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped

        
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6KeyTyped

    private void jTextField6PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextField6PropertyChange

        System.out.println("gui.Home.jTextField6PropertyChange()");
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6PropertyChange

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
String id=jTextField6.getText();

String invoiceid="#" + Math.random();
jLabel70.setText(invoiceid);
        loadclassstudent(id);
        try {
            ResultSet rs=Mysql.search("SELECT * FROM class INNER JOIN student_has_class1 ON student_has_class1.class1_class_no=class.Classno INNER JOIN student ON student_has_class1.student_student_no=student.Sno INNER JOIN teacher ON teacher.Tno=class.teacher_teacher_no INNER JOIN subject ON subject.Subno=class.subject_subject_id WHERE Sno='"+id+"'");
        
            if(rs.next()){
                String stid=Integer.toString(rs.getInt("Sno"));
                String name=rs.getString("student_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");
                

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                
            
         
      
                
                byte[]imageData =rs.getBytes("image");
                ImageIcon format = new ImageIcon(imageData);
                Image mm=format.getImage();
                Image img2=mm.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(img2);

                jLabel56.setIcon(image);

                jTextField36.setText(rs.getString("student_name"));
                jTextField6.setText(stid);
                jDateChooser5.setDate(dob);
                jTextField37.setText(address);
               
                 jTextField36.setEditable(false);
                
                jTextField37.setEditable(false);
            }
            
        } catch (Exception e) {
        } 


        // TODO add your handling code here:
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
jTabbedPane1.setSelectedIndex(6);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed

 jTabbedPane1.setSelectedIndex(4);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
 jTabbedPane1.setSelectedIndex(1); 


        // TODO add your handling code here:
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed

 jTabbedPane1.setSelectedIndex(5);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jTable8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable8MouseClicked
   int r=jTable8.getSelectedRow();
        String id=(jTable8.getModel().getValueAt(r, 0).toString());

       

        try {
            ResultSet rs=Mysql.search("SELECT * FROM `student` WHERE `Sno`='"+id+"'");

            if(rs.next()){
                String stid=Integer.toString(rs.getInt("Sno"));
                String name=rs.getString("student_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                byte[]imageData =rs.getBytes("image");
                ImageIcon format = new ImageIcon(imageData);
                Image mm=format.getImage();
                Image img2=mm.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(img2);

                jLabel3.setIcon(image);

                jTextField27.setText(rs.getString("student_name"));
               jTextField8.setText(id);
                jDateChooser6.setDate(dob);
                jTextField38.setText(address);
            }

        } catch (Exception e) {
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jTable8MouseClicked

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
   int r=jTable8.getSelectedRow();
        String id=(jTable8.getModel().getValueAt(r, 0).toString());
       String name= jTextField27.getText();
HashMap<String,Object> parameters=new HashMap<>();

parameters.put("Parameter1", id);
parameters.put("Parameter2",name );

        try {
//            JasperDesign jdesign=JRXmlLoader.load("C:\\Users\\sange\\OneDrive\\Documents\\NetBeansProjects\\final\\src\\reports\\Blank_A4.jrxml");
//            JasperReport jreport=JasperCompileManager.compileReport(jdesign);
//            JasperPrint jprint=JasperFillManager.fillReport(jreport, parameters);
//            JasperViewer.viewReport(jprint);
            JRDataSource datasource=new JREmptyDataSource();
 JasperViewer.viewReport(JasperFillManager.fillReport("src/reports/employee.jasper",parameters,datasource),false);
            // TODO add your handling code here:
        } catch (JRException ex) {
            Logger.getLogger(Menu1.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8KeyReleased

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jTabbedPane2.setSelectedIndex(2);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTabbedPane2.setSelectedIndex(3);

        loadTeachers();// TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane2.setSelectedIndex(1);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jTextField9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9KeyReleased

    private void jComboBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox10ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11KeyReleased

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jTable10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable10MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable10MouseClicked

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton51ActionPerformed

    private void jButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton52ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton52ActionPerformed

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void jTextField12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12KeyReleased

    private void jTextField13KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField13KeyReleased

    private void jButton53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton53ActionPerformed

        
        String id= jTextField13.getText();
         String name= jTextField43.getText();
          String address= jTextField44.getText();
           String mobile= jTextField52.getText();
             
           String job= jComboBox12.getSelectedItem().toString();
           int job_id=jobmap.get(job);
           System.out.println(job_id);
           
           try {
               Mysql.iud("INSERT INTO  employee (`user_id`,`name`,`mobile`,`address`) VALUES('"+id+"','"+name+"','"+mobile+"','"+address+"')");
            Mysql.iud("INSERT INTO  employee_has_job (`employee_user_name`,`job_id`) VALUES('"+id+"','"+job_id+"')");
            loademployee();
        } catch (Exception e) {
            e.printStackTrace();
        }
           


// TODO add your handling code here:
    }//GEN-LAST:event_jButton53ActionPerformed

    private void jComboBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox12ActionPerformed

    private void jTextField46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField46ActionPerformed

    private void jTable13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable13MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable13MouseClicked

    private void jButton58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton58ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton58ActionPerformed

    private void jButton59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton59ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton59ActionPerformed

    private void jButton60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton60ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton60ActionPerformed

    private void jTextField48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField48ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField48ActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jTextField15KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15KeyReleased

    private void jTextField16InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField16InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16InputMethodTextChanged

    private void jTextField16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16ActionPerformed

    private void jTextField16PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextField16PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16PropertyChange

    private void jTextField16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16KeyReleased

    private void jTextField16KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16KeyTyped

    private void jComboBox13ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox13ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox13ItemStateChanged

    private void jComboBox13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox13MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox13MouseClicked

    private void jComboBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox13ActionPerformed

    private void jButton61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton61ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton61ActionPerformed

    private void jButton62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton62ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton62ActionPerformed

    private void jComboBox14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox14MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox14MouseClicked

    private void jComboBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox14ActionPerformed

    private void jButton63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton63ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton63ActionPerformed

    private void jButton54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton54ActionPerformed

 jTabbedPane2.setSelectedIndex(11);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton54ActionPerformed

    private void jButton64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton64ActionPerformed
jTabbedPane2.setSelectedIndex(14);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton64ActionPerformed

    private void jButton65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton65ActionPerformed

jTabbedPane2.setSelectedIndex(15);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton65ActionPerformed

    private void jTextField17KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyReleased
String id=jTextField17.getText();    
        try {
           ResultSet rs= Mysql.search("SELECT * FROM employee ");
           if(rs.next()){
           jTextField53.setText(rs.getString("name"));
           jTextField55.setText(rs.getString("mobile"));
           jTextField54.setText(rs.getString("address"));
           }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jTextField17KeyReleased

    private void jButton67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton67ActionPerformed
 String id= jTextField17.getText();
         String name= jTextField53.getText();
          String address= jTextField54.getText();
           String mobile= jTextField55.getText();
             
           String job= jComboBox12.getSelectedItem().toString();
           int job_id=jobmap.get(job);
           System.out.println(job_id);
           
           try {
               Mysql.iud("UPDATE `employee` SET `name`='"+name+"', `address`='"+address+"', `mobile`='"+mobile+"' WHERE `user_id`='"+id+"'");
            Mysql.iud("UPDATE  `employee_has_job` SET `job_id`='"+job_id+"' WHERE employee_user_name='"+id+"'");
            loademployee();
        } catch (Exception e) {
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton67ActionPerformed

    private void jComboBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox15ActionPerformed

    private void jTable16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable16MouseClicked
 int r=jTable16.getSelectedRow();
        String id=(jTable16.getModel().getValueAt(r, 0).toString());  
        
        try {
            ResultSet rs=Mysql.search("SELECT * FROM employee INNER JOIN employee_has_job ON employee.user_id=employee_has_job.employee_user_name INNER JOIN job ON employee_has_job.job_id=job.id WHERE user_id='"+id+"'");
            
            if(rs.next()){
            jTextField17.setText(rs.getString("user_id"));
            jTextField53.setText(rs.getString("employee.name"));
            jTextField54.setText(rs.getString("address"));
            jTextField55.setText(rs.getString("mobile"));
            
            int job=jobmap.get(rs.getString("job.name"));
            jComboBox15.setSelectedItem(rs.getString("job.name"));
            }
            
        } catch (Exception e) {
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jTable16MouseClicked

    private void jTextField18KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField18KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField18KeyReleased

    private void jButton68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton68ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton68ActionPerformed

    private void jComboBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox16ActionPerformed

    private void jTable17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable17MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable17MouseClicked

    private void jTextField19KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField19KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19KeyReleased

    private void jButton69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton69ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton69ActionPerformed

    private void jComboBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox17ActionPerformed

    private void jTable18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable18MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable18MouseClicked

    private void jButton70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton70ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton70ActionPerformed

    private void jTextField20KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField20KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField20KeyReleased

    private void jButton71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton71ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton71ActionPerformed

    private void jComboBox18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox18ActionPerformed

    private void jTable19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable19MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable19MouseClicked

    private void jButton72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton72ActionPerformed
  jTabbedPane2.setSelectedIndex(18);       // TODO add your handling code here:
    }//GEN-LAST:event_jButton72ActionPerformed

    private void jButton73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton73ActionPerformed

jTabbedPane2.setSelectedIndex(19);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton73ActionPerformed

    private void jButton74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton74ActionPerformed
jTabbedPane2.setSelectedIndex(20);         // TODO add your handling code here:
    }//GEN-LAST:event_jButton74ActionPerformed

    private void jButton75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton75ActionPerformed
   jTabbedPane2.setSelectedIndex(21);      // TODO add your handling code here:
    }//GEN-LAST:event_jButton75ActionPerformed

    private void jTable11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable11MouseClicked
int r=jTable11.getSelectedRow();
        String id=(jTable11.getModel().getValueAt(r, 0).toString());

       

        try {
            ResultSet rs=Mysql.search("SELECT * FROM `employee` WHERE `user_id`='"+id+"'");

            if(rs.next()){
               
                String address=rs.getString("address");
             

                

                jTextField65.setText(rs.getString("name"));
               
                
                jTextField66.setText(address);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        

        // TODO add your handling code here:
    }//GEN-LAST:event_jTable11MouseClicked

    private void jButton76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton76ActionPerformed
 int r=jTable11.getSelectedRow();
        String id=(jTable11.getModel().getValueAt(r, 0).toString());
       String name= jTextField65.getText();
HashMap<String,Object> parameters=new HashMap<>();

parameters.put("Parameter1", id);
parameters.put("Parameter2",name );

        try {
//            JasperDesign jdesign=JRXmlLoader.load("C:\\Users\\sange\\OneDrive\\Documents\\NetBeansProjects\\final\\src\\reports\\Blank_A4.jrxml");
//            JasperReport jreport=JasperCompileManager.compileReport(jdesign);
//            JasperPrint jprint=JasperFillManager.fillReport(jreport, parameters);
//            JasperViewer.viewReport(jprint);
            JRDataSource datasource=new JREmptyDataSource();
 JasperViewer.viewReport(JasperFillManager.fillReport("src/reports/employee1.jasper",parameters,datasource),false);
            // TODO add your handling code here:
        } catch (JRException ex) {
            Logger.getLogger(Menu1.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton76ActionPerformed

    private void jTextField67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField67ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField67ActionPerformed

    private void jTextField67KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField67KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField67KeyReleased

    private void jTextField68KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField68KeyReleased
String id=jTextField68.getText();


        try {
            ResultSet rs=Mysql.search("SELECT * FROM employee INNER JOIN employee_has_job ON employee_has_job.employee_user_name=employee.user_id INNER JOIN job ON job.id=employee_has_job.job_id WHERE employee.user_id='"+id+"'");
            if(rs.next()){
                String stid=Integer.toString(rs.getInt("user_id"));
                String name=rs.getString("name");
                

                jTextField69.setText(rs.getString("name"));
                
                
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField68KeyReleased

    private void jButton79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton79ActionPerformed
java.util.Date date1=new java.util.Date();
                String id=jTextField68.getText();
                java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd ");
                String date2=df.format(date1);
                System.out.println(date2);

        try {
            Mysql.iud("INSERT INTO eattendence (date,attendence)VALUES('"+date2+"','present') ");
            JOptionPane.showMessageDialog(this, "Successfully Attendence marked", "Success", JOptionPane.WARNING_MESSAGE);
            loadattendence(id);
        } catch (Exception e) {
            System.err.println(e);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton79ActionPerformed

    private void jButton80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton80ActionPerformed
HashMap<String,Object> parameters=new HashMap<>();

parameters.put("Parameter1","kasun" );

       
try {
    
//             JRTableModelDataSource  datasource=new JRTableModelDataSource(jTable1.getModel());
             JRTableModelDataSource datasource=new JRTableModelDataSource(jTable20.getModel());
              
             JasperViewer.viewReport(JasperFillManager.fillReport("src/reports/empattendence1.jasper", parameters, datasource),false);
             System.out.println(parameters);
             System.out.println(datasource);
             
            
        } catch (Exception e) {
            System.err.println(e);
        }

        try {
           
        } catch (Exception e) {
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton80ActionPerformed

    private void jTextField71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField71ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField71ActionPerformed

    private void jTextField71KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField71KeyReleased
 String id=jTextField71.getText();
        
        loadattendence2(id);         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField71KeyReleased

    private void jButton81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton81ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton81ActionPerformed

    private void jTextField70InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField70InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField70InputMethodTextChanged

    private void jTextField70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField70ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField70ActionPerformed

    private void jTextField70PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextField70PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField70PropertyChange

    private void jTextField70KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField70KeyReleased
String id=jTextField70.getText();

String invoiceid="#" + Math.random();
jLabel217.setText(invoiceid);
        loadclassstudent(id);
        try {
            ResultSet rs=Mysql.search("SELECT * FROM employee INNER JOIN employee_has_job ON employee_has_job.employee_user_name=employee.user_id INNER JOIN job ON job.id=employee_has_job.job_id WHERE employee.user_id='"+id+"'");
            if(rs.next()){
                String stid=Integer.toString(rs.getInt("user_id"));
                String name=rs.getString("name");
                String address=rs.getString("address");
                String mobile=rs.getString("mobile");

                jTextField72.setText(rs.getString("name"));
                jTextField70.setText(stid);
                
                jTextField73.setText(address);
               jLabel210.setText(rs.getString("job.name"));
               jLabel316.setText(rs.getString("job.Salary"));
                 jTextField36.setEditable(false);
                
                jTextField37.setEditable(false);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField70KeyReleased

    private void jTextField70KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField70KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField70KeyTyped

    private void jButton82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton82ActionPerformed
 
       String name= jTextField72.getText();
       String role= jLabel210.getText();
       String month=jComboBox20.getSelectedItem().toString();
       String salary= jLabel316.getText();
HashMap<String,Object> parameters=new HashMap<>();

parameters.put("Parameter1", name);
parameters.put("Parameter2",role );
parameters.put("Parameter3",month );
parameters.put("Parameter4",salary );

        try {
//            JasperDesign jdesign=JRXmlLoader.load("C:\\Users\\sange\\OneDrive\\Documents\\NetBeansProjects\\final\\src\\reports\\Blank_A4.jrxml");
//            JasperReport jreport=JasperCompileManager.compileReport(jdesign);
//            JasperPrint jprint=JasperFillManager.fillReport(jreport, parameters);
//            JasperViewer.viewReport(jprint);
            JRDataSource datasource=new JREmptyDataSource();
 JasperViewer.viewReport(JasperFillManager.fillReport("src/reports/salary.jasper",parameters,datasource),false);
            // TODO add your handling code here:
        } catch (JRException ex) {
            Logger.getLogger(Menu1.class.getName()).log(Level.SEVERE, null, ex);
        }            // TODO add your handling code here:
    }//GEN-LAST:event_jButton82ActionPerformed

    private void jButton83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton83ActionPerformed
String student_id=jTextField70.getText();
String name=jTextField72.getText();
String salary=jLabel316.getText();
String role=jLabel210.getText();


        String month=jComboBox20.getSelectedItem().toString();
        int month_id=monthMap.get(month);

        

              Vector<String> v=new Vector<>();
v.add(String.valueOf(student_id));
v.add(role);
v.add(name);
v.add(month);
v.add(salary);
DefaultTableModel m=(DefaultTableModel)jTable21.getModel();
m.addRow(v);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton83ActionPerformed

    private void jComboBox20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox20MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox20MouseClicked

    private void jComboBox20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox20ActionPerformed

    private void jButton84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton84ActionPerformed
String id=jTextField70.getText();

String invoiceid="#" + Math.random();
jLabel217.setText(invoiceid);
        loadclassstudent(id);
        try {
            ResultSet rs=Mysql.search("SELECT * FROM employee INNER JOIN employee_has_job ON employee_has_job.employee_user_name=employee.user_id INNER JOIN job ON job.id=employee_has_job.job_id WHERE employee.user_id='"+id+"'");
            if(rs.next()){
                String stid=Integer.toString(rs.getInt("user_id"));
                String name=rs.getString("name");
                String address=rs.getString("address");
                String mobile=rs.getString("mobile");

                jTextField72.setText(rs.getString("name"));
                jTextField70.setText(stid);
                
                jTextField73.setText(address);
               jLabel210.setText(rs.getString("job.name"));
               jLabel316.setText(rs.getString("job.Salary"));
                 jTextField36.setEditable(false);
                
                jTextField37.setEditable(false);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }            // TODO add your handling code here:
    }//GEN-LAST:event_jButton84ActionPerformed

    private void jTextField75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField75ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField75ActionPerformed

    private void jTable22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable22MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable22MouseClicked

    private void jButton85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton85ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton85ActionPerformed

    private void jTextField77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField77ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField77ActionPerformed

    private void jTextField78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField78ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField78ActionPerformed

    private void jTextField78KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField78KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField78KeyReleased

    private void jButton88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton88ActionPerformed

   jTabbedPane2.setSelectedIndex(1);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton88ActionPerformed

    private void jButton90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton90ActionPerformed
jTabbedPane2.setSelectedIndex(31);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton90ActionPerformed

    private void jButton91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton91ActionPerformed
jTabbedPane2.setSelectedIndex(7);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton91ActionPerformed

    private void jButton92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton92ActionPerformed
jTabbedPane2.setSelectedIndex(5);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton92ActionPerformed

    private void jButton93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton93ActionPerformed
jTabbedPane2.setSelectedIndex(8);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton93ActionPerformed

    private void jButton94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton94ActionPerformed
jTabbedPane2.setSelectedIndex(24);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton94ActionPerformed

    private void jButton95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton95ActionPerformed
jTabbedPane2.setSelectedIndex(4);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton95ActionPerformed

    private void jButton96ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton96ActionPerformed
jTabbedPane2.setSelectedIndex(23);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton96ActionPerformed

    private void jButton97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton97ActionPerformed

 jTabbedPane2.setSelectedIndex(22);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton97ActionPerformed

    private void jTable23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable23MouseClicked

int r=jTable23.getSelectedRow();
        String id=(jTable23.getModel().getValueAt(r, 0).toString());

       

        try {
            ResultSet rs=Mysql.search("SELECT * FROM `student` WHERE `Sno`='"+id+"'");

            if(rs.next()){
                String stid=Integer.toString(rs.getInt("Sno"));
                String name=rs.getString("student_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                byte[]imageData =rs.getBytes("image");
                ImageIcon format = new ImageIcon(imageData);
                Image mm=format.getImage();
                Image img2=mm.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(img2);

                jLabel233.setIcon(image);

                jTextField79.setText(rs.getString("student_name"));
               
                jDateChooser11.setDate(dob);
                jTextField80.setText(address);
            }

        } catch (Exception e) {
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jTable23MouseClicked

    private void jButton89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton89ActionPerformed
  JFileChooser chooser=new JFileChooser();
        chooser.showOpenDialog(null);
        File f=chooser.getSelectedFile();
        String path=f.getAbsolutePath();

        try {
            BufferedImage b1=ImageIO.read(new File(path));
            Image img=b1.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
            ImageIcon icon=new ImageIcon(img);
            jLabel233.setIcon(icon);
            path3=path;

            // TODO add your handling code here:
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton89ActionPerformed

    private void jButton98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton98ActionPerformed
int row=jTable23.getSelectedRow();
String id=String.valueOf(jTable23.getValueAt(row, 0));
        
        String fname=jTextField79.getText();
       

        String address = jTextField80.getText();

        
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String date=sdf.format(jDateChooser11.getDate());
                
                
        
        try {
            
            Mysql.iud("UPDATE `student` SET `student_name`='"+fname+"', `address`='"+address+"',`dob`='"+date+"'  WHERE `Sno`='"+id+"'");
            loadStudent();
            studentreset();
        } catch (Exception e) {
            System.err.println(e);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton98ActionPerformed

    private void jTextField81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField81ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField81ActionPerformed

    private void jTextField81KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField81KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField81KeyReleased

    private void jTable24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable24MouseClicked

int r=jTable24.getSelectedRow();
        String id=(jTable24.getModel().getValueAt(r, 0).toString());

       

        try {
            ResultSet rs=Mysql.search("SELECT * FROM `student` WHERE `Sno`='"+id+"'");

            if(rs.next()){
                String stid=Integer.toString(rs.getInt("Sno"));
                String name=rs.getString("student_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                byte[]imageData =rs.getBytes("image");
                ImageIcon format = new ImageIcon(imageData);
                Image mm=format.getImage();
                Image img2=mm.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(img2);

                jLabel245.setIcon(image);

                jTextField82.setText(rs.getString("student_name"));
               
                jDateChooser12.setDate(dob);
                jTextField83.setText(address);
            }

        } catch (Exception e) {
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable24MouseClicked

    private void jButton101ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton101ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton101ActionPerformed

    private void jButton103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton103ActionPerformed
 int row=jTable24.getSelectedRow();
String id=String.valueOf(jTable24.getValueAt(row, 0));

        

        try {
            Mysql.iud("DELETE FROM `student` WHERE `Sno`='"+id+"'");
            loadStudent();
            studentreset();
        } catch (Exception e) {
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton103ActionPerformed

    private void jTextField84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField84ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField84ActionPerformed

    private void jTextField84KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField84KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField84KeyReleased

    private void jTextField85KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField85KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField85KeyReleased

    private void jButton104ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton104ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton104ActionPerformed

    private void jComboBox21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox21ActionPerformed

    private void jTable25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable25MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable25MouseClicked

    private void jButton105ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton105ActionPerformed

 jTabbedPane2.setSelectedIndex(3);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton105ActionPerformed

    private void jButton106ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton106ActionPerformed
jTabbedPane2.setSelectedIndex(32);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton106ActionPerformed

    private void jButton107ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton107ActionPerformed
jTabbedPane2.setSelectedIndex(33);      // TODO add your handling code here:
    }//GEN-LAST:event_jButton107ActionPerformed

    private void jButton108ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton108ActionPerformed
jTabbedPane2.setSelectedIndex(32);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton108ActionPerformed

    private void jButton109ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton109ActionPerformed
jTabbedPane2.setSelectedIndex(29);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton109ActionPerformed

    private void jButton110ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton110ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton110ActionPerformed

    private void jButton112ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton112ActionPerformed
   jTabbedPane2.setSelectedIndex(26);     // TODO add your handling code here:
    }//GEN-LAST:event_jButton112ActionPerformed

    private void jButton113ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton113ActionPerformed
 jTabbedPane2.setSelectedIndex(25);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton113ActionPerformed

    private void jTable26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable26MouseClicked
int r=jTable26.getSelectedRow();
String id=(jTable26.getModel().getValueAt(r, 0).toString());

        try {
        ResultSet rs=    Mysql.search("SELECT * FROM teacher WHERE Tno='"+id+"'");
            if(rs.next()){
            String teacher_name=rs.getString("teacher_name");
            Date date_birth=rs.getDate("dob");
            String address=rs.getString("address");
            
            
            jTextField89.setText(teacher_name);
            jTextField90.setText(address);
            jDateChooser13.setDate(date_birth);
            }
        } catch (Exception e) {
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable26MouseClicked

    private void jButton114ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton114ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton114ActionPerformed

    private void jButton115ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton115ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton115ActionPerformed

    private void jButton116ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton116ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton116ActionPerformed

    private void jTextField91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField91ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField91ActionPerformed

    private void jTextField91KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField91KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField91KeyReleased

    private void jTable27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable27MouseClicked
int r=jTable27.getSelectedRow();
String id=(jTable27.getModel().getValueAt(r, 0).toString());

        try {
        ResultSet rs=    Mysql.search("SELECT * FROM teacher WHERE Tno='"+id+"'");
            if(rs.next()){
            String teacher_name=rs.getString("teacher_name");
            Date date_birth=rs.getDate("dob");
            String address=rs.getString("address");
            
            
            jTextField92.setText(teacher_name);
            jTextField93.setText(address);
            jDateChooser14.setDate(date_birth);
            }
        } catch (Exception e) {
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable27MouseClicked

    private void jButton117ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton117ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton117ActionPerformed

    private void jButton118ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton118ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton118ActionPerformed

    private void jButton119ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton119ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton119ActionPerformed

    private void jTextField94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField94ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField94ActionPerformed

    private void jTextField94KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField94KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField94KeyReleased

    private void jButton111ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton111ActionPerformed

 jTabbedPane2.setSelectedIndex(6);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton111ActionPerformed

    private void jButton125ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton125ActionPerformed
 jTabbedPane2.setSelectedIndex(28);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton125ActionPerformed

    private void jButton126ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton126ActionPerformed
 jTabbedPane2.setSelectedIndex(27);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton126ActionPerformed

    private void jComboBox22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox22ActionPerformed

    private void jComboBox23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox23ActionPerformed

    private void jTable28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable28MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable28MouseClicked

    private void jButton121ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton121ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton121ActionPerformed

    private void jComboBox24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox24ActionPerformed

    private void jComboBox25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox25ActionPerformed

    private void jTable29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable29MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable29MouseClicked

    private void jButton127ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton127ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton127ActionPerformed

    private void jTable30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable30MouseClicked

        int r=jTable30.getSelectedRow();
String id=(jTable30.getModel().getValueAt(r, 0).toString());

        try {
        ResultSet rs=    Mysql.search("SELECT * FROM teacher WHERE Tno='"+id+"'");
            if(rs.next()){
            String teacher_name=rs.getString("teacher_name");
            Date date_birth=rs.getDate("dob");
            String address=rs.getString("address");
            
            
            jTextField97.setText(teacher_name);
            jTextField98.setText(address);
            jDateChooser15.setDate(date_birth);
            }
        } catch (Exception e) {
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jTable30MouseClicked

    private void jButton99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton99ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton99ActionPerformed

    private void jButton100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton100ActionPerformed
        int r=jTable30.getSelectedRow();
        String id=(jTable30.getModel().getValueAt(r, 0).toString());
       String name= jTextField97.getText();
HashMap<String,Object> parameters=new HashMap<>();

parameters.put("Parameter1", id);
parameters.put("Parameter2",name );

        try {
//            JasperDesign jdesign=JRXmlLoader.load("C:\\Users\\sange\\OneDrive\\Documents\\NetBeansProjects\\final\\src\\reports\\Blank_A4.jrxml");
//            JasperReport jreport=JasperCompileManager.compileReport(jdesign);
//            JasperPrint jprint=JasperFillManager.fillReport(jreport, parameters);
//            JasperViewer.viewReport(jprint);
            JRDataSource datasource=new JREmptyDataSource();
 JasperViewer.viewReport(JasperFillManager.fillReport("src/reports/tattendence.jasper",parameters,datasource),false);
            // TODO add your handling code here:
        } catch (JRException ex) {
            Logger.getLogger(Menu1.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton100ActionPerformed

    private void jButton102ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton102ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton102ActionPerformed

    private void jButton128ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton128ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton128ActionPerformed

    private void jTextField99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField99ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField99ActionPerformed

    private void jTextField99KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField99KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField99KeyReleased

    private void jTextField100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField100ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField100ActionPerformed

    private void jTable31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable31MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable31MouseClicked

    private void jButton120ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton120ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton120ActionPerformed

    private void jTextField101ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField101ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField101ActionPerformed

    private void jTextField102ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField102ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField102ActionPerformed

    private void jTextField102KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField102KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField102KeyReleased

    private void jTextField103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField103ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField103ActionPerformed

    private void jTable32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable32MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable32MouseClicked

    private void jButton122ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton122ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton122ActionPerformed

    private void jTextField104ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField104ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField104ActionPerformed

    private void jTextField105ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField105ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField105ActionPerformed

    private void jTextField105KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField105KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField105KeyReleased

    private void jTextField76KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField76KeyReleased
String id=jTextField76.getText();

    
        try {
            ResultSet rs=Mysql.search("SELECT * FROM teacher WHERE Tno='"+id+"'");
        
            if(rs.next()){
                String stid=Integer.toString(rs.getInt("Tno"));
                String name=rs.getString("teacher_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");
                

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                
            
         
      
              

                jTextField106.setText(rs.getString("teacher_name"));
                jTextField76.setText(stid);
                jDateChooser16.setDate(dob);
                jTextField107.setText(address);
               
                 jTextField36.setEditable(false);
                
                jTextField37.setEditable(false);
            }
            
        } catch (Exception e) {
        } 


        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField76KeyReleased

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed

java.util.Date date1=new java.util.Date();
                String id=jTextField76.getText();
                java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd ");
                String date2=df.format(date1);
                System.out.println(date2);

        try {
            Mysql.iud("INSERT INTO tattendence (date,attendence,teacher_Tno)VALUES('"+date2+"','present','"+id+"') ");
            JOptionPane.showMessageDialog(this, "Successfully Attendence marked", "Success", JOptionPane.WARNING_MESSAGE);
            loadattendence(id);
        } catch (Exception e) {
            System.err.println(e);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
HashMap<String,Object> parameters=new HashMap<>();

parameters.put("Parameter1","Teacher" );

       
try {
    
//             JRTableModelDataSource  datasource=new JRTableModelDataSource(jTable1.getModel());
             JRTableModelDataSource datasource=new JRTableModelDataSource(jTable33.getModel());
              
             JasperViewer.viewReport(JasperFillManager.fillReport("src/reports/teattendence.jasper", parameters, datasource),false);
             System.out.println(parameters);
             System.out.println(datasource);
             
            
        } catch (Exception e) {
            System.err.println(e);
        }

        try {
           
        } catch (Exception e) {
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jTextField108ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField108ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField108ActionPerformed

    private void jTextField108KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField108KeyReleased

        String id=jTextField108.getText();
        
        loadattendence1(id);        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField108KeyReleased

    private void jButton86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton86ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton86ActionPerformed

    private void jTextField109InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField109InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField109InputMethodTextChanged

    private void jTextField109ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField109ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField109ActionPerformed

    private void jTextField109PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextField109PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField109PropertyChange

    private void jTextField109KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField109KeyReleased
String id=jTextField109.getText();
        loadteacherclass(id);
String invoiceid="#" + Math.random();
jLabel334.setText(invoiceid);
        loadclassstudent(id);
        try {
            ResultSet rs=Mysql.search("SELECT * FROM teacher WHERE Tno='"+id+"'");
        
            if(rs.next()){
                String stid=Integer.toString(rs.getInt("Tno"));
                String name=rs.getString("teacher_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");
                

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                
            
         
      
              

                jTextField110.setText(rs.getString("teacher_name"));
                jTextField109.setText(stid);
                jDateChooser17.setDate(dob);
                jTextField111.setText(address);
               
                 jTextField36.setEditable(false);
                
                jTextField37.setEditable(false);
            }
            
        } catch (Exception e) {
        } 

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField109KeyReleased

    private void jTextField109KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField109KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField109KeyTyped

    private void jComboBox27ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox27ItemStateChanged
String class1=jComboBox27.getSelectedItem().toString();

int class_id=classMap.get(class1);


        try {
            ResultSet rs=Mysql.search("SELECT * FROM class INNER JOIN subject ON class.subject_subject_id=subject.Subno WHERE Classno='"+class_id+"'");
            ResultSet rs1=Mysql.search("SELECT COUNT(*) FROM class INNER JOIN student_has_class1 ON student_has_class1.class1_class_no=class.ClassNo WHERE class1_class_no='"+class_id+"'");
            if(rs.next() && rs1.next()){
              jLabel327.setText(String.valueOf(rs.getDouble("subject.price")*rs1.getInt("COUNT(*)")));   
            }
           
        } catch (Exception e) {
            System.err.println(e);
        }

        

        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox27ItemStateChanged

    private void jComboBox27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox27MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox27MouseClicked

    private void jComboBox27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox27ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
 java.util.Date date=new java.util.Date();
        SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
         String month=String.valueOf(jTable34.getValueAt(0,3 )) ;
            String month_id=String.valueOf(monthMap.get(month)) ;
      String date1=  formate.format(date);
HashMap<String,Object> parameters=new HashMap<>();

        try {
            
            Mysql.iud("INSERT INTO payments (id,teacher_Tno,total,date,month_id) VALUES('"+jLabel334.getText()+"','"+jTextField109.getText()+"','"+jLabel327.getText()+"','"+date1+"','"+month_id+"')");
            
             JRTableModelDataSource datasource=new JRTableModelDataSource(jTable34.getModel());
             JasperViewer.viewReport(JasperFillManager.fillReport("src/reports/payment.jasper", parameters, datasource),false);
             System.out.println(parameters);
        } catch (Exception e) {
            System.err.println(e);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
String student_id=jTextField109.getText();
String class1=jComboBox27.getSelectedItem().toString();
        String price=jLabel327.getText();
        String month=jComboBox28.getSelectedItem().toString();
        int month_id=monthMap.get(month);
int class_id=classMap.get(class1);
        Class1 class2=classMap2.get(String.valueOf(class_id));
System.out.println(class2);
              Vector<String> v=new Vector<>();
v.add(String.valueOf(class_id));
v.add(class2.getSubject());
v.add(class2.getTeacher());
v.add(month);
v.add(price);
DefaultTableModel m=(DefaultTableModel)jTable34.getModel();
m.addRow(v);

calculateTotal1();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jComboBox28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox28MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox28MouseClicked

    private void jComboBox28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox28ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
String id=jTextField109.getText();
        loadteacherclass(id);
String invoiceid="#" + Math.random();
jLabel334.setText(invoiceid);
        loadclassstudent(id);
        try {
            ResultSet rs=Mysql.search("SELECT * FROM teacher WHERE Tno='"+id+"'");
        
            if(rs.next()){
                String stid=Integer.toString(rs.getInt("Tno"));
                String name=rs.getString("teacher_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");
                

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                
            
         
      
              

                jTextField110.setText(rs.getString("teacher_name"));
                jTextField109.setText(stid);
                jDateChooser17.setDate(dob);
                jTextField111.setText(address);
               
                 jTextField36.setEditable(false);
                
                jTextField37.setEditable(false);
            }
            
        } catch (Exception e) {
        }         // TODO add your handling code here:
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
String id=jTextField5.getText();
loadclassstudent(id);
        try {
            ResultSet rs=Mysql.search("SELECT * FROM class INNER JOIN student_has_class1 ON student_has_class1.class1_class_no=class.Classno INNER JOIN student ON student_has_class1.student_student_no=student.Sno INNER JOIN teacher ON teacher.Tno=class.teacher_teacher_no INNER JOIN subject ON subject.Subno=class.subject_subject_id WHERE Sno='"+id+"'");
        
            Vector v1=new Vector();
         
         if(rs.next()){
                String stid=Integer.toString(rs.getInt("Sno"));
                String name=rs.getString("student_name");
                String address=rs.getString("address");
                Date dob=rs.getDate("dob");
                

                //                Blob image=rs.getBlob("image");
                //                String path="C:\\Users\\WW\\OneDrive\\Documents\\NetBeansProjects\\sad007\\src\\img";
                //                byte [] bytes=image.getBytes(1, (int)image.length());
                //                FileOutputStream fos=new FileOutputStream(path);
                //                fos.write(bytes);
                //                ImageIcon icon=new ImageIcon(bytes);
                //                System.out.println(name);
                //                jLabel3.setIcon(icon);

                
                   
        
      
      
          
      
        
                
                byte[]imageData =rs.getBytes("image");
                ImageIcon format = new ImageIcon(imageData);
                Image mm=format.getImage();
                Image img2=mm.getScaledInstance(160, 180, Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(img2);

                jLabel44.setIcon(image);

                jTextField34.setText(rs.getString("student_name"));
                jTextField5.setText(stid);
                jDateChooser4.setDate(dob);
                jTextField35.setText(address);
               
                jTextField34.setEditable(false);
               
                
                jTextField35.setEditable(false);
            }
            
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton87ActionPerformed
String id=jTextField68.getText();


        try {
            ResultSet rs=Mysql.search("SELECT * FROM employee INNER JOIN employee_has_job ON employee_has_job.employee_user_name=employee.user_id INNER JOIN job ON job.id=employee_has_job.job_id WHERE employee.user_id='"+id+"'");
            if(rs.next()){
                String stid=Integer.toString(rs.getInt("user_id"));
                String name=rs.getString("name");
                

                jTextField69.setText(rs.getString("name"));
                
                
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }             // TODO add your handling code here:
    }//GEN-LAST:event_jButton87ActionPerformed
   
    /**
     * @param args the command line arguments
    
    */
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        FlatCyanLightIJTheme.setup();
        //</editor-fold>
java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
        /* Create and display the form */
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton100;
    private javax.swing.JButton jButton101;
    private javax.swing.JButton jButton102;
    private javax.swing.JButton jButton103;
    private javax.swing.JButton jButton104;
    private javax.swing.JButton jButton105;
    private javax.swing.JButton jButton106;
    private javax.swing.JButton jButton107;
    private javax.swing.JButton jButton108;
    private javax.swing.JButton jButton109;
    private javax.swing.JButton jButton110;
    private javax.swing.JButton jButton111;
    private javax.swing.JButton jButton112;
    private javax.swing.JButton jButton113;
    private javax.swing.JButton jButton114;
    private javax.swing.JButton jButton115;
    private javax.swing.JButton jButton116;
    private javax.swing.JButton jButton117;
    private javax.swing.JButton jButton118;
    private javax.swing.JButton jButton119;
    private javax.swing.JButton jButton120;
    private javax.swing.JButton jButton121;
    private javax.swing.JButton jButton122;
    private javax.swing.JButton jButton125;
    private javax.swing.JButton jButton126;
    private javax.swing.JButton jButton127;
    private javax.swing.JButton jButton128;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton60;
    private javax.swing.JButton jButton61;
    private javax.swing.JButton jButton62;
    private javax.swing.JButton jButton63;
    private javax.swing.JButton jButton64;
    private javax.swing.JButton jButton65;
    private javax.swing.JButton jButton67;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton69;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton70;
    private javax.swing.JButton jButton71;
    private javax.swing.JButton jButton72;
    private javax.swing.JButton jButton73;
    private javax.swing.JButton jButton74;
    private javax.swing.JButton jButton75;
    private javax.swing.JButton jButton76;
    private javax.swing.JButton jButton79;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton80;
    private javax.swing.JButton jButton81;
    private javax.swing.JButton jButton82;
    private javax.swing.JButton jButton83;
    private javax.swing.JButton jButton84;
    private javax.swing.JButton jButton85;
    private javax.swing.JButton jButton86;
    private javax.swing.JButton jButton87;
    private javax.swing.JButton jButton88;
    private javax.swing.JButton jButton89;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton90;
    private javax.swing.JButton jButton91;
    private javax.swing.JButton jButton92;
    private javax.swing.JButton jButton93;
    private javax.swing.JButton jButton94;
    private javax.swing.JButton jButton95;
    private javax.swing.JButton jButton96;
    private javax.swing.JButton jButton97;
    private javax.swing.JButton jButton98;
    private javax.swing.JButton jButton99;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox<String> jComboBox16;
    private javax.swing.JComboBox<String> jComboBox17;
    private javax.swing.JComboBox<String> jComboBox18;
    private javax.swing.JComboBox<String> jComboBox20;
    private javax.swing.JComboBox<String> jComboBox21;
    private javax.swing.JComboBox<String> jComboBox22;
    private javax.swing.JComboBox<String> jComboBox23;
    private javax.swing.JComboBox<String> jComboBox24;
    private javax.swing.JComboBox<String> jComboBox25;
    private javax.swing.JComboBox<String> jComboBox27;
    private javax.swing.JComboBox<String> jComboBox28;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser10;
    private com.toedter.calendar.JDateChooser jDateChooser11;
    private com.toedter.calendar.JDateChooser jDateChooser12;
    private com.toedter.calendar.JDateChooser jDateChooser13;
    private com.toedter.calendar.JDateChooser jDateChooser14;
    private com.toedter.calendar.JDateChooser jDateChooser15;
    private com.toedter.calendar.JDateChooser jDateChooser16;
    private com.toedter.calendar.JDateChooser jDateChooser17;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private com.toedter.calendar.JDateChooser jDateChooser7;
    private com.toedter.calendar.JDateChooser jDateChooser8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel180;
    private javax.swing.JLabel jLabel181;
    private javax.swing.JLabel jLabel182;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel184;
    private javax.swing.JLabel jLabel185;
    private javax.swing.JLabel jLabel187;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel190;
    private javax.swing.JLabel jLabel192;
    private javax.swing.JLabel jLabel193;
    private javax.swing.JLabel jLabel195;
    private javax.swing.JLabel jLabel196;
    private javax.swing.JLabel jLabel197;
    private javax.swing.JLabel jLabel198;
    private javax.swing.JLabel jLabel199;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel202;
    private javax.swing.JLabel jLabel203;
    private javax.swing.JLabel jLabel204;
    private javax.swing.JLabel jLabel205;
    private javax.swing.JLabel jLabel206;
    private javax.swing.JLabel jLabel207;
    private javax.swing.JLabel jLabel208;
    private javax.swing.JLabel jLabel209;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel210;
    private javax.swing.JLabel jLabel216;
    private javax.swing.JLabel jLabel217;
    private javax.swing.JLabel jLabel218;
    private javax.swing.JLabel jLabel219;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel220;
    private javax.swing.JLabel jLabel221;
    private javax.swing.JLabel jLabel222;
    private javax.swing.JLabel jLabel223;
    private javax.swing.JLabel jLabel224;
    private javax.swing.JLabel jLabel225;
    private javax.swing.JLabel jLabel226;
    private javax.swing.JLabel jLabel227;
    private javax.swing.JLabel jLabel228;
    private javax.swing.JLabel jLabel229;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel230;
    private javax.swing.JLabel jLabel231;
    private javax.swing.JLabel jLabel232;
    private javax.swing.JLabel jLabel233;
    private javax.swing.JLabel jLabel234;
    private javax.swing.JLabel jLabel235;
    private javax.swing.JLabel jLabel236;
    private javax.swing.JLabel jLabel237;
    private javax.swing.JLabel jLabel238;
    private javax.swing.JLabel jLabel239;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel240;
    private javax.swing.JLabel jLabel241;
    private javax.swing.JLabel jLabel242;
    private javax.swing.JLabel jLabel243;
    private javax.swing.JLabel jLabel244;
    private javax.swing.JLabel jLabel245;
    private javax.swing.JLabel jLabel246;
    private javax.swing.JLabel jLabel247;
    private javax.swing.JLabel jLabel248;
    private javax.swing.JLabel jLabel249;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel250;
    private javax.swing.JLabel jLabel251;
    private javax.swing.JLabel jLabel252;
    private javax.swing.JLabel jLabel253;
    private javax.swing.JLabel jLabel254;
    private javax.swing.JLabel jLabel255;
    private javax.swing.JLabel jLabel256;
    private javax.swing.JLabel jLabel257;
    private javax.swing.JLabel jLabel258;
    private javax.swing.JLabel jLabel259;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel260;
    private javax.swing.JLabel jLabel261;
    private javax.swing.JLabel jLabel262;
    private javax.swing.JLabel jLabel263;
    private javax.swing.JLabel jLabel264;
    private javax.swing.JLabel jLabel265;
    private javax.swing.JLabel jLabel266;
    private javax.swing.JLabel jLabel267;
    private javax.swing.JLabel jLabel268;
    private javax.swing.JLabel jLabel269;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel270;
    private javax.swing.JLabel jLabel271;
    private javax.swing.JLabel jLabel272;
    private javax.swing.JLabel jLabel273;
    private javax.swing.JLabel jLabel274;
    private javax.swing.JLabel jLabel275;
    private javax.swing.JLabel jLabel276;
    private javax.swing.JLabel jLabel277;
    private javax.swing.JLabel jLabel278;
    private javax.swing.JLabel jLabel279;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel280;
    private javax.swing.JLabel jLabel281;
    private javax.swing.JLabel jLabel282;
    private javax.swing.JLabel jLabel283;
    private javax.swing.JLabel jLabel284;
    private javax.swing.JLabel jLabel285;
    private javax.swing.JLabel jLabel286;
    private javax.swing.JLabel jLabel287;
    private javax.swing.JLabel jLabel288;
    private javax.swing.JLabel jLabel289;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel290;
    private javax.swing.JLabel jLabel291;
    private javax.swing.JLabel jLabel292;
    private javax.swing.JLabel jLabel293;
    private javax.swing.JLabel jLabel294;
    private javax.swing.JLabel jLabel295;
    private javax.swing.JLabel jLabel296;
    private javax.swing.JLabel jLabel297;
    private javax.swing.JLabel jLabel298;
    private javax.swing.JLabel jLabel299;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel300;
    private javax.swing.JLabel jLabel301;
    private javax.swing.JLabel jLabel302;
    private javax.swing.JLabel jLabel303;
    private javax.swing.JLabel jLabel304;
    private javax.swing.JLabel jLabel305;
    private javax.swing.JLabel jLabel306;
    private javax.swing.JLabel jLabel307;
    private javax.swing.JLabel jLabel308;
    private javax.swing.JLabel jLabel309;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel310;
    private javax.swing.JLabel jLabel311;
    private javax.swing.JLabel jLabel312;
    private javax.swing.JLabel jLabel313;
    private javax.swing.JLabel jLabel314;
    private javax.swing.JLabel jLabel315;
    private javax.swing.JLabel jLabel316;
    private javax.swing.JLabel jLabel317;
    private javax.swing.JLabel jLabel318;
    private javax.swing.JLabel jLabel319;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel320;
    private javax.swing.JLabel jLabel322;
    private javax.swing.JLabel jLabel323;
    private javax.swing.JLabel jLabel324;
    private javax.swing.JLabel jLabel325;
    private javax.swing.JLabel jLabel326;
    private javax.swing.JLabel jLabel327;
    private javax.swing.JLabel jLabel328;
    private javax.swing.JLabel jLabel329;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel333;
    private javax.swing.JLabel jLabel334;
    private javax.swing.JLabel jLabel335;
    private javax.swing.JLabel jLabel336;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    private javax.swing.JPanel jPanel79;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane32;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JScrollPane jScrollPane34;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable13;
    private javax.swing.JTable jTable14;
    private javax.swing.JTable jTable15;
    private javax.swing.JTable jTable16;
    private javax.swing.JTable jTable17;
    private javax.swing.JTable jTable18;
    private javax.swing.JTable jTable19;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable20;
    private javax.swing.JTable jTable21;
    private javax.swing.JTable jTable22;
    private javax.swing.JTable jTable23;
    private javax.swing.JTable jTable24;
    private javax.swing.JTable jTable25;
    private javax.swing.JTable jTable26;
    private javax.swing.JTable jTable27;
    private javax.swing.JTable jTable28;
    private javax.swing.JTable jTable29;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable30;
    private javax.swing.JTable jTable31;
    private javax.swing.JTable jTable32;
    private javax.swing.JTable jTable33;
    private javax.swing.JTable jTable34;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField100;
    private javax.swing.JTextField jTextField101;
    private javax.swing.JTextField jTextField102;
    private javax.swing.JTextField jTextField103;
    private javax.swing.JTextField jTextField104;
    private javax.swing.JTextField jTextField105;
    private javax.swing.JTextField jTextField106;
    private javax.swing.JTextField jTextField107;
    private javax.swing.JTextField jTextField108;
    private javax.swing.JTextField jTextField109;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField110;
    private javax.swing.JTextField jTextField111;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField50;
    private javax.swing.JTextField jTextField52;
    private javax.swing.JTextField jTextField53;
    private javax.swing.JTextField jTextField54;
    private javax.swing.JTextField jTextField55;
    private javax.swing.JTextField jTextField56;
    private javax.swing.JTextField jTextField57;
    private javax.swing.JTextField jTextField58;
    private javax.swing.JTextField jTextField59;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField60;
    private javax.swing.JTextField jTextField61;
    private javax.swing.JTextField jTextField62;
    private javax.swing.JTextField jTextField63;
    private javax.swing.JTextField jTextField64;
    private javax.swing.JTextField jTextField65;
    private javax.swing.JTextField jTextField66;
    private javax.swing.JTextField jTextField67;
    private javax.swing.JTextField jTextField68;
    private javax.swing.JTextField jTextField69;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField70;
    private javax.swing.JTextField jTextField71;
    private javax.swing.JTextField jTextField72;
    private javax.swing.JTextField jTextField73;
    private javax.swing.JTextField jTextField75;
    private javax.swing.JTextField jTextField76;
    private javax.swing.JTextField jTextField77;
    private javax.swing.JTextField jTextField78;
    private javax.swing.JTextField jTextField79;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField80;
    private javax.swing.JTextField jTextField81;
    private javax.swing.JTextField jTextField82;
    private javax.swing.JTextField jTextField83;
    private javax.swing.JTextField jTextField84;
    private javax.swing.JTextField jTextField85;
    private javax.swing.JTextField jTextField86;
    private javax.swing.JTextField jTextField87;
    private javax.swing.JTextField jTextField88;
    private javax.swing.JTextField jTextField89;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextField jTextField90;
    private javax.swing.JTextField jTextField91;
    private javax.swing.JTextField jTextField92;
    private javax.swing.JTextField jTextField93;
    private javax.swing.JTextField jTextField94;
    private javax.swing.JTextField jTextField95;
    private javax.swing.JTextField jTextField96;
    private javax.swing.JTextField jTextField97;
    private javax.swing.JTextField jTextField98;
    private javax.swing.JTextField jTextField99;
    // End of variables declaration//GEN-END:variables

    void text() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void text(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void settext(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    

    
}
