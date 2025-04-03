package page.info.edit;

import common.CommonStatic;
import common.pack.PackData;
import common.util.stage.Stage;
import common.util.stage.StageLimit;
import main.MainBCU;
import page.*;
import page.support.CrossList;
import utilpc.Interpret;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class StageLimitTable extends Page {

    private static final long serialVersionUID = 1L;

    private static String[] rarity;

    static {
        redefine();
    }

    protected static void redefine() {
        rarity = new String[] { "N", "EX", "R", "SR", "UR", "LR" };
    }

    private final JL bank = new JL(MainLocale.INFO, "ht20");
    private final JL cres = new JL(MainLocale.INFO, "ht21");
    private final JL cores = new JL(MainLocale.INFO, "ht22");
    private final JL totunit = new JL(MainLocale.INFO, "ht23");
    private final JL racost = new JL(MainLocale.INFO, "price");
    private final JL racool = new JL(MainLocale.INFO, "cdo");
    private final JL radeploy = new JL(MainLocale.INFO, "dep");
    private final JL raduamo = new JL(MainLocale.INFO, "dua");
    private final JL radudelay = new JL(MainLocale.INFO, "dud");
    private final JTF jban = new JTF();
    private final JTF jcre = new JTF();
    private final JTF jtou = new JTF();
    private final JTF jcos = new JTF();
    private final JTF[] jcool = new JTF[rarity.length];
    private final JTF[] jcost = new JTF[rarity.length];
    private final JTF[] jdeploy = new JTF[rarity.length];
    private final JTF[] jduamo = new JTF[rarity.length];
    private final JTF[] jdudelay = new JTF[rarity.length];
    private final JTG cdst = new JTG(MainLocale.INFO, "CDstart");

    private final CrossList<String> jlco = new CrossList<>(Interpret.getComboFilter(0));
    private final JScrollPane jsco = new JScrollPane(jlco);
    private final JBTN banc = new JBTN(MainLocale.PAGE, "ban0");

    private final PackData.UserPack pac;

    private StageLimit stli;

    protected StageLimitTable(Page p, PackData.UserPack pack) {
        super(p);
        pac = pack;
        ini();
    }

    @Override
    protected void resized(int x, int y) {
        int w = 1400 / 9;
        int r = 6;

        set(bank, x, y, 0, 0, w, 50);
        set(jban, x, y, w, 0, w, 50);
        set(cres, x, y, w * 2, 0, w, 50);
        set(jcre, x, y, w * 3, 0, w, 50);
        set(cores, x, y, w * 4, 0, w, 50);
        set(jcos, x, y, w * 5, 0, w, 50);
        set(totunit, x, y, w * 6, 0, w, 50);
        set(jtou, x, y, w * 7, 0, w, 50);

        set(racost, x, y, 0, 50, w, 50);
        set(racool, x, y, 0, 100, w, 50);
        set(radeploy, x, y, 0, 150, w, 50);
        set(raduamo, x, y, 0, 200, w, 50);
        set(radudelay, x, y, 0, 250, w, 50);
        for (int i = 0; i < rarity.length; i++) {
            set(jcost[i], x, y, w * ((i % r) + 1), 50 * ((i / r) + 1), w, 50);
            set(jcool[i], x, y, w * ((i % r) + 1), 50 * ((i / r) + 2), w, 50);
            set(jdeploy[i], x, y, w * ((i % r) + 1), 50 * ((i / r) + 3), w, 50);
            set(jduamo[i], x, y, w * ((i % r) + 1), 50 * ((i / r) + 4), w, 50);
            set(jdudelay[i], x, y, w * ((i % r) + 1), 50 * ((i / r) + 5), w, 50);
        }
        set(cdst, x, y, 0, 300, w, 50);

        set(jsco, x, y, (int) (w * 7), 50, w*2, 250);
        set(banc, x, y, w * 7, 300, w, 50);
    }

    private void ini() {
        add(bank);
        reg(jban);
        add(cres);
        reg(jcre);
        add(cores);
        reg(jcos);
        add(totunit);
        reg(jtou);
        add(jsco);
        add(banc);
        add(racool);
        add(racost);
        add(radeploy);
        add(raduamo);
        add(radudelay);
        add(cdst);

        for (int i = 0; i < rarity.length; i++) {
            reg(jcool[i] = new JTF());
            reg(jcost[i] = new JTF());
            reg(jdeploy[i] = new JTF());
            reg(jduamo[i] = new JTF());
            reg(jdudelay[i] = new JTF());
        }

        jlco.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlco.setCheck(i -> stli != null && stli.bannedCatCombo.contains(i));

        addListeners();
    }

    public void setData(Stage st) {
        if (st == null) {
            abler(false);
            return;
        }
        setStageLimit(st.lim.stageLimit == null ? st.lim.stageLimit = new StageLimit() : st.lim.stageLimit);
    }

    public void setStageLimit(StageLimit sl) {
        if (sl == null) {
            abler(false);
            return;
        }
        stli = sl;
        for (int i = 0; i < rarity.length; i++) {
            jcost[i].setText(rarity[i] + ": " + stli.costMultiplier[i] + "%");
            jcool[i].setText(rarity[i] + ": " + stli.cooldownMultiplier[i] + "%");
            jdeploy[i].setText(rarity[i] + ": " + stli.rarityDeployLimit[i]);
            jduamo[i].setText(rarity[i] + ": " + stli.deployDuplicationTimes[i]);
            if(MainBCU.seconds) jdudelay[i].setText(rarity[i] + ": " + MainBCU.toSeconds(stli.deployDuplicationDelay[i]));
            else jdudelay[i].setText(rarity[i] + ": " + stli.deployDuplicationDelay[i] + "f");
        }
        jban.setText(stli.maxMoney + "");
        jcre.setText(stli.globalCooldown + "");
        jcos.setText(stli.globalCost + "");
        jtou.setText(stli.maxUnitSpawn + "");
        cdst.setSelected(stli.coolStart);
        jlco.repaint();
        abler(true);
    }

    private void abler(boolean b) {
        for (int i = 0; i < rarity.length; i++) {
            jcost[i].setEnabled(b);
            jcool[i].setEnabled(b);
            jdeploy[i].setEnabled(b);
            jduamo[i].setEnabled(b);
            jdudelay[i].setEnabled(b);
        }
        jban.setEnabled(b);
        jcre.setEnabled(b);
        jcos.setEnabled(b);
        jtou.setEnabled(b);
        jlco.setEnabled(b);
        cdst.setEnabled(b);
        banc.setEnabled(b && jlco.getSelectedIndex() != -1);
    }

    private void reg(JTF jtf) { // using "reg" for "register" because "set" is already used for ui
        add(jtf);

        jtf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (getFront().isAdj())
                    return;
                input(jtf, jtf.getText());
                getFront().callBack(stli);
            }
        });
    }

    private void input(JTF jtf, String text) {
        if (jtf == jban)
            stli.maxMoney = Math.max(CommonStatic.parseIntN(text), 0);
        else if (jtf == jcre)
            stli.globalCooldown = Math.max(CommonStatic.parseIntN(text), 0);
        else if (jtf == jcos)
            stli.globalCost = Math.max(CommonStatic.parseIntN(text), 0);
        else if (jtf == jtou)
            stli.maxUnitSpawn = Math.max(CommonStatic.parseIntN(text), 0);
        else {
            for (int i = 0; i < rarity.length; i++) {
                if (jcost[i] == jtf) {
                    stli.costMultiplier[i] = Math.max(CommonStatic.parseIntN(text), 0);
                    break;
                } else if (jcool[i] == jtf) {
                    stli.cooldownMultiplier[i] = Math.max(CommonStatic.parseIntN(text), 0);
                    break;
                } else if (jdeploy[i] == jtf) {
                    stli.rarityDeployLimit[i] = Math.max(CommonStatic.parseIntN(text), 0);
                    break;
                } else if (jduamo[i] == jtf) {
                    stli.deployDuplicationTimes[i] = Math.max(CommonStatic.parseIntN(text), 0);
                    break;
                } else if (jdudelay[i] == jtf) {
                    stli.deployDuplicationDelay[i] = Math.max(CommonStatic.parseIntN(text), 0);
                    break;
                }
            }
        }
    }

    private void addListeners() {
        jlco.addListSelectionListener(x -> {
            banc.setEnabled(jlco.getSelectedIndex() != -1);
            banc.setText(MainLocale.PAGE, "ban" + (!stli.bannedCatCombo.contains(jlco.getSelectedIndex()) ? "0" : "1"));
        });

        banc.setLnr(x -> {
            if (stli == null || jlco.getSelectedIndex() == -1)
                return;

            if (stli.bannedCatCombo.contains(jlco.getSelectedIndex())) {
                stli.bannedCatCombo.remove(jlco.getSelectedIndex());
                banc.setText(MainLocale.PAGE, "ban0");
            } else {
                stli.bannedCatCombo.add(jlco.getSelectedIndex());
                banc.setText(MainLocale.PAGE, "ban1");
            }

            jlco.repaint();
        });

        cdst.setLnr(x -> {
           if (stli == null)
               return;
           stli.coolStart = cdst.isSelected();
        });
    }

    @Override
    protected JButton getBackButton() {
        return null;
    }
}
