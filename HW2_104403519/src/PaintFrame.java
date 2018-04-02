/*** 
 * @Author �J�͵�
 * �Ǹ�: 104403519
 * �t��: ���3A
 * HW2: �p�e�a�\���@
***/

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class PaintFrame extends JFrame {

    JPanel ToolList = new JPanel();
    JLabel StateLabel = new JLabel("��Ц�m�G�e���~");
    JPanel PaintField = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Draw
            for (PaintObjects point : points) {
                g2d.setStroke(point.size);
                g2d.setColor(point.c);
                if (point.isFill) {
                    g2d.fill(point.s);
                } else {
                    g2d.draw(point.s);
                }

            }
            // Shape
            g2d.setStroke(displayPoints.size);
            g2d.setColor(displayPoints.c);
            if (displayPoints.isFill) {
                g2d.fill(displayPoints.s);
            } else {
                g2d.draw(displayPoints.s);
            }
            // Eraser
            for(PaintObjects easer : easers){
                g2d.setStroke(easer.size);
                g2d.setColor(tmpBackColor);
                g2d.draw(easer.s);
            }

        }
    };
    private final JCheckBox fillJCheckBox;
    private final JRadioButton smallJRadioButton;
    private final JRadioButton midJRadioButton;
    private final JRadioButton largeJRadioButton;
    private final JButton FGJButton;
    private final JButton BGJButton;
    private final JButton ClearButton;
    private final JButton UndoButton;
    private final JButton SaveButton;
    private String[] toolList
            = {"����", "���u", "����", "�x��", "�ꨤ�x��", "�����"};
    Shape nu = new Line2D.Double(0, 0, 0, 0);
    private final ArrayList<PaintObjects> points = new ArrayList<>();// �]�wArrayList���A���ۭq����PaintObjects
    private final ArrayList<PaintObjects> easers = new ArrayList<>();// �]�wArrayList���A���ۭq����PaintObjects
    private PaintObjects displayPoints;// �ƹ��԰ʮɪ��w���ϧ�
    private final int[] painterSize = {5, 10, 15}; // size of the pen
    private int painterSizeSelecter = 0, set = 0; 
    private Color tmpBrushColor = Color.black;
    private Color tmpBackColor;
    JFileChooser save;

    public PaintFrame() {
        this.save = new JFileChooser();
        this.displayPoints = new PaintObjects(nu, new BasicStroke(0), tmpBrushColor);
        // �]�w����
        this.setTitle("�p�e�a");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(true);

        // �]�wLayout
        ToolList.setLayout(new GridLayout(12, 1));
        add(StateLabel, BorderLayout.SOUTH);
        add(ToolList, BorderLayout.WEST);
        add(PaintField, BorderLayout.CENTER);
        PaintField.setBackground(Color.white);

        // �]�w�����\���
        JLabel ptitle = new JLabel("[ø�Ϥu��]");
        ToolList.add(ptitle);

        JComboBox toolJComboBox = new JComboBox<String>(toolList);
        toolJComboBox.setMaximumRowCount(8);
        ToolList.add(toolJComboBox);

        JLabel toolSizeTitle = new JLabel("[����j�p]");
        ToolList.add(toolSizeTitle);

        smallJRadioButton = new JRadioButton("�p", true);
        midJRadioButton = new JRadioButton("��", false);
        largeJRadioButton = new JRadioButton("�j", false);
        ToolList.add(smallJRadioButton);
        ToolList.add(midJRadioButton);
        ToolList.add(largeJRadioButton);
        ButtonGroup sizeGroup = new ButtonGroup();
        sizeGroup.add(smallJRadioButton);
        sizeGroup.add(midJRadioButton);
        sizeGroup.add(largeJRadioButton);

        fillJCheckBox = new JCheckBox("��");
        ToolList.add(fillJCheckBox);

        FGJButton = new JButton("�e����");
        BGJButton = new JButton("�I����");
        FGJButton.setBackground(Color.black);
        BGJButton.setBackground(Color.white);
        tmpBackColor = Color.white;
        ClearButton = new JButton("�M���e��");
        UndoButton = new JButton("�W�@�B");
        SaveButton = new JButton("�s��");
        ToolList.add(FGJButton);
        ToolList.add(BGJButton);
        ToolList.add(ClearButton);
        ToolList.add(UndoButton);
        ToolList.add(SaveButton);

        // Combobox's listener
        toolJComboBox.addItemListener(
                new ItemListener() // anonymous inner class
        {
            // handle JComboBox event
            @Override
            public void itemStateChanged(ItemEvent event) {
                // determine whether item selected
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    set = toolJComboBox.getSelectedIndex();
                }
            }
        }
        );

        // Radiobutton's listener
        smallJRadioButton.addItemListener(
                new RadioButtonHandler());
        midJRadioButton.addItemListener(
                new RadioButtonHandler());
        largeJRadioButton.addItemListener(
                new RadioButtonHandler());

        // Checkbox's listener
        CheckBoxHandler handler = new CheckBoxHandler();
        fillJCheckBox.addActionListener(handler);

        // Button's listener
        ButtonHandler btnhandler = new ButtonHandler();
        FGJButton.addActionListener(btnhandler);
        BGJButton.addActionListener(btnhandler);
        ClearButton.addActionListener(btnhandler);
        UndoButton.addActionListener(btnhandler);
        SaveButton.addActionListener(btnhandler);

        // Mouse listener
        MouseHandler moshandler = new MouseHandler();
        PaintField.addMouseListener(moshandler);
        PaintField.addMouseMotionListener(moshandler);
    }

    private class RadioButtonHandler implements ItemListener {

        String[] selection = {"�p", "��", "�j"};

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (e.getSource() == smallJRadioButton) {
                    painterSizeSelecter = 0;
                } else if (e.getSource() == midJRadioButton) {
                    painterSizeSelecter = 1;
                } else if (e.getSource() == largeJRadioButton) {
                    painterSizeSelecter = 2;
                }
            }
        }
    }
    private boolean isFill = false;

    private class CheckBoxHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (fillJCheckBox.isSelected()) {
                isFill = true;
            } else {
                isFill = false;
            }
        }
    }

    private class ButtonHandler implements ActionListener {
        // handle button event

        @Override
        public void actionPerformed(ActionEvent event) {
            //�M���e�� �I���אּwhite �e���אּblack
            if (event.getSource() == ClearButton) {
                points.clear();
                displayPoints = new PaintObjects(nu, new BasicStroke(0), tmpBrushColor);
                tmpBrushColor = Color.black;
                FGJButton.setBackground(tmpBrushColor);
                tmpBackColor = Color.white;
                PaintField.setBackground(tmpBackColor);
                BGJButton.setBackground(tmpBackColor);
                repaint();
            }
            // JColorChooser
            if (event.getSource() == FGJButton) {
                tmpBrushColor = JColorChooser.showDialog(PaintFrame.this, "�C���ܾ�", tmpBrushColor);
                if (tmpBrushColor == null) {
                    tmpBrushColor = Color.black;
                }
                FGJButton.setBackground(tmpBrushColor);
            }
            if (event.getSource() == BGJButton) {
                tmpBackColor = JColorChooser.showDialog(PaintFrame.this, "�C���ܾ�", tmpBackColor);
                PaintField.setBackground(tmpBackColor);
                BGJButton.setBackground(tmpBackColor);
                repaint();
            }
            // Undo(�W�@�B), delete the last element in ArrayList
            if (event.getSource() == UndoButton) {
                points.remove(points.remove(points.size() - 1));
                repaint();
            }
            // Save picture: use JFileChooser to select file (where to save) and use ImageIO to save JPanel as "picture.jpg"
            if (event.getSource() == SaveButton) {
                save.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                BufferedImage saveImg = new BufferedImage(PaintField.getWidth(), PaintField.getHeight(), BufferedImage.TYPE_INT_RGB);
                PaintField.paint(saveImg.createGraphics());
                int result = save.showDialog(SaveButton, "Save");
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        ImageIO.write(saveImg, "jpg", new File(save.getSelectedFile().getAbsolutePath() + "\\picture.jpg"));
                    } catch (IOException ex) {
                        Logger.getLogger(PaintFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    int x1, x2, y1, y2; // �ƹ��y���I
    final float dash1[] = {10}; // ��u���Z          

    // ��H��k��������@    
    private class MouseHandler implements MouseListener,
            MouseMotionListener {

        @Override
        public void mouseEntered(MouseEvent e) {
            StateLabel.setText("��Ц�m�G�e����");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            StateLabel.setText("��Ц�m�G�e���~");

        }

        @Override
        // �Φ���k�ӰO�� ���u �H��� �x�� �ꨤ�x��
        public void mouseReleased(MouseEvent e) {
            switch (set) {
                case 1: //���u
                    x2 = e.getX();
                    y2 = e.getY();
                    Shape line2 = new Line2D.Double(x1, y1, x2, y2);
                    if (isFill) {
                        PaintObjects strline = new PaintObjects(line2, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor);
                        points.add(strline);
                    } else {
                        final BasicStroke dashed = new BasicStroke(painterSize[painterSizeSelecter],
                                BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER,
                                10.0f, dash1, 0.0f);
                        PaintObjects strline = new PaintObjects(line2, dashed, tmpBrushColor);
                        points.add(strline);
                    }

                    break;
                case 2: //����
                    Ellipse2D c = new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                    PaintObjects circle = new PaintObjects(c, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor, isFill);
                    points.add(circle);
                    break;
                case 3: //�x��
                    Rectangle2D r = new Rectangle2D.Double(Math.min(x1,x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                    PaintObjects rectangle = new PaintObjects(r, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor, isFill);
                    points.add(rectangle);
                    break;
                case 4: //�ꨤ�x��
                    RoundRectangle2D rr = new RoundRectangle2D.Double(Math.min(x1,x2), Math.min(y1, y2), Math.abs(x2 - x1),Math.abs(y2 - y1), 30, 30);
                    PaintObjects RoundRectangle = new PaintObjects(rr, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor, isFill);
                    points.add(RoundRectangle);
                    break;
            }
            displayPoints = new PaintObjects(nu, new BasicStroke(0), tmpBrushColor);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            x2 = e.getX();
            y2 = e.getY();
            switch (set) {
                case 0: // only one click (�e�@���I)                    
                    Shape line = new Line2D.Double(x1, y1, x2, y2);
                    PaintObjects tmppoint = new PaintObjects(line, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor);
                    points.add(tmppoint);
                    x1 = x2;
                    y1 = y2;
                    break;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            x1 = e.getX();
            y1 = e.getY();
        }

        @Override
        // Preview the shape when mouse drag
        public void mouseDragged(MouseEvent e) {
            x2 = e.getX();
            y2 = e.getY();
            switch (set) {
                case 0:
                    Shape line = new Line2D.Double(x1, y1, x2, y2);
                    PaintObjects tmppoint = new PaintObjects(line, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor);
                    points.add(tmppoint);
                    x1 = x2;
                    y1 = y2;
                    break;
                case 1:
                    Shape line2 = new Line2D.Double(x1, y1, x2, y2);
                    if (isFill) {
                        PaintObjects strline = new PaintObjects(line2, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor);
                        displayPoints = strline;
                    } else {
                        final BasicStroke dashed = new BasicStroke(painterSize[painterSizeSelecter],
                                BasicStroke.CAP_BUTT,
                                BasicStroke.JOIN_MITER,
                                10.0f, dash1, 0.0f);
                        PaintObjects strline = new PaintObjects(line2, dashed, tmpBrushColor);
                        displayPoints = strline;
                    }
                    break;
                case 2:
                    Ellipse2D c = new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
                    PaintObjects circle = new PaintObjects(c, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor, isFill);
                    displayPoints = circle;
                    break;
                case 3:
                    Rectangle2D r = new Rectangle2D.Double(Math.min(x1,
                            x2), Math.min(y1, y2), Math.abs(x2 - x1),
                            Math.abs(y2 - y1));
                    PaintObjects rectangle = new PaintObjects(r, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor, isFill);
                    displayPoints = rectangle;
                    break;
                case 4:
                    RoundRectangle2D rr = new RoundRectangle2D.Double(Math.min(x1,
                            x2), Math.min(y1, y2), Math.abs(x2 - x1),
                            Math.abs(y2 - y1), 30, 30);
                    PaintObjects RoundRectangle = new PaintObjects(rr, new BasicStroke(painterSize[painterSizeSelecter]), tmpBrushColor, isFill);
                    displayPoints = RoundRectangle;
                    break;
                case 5:
                    Shape es = new Line2D.Double(x1, y1, x2, y2);
                    PaintObjects easer = new PaintObjects(es, new BasicStroke(painterSize[painterSizeSelecter]));
                    easers.add(easer);
                    x1 = x2;
                    y1 = y2;
                    break;

            }
            repaint();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            StateLabel.setText(String.format("��Ц�m�G(%d, %d)",
                    e.getX(), e.getY()));
        }

    }

}
