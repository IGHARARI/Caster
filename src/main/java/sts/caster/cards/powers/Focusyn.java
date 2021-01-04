package sts.caster.cards.powers;

import static sts.caster.core.CasterMod.makeCardPath;

import com.evacipated.cardcrawl.mod.stslib.variables.RefundVariable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import basemod.abstracts.CustomCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;

public class Focusyn extends CustomCard {

    public static final String ID = CasterMod.makeID("Focusyn");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("immersion.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = -1;
    private static final int UPG_REFUND = 1;

    public Focusyn() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        RefundVariable.setBaseValue(this, 0);
    }
    
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        if (energyOnUse < EnergyPanel.totalCount) {
            energyOnUse = EnergyPanel.totalCount;
        }
        if (energyOnUse > 0) {
        	addToBot(new ApplyPowerAction(p, p, new FocusPower(p, energyOnUse), energyOnUse));
        }
    	if (!freeToPlayOnce) {
    		AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
    	}
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            RefundVariable.upgrade(this, UPG_REFUND);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}