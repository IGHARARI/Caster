package sts.caster.cards.powers;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import sts.caster.cards.skills.Meteor;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;

public class MeteorStorm extends CustomCard {

    public static final String ID = CasterMod.makeID("MeteorStorm");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Power.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 3;
    private static final int METEOR_AMOUNT = 3;
    private static final int UPGR_METEOR_AMOUNT = 1;

    public MeteorStorm() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = METEOR_AMOUNT;
    }
    
    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
    	Meteor freeMeteor = new Meteor();
    	freeMeteor.cost = 0;
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(freeMeteor, magicNumber, true, false));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGR_METEOR_AMOUNT);
            initializeDescription();
        }
    }
}