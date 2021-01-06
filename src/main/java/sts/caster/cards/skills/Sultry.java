package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.ThawCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

public class Sultry extends CasterCard {

    public static final String ID = CasterMod.makeID("Sultry");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("sultry.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = -2;
    private static final int UPG_COST = 0;
    private static final int THAW_AMOUNT = 1;
    private static final int THAW_UPGRADE = 1;
    private static final int DRAW_AMOUNT = 1;

    public Sultry() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = THAW_AMOUNT;
        m2 = baseM2 = DRAW_AMOUNT;

        setCardElement(MagicElement.FIRE);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return upgraded;
    }

     @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new ThawCardAction(magicNumber, false, upgraded));
    }

    public void triggerWhenDrawn() {
        addToBot(new DrawCardAction(m2));
        if (!upgraded) addToBot(new ThawCardAction(magicNumber, false));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeMagicNumber(THAW_UPGRADE);
            upgradeBaseCost(UPG_COST);
            initializeDescription();
        }
    }
}
