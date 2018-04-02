/*** 
 * @Author 侯凱翔
 * 學號: 104403519
 * 系級: 資管3A
 * HW2: 小畫家功能實作
***/

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;

public class PaintObjects{    
    Shape s; // 存放形狀
    Color c; // 存放顏色
    BasicStroke size; // 存放筆刷大小 虛線與否
    boolean emp = true ,isFill = false; // 判斷是否填滿
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
