package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import sts.caster.actions.ElectrifyCardsAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

import static sts.caster.core.CasterMod.makeCardPath;

public class Bzzzt extends CasterCard {

    public static final String ID = CasterMod.makeID("Bzzzt");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("bzzt.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int ENERGY_GAIN = 1;
    private static final int DRAW_AMT = 2;
    private static final int UPGR_DRAW_AMT = 2;

    public Bzzzt() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW_AMT;
        exhaust = true;
        setCardElement(MagicElement.ELECTRIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new DrawCardAction(p, magicNumber));
    	addToBot(new GainEnergyAction(ENERGY_GAIN));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (canUse) {
            if (EnergyPanel.totalCount == 0) return true;
            cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        }
        return false;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeMagicNumber(UPGR_DRAW_AMT);
        }
    }
}
