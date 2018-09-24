
package Frames;

import Exceptions.RecordNotFoundException;
import Models.Account;
import Models.Concept;
import Models.Movement;
import Enumerators.ConceptType;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;



public class FrameAccount {
    //declaracion de controles
    private final JFrame frame = new JFrame("Account Info");
    private final JPanel panelAccount =  new JPanel();
    private final JPanel panelButtons =  new JPanel();
    private final JLabel labelNumber = new JLabel("Number: ");
    private final JLabel labelName = new JLabel("Name: ");
    private final JLabel labelBalance = new JLabel("Balance: ");
    private final JTextField textNumber = new JTextField(10); 
    private final JTextField textName = new JTextField(20); 
    private final JTextField textBalance = new JTextField(10); 
    private final JButton buttonSearch = new JButton("",new ImageIcon("Images/search.png"));
    private final JButton buttonClose = new JButton("Close", new ImageIcon("Images/cancel.png"));
    //table
    private final JPanel panelMovements=new JPanel();
    private String[] columnHeaders={"Date","Concept","Deposit","Widrawal"};
    private final JTable tableMovements=new JTable();
    private final JScrollPane scrollMovements=new JScrollPane(tableMovements);
    private DefaultTableModel modelMovements= new DefaultTableModel(0,0);
    
    
    
    
    
    
    
    //crearemos el constructor del frame
    public FrameAccount(){
        //agregamos propiedades al frame, como el tamano, la locacion , etc.
        this.frame.setSize(820,600);//definimos alto y ancho
        this.frame.setMinimumSize(new Dimension(820,600)); //dejamos claro que la medida menor sera 
        this.frame.setLocationRelativeTo(null);//centramos el frame
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);//especificamos que la "X" no cierra el programa
        //creamos el contenedor que llevara el frame donde iran todos los controles
        Container container = this.frame.getContentPane(); 
        container.setLayout(new BorderLayout());//se asigna un layout al container
        this.panelAccount.setLayout(new GridBagLayout());
        this.panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.panelMovements.setLayout(new BorderLayout());
        
        
        //acomodando el panel de 'Cuenta' 
        GridBagConstraints gb = new GridBagConstraints();
        gb.insets = new Insets(10,10,0,10);
        gb.weightx = .5;
        gb.anchor = GridBagConstraints.WEST;
        //asignando controles con posicione x y Y
        gb.gridy = 0; gb.gridx = 0; this.panelAccount.add(this.labelNumber, gb);
        gb.gridy = 0; gb.gridx = 1; this.panelAccount.add(this.textNumber, gb);
        this.buttonSearch.setEnabled(false);
        gb.gridy = 0; gb.gridx = 2; this.panelAccount.add(this.buttonSearch, gb);
        gb.gridy = 0; gb.gridx = 3; this.panelAccount.add(this.labelName, gb);
        this.textName.setEditable(false);//se desactiva la edicion del input textName
        gb.gridy = 0; gb.gridx = 4; this.panelAccount.add(this.textName, gb);//se asigna posicion a textName
        gb.gridy = 0; gb.gridx = 5; this.panelAccount.add(this.labelBalance, gb);//se asigna la posicion a label balance
        this.textBalance.setEditable(false);//se asigna no editable el input de balance
        gb.gridy = 0; gb.gridx = 6; this.panelAccount.add(this.textBalance, gb);//se asigna la posicion de input de balance
        container.add(panelAccount, BorderLayout.NORTH);//se agrega el panel account al container del frame
       
        //movements
        this.tableMovements.setFillsViewportHeight(true);
        this.tableMovements.setModel(this.modelMovements);
        this.modelMovements.setColumnIdentifiers(this.columnHeaders);
        //align currency cells t right
        DefaultTableCellRenderer rightAlign=new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(SwingConstants.RIGHT);
        this.tableMovements.getColumnModel().getColumn(2).setCellRenderer(rightAlign);
        this.tableMovements.getColumnModel().getColumn(3).setCellRenderer(rightAlign);
        this.tableMovements.setRowHeight(25);
        panelMovements.add(scrollMovements,BorderLayout.CENTER);
        container.add(panelMovements,BorderLayout.CENTER);
        //panel de botones
        this.buttonClose.setHorizontalTextPosition(SwingConstants.CENTER);
        this.buttonClose.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.panelButtons.add(this.buttonClose);
        container.add(this.panelButtons, BorderLayout.SOUTH);
        
        //event handlers
        //se asiga un listener al input de numero de cuenta el cual detecta cada que se presiona una tecla 
        this.textNumber.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                enableSearchButton();
            }
            
        });
        //se agrega accion al momento de dar click en el boton 'buscar'
        this.buttonSearch.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                showAccountInfo();
            }
            
        });
        //se agrega accion al momento de dar click en el boton 'cerrar'
        this.buttonClose.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                close();
            }
        });        
    }
    
    //metodos
    //metodo que muestra el frame
    public void show(){
        this.frame.setVisible(true);
    }
    
    //metodo que cierra el frame
    private void close(){
        if(JOptionPane.showConfirmDialog(this.frame, "Exit Application","Confirm",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            System.exit(0);
    }
    //metodo que lee la longitud en el textbox y si es ayor a 0 activa el boton buscar
    private void enableSearchButton(){
        System.out.println(textNumber.getText().length());
        if(textNumber.getText().length() > 0){
            this.buttonSearch.setEnabled(true);
        }else{
            this.buttonSearch.setEnabled(false);
        }
    }
    
    //metodo que muestra la info del cuentahabiente
    private void showAccountInfo(){
        textName.setText("");
        textBalance.setText("");
        try{
            Account a = new Account(Integer.parseInt(textNumber.getText()));
            textName.setText(a.getFullName());
            textBalance.setText(a.getBalanceFormatted());
            //movements
            for (Movement m:a.getMovements())
            {
                String deposit="",withdrawal="";
                if(m.getConcept().getType()==ConceptType.DEPOSIT)
                {
                    deposit=m.getAmmountFormatted();
                }
                else{withdrawal=m.getAmmountFormatted();}
                this.modelMovements.addRow(new Object[]{m.getDate(),m.getConcept(),deposit,withdrawal});
            }
            
            
            
        }catch(RecordNotFoundException ex){
            JOptionPane.showMessageDialog(this.frame,"Account Not Found", "Error", JOptionPane.ERROR_MESSAGE);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this.frame,"Account number must be only numbers", "Error", JOptionPane.ERROR_MESSAGE);
            textNumber.requestFocus();
            textNumber.selectAll();
        }
    }

    
    
    
}
    

