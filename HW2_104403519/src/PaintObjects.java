/*** 
 * @Author �J�͵�
 * �Ǹ�: 104403519
 * �t��: ���3A
 * HW2: �p�e�a�\���@
***/

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;

public class PaintObjects{    
    Shape s; // �s��Ϊ�
    Color c; // �s���C��
    BasicStroke size; // �s�񵧨�j�p ��u�P�_
    boolean emp = true ,isFill = false; // �P�_�O�_��
    public PaintObjects(Shape s,BasicStroke size,Color c){
            this.s = s;            
            this.size = size;
            this.c = c;
            this.emp = false;
    }
    public PaintObjects(Shape s,BasicStroke size,Color c,boolean isFill){
            this.s = s;            
            this.size = size;
            this.c = c;
            this.emp = false;
            this.isFill = isFill;
    }
    public PaintObjects(Shape s,BasicStroke size){
            this.s = s;            
            this.size = size;
            this.emp = false;
    }
    public boolean isEmpty(){
        return emp;
    }
    
    
}
