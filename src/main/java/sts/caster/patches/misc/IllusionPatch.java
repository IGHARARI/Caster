package sts.caster.patches.misc;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import sts.caster.powers.IllusionPower;

@SpirePatch(
        clz=GameActionManager.class,
        method="getNextAction"
)
public class IllusionPatch
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals("com.megacrit.cardcrawl.monsters.AbstractMonster")
                        && m.getMethodName().equals("takeTurn")) {

                    m.replace("if (!m.hasPower("+IllusionPower.class.getName()+".POWER_ID)) {" +
                            "$_ = $proceed($$);" +
                            "} else if (!m.hasPower(" + StunMonsterPower.class.getName() + ".POWER_ID)) {" +
                            "int maxValue = com.badlogic.gdx.math.MathUtils.random(51,99); "+
                            "com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom("
                            + "new com.megacrit.cardcrawl.actions.common.GainBlockAction(m, m, java.lang.Math.min(maxValue, java.lang.Math.max(m.maxHealth/4, 9)))); " +
                            "com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(m, m, "+IllusionPower.class.getName()+".POWER_ID));" +
                            "}");
                }
            }
        };
    }
}