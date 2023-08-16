package skill.issue.traconsim;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static skill.issue.traconsim.Main.Action.ALT_SEL;

public class Main {
    /*
    UNDER ABSOLUTELY NO CIRCUMSTANCES SHOULD THIS VARIABLE BE TOUCHED OR CHANGED
    I REPEAT. ***NO*** CHANGES TO THIS VARIABLE
    THIS WILL AFFECT THE LOOK AND FEEL OF THE MOVEMENT OF THE AIRCRAFT
    THIS WILL ***NOT*** BE CHANGED UNDER ANY CIRCUMSTANCE
    */
    private static final Random RNG = new Random();
    private static final double SPD_CONVERSION = 0.0005/180;
    static int selectedDb = -1;
    static String enteredText = "";
    static int enteredNumber = -1;
    static Action selectedAction = null;
    enum Action {
        HO_TWR,HO_CTR,HDG_SEL,HDG_SEL_SELECTED,ALT_SEL,ALT_SEL_SELECTED
    }
    enum Owner {
        APP, TWR, CTR
    }
    enum DBInfo {
        XPDR, ALT, HO, SPD
    }
    static void renderText() {
        double x = -0.9, y = 0.9;

        glColor3d(1,1,1);
        if (selectedAction == Action.HDG_SEL) rH(x,y);
        if (selectedAction == Action.ALT_SEL) rA(x,y);
        if (selectedAction == Action.HO_TWR) {rH(x,y); rT(x+0.013,y);}
        else if (selectedAction == Action.HO_CTR) {rH(x,y); rC(x+0.013,y);}
        y -= 0.035;
        for (char c : enteredText.toCharArray()) {
            if (Character.isDigit(c)) {
                renderNumber(Character.getNumericValue(c), x, y);
                x+=0.013;
            }
        }
        y-= 0.035;
        x = -0.9;
        if (enteredNumber == -1) return;
        System.out.println(enteredNumber);
        int[] digits = new int[3];
        int enteredNumberCopy = enteredNumber;
        for (int i = 0; i < 3; i++) {
            digits[i] = enteredNumberCopy % 10;
            enteredNumberCopy /= 10;
        }
        System.out.println(Arrays.toString(digits));
        for (int i = 2; i >= 0; i--) {
            renderNumber(digits[i], x, y);
            x+=0.013;
        }
    }
    static void renderNumber(int n, double x, double y) {
        switch (n) {
            case 0 -> r0(x, y);
            case 1 -> r1(x, y);
            case 2 -> r2(x, y);
            case 3 -> r3(x, y);
            case 4 -> r4(x, y);
            case 5 -> r5(x, y);
            case 6 -> r6(x, y);
            case 7 -> r7(x, y);
            case 8 -> r8(x, y);
            case 9 -> r9(x, y);
            case 10 -> rUpArrow(x, y);
            case 11 -> rDownArrow(x, y);
        }
    }
    static void renderDBPart(DBInfo part, int n, double x, double y) {
        switch (part) {
            case XPDR -> {
                int[] digits = new int[4];
                for (int i = 0; i < 4; i++) {
                    digits[i] = n % 10;
                    n /= 10;
                }
                renderNumber(digits[3], x, y);
                renderNumber(digits[2], x + 0.013, y);
                renderNumber(digits[1], x + 0.026, y);
                renderNumber(digits[0], x + 0.039, y);
            }
            case SPD -> {
                renderNumber(n / 100, x, y);
                renderNumber((n / 10) % 10, x + 0.013, y);
            }
        }
    }
    static void renderDBPart(DBInfo part, int n, double x, double y, DB db) {
        if (part == DBInfo.HO) {
            if (!db.ho()) return;
            if (!db.owned()) {
                rDownArrow(x,y);
                if (db.owner() == Owner.TWR) rT(x+0.013,y);
                else if (db.owner() == Owner.CTR) rC(x+0.013,y);
            } else {
                rUpArrow(x,y);
                if (db.hoTgt() == Owner.TWR) rT(x+0.013,y);
                else if (db.hoTgt() == Owner.CTR) rC(x+0.013,y);
            }
            return;
        }
        if (db.app() && !db.departure()) {
            rGLS(x,y);
            return;
        }
        int[] digitsAlt = {
                n/10000,
                (n/1000)%10,
                (n/100)%10,
        };
        if (db.vs() > 0) renderNumber(10, x, y);
        else if (db.vs() < 0) renderNumber(11, x, y);
        renderNumber(digitsAlt[0], x + 0.013, y);
        renderNumber(digitsAlt[1], x + 0.026, y);
        renderNumber(digitsAlt[2], x + 0.039, y);
    }
    static void r0(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.03);
        glVertex2d(x,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x,y+0.03);
        glEnd();
    }
    static void r1(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x+0.005,y);
        glVertex2d(x+0.005,y+0.03);
        glEnd();
    }
    static void r2(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.015);
        glVertex2d(x+0.01,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.015);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.03);
        glVertex2d(x,y+0.03);
        glEnd();
    }
    static void r3(double x, double  y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.015);
        glVertex2d(x,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.03);
        glVertex2d(x,y+0.03);
        glEnd();
    }
    static void r4(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.015);
        glVertex2d(x,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.015);
        glVertex2d(x,y+0.03);
        glEnd();
    }
    static void r5(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y);
        glVertex2d(x+0.01,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.015);
        glVertex2d(x,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.015);
        glVertex2d(x,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.03);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
    }
    static void r6(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.03);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.015);
        glVertex2d(x+0.01,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.015);
        glVertex2d(x,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01, y+0.015);
        glVertex2d(x+0.01, y);
        glEnd();
    }
    static void r7(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.03);
        glVertex2d(x,y+0.03);
        glEnd();
    }
    static void r8(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.015);
        glVertex2d(x+0.01,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.03);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.03);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x,y+0.03);
        glEnd();
    }
    static void r9(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y+0.03);
        glVertex2d(x,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.015);
        glVertex2d(x,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.015);
        glVertex2d(x+0.01,y+0.015);
        glEnd();
    }
    static void rUpArrow(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x+0.005,y);
        glVertex2d(x+0.005,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.005,y+0.03);
        glVertex2d(x,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.005,y+0.03);
        glVertex2d(x+0.01,y+0.015);
        glEnd();
    }
    static void rDownArrow(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x+0.005,y+0.03);
        glVertex2d(x+0.005,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.005,y);
        glVertex2d(x,y+0.015);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.005,y);
        glVertex2d(x+0.01,y+0.015);
        glEnd();
    }
    static void rL(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.01,y);
        glEnd();
    }
    static void rGLS(double x, double y) {
        rDownArrow(x,y);
        r6(x+0.013,y);
        rL(x+0.026, y);
        r5(x+0.039, y);
    }
    static void rT(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x+0.005,y);
        glVertex2d(x+0.005,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.03);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
    }
    static void rC(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.03);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
    }
    static void rH(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.01,y);
        glVertex2d(x+0.01,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x,y+0.015);
        glVertex2d(x+0.01,y+0.015);
        glEnd();
    }
    static void rA(double x, double y) {
        glBegin(GL_LINES);
        glVertex2d(x,y);
        glVertex2d(x+0.005,y+0.03);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.005,y+0.03);
        glVertex2d(x+0.01,y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x+0.0025,y+0.015);
        glVertex2d(x+0.0075,y+0.015);
        glEnd();
    }
    static ArrayList<DB> dbs = new ArrayList<>();
    static int newSquawk() {
        int squawk = 0;
        for (int i = 0; i < 4; i++) squawk += ((int) (Math.random()*7)) * Math.pow(10,i);
        return squawk;
    }
    static long ticks;
    static long frame;
    public static void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) System.exit(1);

        frame = glfwCreateWindow(1000, 800, "theaviationbee's TRACON simulator", 0, 0);
        if (frame == NULL) System.exit(1);

        glfwSetKeyCallback(frame,  (window, key, scancode, action, mods) -> {
            if (action != GLFW_RELEASE) return;
            if (key >= 48 && key <= 57) {
                enteredText += (char) key;
                return;
            }
            if (key == GLFW_KEY_T) {
                selectedAction = Action.HO_TWR;
                return;
            }
            if (key == GLFW_KEY_W) {
                selectedAction = ALT_SEL;
            }
            if (key == GLFW_KEY_C) {
                selectedAction = Action.HO_CTR;
                return;
            }
            if (key == GLFW_KEY_Q) {
                selectedAction = Action.HDG_SEL;
                return;
            }
            if (key == GLFW_KEY_ESCAPE) {
                selectedAction = null;
                enteredText = "";
                return;
            }
            if (key == GLFW_KEY_ENTER) {
                try {
                    selectedDb = selectedAction == Action.HDG_SEL || selectedAction == Action.ALT_SEL ? Integer.parseInt(enteredText.substring(0,3)) : Integer.parseInt(enteredText.substring(0,4));
                    for (int i = 0; i < dbs.size(); i++) {
                        DB db = dbs.get(i);
                        if (db.xpdr() == selectedDb && db.ho() && !db.owned() && selectedAction == null) {
                            dbs.set(i, new DB(db.x(), db.y(), db.heading(), db.speed(), db.alt(), db.vs(), true, Owner.APP, db.xpdr(),false, db.tgtAlt(),db.tgtSpd(),db.app(), db.departure(), Owner.APP));
                            selectedAction = null;
                            break;
                        }
                        if (selectedAction == Action.HDG_SEL && db.owned() && !db.ho()) {
                            enteredNumber = selectedDb;
                            selectedAction = Action.HDG_SEL_SELECTED;
                            break;
                        } else if (selectedAction == ALT_SEL && db.owned() && !db.ho()) {
                            enteredNumber = selectedDb;
                            selectedAction = Action.ALT_SEL_SELECTED;
                        }

                        if (db.xpdr() == selectedDb && db.owned() && !db.ho()) {
                            if (selectedAction == Action.HO_TWR) {
                                dbs.set(i, new DB(db.x(), db.y(), db.heading(), db.speed(), db.alt(), db.vs(), true, Owner.APP, db.xpdr(),true, db.tgtAlt(),db.tgtSpd(),db.app(), db.departure(), Owner.TWR));
                                selectedAction = null;
                                break;
                            } else if (selectedAction == Action.HO_CTR) {
                                dbs.set(i, new DB(db.x(), db.y(), db.heading(), db.speed(), db.alt(), db.vs(), true, Owner.APP, db.xpdr(),true, db.tgtAlt(),db.tgtSpd(),db.app(), db.departure(), Owner.CTR));
                                selectedAction = null;
                                break;
                            } else if (selectedAction == Action.HDG_SEL_SELECTED) {
                                dbs.set(i, new DB(db.x(), db.y(), enteredNumber, db.speed(), db.alt(), db.vs(), true, Owner.APP, db.xpdr(),false, db.tgtAlt(),db.tgtSpd(),db.app(), db.departure(), Owner.APP));
                                selectedAction = null;
                                enteredNumber = -1;
                                break;
                            } else if (selectedAction == Action.ALT_SEL_SELECTED) {
                                dbs.set(i, new DB(db.x(), db.y(), db.heading(), db.speed(), db.alt(), db.vs(), true, Owner.APP, db.xpdr(),false, enteredNumber,db.tgtSpd(),db.app(), db.departure(), Owner.APP));
                                selectedAction = null;
                                enteredNumber = -1;
                                break;
                            }
                        }
                    }
                    selectedDb = -1;
                    enteredText = "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        glfwMakeContextCurrent(frame);
        glfwSwapInterval(1);
        glfwShowWindow(frame);
    }
    public static void loop() {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while (!glfwWindowShouldClose(frame)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            renderText();

            glBegin(GL_QUADS);
            glColor3f(1f, 1f, 1f);
            float baseX = 0f;
            float baseY = 0f;
            float delta = 0.01f;
            glVertex2d(baseX + delta, baseY + delta);
            glVertex2d(baseX + delta, baseY - delta);
            glVertex2d(baseX - delta, baseY - delta);
            glVertex2d(baseX - delta, baseY + delta);
            glEnd();

            glBegin(GL_LINES);
            glColor3f(0.5f, 0.5f, 0.5f);
            glVertex2d(0d, 0d);
            glVertex2d(0.5d, 0d);
            glEnd();
            glBegin(GL_LINES);
            glColor3f(0.5f, 0.5f, 0.5f);
            glVertex2d(0.5d, 0.025d);
            glVertex2d(0.5d, -0.025d);
            glEnd();

            glBegin(GL_LINES);
            glColor3f(1f, 1f, 1f);
            glVertex2d(0d, 0d);
            glVertex2d(-0.125d, 0d);
            glEnd();
            glBegin(GL_LINES);
            glColor3f(1f, 1f, 1f);
            glVertex2d(-0.25d, 0d);
            glVertex2d(-0.375d, 0d);
            glEnd();
            glBegin(GL_LINES);
            glColor3f(1f, 1f, 1f);
            glVertex2d(-0.5d, 0d);
            glVertex2d(-0.625d, 0d);
            glEnd();
            glBegin(GL_LINES);
            glColor3f(1f, 1f, 1f);
            glVertex2d(-0.625d, 0.025d);
            glVertex2d(-0.625d, -0.025d);
            glEnd();

            for (int i = 0; i < dbs.size(); i++) {
                DB db = dbs.get(i);
                if (db.speed() == 0) {
                    continue;
                }
                glBegin(GL_LINES);
                glColor3f(0.5f, 0.5f, 0.5f);
                glVertex2d(db.x(), db.y());
                glVertex2d(db.x() + sin(toRadians(db.heading())) * 0.125, db.y() + cos(toRadians(db.heading())) * 0.125);
                glEnd();
                glBegin(GL_QUADS);
                glColor3f(0f, 0f, 1f);
                glVertex2d(db.x() + delta, db.y() + delta);
                glVertex2d(db.x() + delta, db.y() - delta);
                glVertex2d(db.x() - delta, db.y() - delta);
                glVertex2d(db.x() - delta, db.y() + delta);
                glEnd();
                if (db.ho()) {
                    glColor3f(1f, 1f, 0f);
                } else if (db.owned()) {
                    glColor3f(1f, 1f, 1f);
                } else {
                    glColor3f(0f, 1f, 0f);
                }
                renderDBPart(DBInfo.ALT, db.alt(), db.x() + 0.01, db.y() + 0.01, db);
                renderDBPart(DBInfo.SPD, db.speed(), db.x() + 0.07, db.y() + 0.01);
                renderDBPart(DBInfo.HO, 0, db.x() + 0.065, db.y() + 0.045, db);
                if (db.ho() || db.owned()) {
                    renderDBPart(DBInfo.XPDR, db.xpdr(), db.x() + 0.01, db.y() + 0.045);
                }
                double newHeading = db.heading();
                if (db.y() > -0.001 && db.y() < 0.001 && db.x() < 0) {
                    newHeading = 90d;
                }
                DB updatedBlock = new DB(db.x() + sin(toRadians(db.heading())) * SPD_CONVERSION * db.speed(),
                        db.y() + cos(toRadians(db.heading())) * SPD_CONVERSION * db.speed(),
                        newHeading, db.nextSpd(),
                        db.alt() + db.vs() / 10,
                        db.nextVS(),
                        db.owned(),
                        db.owner(),
                        db.xpdr(),
                        db.ho(),
                        (newHeading == 90 && !db.departure()) ? 0 : db.tgtAlt(),
                        (newHeading == 90 && !db.departure()) ? 100 : db.tgtSpd(),
                        newHeading == 90,
                        db.departure(),
                        db.hoTgt()
                );
                dbs.set(i,updatedBlock);
            }
            if (ticks%1500 == 0) {
                double y = RNG.nextDouble(-1,1);
                dbs.add(new DB(-1, y, y<0?RNG.nextInt(70,100):RNG.nextInt(80,160),280,2000,30,false,Owner.CTR,newSquawk(), true, 3000,250, false,false, Owner.APP));
                dbs.add(new DB(0,0, 90, 140,100,50,false,Owner.TWR,newSquawk(),true,12000,250, false,true, Owner.APP));
            }
            if (ticks%250 == 0) {
                for (int i = 0; i < dbs.size(); i++) {
                    DB db = dbs.get(i);
                    if (db.ho() && db.owned()) {
                        dbs.set(i, new DB(db.x(),db.y(), db.heading(), db.speed(), db.alt(), db.vs(), false, db.hoTgt(), db.xpdr(), false, db.tgtAlt(), db.tgtSpd(), db.app(), db.departure(), db.hoTgt()));
                    }
                }
            }
            ticks++;
            glfwSwapBuffers(frame);
            glfwPollEvents();
        }
    }
    public static void shutdown() {
        glfwDestroyWindow(frame);
        glfwTerminate();
    }
    public static void main(String[] args) {
        init();
        loop();
        shutdown();
    }
}
