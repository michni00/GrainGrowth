package com.simulation;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;


import static com.simulation.MyFrame.game;

public class MyPanel extends JPanel implements Runnable, MouseListener {
    static public BufferedImage bi;

    static {
        try {
            bi = ImageIO.read(new File("C:\\Users\\michn\\Desktop\\Git\\images\\after.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int width = 650;
    static int height = 650;
    static int howMany = 10;
    static int w = width + 2;
    static int h = height + 2;
    static int[][] originalTab = new int[w][h];
    static int[][] copyTab = new int[w][h];
    static int[][] colors = new int[w][h];
    static int[][] image = new int[bi.getWidth()][bi.getHeight()];
    static double[][] energy = new double[w][h];
    static double[][] H = new double[w][h];
    static Random rand = new Random();
    static boolean boardFilled = false;
    static int checkIfDone = 0;
    static int potentialColor = 0;
    static int addEveryXIterations = 5;
    static int embryos = 8;
    static int iterator = 0;
    static double J = 0.5;
    static double kt = 2;
    static double N = 100;
    static double XX = 5;
    static double YY = 5;
    static int dispersion = 20;
    static boolean showImage = false;


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        originalTab[x][y] = 1;
        int color = rand.nextInt(16000000);
        colors[x][y] = color;
        MyFrame frame = new MyFrame();
        JFrame frame1 = frame.giveFrame();
        frame1.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    enum BoundaryCondition {
        Periodic,
        Absorbing
    }

    enum Neighborhood {
        VonNeumann,
        HexRandom
    }

    enum Nucleation {
        Random,
        Homogeneous
    }

    enum ShowType {
        Simulation,
        Energy
    }

    enum MethodName {
        MC,
        SRX
    }

    static BoundaryCondition boundaryCondition = BoundaryCondition.Periodic;
    static Neighborhood neighborhood = Neighborhood.VonNeumann;
    static Nucleation nucleation = Nucleation.Random;
    static ShowType showType = ShowType.Simulation;
    static MethodName methodName = MethodName.MC;


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    void setRes(int width, int height) {
        MyPanel.width = width;
        MyPanel.height = height;
        setPreferredSize(new Dimension(width, height));
        w = width + 2;
        h = height + 2;
        originalTab = new int[w][h];
        copyTab = new int[w][h];
        colors = new int[w][h];
    }

    void setBCnN(BoundaryCondition bc, Neighborhood n) {
        boundaryCondition = bc;
        neighborhood = n;
    }

    void setNucleation(Nucleation n) {
        nucleation = n;
    }

    void setJkt(double J, double kt) {
        MyPanel.J = J;
        MyPanel.kt = kt;
    }

    void setGrains(int num) {
        howMany = num;
    }

    void setDXXYY(double XX, double YY, int dispersion, int N) {
        MyPanel.XX = XX;
        MyPanel.YY = YY;
        MyPanel.dispersion = dispersion;
        MyPanel.N = N;
    }

    void setEnergy(ShowType st) {
        showType = st;
        MyFrame frame = new MyFrame();
        JFrame frame1 = frame.giveFrame();
        frame1.repaint();
    }

    void setMethod(MethodName mn) {
        methodName = mn;
    }

    void setEmbryos(int time, int amount) {
        embryos = amount;
        addEveryXIterations = time;
    }

    static void update(int[][] originalTab, int[][] copyTab, int[][] colors, int X, int Y) {
        int UP = originalTab[X][Y - 1];
        int LEFT = originalTab[X - 1][Y];
        int RIGHT = originalTab[X + 1][Y];
        int DOWN = originalTab[X][Y + 1];
        int sum = UP + LEFT + DOWN + RIGHT;


        if (originalTab[X][Y] == 1) {
            copyTab[X][Y] = 1;
            if (boundaryCondition == BoundaryCondition.Periodic)
                insertPeriod(copyTab, colors, X, Y);
        } else {
            if (sum > 0) {
                copyTab[X][Y] = 1;
                colors[X][Y] = colorMax(colors, X, Y);
            }
        }
    }

    static void updateHexRandom(int[][] board, int[][] nextBoard, int[][] colors, int X, int Y) {
        int UP = board[X][Y - 1];
        int LEFT = board[X - 1][Y];
        int RIGHT = board[X + 1][Y];
        int DOWN = board[X][Y + 1];
        int rnd = rand.nextInt(100 + 1);
        int sum;

        if (rnd > 50) {
            int UPRIGHT = board[X + 1][Y - 1];
            int DOWNLEFT = board[X - 1][Y + 1];
            sum = UP + LEFT + DOWN + RIGHT + UPRIGHT + DOWNLEFT;
        } else {
            int UPLEFT = board[X - 1][Y - 1];
            int DOWNRIGHT = board[X + 1][Y + 1];
            sum = UP + LEFT + DOWN + RIGHT + UPLEFT + DOWNRIGHT;
        }

        if (board[X][Y] == 1) {
            nextBoard[X][Y] = 1;
            if (boundaryCondition == BoundaryCondition.Periodic)
                insertPeriod(nextBoard, colors, X, Y);
        } else {
            if (sum > 0) {
                nextBoard[X][Y] = 1;
                colors[X][Y] = colorMaxRnd(colors, X, Y, rnd);
            }
        }
    }

    static int colorMax(int[][] colors, int X, int Y) {
        ArrayList<Integer> max = new ArrayList<>();
        int tmp = 0;

        int[] colorsArr = {colors[X][Y - 1], colors[X][Y + 1], colors[X - 1][Y], colors[X + 1][Y]};
        for (int j : colorsArr) {
            for (int k : colorsArr) {
                if (j == k && j != 0) {
                    tmp += 1;
                }
            }
            max.add(tmp);
            tmp = 0;
        }
        tmp = Collections.max(max);
        int position = max.indexOf(tmp);

        return colorsArr[position];
    }

    static int colorMaxRnd(int[][] colors, int X, int Y, int rnd) {
        ArrayList<Integer> max = new ArrayList<>();
        int tmp = 0;
        int[] colorsArr;
        if (rnd > 50)
            colorsArr = new int[]{colors[X][Y - 1], colors[X][Y + 1], colors[X - 1][Y], colors[X + 1][Y], colors[X + 1][Y - 1], colors[X - 1][Y + 1]};
        else
            colorsArr = new int[]{colors[X][Y - 1], colors[X][Y + 1], colors[X - 1][Y], colors[X + 1][Y], colors[X - 1][Y - 1], colors[X + 1][Y + 1]};

        for (int j : colorsArr) {
            for (int k : colorsArr) {
                if (j == k && j != 0) {
                    tmp += 1;
                }
            }
            max.add(tmp);
            tmp = 0;
        }
        tmp = Collections.max(max);
        int position = max.indexOf(tmp);

        return colorsArr[position];
    }

    static void insertPeriod(int[][] nextBoard, int[][] colors, int X, int Y) {
        if (X == width - 2)
            if (nextBoard[2][Y] == 0) {
                nextBoard[2][Y] = 1;
                colors[2][Y] = colors[X][Y];
            }
        if (X == 2)
            if (nextBoard[width - 2][Y] == 0) {
                nextBoard[width - 2][Y] = 1;
                colors[width - 2][Y] = colors[X][Y];
            }
        if (Y == height - 2)
            if (nextBoard[X][2] == 0) {
                nextBoard[X][2] = 1;
                colors[X][2] = colors[X][Y];
            }
        if (Y == 1)
            if (nextBoard[X][height - 2] == 0) {
                nextBoard[X][height - 2] = 1;
                colors[X][height - 2] = colors[X][Y];
            }
    }

    static void insertRandom(int[][] tab, int[][] color, int width, int height) {
        int x, y;
        int co;
        for (int i = 0; i < howMany; i++) {
            x = rand.nextInt(width);
            y = rand.nextInt(height);
            co = rand.nextInt(12000000);
            int tmp = 0;
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    if (co == color[j][k]) {
                        tmp++;
                    }
                }
            }
            if (tab[x][y] == 1) {
                howMany++;
                continue;
            }
            if (tmp > 1) {
                howMany++;
                continue;
            }
            color[x][y] = co;
            tab[x][y] = 1;
        }
    }

    static void insertHomogeneous(int[][] tab, int[][] color, int width, int height, int x, int y) {
        int co;
        int rows = width / x;
        int cols = height / y;
        for (int i = cols / 2; i < height; i += cols) {
            for (int j = rows / 2; j < width; j += rows) {
                co = rand.nextInt(16000000);
                color[j][i] = co;
                tab[j][i] = 1;
            }
        }
    }

    static int[] rgb(int argb) {
        return new int[]{(argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF};
    }

    MyPanel() {
        if (showImage) {
            readImage();
            setPreferredSize(new Dimension(bi.getWidth() + 250, bi.getHeight()));
        } else {
            setPreferredSize(new Dimension(width + 250, height));
        }
        addMouseListener(this);
    }

    static void insert() {
        if (nucleation == Nucleation.Random)
            insertRandom(originalTab, colors, w, h);
        else if (nucleation == Nucleation.Homogeneous)
            insertHomogeneous(originalTab, colors, w, h, 5, 5);
        MyFrame frame = new MyFrame();
        JFrame frame1 = frame.giveFrame();
        frame1.repaint();
    }

    static void clearTabs() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                originalTab[i][j] = 0;
                copyTab[i][j] = 0;
                colors[i][j] = 0;
                energy[i][j] = 0;
                H[i][j] = 0;
            }
        }
        boardFilled = false;
        N = 10;
        MyFrame frame = new MyFrame();
        JFrame frame1 = frame.giveFrame();
        frame1.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[] rgb;
        if (showType == ShowType.Energy) {
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    int color = (int) energy[j][i] + (int) H[j][i];

                    if (color >= 9)
                        g.setColor(new Color(255, 0, 0));
                    if (color == 8)
                        g.setColor(new Color(185, 0, 0));
                    if (color == 7)
                        g.setColor(new Color(135, 0, 0));
                    if (color == 6)
                        g.setColor(new Color(85, 0, 0));
                    if (color == 5)
                        g.setColor(new Color(35, 0, 0));
                    if (color == 4)
                        g.setColor(new Color(0, 45, 0));
                    if (color == 3)
                        g.setColor(new Color(0, 95, 0));
                    if (color == 2)
                        g.setColor(new Color(0, 145, 0));
                    if (color == 1)
                        g.setColor(new Color(0, 195, 0));
                    if (color == 0)
                        g.setColor(new Color(0, 255, 0));

                    g.drawRect(j, i, 1, 1);
                }
            }
        } else {
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    if (originalTab[j][i] == 0) {
                        g.setColor(Color.white);
                        g.drawRect(j, i, 1, 1);
                    } else {
                        rgb = rgb(colors[j][i]);
                        g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
                        g.drawRect(j, i, 1, 1);
                    }
                }
            }
        }
        if (showImage) {
            for (int i = 1; i < bi.getHeight() - 1; i++) {
                for (int j = 1; j < bi.getWidth() - 1; j++) {
                    rgb = rgb(image[j][i]);
                    if (image[j][i] == -1) {
                        g.setColor(Color.BLACK);
                        g.drawRect(j, i, 1, 1);
                    }
                }
            }
        }
    }


    int calcEnergy(int x, int y) {
        int energy = 0;
        int[] neighborhood = {colors[x][y - 1], colors[x][y + 1], colors[x - 1][y], colors[x + 1][y], colors[x - 1][y - 1], colors[x - 1][y + 1], colors[x + 1][y - 1], colors[x + 1][y + 1]};
        for (int j : neighborhood) {
            if (colors[x][y] != j)
                energy++;
        }
        return energy;
    }

    int calcEnergyAfter(int color, int x, int y) {
        int energy = 0;
        int[] neighborhood = {colors[x][y - 1], colors[x][y + 1], colors[x - 1][y], colors[x + 1][y], colors[x - 1][y - 1], colors[x - 1][y + 1], colors[x + 1][y - 1], colors[x + 1][y + 1]};
        for (int j : neighborhood) {
            if (color != j)
                energy++;
        }
        return energy;
    }

    int hypColor(int x, int y) {
        int random = rand.nextInt(8);
        int[] neighborhood = {colors[x][y - 1], colors[x][y + 1], colors[x - 1][y], colors[x + 1][y], colors[x - 1][y - 1], colors[x - 1][y + 1], colors[x + 1][y - 1], colors[x + 1][y + 1]};
        return neighborhood[random];
    }

    void colorBounds(int x, int y) {
        if (y == 1)
            colors[y - 1][x] = colors[y][x];
        if (x == 1)
            colors[y][x - 1] = colors[y][x];
        if (x == width - 2)
            colors[width - 1][y] = colors[x][y];
        if (y == height - 2)
            colors[x][height - 1] = colors[x][y];
    }

    void insertSRX() {
        for (int i = 0; i < N; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            originalTab[x][y] = -1;
            colors[x][y] = 12000000 + rand.nextInt(1000000);
            energy[x][y] = 8;
        }
        MyFrame frame = new MyFrame();
        JFrame frame1 = frame.giveFrame();
        frame1.repaint();
        N = 0;
    }

    boolean isABoundary(int x, int y) {
        int[] neighborhood = {colors[x][y - 1], colors[x][y + 1], colors[x - 1][y], colors[x + 1][y]};
        int count = 0;
        for (int i = 0; i < neighborhood.length; i++) {
            for (int j = i + 1; j < neighborhood.length; j++) {
                if (neighborhood[i] == neighborhood[j]) {
                    count++;
                }
            }
        }
        return count < 6;
    }

    boolean isABoundarySRX(int x, int y) {
        int[] neighborhood = {originalTab[x][y - 1], originalTab[x][y + 1], originalTab[x - 1][y], originalTab[x + 1][y], originalTab[x - 1][y - 1], originalTab[x - 1][y + 1],
                originalTab[x + 1][y - 1], originalTab[x + 1][y + 1]};
        int count = 0;
        for (int i = 0; i < neighborhood.length; i++) {
            if (neighborhood[i] == -1)
                count++;
        }
        return count != 1;
    }

    void calcH() {
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int dis = rand.nextInt(dispersion * 2) - dispersion; // limited (-dispersion, +dispersion)
                if (isABoundary(x, y)) {
                    double trueVal = YY + (YY * dis / 100);
                    H[x][y] = trueVal;
                } else {
                    double trueVal = XX + (XX * dis / 100);
                    H[x][y] = trueVal;
                }
            }
        }
    }

    boolean neighborhoodRecrystallized(int x, int y) {
        int[] neighborhood = {originalTab[x][y - 1], originalTab[x][y + 1], originalTab[x - 1][y], originalTab[x + 1][y], originalTab[x - 1][y - 1], originalTab[x - 1][y + 1],
                originalTab[x + 1][y - 1], originalTab[x + 1][y + 1]};
        int[] color = {colors[x][y - 1], colors[x][y + 1], colors[x - 1][y], colors[x + 1][y], colors[x - 1][y - 1], colors[x - 1][y + 1],
                colors[x + 1][y - 1], colors[x + 1][y + 1]};
        for (int i = 0; i < neighborhood.length; i++) {
            if (neighborhood[i] == -1) {
                if (originalTab[x][y] == -1)
                    if (isABoundarySRX(x, y))
                        return true;
                potentialColor = color[i];
                return false;
            }
        }

        return true;
    }

    void addEmbroysInTime() {
        if (iterator == addEveryXIterations) {
            iterator = 0;
            for (int i = 0; i < embryos; i++) {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);
                if (originalTab[x][y] == -1)
                    continue;
                originalTab[x][y] = -1;
                colors[x][y] = 12000000 + rand.nextInt(1000000);
                energy[x][y] = 8;
            }
            embryos--;
            if (embryos < 0)
                embryos = 0;
            MyFrame frame = new MyFrame();
            JFrame frame1 = frame.giveFrame();
            frame1.repaint();
        }
    }

    void readImage() {
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                image[x][y] = bi.getRGB(x, y);
            }
        }
    }

    @Override
    public void run() {
        if (!boardFilled) {
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    if (neighborhood == Neighborhood.VonNeumann)
                        update(originalTab, copyTab, colors, j, i);
                    else
                        updateHexRandom(originalTab, copyTab, colors, j, i);
                }
            }
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    if (copyTab[j][i] == 1)
                        checkIfDone++;
                }
            }
            if (checkIfDone == (height - 2) * (width - 2)) {
                boardFilled = true;
                for (int i = 1; i < height - 1; i++)
                    for (int j = 1; j < width - 1; j++)
                        colorBounds(j, i);
                game.interrupt();

            } else
                checkIfDone = 0;

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    originalTab[i][j] = copyTab[i][j];
                    copyTab[i][j] = 0;
                }
            }
            MyFrame frame = new MyFrame();
            JFrame frame1 = frame.giveFrame();
            frame1.repaint();
        } else {
            if (methodName == MethodName.MC) {
                int amount = (width - 2) * (height - 2);
                ArrayList<Integer> monteCarlo = new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    monteCarlo.add(i);
                }
                Collections.shuffle(monteCarlo);
                for (Integer i : monteCarlo) {
                    int y = i / (width - 2);
                    int x = i % (width - 2);
                    x++;
                    y++;
                    double Eb = calcEnergy(x, y);
                    energy[x][y] = Eb;
                    int hypotheticalColor = hypColor(x, y);
                    double Ea = calcEnergyAfter(hypotheticalColor, x, y);
                    double dE = Ea - Eb;
                    if (dE > 0) {
                        double d1 = rand.nextDouble();
                        double prob = Math.exp(-(dE / kt));
                        if (d1 <= prob) {
                            colors[x][y] = hypotheticalColor;
                            energy[x][y] = Ea;
                        }
                    } else {
                        colors[x][y] = hypotheticalColor;
                        energy[x][y] = Ea;
                    }
                }
                MyFrame frame = new MyFrame();
                JFrame frame1 = frame.giveFrame();
                frame1.repaint();
            } else {
                if (N > 0) {
                    insertSRX();
                    calcH();
                } else {
                    addEmbroysInTime();
                    int amount = (width - 2) * (height - 2);
                    ArrayList<Integer> monteCarlo = new ArrayList<>();
                    for (int i = 0; i < amount; i++) {
                        monteCarlo.add(i);
                    }
                    Collections.shuffle(monteCarlo);
                    for (Integer i : monteCarlo) {
                        int y = i / (width - 2);
                        int x = i % (width - 2);
                        x++;
                        y++;
                        int hypotheticalColor = hypColor(x, y);
                        if (neighborhoodRecrystallized(x, y))
                            continue;
                        double Eb = calcEnergy(x, y) + H[x][y];
                        energy[x][y] = Eb;
                        double Ea = calcEnergyAfter(hypotheticalColor, x, y);
                        double dE = Ea - Eb;
                        if (dE < 0) {
                            colors[x][y] = potentialColor;
                            originalTab[x][y] = -1;
                            energy[x][y] = Ea;
                        }
                    }
                    MyFrame frame = new MyFrame();
                    JFrame frame1 = frame.giveFrame();
                    frame1.repaint();
                    iterator++;
                }
            }
        }

        try {
            Thread.sleep(1);
            run();
        } catch (InterruptedException ignored) {
        }
    }

    void step(int steps) {

        for (int s = 0; s < steps; s++) {
            if (!boardFilled) {
                for (int i = 1; i < height - 1; i++) {
                    for (int j = 1; j < width - 1; j++) {
                        update(originalTab, copyTab, colors, j, i);
                    }
                }
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        originalTab[i][j] = copyTab[i][j];
                        copyTab[i][j] = 0;
                    }
                }
                for (int i = 1; i < height - 1; i++) {
                    for (int j = 1; j < width - 1; j++) {
                        if (copyTab[j][i] == 1)
                            checkIfDone++;
                    }
                }
                if (checkIfDone == (height - 2) * (width - 2)) {
                    boardFilled = true;
                    for (int i = 1; i < height - 1; i++)
                        for (int j = 1; j < width - 1; j++)
                            colorBounds(j, i);
                } else
                    checkIfDone = 0;
                MyFrame frame = new MyFrame();
                JFrame frame1 = frame.giveFrame();
                frame1.repaint();
            } else {
                if (methodName == MethodName.MC) {
                    int amount = (width - 2) * (height - 2);
                    ArrayList<Integer> monteCarlo = new ArrayList<>();
                    for (int i = 0; i < amount; i++) {
                        monteCarlo.add(i);
                    }
                    Collections.shuffle(monteCarlo);
                    for (Integer i : monteCarlo) {
                        int y = i / (width - 2);
                        int x = i % (width - 2);
                        x++;
                        y++;
                        double Eb = calcEnergy(x, y);
                        energy[x][y] = Eb;
                        int hypotheticalColor = hypColor(x, y);
                        double Ea = calcEnergyAfter(hypotheticalColor, x, y);
                        double dE = Ea - Eb;
                        if (dE > 0) {
                            double d1 = rand.nextDouble();
                            double prob = Math.exp(-(dE / kt));
                            if (d1 <= prob) {
                                colors[x][y] = hypotheticalColor;
                                energy[x][y] = Ea;
                            }
                        } else {
                            colors[x][y] = hypotheticalColor;
                            energy[x][y] = Ea;
                        }
                    }
                    MyFrame frame = new MyFrame();
                    JFrame frame1 = frame.giveFrame();
                    frame1.repaint();
                } else {
                    if (N > 0) {
                        insertSRX();
                        calcH();
                    } else {
                        addEmbroysInTime();
                        int amount = (width - 2) * (height - 2);
                        ArrayList<Integer> monteCarlo = new ArrayList<>();
                        for (int i = 0; i < amount; i++) {
                            monteCarlo.add(i);
                        }
                        Collections.shuffle(monteCarlo);
                        for (Integer i : monteCarlo) {
                            int y = i / (width - 2);
                            int x = i % (width - 2);
                            x++;
                            y++;
                            int hypotheticalColor = hypColor(x, y);
                            if (neighborhoodRecrystallized(x, y))
                                continue;
                            double Eb = calcEnergy(x, y) + H[x][y];
                            energy[x][y] = Eb;
                            double Ea = calcEnergyAfter(hypotheticalColor, x, y);
                            double dE = Ea - Eb;
                            if (dE < 0) {
                                colors[x][y] = potentialColor;
                                originalTab[x][y] = -1;
                                energy[x][y] = Ea;
                            }
                        }
                        MyFrame frame = new MyFrame();
                        JFrame frame1 = frame.giveFrame();
                        frame1.repaint();
                        iterator++;
                    }
                }
            }
        }
    }
}