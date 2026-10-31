package p02.pres;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.JLayeredPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Component;
import p02.game.Board;

public class TurtleBridge {

    private static SevenSegmentDigit unitsDigit;
    private static SevenSegmentDigit tensDigit;
    private static SevenSegmentDigit hundredsDigit;
    private static Board boardLogic;

    private static void createGUI() {
        JFrame frame = new JFrame("Turtle Bridge s33735");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardLogic = new Board();
        
        JPanel scoreDisplayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scoreDisplayPanel.setOpaque(false);

        hundredsDigit = new SevenSegmentDigit(0);
        hundredsDigit.setName("HundredsDigit");
        tensDigit = new SevenSegmentDigit(0);
        tensDigit.setName("TensDigit");
        unitsDigit = new SevenSegmentDigit(0);
        unitsDigit.setName("UnitsDigit");

        unitsDigit.registerPlusEventListener(tensDigit);
        tensDigit.registerPlusEventListener(hundredsDigit);
        boardLogic.setScore(unitsDigit, tensDigit, hundredsDigit);
        if (hundredsDigit != null && boardLogic != null) {
            hundredsDigit.registerPlusEventListener(boardLogic);
        }

        scoreDisplayPanel.add(hundredsDigit);
        scoreDisplayPanel.add(tensDigit);
        scoreDisplayPanel.add(unitsDigit);

        JLayeredPane layeredPane = new JLayeredPane();
        
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        Image bgImage = backgroundPanel.getBackgroundImage();
        int preferredWidth = 1000;
        int preferredHeight = 400;

        if (bgImage != null) {
            preferredWidth = bgImage.getWidth(null);
            preferredHeight = bgImage.getHeight(null);
        }
        layeredPane.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        backgroundPanel.setBounds(0, 0, preferredWidth, preferredHeight);

        BoardModel boardTableModel = new BoardModel(boardLogic);
        boardLogic.setBoardModel(boardTableModel);
        JTable gameTableView = new JTable(boardTableModel);

        gameTableView.setTableHeader(null);

        gameTableView.setFocusable(false);
        gameTableView.setDefaultRenderer(Object.class, new GameBoardCellRenderer());
        gameTableView.setRowHeight(60);

        gameTableView.setOpaque(false);
        Component rendererComponent = gameTableView.getDefaultRenderer(Object.class).getTableCellRendererComponent(gameTableView, "", false, false, 0,0);
        if (rendererComponent instanceof JPanel) {
            ((JPanel)rendererComponent).setOpaque(false);
        } else if (rendererComponent instanceof JLabel) {
            ((JLabel)rendererComponent).setOpaque(false);
        }
        gameTableView.setShowGrid(false);

        JScrollPane tableScrollPane = new JScrollPane(gameTableView);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        int tableX = 50; 
        int tableY = 550; 
        int tableWidth = preferredWidth - 100; 
        int tableHeight = preferredHeight - 200; 
        tableScrollPane.setBounds(tableX, tableY, tableWidth, tableHeight);
        
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(tableScrollPane, Integer.valueOf(10)); 

        layeredPane.setFocusable(true);
        layeredPane.addKeyListener(boardLogic);

        frame.setLayout(new BorderLayout());
        frame.add(scoreDisplayPanel, BorderLayout.NORTH);
        frame.add(layeredPane, BorderLayout.CENTER);
        
        frame.pack(); 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        layeredPane.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createGUI();
            }
        });
    }
}
