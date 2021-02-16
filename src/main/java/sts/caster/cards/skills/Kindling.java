package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.powers.BlazedPower;

import static sts.caster.core.CasterMod.makeCardPath;

public class Kindling extends CasterCard {

    public static final String ID = CasterMod.makeID("Kindling");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("kindling.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_BLAZED = 5;
    private static final int UPG_BLAZED = 1;
    private static final int BASE_DRAW = 1;
    private static final int UPG_DRAW = 1;


    public Kindling() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_BLAZED;
        baseM2 = m2 = BASE_DRAW;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.hasPower(BlazedPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, p, new BlazedPower(m, p, magicNumber), magicNumber));
        }
        addToBot(new ApplyPowerAction(m, p, new BlazedPower(m, p, magicNumber), magicNumber));
        addToBot(new DrawCardAction(p, m2));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            upgradeM2(UPG_DRAW);
            upgradeMagicNumber(UPG_BLAZED);
        }
    }
}
